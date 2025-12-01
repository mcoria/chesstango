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
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import org.slf4j.MDC;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
@Slf4j
public class LichessGame implements Runnable, SearchListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private final LichessClient client;
    private final String gameId;
    private final Session session;
    private final Color myColor;

    private FEN startPosition;

    public LichessGame(LichessClient client, Event.GameStartEvent gameStartEvent, Session session) {
        this.client = client;
        this.gameId = gameStartEvent.gameId();

        GameInfo gameInfo = gameStartEvent.game();

        // gameInfo.color() indica con que color juego
        if (Enums.Color.white == gameInfo.color()) {
            this.myColor = Color.WHITE;
        } else {
            this.myColor = Color.BLACK;
        }

        this.session = session;
        this.session.setSearchListener(this);
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
        log.info("[{}] Search started", gameId);
    }

    @Override
    public void searchInfo(String searchInfo) {
        log.info("[{}] {}", gameId, searchInfo);
    }

    @Override
    public void searchFinished(SearchResponse searchResult) {
        log.info("[{}] Search finished {}", gameId, searchResult);
    }

    private void gameFull(GameStateEvent.Full gameFullEvent) {
        log.info("[{}] GameStateEvent.Full: {}", gameId, gameFullEvent);

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

        this.session.setFen(startPosition);

        gameState(gameFullEvent.state());
    }

    private void gameState(GameStateEvent.State state) {
        log.info("[{}] GameStateEvent.State: {}", gameId, state);

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
        Game game = Game.from(startPosition, state.moveList());

        PositionReader currentChessPosition = game
                .getPosition();

        if (Objects.equals(myColor, currentChessPosition.getCurrentTurn())
                && !game.getStatus().isFinalStatus()) {

            session.setMoves(state.moveList());

            long wTime = state.wtime().toMillis();
            long bTime = state.btime().toMillis();

            long wInc = state.winc().toMillis();
            long bInc = state.binc().toMillis();

            try {
                SearchResponse searchResponse = session
                        .goFast((int) wTime, (int) bTime, (int) wInc, (int) bInc)
                        .get();

                String moveUci = simpleMoveEncoder.encode(searchResponse.move());

                client.gameMove(gameId, moveUci);

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
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
