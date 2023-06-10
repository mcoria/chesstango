package net.chesstango.board.debug.chess;

import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.imp.ZobristHashImp;
import net.chesstango.board.representations.polyglot.PolyglotEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 *
 */
public class ZobristHashDebug extends ZobristHashImp {

    public void validar(ChessPositionReader position) {
        assertEquals(PolyglotEncoder.getKey(position).longValue(), position.getPositionHash());
    }

}
