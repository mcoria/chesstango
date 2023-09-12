package net.chesstango.li;

import chariot.model.*;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.search.SearchInfo;
import net.chesstango.search.SearchListener;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.protocol.UCIEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
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

    @Setter
    private int maxDepth;
    private String fenPosition;
    private Color myColor;

    public LichessTango(LichessClient client, String gameId) {
        this.client = client;
        this.gameId = gameId;
        this.tango = new Tango();
        this.tango.setListenerClient(new SearchListener() {
            @Override
            public void searchInfo(SearchInfo info) {
                StringBuilder sb = new StringBuilder();
                for (Move move : info.pv()) {
                    sb.append(String.format("%s ", UCIEncoder.encode(move)));
                }

                logger.info("[{}] Depth {} seldepth {} pv {}", gameId, info.depth(), info.selDepth(), sb);
            }

            @Override
            public void searchFinished(SearchMoveResult searchResult) {
                logger.info("[{}] Search finished", gameId);
                String moveUci = UCIEncoder.encode(searchResult.getBestMove());
                client.gameMove(gameId, moveUci);
            }
        });
    }

    public void start(Event.GameStartEvent gameStartEvent) {
        GameInfo gameInfo = gameStartEvent.game();

        if (Enums.Color.white.equals(gameInfo.color())) {
            myColor = Color.WHITE;
        } else if (Enums.Color.black.equals(gameInfo.color())) {
            myColor = Color.BLACK;
        } else {
            throw new RuntimeException("Unknown color");
        }

        tango.open();

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

        play(gameFullEvent.state());
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

        ChessPositionReader currentChessPosition = tango.getCurrentSession().getGame().getChessPosition();

        if (Objects.equals(myColor, currentChessPosition.getCurrentTurn())) {
            tango.goDepth(maxDepth);
        }
    }

    private void receiveChatMessage(GameStateEvent.Chat chat) {
        logger.info("[{}] Chat: [{}] -> {}", gameId, chat.username(), chat.text());
    }

    private void sendChatMessage(String message) {
        logger.info("[{}] Chat: [{}] -> {}", gameId, "chesstango", message);
        client.gameChat(gameId, message);
    }
}
