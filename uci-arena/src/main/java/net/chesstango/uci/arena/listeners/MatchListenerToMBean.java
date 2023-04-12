package net.chesstango.uci.arena.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.mbeans.Arena;
import net.chesstango.mbeans.GameDescriptionCurrent;
import net.chesstango.mbeans.GameDescriptionInitial;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.arena.MatchListener;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Mauricio Coria
 */
public class MatchListenerToMBean implements MatchListener {

    private final Arena arena;

    public MatchListenerToMBean(Arena arena) {
        this.arena = arena;
    }


    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
        UUID uuid = UUID.randomUUID();

        String whiteName = white.getEngineName();

        String blackName = black.getEngineName();

        String turn = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? "white" : "black";

        GameDescriptionInitial gameDescriptionInitial = new GameDescriptionInitial(uuid.toString(), game.getInitialFen(), whiteName, blackName, turn);

        arena.newGame(gameDescriptionInitial);
    }


    @Override
    public void notifyMove(Game game, Move move) {
        List<String> theMoves = new ArrayList<>();
        game.accept(gameState -> {
            Move move1 = gameState.getSelectedMove();
            if (move1 != null) {
                theMoves.add(encodeMove(move1));
            }
        });

        String[] arrayMoveStr = theMoves.toArray(new String[theMoves.size()]);

        String turn = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? "white" : "black";

        String lastMove = encodeMove(move);

        GameDescriptionCurrent gameDescriptionCurrent = new GameDescriptionCurrent(arena.getCurrentGameId(), FENEncoder.encodeGame(game), turn, lastMove, arrayMoveStr);

        arena.newMove(gameDescriptionCurrent);
    }

    @Override
    public void notifyEndGame(GameResult gameResult) {

    }

    // TODO: obviously some moves are not encoded properly
    protected String encodeMove(Move move) {
        return String.format("%s-%s", move.getFrom().getSquare(), move.getTo().getSquare());
    }

}