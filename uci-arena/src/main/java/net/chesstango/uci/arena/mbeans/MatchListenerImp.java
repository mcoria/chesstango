package net.chesstango.uci.arena.mbeans;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.GameStateReader;
import net.chesstango.board.GameVisitor;
import net.chesstango.board.moves.Move;
import net.chesstango.uci.arena.MatchListener;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;

public class MatchListenerImp implements MatchListener {

    private final Arena arena;

    public MatchListenerImp(Arena arena) {
        this.arena = arena;
    }

    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
        arena.white = white.getEngineName();
        arena.black = black.getEngineName();
        arena.turn = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? "white" : "black";
    }


    @Override
    public void notifyExecutedMove(Game game, Move move) {
        arena.fen = game.getInitialFen();

        List<String> theMoves = new ArrayList<>();
        game.accept(new GameVisitor() {
            @Override
            public void visit(GameStateReader gameState) {
                Move move = gameState.getSelectedMove();
                if (move != null) {
                    theMoves.add(String.format("%s%s", move.getFrom().getSquare(), move.getTo().getSquare()));
                }
            }
        });

        String[] arrayMoveStr = new String[theMoves.size()];

        theMoves.toArray(arrayMoveStr);

        arena.moveList = arrayMoveStr;

        arena.turn = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? "white" : "black";

        /*
        String oldName = this.playerName;
        Notification n =
                new AttributeChangeNotification(this,
                        sequenceNumber++,
                        System.currentTimeMillis(),
                        "CacheSize changed",
                        "PlayerName",
                        "String",
                        oldName,
                        this.playerName);

        sendNotification(n);
         */
    }

    @Override
    public void notifyFinishedGame(Game game) {

            /*
            PGNGame pgnGame = PGNGame.createFromGame(game);
            pgnGame.setEvent(String.format("%s vs %s - Match", white.getEngineName(), black.getEngineName()));
            pgnGame.setWhite(white.getEngineName());
            pgnGame.setBlack(black.getEngineName());


            synchronized (this) {
                GameDescription gameDescription = gameGameDescriptionMap.get(game);
                activeGames.remove(gameDescription);
                inactiveGames.add(gameDescription);
            }
             */
    }

}