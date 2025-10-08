package net.chesstango.search.smart.features.zobrist.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;

import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class ZobristTracker implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private Map<Long, String> zobristMaxMap;
    private Map<Long, String> zobristMinMap;
    private List<String> zobristCollisions;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
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
        throw new UnsupportedOperationException("Reveer");
        /*
        /FENStringBuilder builder = new FENStringBuilder();

        PositionReader chessPosition = game.getPosition();

        chessPosition.export(builder);

        //String fenWithoutClocks = builder.getPositionRepresentation();

        long hash = chessPosition.getZobristHash();

        String oldFenWithoutClocks = theMap.put(hash, fenWithoutClocks);
        if (Objects.nonNull(oldFenWithoutClocks) && !Objects.equals(oldFenWithoutClocks, fenWithoutClocks)) {
            HexFormat hexFormat = HexFormat.of().withUpperCase();
            zobristCollisions.add(String.format("0x%sL - %s - %s", hexFormat.formatHex(longToByte(hash)), oldFenWithoutClocks, fenWithoutClocks));
        }*/
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
