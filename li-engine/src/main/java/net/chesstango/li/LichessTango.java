package net.chesstango.li;

import chariot.model.*;
import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.search.SearchListener;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.protocol.UCIEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessTango implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(LichessTango.class);
    private final LichessClient client;
    private final String gameId;
    private final Tango tango;
    private String fenPosition;
    private Color myColor;
    private GameInfo gameInfo;

    private Map<String, Object> properties;

    public LichessTango(LichessClient client, String gameId, Map<String, Object> properties) {
        this.client = client;
        this.gameId = gameId;
        this.properties = properties;

        this.tango = new Tango();
        this.tango.setListenerClient(new SearchListener() {
            @Override
            public void searchInfo(SearchMoveResult searchResult) {
                StringBuilder pvString = new StringBuilder();
                List<Move> pv = searchResult.getPrincipalVariation();
                for (Move move : pv) {
                    pvString.append(String.format("%s ", UCIEncoder.encode(move)));
                }

                logger.info("[{}] Depth {} seldepth {} eval {} pv {}", gameId, String.format("%2d", searchResult.getDepth()), String.format("%2d",searchResult.getDepth()), String.format("%8d", searchResult.getEvaluation()), pvString);
            }

            @Override
            public void searchFinished(SearchMoveResult searchResult) {
                String moveUci = UCIEncoder.encode(searchResult.getBestMove());
                logger.info("[{}] Search finished: eval {} move {}", gameId, String.format("%8d", searchResult.getEvaluation()), moveUci);
                client.gameMove(gameId, moveUci);
            }
        });
    }

    public void start(Event.GameStartEvent gameStartEvent) {
        gameInfo = gameStartEvent.game();

        if (Enums.Color.white.equals(gameInfo.color())) {
            myColor = Color.WHITE;
        } else if (Enums.Color.black.equals(gameInfo.color())) {
            myColor = Color.BLACK;
        } else {
            throw new RuntimeException("Unknown color");
        }

        tango.open();

        String polyglotBookPath = (String) properties.get(LichessBotMain.POLYGLOT_BOOK);
        if (Objects.nonNull(polyglotBookPath)) {
            tango.setPolyglotBook(polyglotBookPath);
        }

        tango.newGame();
    }

    public void stop(Event.GameStopEvent gameStopEvent) {
        tango.close();
    }

    @Override
    public void run() {
        logger.info("[{}] Ready to play game. Entering game event loop...", gameId);

        Stream<GameStateEvent> gameEvents = client.gameStreamEvents(gameId);

        gameEvents.forEach(gameEvent -> {
            try {
                switch (gameEvent.type()) {
                    case gameFull -> gameFull((GameStateEvent.Full) gameEvent);
                    case gameState -> gameState((GameStateEvent.State) gameEvent);
                    case chatLine -> receiveChatMessage((GameStateEvent.Chat) gameEvent);
                    case opponentGone -> opponentGone((GameStateEvent.OpponentGone) gameEvent);
                }
            } catch (RuntimeException e) {
                logger.error("[{}] {}", gameId, e.getMessage());
            }
        });

        logger.info("[{}] Event loop finished", gameId);
    }

    private void gameFull(GameStateEvent.Full gameFullEvent) {
        logger.info("[{}] gameFull: {}", gameId, gameFullEvent);

        GameType gameType = gameFullEvent.gameType();
        VariantType gameVariant = gameType.variant();
        if (VariantType.Variant.standard.equals(gameType.variant())) {
            fenPosition = FENDecoder.INITIAL_FEN;
        } else if (gameVariant instanceof VariantType.Variant.FromPosition fromPositionVariant) {
            Some<String> someFen = (Some) fromPositionVariant.fen();

            fenPosition = someFen.value();
        } else {
            throw new RuntimeException("GameVariant not supported variant");
        }

        ZonedDateTime createdTime = gameFullEvent.createdAt();
        long elapsedMinutes = Duration.between(createdTime, ZonedDateTime.now()).toMinutes();
        if (elapsedMinutes > 20) {
            client.gameAbort(gameId);
        } else {
            play(gameFullEvent.state());
        }
    }

    private void gameState(GameStateEvent.State state) {
        logger.info("[{}] gameState: {}", gameId, state);

        var status = state.status();

        switch (status) {
            case mate, resign, outoftime, stalemate, draw -> sendChatMessage("good game!!!");
            case started, created -> play(state);
            default -> {
                logger.warn("[{}] No action handler for status {}", gameId, status);
            }
        }
    }

    private void opponentGone(GameStateEvent.OpponentGone gameEvent) {
        logger.warn("[{}] opponentGone {}", gameId, gameEvent);
    }

    private void play(GameStateEvent.State state) {
        tango.setPosition(fenPosition, state.moveList());

        ChessPositionReader currentChessPosition = tango
                .getCurrentSession()
                .getGame()
                .getChessPosition();

        if (Objects.equals(myColor, currentChessPosition.getCurrentTurn())) {
            long wTime = state.wtime().toMillis();
            long bTime = state.btime().toMillis();

            long wInc = state.winc().toMillis();
            long bInc = state.binc().toMillis();

            tango.goFast((int) wTime, (int) bTime, (int) wInc, (int) bInc);
        }
    }

    private void receiveChatMessage(GameStateEvent.Chat chat) {
        logger.info("[{}] Chat: [{}] -> {}", gameId, chat.username(), chat.text());
    }

    private void sendChatMessage(String message) {
        logger.info("[{}] Chat: [{}] -> {}", gameId, "chesstango", message);
        client.gameChat(gameId, message);
    }

    public boolean isTimeControlledGame() {
        GameInfo.TimeInfo timeInfo = gameInfo.time();

        return !Enums.Speed.classical.equals(timeInfo.speed()) && !Enums.Speed.correspondence.equals(timeInfo.speed());
    }
}
