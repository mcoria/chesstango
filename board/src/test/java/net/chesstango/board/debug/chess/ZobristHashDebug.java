package net.chesstango.board.debug.chess;

import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.PiecePlacementReader;
import net.chesstango.board.position.imp.ZobristHash;
import net.chesstango.board.representations.polyglot.PolyglotEncoder;

import static org.junit.Assert.assertEquals;

/**
 * @author Mauricio Coria
 *
 */
public class ZobristHashDebug extends ZobristHash {

    public void validar(ChessPositionReader position) {
        assertEquals(PolyglotEncoder.getKey(position).longValue(), position.getHash());
    }

}
