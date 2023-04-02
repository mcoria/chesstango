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

    private int currentGameId;

    public MatchListenerImp(Arena arena) {
        this.arena = arena;
    }


    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
        String whiteName = white.getEngineName();

        String blackName = black.getEngineName();

        currentGameId = arena.sequenceId.getAndIncrement();

        GameDescriptionInitial gameDescriptionInitial = new GameDescriptionInitial(currentGameId, game.getInitialFen(), whiteName, blackName);

        ArenaJMXClient.printInitialStatus(gameDescriptionInitial);

        arena.initialMap.put(currentGameId, gameDescriptionInitial);
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

        GameDescriptionCurrent gameDescriptionCurrent = new GameDescriptionCurrent(currentGameId, FENEncoder.encodeGame(game), turn, arrayMoveStr);

        ArenaJMXClient.printCurrentStatus(gameDescriptionCurrent);

        arena.currentMap.put(currentGameId, gameDescriptionCurrent);

        arena.notifyMove(String.format("%s%s", move.getFrom().getSquare(), move.getTo().getSquare()), gameDescriptionCurrent);
    }

}