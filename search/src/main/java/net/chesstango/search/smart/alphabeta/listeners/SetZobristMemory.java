package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.search.smart.ResetListener;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class SetZobristMemory implements SearchByCycleListener, ResetListener {

    private Map<Long, String> zobristMaxMap = new HashMap<>();
    private Map<Long, String> zobristMinMap = new HashMap<>();
    private List<String> zobristCollisions = new LinkedList<>();


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        Game game = context.getGame();
        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            trackRootNode(game, zobristMaxMap);
        } else {
            trackRootNode(game, zobristMinMap);
        }

        context.setZobristMaxMap(zobristMaxMap);
        context.setZobristMinMap(zobristMinMap);
        context.setZobristCollisions(zobristCollisions);
    }

    @Override
    public void afterSearch() {
        if (zobristCollisions.isEmpty()) {
            System.out.println("No Zobrist collision");
        } else {
            System.out.println("Zobrist collisions:");
            zobristCollisions.forEach(collision -> System.out.printf("%s\n", collision));
        }
    }

    private void trackRootNode(Game game, Map<Long, String> map) {
        FENEncoder encoder = new FENEncoder();

        ChessPositionReader chessPosition = game.getChessPosition();

        chessPosition.constructChessPositionRepresentation(encoder);

        String fenWithoutClocks = encoder.getFENZobrist();

        long hash = chessPosition.getZobristHash();

        map.put(hash, fenWithoutClocks);
    }

    @Override
    public void reset() {
        zobristMaxMap.clear();
        zobristMinMap.clear();
        zobristCollisions.clear();
    }
}
