package net.chesstango.li;

import chariot.model.*;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.engine.Tango;
import net.chesstango.search.SearchListener;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.protocol.UCIEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessTango implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(LichessTango.class);
    private static final int DEFAULT_DEPTH = 5;
    private final LichessClient client;
    private final String gameId;
    private final Tango tango;

    @Setter
    private ChallengeInfo challenge;
    private GameInfo game;

    public LichessTango(LichessClient client, String gameId) {
        this.client = client;
        this.gameId = gameId;
        this.tango = new Tango();
        this.tango.setListenerClient(new SearchListener() {
            @Override
            public void searchFinished(SearchMoveResult searchResult) {
                logger.info("Search finished");
                String moveUci = UCIEncoder.encode(searchResult.getBestMove());
                client.gameMove(gameId, moveUci);
            }
        });
    }

    public void start(Event.GameStartEvent gameStartEvent) {
        game = gameStartEvent.game();

        tango.open();

        tango.newGame();
    }

    public void stop(Event.GameStopEvent gameStopEvent) {
        tango.close();
    }

    @Override
    public void run() {
        logger.info("Ready to play game {}, entering game event loop...", gameId);

        Stream<GameStateEvent> gameEvents = client.gameStreamEvents(gameId);

        gameEvents.forEach(gameEvent -> {
            switch (gameEvent.type()) {
                case gameFull -> gameFull((GameStateEvent.Full) gameEvent);
                case gameState -> gameState((GameStateEvent.State) gameEvent);
                case chatLine -> receiveChatMessage((GameStateEvent.Chat) gameEvent);
                case opponentGone -> opponentGone((GameStateEvent.OpponentGone) gameEvent);
            }
        });

        logger.info("game {} event loop finished", gameId);
    }

    private void gameFull(GameStateEvent.Full gameFullEvent) {
        logger.info("gameFull: {}", gameFullEvent);

        play(gameFullEvent.state());
    }

    private void gameState(GameStateEvent.State state) {
        logger.info("gameState: {}", state);

        var status = state.status();

        switch (status) {
            case mate, resign, outoftime, stalemate, draw -> sendChatMessage("good game!!!");
            case started, created -> play(state);
            default -> {
                logger.warn("no action handler for status {}", status);
            }
        }

    }

    private void opponentGone(GameStateEvent.OpponentGone gameEvent) {
        logger.warn("opponentGone {}", gameEvent);
    }

    private void play(GameStateEvent.State state) {
        tango.setPosition(game.fen(), state.moveList());
        if (isMyTurn()) {
            tango.goDepth(DEFAULT_DEPTH);
        }
    }

    private boolean isMyTurn() {
        ChessPositionReader currentChessPosition = tango.getCurrentSession().getGame().getChessPosition();
        return Enums.Color.white.equals(game.color()) && Color.WHITE.equals(currentChessPosition.getCurrentTurn()) ||
                Enums.Color.black.equals(game.color()) && Color.BLACK.equals(currentChessPosition.getCurrentTurn());
    }

    private void receiveChatMessage(GameStateEvent.Chat chat) {
        logger.info("Chat: [{}] -> {}", chat.username(), chat.text());
    }

    private void sendChatMessage(String message) {
        logger.info("Chat: [{}] -> {}", "chesstango", message);
        client.gameChat(gameId, message);
    }
}