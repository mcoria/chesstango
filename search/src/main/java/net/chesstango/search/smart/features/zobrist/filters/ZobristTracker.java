package net.chesstango.search.smart.features.zobrist.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.position.PositionReader;
import net.chesstango.gardel.fen.FENStringBuilder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;

import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class ZobristTracker implements AlphaBetaFilter, SearchByCycleListener {
    @Setter
    @Getter
    private AlphaBetaFilter next;
    private Map<Long, String> zobristMaxMap;
    private Map<Long, String> zobristMinMap;
    private List<String> zobristCollisions;
    private Game game;


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.zobristMaxMap = context.getZobristMaxMap();
        this.zobristMinMap = context.getZobristMinMap();
        this.zobristCollisions = context.getZobristCollisions();
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        findCollision(zobristMaxMap);
        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        findCollision(zobristMinMap);
        return next.minimize(currentPly, alpha, beta);
    }

    protected void findCollision(Map<Long, String> theMap) {
        FENStringBuilder builder = new FENStringBuilder();

        PositionReader chessPosition = game.getPosition();

        chessPosition.export(builder);

        String fenWithoutClocks = builder.getPositionRepresentation();

        long hash = chessPosition.getZobristHash();

        String oldFenWithoutClocks = theMap.put(hash, fenWithoutClocks);
        if (Objects.nonNull(oldFenWithoutClocks) && !Objects.equals(oldFenWithoutClocks, fenWithoutClocks)) {
            HexFormat hexFormat = HexFormat.of().withUpperCase();
            zobristCollisions.add(String.format("0x%sL - %s - %s", hexFormat.formatHex(longToByte(hash)), oldFenWithoutClocks, fenWithoutClocks));
        }
    }

    private byte[] longToByte(long lng) {
        return new byte[]{
                (byte) (lng >> 56),
                (byte) (lng >> 48),
                (byte) (lng >> 40),
                (byte) (lng >> 32),
                (byte) (lng >> 24),
                (byte) (lng >> 16),
                (byte) (lng >> 8),
                (byte) lng
        };
    }
}
