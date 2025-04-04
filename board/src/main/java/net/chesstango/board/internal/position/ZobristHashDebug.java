package net.chesstango.board.internal.position;

import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.polyglot.PolyglotEncoder;

/**
 * @author Mauricio Coria
 */
public class ZobristHashDebug extends ZobristHashImp {

    public void validar(ChessPositionReader position) {
        if (!PolyglotEncoder.getKey(position).equals(position.getZobristHash())) {
            throw new RuntimeException("Zobrist hash does not match");
        }
    }

}
