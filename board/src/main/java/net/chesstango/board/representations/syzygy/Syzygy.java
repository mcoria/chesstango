package net.chesstango.board.representations.syzygy;

import net.chesstango.board.Color;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.Position;
import net.chesstango.board.position.PositionState;

/**
 * @author Mauricio Coria
 */
public class Syzygy {
    protected static final long PRIME_WHITE_QUEEN = Long.parseUnsignedLong("11811845319353239651");
    protected static final long PRIME_WHITE_ROOK = Long.parseUnsignedLong("10979190538029446137");
    protected static final long PRIME_WHITE_BISHOP = Long.parseUnsignedLong("12311744257139811149");
    protected static final long PRIME_WHITE_KNIGHT = Long.parseUnsignedLong("15202887380319082783");
    protected static final long PRIME_WHITE_PAWN = Long.parseUnsignedLong("17008651141875982339");
    protected static final long PRIME_BLACK_QUEEN = Long.parseUnsignedLong("15484752644942473553");
    protected static final long PRIME_BLACK_ROOK = Long.parseUnsignedLong("18264461213049635989");
    protected static final long PRIME_BLACK_BISHOP = Long.parseUnsignedLong("15394650811035483107");
    protected static final long PRIME_BLACK_KNIGHT = Long.parseUnsignedLong("13469005675588064321");
    protected static final long PRIME_BLACK_PAWN = Long.parseUnsignedLong("11695583624105689831");

    protected static final int TB_PIECES = 7;
    protected static final int TB_HASHBITS = (TB_PIECES < 7 ? 11 : 12);

    public void probeTable(Position chessPosition) {
        BitPosition bitPosition = toPosition(chessPosition);
        long key = calcKey(bitPosition);
        int idx = (int) (key >>> (64 - TB_HASHBITS));

        System.out.println(idx);
    }

    protected BitPosition toPosition(Position chessPosition) {
        BitBoard bitBoard = chessPosition.getBitBoard();
        PositionState positionState = chessPosition.getPositionState();
        long white = bitBoard.getPositions(Color.WHITE);
        long black = bitBoard.getPositions(Color.BLACK);
        long kings = bitBoard.getKingPositions();
        long queens = bitBoard.getQueenPositions();
        long rooks = bitBoard.getRookPositions();
        long bishops = bitBoard.getBishopPositions();
        long knights = bitBoard.getKnightPositions();
        long pawns = bitBoard.getPawnPositions();

        byte rule50 = 0;
        byte ep = 0;
        if (positionState.getEnPassantSquare() != null) {
            ep = (byte) positionState.getEnPassantSquare().toIdx();
        }
        boolean turn = positionState.getCurrentTurn() == Color.WHITE;

        return new BitPosition(white,
                black,
                kings,
                queens,
                rooks,
                bishops,
                knights,
                pawns,
                rule50,
                ep,
                turn);
    }

    protected long calcKey(BitPosition bitPosition) {
        return Long.bitCount(bitPosition.white & bitPosition.queens) * PRIME_WHITE_QUEEN +
                Long.bitCount(bitPosition.white & bitPosition.rooks) * PRIME_WHITE_ROOK +
                Long.bitCount(bitPosition.white & bitPosition.bishops) * PRIME_WHITE_BISHOP +
                Long.bitCount(bitPosition.white & bitPosition.knights) * PRIME_WHITE_KNIGHT +
                Long.bitCount(bitPosition.white & bitPosition.pawns) * PRIME_WHITE_PAWN +
                Long.bitCount(bitPosition.black & bitPosition.queens) * PRIME_BLACK_QUEEN +
                Long.bitCount(bitPosition.black & bitPosition.rooks) * PRIME_BLACK_ROOK +
                Long.bitCount(bitPosition.black & bitPosition.bishops) * PRIME_BLACK_BISHOP +
                Long.bitCount(bitPosition.black & bitPosition.knights) * PRIME_BLACK_KNIGHT +
                Long.bitCount(bitPosition.black & bitPosition.pawns) * PRIME_BLACK_PAWN;
    }


    protected record BitPosition(long white,
                                 long black,
                                 long kings,
                                 long queens,
                                 long rooks,
                                 long bishops,
                                 long knights,
                                 long pawns,
                                 byte rule50,
                                 byte ep,
                                 boolean turn) {
    }
}
