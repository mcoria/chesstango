package net.chesstango.board.representations.polyglot;

import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class PolyglotFileReaderTest {

    @Test
    public void testRead(){
        PolyglotFileReader reader = new PolyglotFileReader();

        Map<Long, List<PolyglotFileReader.MoveBookEntry>> book = reader.read();

        //Long polyglotKey = getKey(FENDecoder.INITIAL_FEN);
        Long polyglotKey = getKey("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");

        List<PolyglotFileReader.MoveBookEntry> posibleMoves = book.get(polyglotKey);

        //System.out.println(posibleMoves);

        for (PolyglotFileReader.MoveBookEntry move: posibleMoves) {
            System.out.println(move);
        }
    }

    private Long getKey(String fen) {
        ChessPosition initialPosition = FENDecoder.loadChessPosition(fen);
        PolyglotEncoder polyglotEncoder = new PolyglotEncoder();
        initialPosition.constructBoardRepresentation(polyglotEncoder);
        return polyglotEncoder.getChessRepresentation();
    }
}
