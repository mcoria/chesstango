package net.chesstango.board.internal.position;

import net.chesstango.board.position.PositionReader;
import net.chesstango.board.representations.polyglot.PolyglotKeyBuilder;

/**
 * @author Mauricio Coria
 */
public class ZobristHashDebug extends ZobristHashImp {

    public void validar(PositionReader position) {
        if (!PolyglotKeyBuilder.getKey(position).equals(position.getZobristHash())) {
            throw new RuntimeException("Zobrist hash does not match");
        }
    }

}
