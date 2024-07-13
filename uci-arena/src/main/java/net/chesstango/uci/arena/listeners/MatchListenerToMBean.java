package net.chesstango.uci.arena.listeners;

import lombok.Getter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.GameStateReader;
import net.chesstango.board.GameVisitor;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.mbeans.Arena;
import net.chesstango.mbeans.GameDescriptionCurrent;
import net.chesstango.mbeans.GameDescriptionInitial;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;

import java.util.*;

/**
 * @author Mauricio Coria
 */
public class MatchListenerToMBean implements MatchListener {

    @Getter
    private final Arena arena;

    private volatile String currentGameId;

    public MatchListenerToMBean() {
        this(Arena.createAndRegisterMBean());
    }
    
    public MatchListenerToMBean(Arena arena) {
        this.arena = arena;
    }


    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
        currentGameId = UUID.randomUUID().toString();

        String whiteName = white.getEngineName();

        String blackName = black.getEngineName();

        String turn = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? "white" : "black";

        GameDescriptionInitial gameDescriptionInitial = new GameDescriptionInitial(currentGameId, game.getInitialFEN().toString(), whiteName, blackName, turn);

        arena.newGame(gameDescriptionInitial);
    }


    @Override
    public void notifyMove(Game game, Move move) {
        List<String> theMoves = new ArrayList<>();

        //Arreglarlo
        game.accept(new GameVisitor() {
            @Override
            public void visit(ChessPositionReader chessPositionReader) {

            }

            @Override
            public void visit(GameStateReader gameState) {
                List<Move> theMovesReversed = new LinkedList<>();
                GameStateReader currentGameState = gameState.getPreviousState();
                while (currentGameState != null) {
                    theMovesReversed.add(currentGameState.getSelectedMove());
                    currentGameState = currentGameState.getPreviousState();
                }
                Collections.reverse(theMovesReversed);
                theMovesReversed
                        .stream()
                        .map(MatchListenerToMBean::encodeMove)
                        .forEach(theMoves::add);
            }

            @Override
            public void visit(MoveGenerator moveGenerator) {

            }
        });

        String[] arrayMoveStr = theMoves.toArray(String[]::new);

        String turn = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? "white" : "black";

        String lastMove = encodeMove(move);

        GameDescriptionCurrent gameDescriptionCurrent = new GameDescriptionCurrent(currentGameId, game.getCurrentFEN().toString(), turn, lastMove, arrayMoveStr);

        arena.newMove(gameDescriptionCurrent);
    }

    @Override
    public void notifyEndGame(Game game, MatchResult matchResult) {
    }

    // TODO: obviously some moves are not encoded properly
    protected static String encodeMove(Move move) {
        return String.format("%s-%s", move.getFrom().getSquare(), move.getTo().getSquare());
    }

}