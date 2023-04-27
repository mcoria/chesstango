package net.chesstango.board.representations.polyglot;

import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class PolyglotEncoderTest {

    @Test
    public void generateKeyINITIAL_FEN(){
        ChessPosition initialPosition = FENDecoder.loadChessPosition(FENDecoder.INITIAL_FEN);

        PolyglotEncoder polyglotEncoder = new PolyglotEncoder();

        initialPosition.constructChessPositionRepresentation(polyglotEncoder);

        Long polyglotKey = polyglotEncoder.getChessRepresentation();

        System.out.printf("%016X", polyglotKey);

        assertEquals(0x463b96181691fc9cL, polyglotKey.longValue());
    }

    /**
     * position after e2e4
     * FEN=rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1
     * key=823c9b50fd114196
     */

    @Test
    public void generateKey01(){
        ChessPosition initialPosition = FENDecoder.loadChessPosition("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");

        PolyglotEncoder polyglotEncoder = new PolyglotEncoder();

        initialPosition.constructChessPositionRepresentation(polyglotEncoder);

        Long polyglotKey = polyglotEncoder.getChessRepresentation();

        System.out.printf("%016X", polyglotKey);

        assertEquals(0x823c9b50fd114196L, polyglotKey.longValue());
    }


    /**
     * position after e2e4 d75
     * FEN=rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2
     * key=0756b94461c50fb0
     */

    @Test
    public void generateKey02(){
        ChessPosition initialPosition = FENDecoder.loadChessPosition("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2");

        PolyglotEncoder polyglotEncoder = new PolyglotEncoder();

        initialPosition.constructChessPositionRepresentation(polyglotEncoder);

        Long polyglotKey = polyglotEncoder.getChessRepresentation();

        System.out.printf("%016X", polyglotKey);

        assertEquals(0x0756b94461c50fb0L, polyglotKey.longValue());
    }

    /**
     * position after e2e4 d7d5 e4e5
     * FEN=rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2
     * key=662fafb965db29d4
     */

    @Test
    public void generateKey03(){
        ChessPosition initialPosition = FENDecoder.loadChessPosition("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2");

        PolyglotEncoder polyglotEncoder = new PolyglotEncoder();

        initialPosition.constructChessPositionRepresentation(polyglotEncoder);

        Long polyglotKey = polyglotEncoder.getChessRepresentation();

        System.out.printf("%016X", polyglotKey);

        assertEquals(0x662fafb965db29d4L, polyglotKey.longValue());
    }

    /**
     * position after e2e4 d7d5 e4e5 f7f5
     * FEN=rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3
     * key=22a48b5a8e47ff78
     */
    @Test
    public void generateKey04(){
        ChessPosition initialPosition = FENDecoder.loadChessPosition("rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3");

        PolyglotEncoder polyglotEncoder = new PolyglotEncoder();

        initialPosition.constructChessPositionRepresentation(polyglotEncoder);

        Long polyglotKey = polyglotEncoder.getChessRepresentation();

        System.out.printf("%016X", polyglotKey);

        assertEquals(0x22a48b5a8e47ff78L, polyglotKey.longValue());
    }

     /**
     * position after e2e4 d7d5 e4e5 f7f5 e1e2
     * FEN=rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPPKPPP/RNBQ1BNR b kq - 0 3
     * key=652a607ca3f242c1
     */

     @Test
     public void generateKey05(){
         ChessPosition initialPosition = FENDecoder.loadChessPosition("rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPPKPPP/RNBQ1BNR b kq - 0 3");

         PolyglotEncoder polyglotEncoder = new PolyglotEncoder();

         initialPosition.constructChessPositionRepresentation(polyglotEncoder);

         Long polyglotKey = polyglotEncoder.getChessRepresentation();

         System.out.printf("%016X", polyglotKey);

         assertEquals(0x652a607ca3f242c1L, polyglotKey.longValue());
     }

    /**
     * position after e2e4 d7d5 e4e5 f7f5 e1e2 e8f7
     * FEN=rnbq1bnr/ppp1pkpp/8/3pPp2/8/8/PPPPKPPP/RNBQ1BNR w - - 0 4
     * key=00fdd303c946bdd9
     */
    @Test
    public void generateKey06(){
        ChessPosition initialPosition = FENDecoder.loadChessPosition("rnbq1bnr/ppp1pkpp/8/3pPp2/8/8/PPPPKPPP/RNBQ1BNR w - - 0 4");

        PolyglotEncoder polyglotEncoder = new PolyglotEncoder();

        initialPosition.constructChessPositionRepresentation(polyglotEncoder);

        Long polyglotKey = polyglotEncoder.getChessRepresentation();

        System.out.printf("%016X", polyglotKey);

        assertEquals(0x00fdd303c946bdd9L, polyglotKey.longValue());
    }


    /**
     * position after a2a4 b7b5 h2h4 b5b4 c2c4
     * FEN=rnbqkbnr/p1pppppp/8/8/PpP4P/8/1P1PPPP1/RNBQKBNR b KQkq c3 0 3
     * key=3c8123ea7b067637
     */
    @Test
    public void generateKey07(){
        ChessPosition initialPosition = FENDecoder.loadChessPosition("rnbqkbnr/p1pppppp/8/8/PpP4P/8/1P1PPPP1/RNBQKBNR b KQkq c3 0 3");

        PolyglotEncoder polyglotEncoder = new PolyglotEncoder();

        initialPosition.constructChessPositionRepresentation(polyglotEncoder);

        Long polyglotKey = polyglotEncoder.getChessRepresentation();

        System.out.printf("%016X", polyglotKey);

        assertEquals(0x3c8123ea7b067637L, polyglotKey.longValue());
    }


     /**
     * position after a2a4 b7b5 h2h4 b5b4 c2c4 b4c3 a1a3
     * FEN=rnbqkbnr/p1pppppp/8/8/P6P/R1p5/1P1PPPP1/1NBQKBNR b Kkq - 0 4
     * key=5c3f9b829b279560
     */
     @Test
     public void generateKey08(){
         ChessPosition initialPosition = FENDecoder.loadChessPosition("rnbqkbnr/p1pppppp/8/8/P6P/R1p5/1P1PPPP1/1NBQKBNR b Kkq - 0 4");

         PolyglotEncoder polyglotEncoder = new PolyglotEncoder();

         initialPosition.constructChessPositionRepresentation(polyglotEncoder);

         Long polyglotKey = polyglotEncoder.getChessRepresentation();

         System.out.printf("%016X", polyglotKey);

         assertEquals(0x5c3f9b829b279560L, polyglotKey.longValue());
     }
}
