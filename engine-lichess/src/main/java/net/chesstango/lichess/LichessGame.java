package net.chesstango.lichess;

import chariot.model.*;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.position.PositionReader;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchListener;
import net.chesstango.engine.SearchResponse;
import net.chesstango.engine.Session;
import net.chesstango.engine.Tango;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import org.slf4j.MDC;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
@Slf4j
public class LichessGame implements Runnable, SearchListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private final LichessClient client;
    private final String gameId;
    private final Tango tango;
    private final Color myColor;


    private GameStateEvent.Full gameFullEvent;

    private Session session;

    private FEN startPosition;

    private volatile int moveCounter;

    public LichessGame(LichessClient client, Event.GameStartEvent gameStartEvent, Tango tango) {
        this.client = client;
        this.gameId = gameStartEvent.gameId();

        GameInfo gameInfo = gameStartEvent.game();

        // gameInfo.color() indica con que colo juego
        if (Enums.Color.white == gameInfo.color()) {
            this.myColor = Color.WHITE;
        } else {
            this.myColor = Color.BLACK;
        }

        this.tango = tango;
    }

    public void gameWatchDog() {
        MDC.put("gameId", gameId);
        if (gameFullEvent != null) {
            ZonedDateTime createdAt = gameFullEvent.createdAt();
            ZonedDateTime now = ZonedDateTime.now();
            long diff = now.toEpochSecond() - createdAt.toEpochSecond();
            if (diff > 60 && moveCounter < 2) {
                log.info("[{}] Game watchdog: game is over after {} minutes", gameId, diff / 60);
                client.gameAbort(gameId);
            } else {
                log.info("[{}] Game watchdog: game is still running", gameId);
            }
        }
        MDC.remove("gameId");
    }

    @Override
    public void run() {
        MDC.put("gameId", gameId);

        log.info("[{}] Entering Game event loop...", gameId);
        try (Stream<GameStateEvent> gameEvents = client.streamGameStateEvent(gameId)) {
            gameEvents.forEach(gameEvent -> {
                switch (gameEvent.type()) {
                    case gameFull -> gameFull((GameStateEvent.Full) gameEvent);
                    case gameState -> gameState((GameStateEvent.State) gameEvent);
                    case chatLine -> receiveChatMessage((GameStateEvent.Chat) gameEvent);
                    case opponentGone -> opponentGone((GameStateEvent.OpponentGone) gameEvent);
                    default -> log.warn("[{}] Game event unknown failed: {}", gameId, gameEvent);
                }
            });
            log.info("[{}] Game event loop finished", gameId);
        } catch (RuntimeException e) {
            log.error("[{}] Game event loop failed", gameId, e);
            throw e;
        }

        MDC.remove("gameId");
    }

    @Override
    public void searchStarted() {
        MDC.put("gameId", gameId);
    }

    @Override
    public void searchInfo(String searchInfo) {
        //String pvString = String.format("%s %s", simpleMoveEncoder.encodeMoves(searchInfo.getPrincipalVariation().stream().map(PrincipalVariation::move).toList()), searchInfo.isPvComplete() ? "" : "*");
        //log.info("[{}] Depth {} seldepth {} eval {} pv {}", gameId, String.format("%2d", searchInfo.getDepth()), String.format("%2d", searchInfo.getDepth()), String.format("%8d", searchInfo.getBestEvaluation()), pvString);
        log.info("[{}] {}", gameId, searchInfo);
    }

    @Override
    public void searchFinished(SearchResponse searchResult) {
        String moveUci = simpleMoveEncoder.encode(searchResult.getMove());
        log.info("[{}] Search finished: move {}", gameId, moveUci);
        client.gameMove(gameId, moveUci);
        MDC.remove("gameId");
    }

    private void gameFull(GameStateEvent.Full gameFullEvent) {
        log.info("[{}] gameFull: {}", gameId, gameFullEvent);

        this.gameFullEvent = gameFullEvent;

        GameType gameType = gameFullEvent.gameType();
        Variant gameVariant = gameType.variant();

        if (Variant.Basic.standard.equals(gameType.variant())) {
            this.startPosition = FEN.of(FENParser.INITIAL_FEN);
        } else if (gameVariant instanceof Variant.FromPosition fromPositionVariant) {
            Opt<String> someFen = fromPositionVariant.fen();
            this.startPosition = FEN.of(someFen.get());
        } else {
            throw new RuntimeException("GameVariant not supported variant");
        }

        this.session = tango.newSession(startPosition);

        this.session.setSearchListener(this);

        gameState(gameFullEvent.state());
    }

    private void gameState(GameStateEvent.State state) {
        log.info("[{}] gameState: {}", gameId, state);

        Enums.Status status = state.status();

        switch (status) {
            case mate, resign, outoftime, stalemate, draw -> sendChatMessage("good game!!!");
            case aborted -> sendChatMessage("goodbye!!!");
            case started, created -> play(state);
            default -> log.warn("[{}] No action handler for status {}", gameId, status);
        }
    }

    private void opponentGone(GameStateEvent.OpponentGone gameEvent) {
        log.warn("[{}] opponentGone {}", gameId, gameEvent);
    }

    private void play(GameStateEvent.State state) {
        moveCounter = state.moveList().size();

        Game game = Game.from(startPosition, state.moveList());

        PositionReader currentChessPosition = game
                .getPosition();

        if (Objects.equals(myColor, currentChessPosition.getCurrentTurn())) {
            session.setMoves(state.moveList());

            long wTime = state.wtime().toMillis();
            long bTime = state.btime().toMillis();

            long wInc = state.winc().toMillis();
            long bInc = state.binc().toMillis();

            session.goFast((int) wTime, (int) bTime, (int) wInc, (int) bInc);
        }
    }

    private void receiveChatMessage(GameStateEvent.Chat chat) {
        log.info("[{}] Chat: [{}] << {}", gameId, chat.username(), chat.text());
    }

    private void sendChatMessage(String message) {
        log.info("[{}] Chat: [{}] >> {}", gameId, "chesstango", message);
        client.gameChat(gameId, message);
    }

}
