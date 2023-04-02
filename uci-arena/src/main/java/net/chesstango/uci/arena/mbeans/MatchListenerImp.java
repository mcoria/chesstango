package net.chesstango.uci.arena.mbeans;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.GameStateReader;
import net.chesstango.board.GameVisitor;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.uci.arena.MatchListener;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MatchListenerImp implements MatchListener {

    private final Arena arena;

    public MatchListenerImp(Arena arena) {
        this.arena = arena;
    }

    private String white;
    private String black;

    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
        this.white = white.getEngineName();

        this.black = black.getEngineName();

        String turn = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? "white" : "black";

        GameDescription gameDescription = new GameDescription(game.getInitialFen(), game.getInitialFen(), this.white, this.black, turn,  new String[]{});

        ArenaJMXClient.printInitialStatus(gameDescription);

        arena.gameDescription = gameDescription;
    }


    @Override
    public void notifyExecutedMove(Game game, Move move) {


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

        String[] arrayMoveStr = theMoves.toArray(new String[theMoves.size()]);

        String turn = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? "white" : "black";

        String currentFEN = FENEncoder.encodeGame(game);

        GameDescription gameDescription = new GameDescription(game.getInitialFen(), currentFEN, this.white, this.black, turn, arrayMoveStr);

        ArenaJMXClient.printCurrentStatus(gameDescription);

        arena.gameDescription = gameDescription;

        arena.notifyMove(String.format("%s%s", move.getFrom().getSquare(), move.getTo().getSquare()));
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