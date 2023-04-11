package net.chesstango.uci.arena.mbeans;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.GameStateReader;
import net.chesstango.board.GameVisitor;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.mbeans.Arena;
import net.chesstango.mbeans.ArenaJMXClient;
import net.chesstango.mbeans.GameDescriptionCurrent;
import net.chesstango.mbeans.GameDescriptionInitial;
import net.chesstango.uci.arena.MatchListener;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Mauricio Coria
 */
public class MatchListenerImp implements MatchListener {

    private final Arena arena;

    public MatchListenerImp(Arena arena) {
        this.arena = arena;
    }


    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
        String whiteName = white.getEngineName();

        String blackName = black.getEngineName();

        UUID uuid = UUID.randomUUID();

        GameDescriptionInitial gameDescriptionInitial = new GameDescriptionInitial(uuid.toString(), game.getInitialFen(), whiteName, blackName);

        arena.newGame(gameDescriptionInitial);

        ArenaJMXClient.printInitialStatus(gameDescriptionInitial);
    }


    @Override
    public void notifyExecutedMove(Game game, Move move) {
        List<String> theMoves = new ArrayList<>();
        game.accept(new GameVisitor() {
            @Override
            public void visit(GameStateReader gameState) {
                Move move = gameState.getSelectedMove();
                if (move != null) {
                    theMoves.add(encodeMove(move));
                }
            }
        });

        String[] arrayMoveStr = theMoves.toArray(new String[theMoves.size()]);

        String turn = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? "white" : "black";

        GameDescriptionCurrent gameDescriptionCurrent = new GameDescriptionCurrent(arena.getCurrentGameId(), FENEncoder.encodeGame(game), turn, arrayMoveStr);

        arena.updateDescriptionCurrent(gameDescriptionCurrent, encodeMove(move));

        System.out.println(String.format("Selected move: %s", encodeMove(move)));

        ArenaJMXClient.printCurrentStatus(gameDescriptionCurrent);
    }

    protected String encodeMove(Move move){
        return String.format("%s-%s", move.getFrom().getSquare(), move.getTo().getSquare());
    }

}