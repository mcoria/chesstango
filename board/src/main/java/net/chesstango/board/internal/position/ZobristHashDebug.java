package net.chesstango.board.internal.position;

import net.chesstango.board.position.PositionReader;
import net.chesstango.gardel.polyglot.PolyglotKeyBuilder;

/**
 * @author Mauricio Coria
 */
public class ZobristHashDebug extends ZobristHashImp {

    public void validar(PositionReader position) {
        PolyglotKeyBuilder polyglotKeyBuilder = new PolyglotKeyBuilder();
        position.constructChessPositionRepresentation(polyglotKeyBuilder);
        Long polyglotKey = polyglotKeyBuilder.getPositionRepresentation();
        if (!polyglotKey.equals(position.getZobristHash())) {
            throw new RuntimeException("Zobrist hash does not match");
        }
    }

}
