package net.chesstango.board.internal.position;

import net.chesstango.board.position.PositionReader;
import net.chesstango.piazzolla.polyglot.PolyglotKeyBuilder;

/**
 * @author Mauricio Coria
 */
public class ZobristHashDebug extends ZobristHashImp {

    public void validar(PositionReader position) {
        PolyglotKeyBuilder polyglotKeyBuilder = new PolyglotKeyBuilder();
        position.export(polyglotKeyBuilder);
        Long polyglotKey = polyglotKeyBuilder.getPositionRepresentation();
        if (!polyglotKey.equals(position.getZobristHash())) {
            throw new RuntimeException("Zobrist hash does not match");
        }
    }

}
