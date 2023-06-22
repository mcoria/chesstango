package net.chesstango.board;

/**
 * @author Mauricio Coria
 */
public enum Square {
    a8(0, 7), b8(1, 7), c8(2, 7), d8(3, 7), e8(4, 7), f8(5, 7), g8(6, 7), h8(7, 7),
    a7(0, 6), b7(1, 6), c7(2, 6), d7(3, 6), e7(4, 6), f7(5, 6), g7(6, 6), h7(7, 6),
    a6(0, 5), b6(1, 5), c6(2, 5), d6(3, 5), e6(4, 5), f6(5, 5), g6(6, 5), h6(7, 5),
    a5(0, 4), b5(1, 4), c5(2, 4), d5(3, 4), e5(4, 4), f5(5, 4), g5(6, 4), h5(7, 4),
    a4(0, 3), b4(1, 3), c4(2, 3), d4(3, 3), e4(4, 3), f4(5, 3), g4(6, 3), h4(7, 3),
    a3(0, 2), b3(1, 2), c3(2, 2), d3(3, 2), e3(4, 2), f3(5, 2), g3(6, 2), h3(7, 2),
    a2(0, 1), b2(1, 1), c2(2, 1), d2(3, 1), e2(4, 1), f2(5, 1), g2(6, 1), h2(7, 1),
    a1(0, 0), b1(1, 0), c1(2, 0), d1(3, 0), e1(4, 0), f1(5, 0), g1(6, 0), h1(7, 0);

    private final int file;
    private final int rank;
    private final long bitPosition;
    private final int idx;
    private final String fileChar;
    private final String rankChar;

    Square(int file, int rank) {
        this.file = file;
        this.rank = rank;
        this.idx = rank * 8 + file;
        this.bitPosition = 1L << idx;
        this.fileChar = switch (file) {
            case 0 -> "a";
            case 1 -> "b";
            case 2 -> "c";
            case 3 -> "d";
            case 4 -> "e";
            case 5 -> "f";
            case 6 -> "g";
            case 7 -> "h";
            default -> throw new IllegalStateException("Unexpected value: " + file);
        };

        this.rankChar = switch (rank) {
            case 0 -> "1";
            case 1 -> "2";
            case 2 -> "3";
            case 3 -> "4";
            case 4 -> "5";
            case 5 -> "6";
            case 6 -> "7";
            case 7 -> "8";
            default -> throw new IllegalStateException("Unexpected value: " + file);
        };
    }

    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }

    public String getFileChar() {
        return fileChar;
    }

    public String getRankChar() {
        return rankChar;
    }

    public long getBitPosition() {
        return bitPosition;
    }

    public int toIdx() {
        return idx;
    }

    public long getKingJumps() {
        return KING_JUMPS[idx];
    }

    public long getKnightJumps() {
        return KNIGHT_JUMPS[idx];
    }

    public long getPawnWhiteCaptureJumps() {
        return PAWN_WHITE_CAPTURE_JUMPS[idx];
    }

    public long getPawnBlackCaptureJumps() {
        return PAWN_BLACK_CAPTURE_JUMPS[idx];
    }

    public Square getMirrorSquare() {
        return getSquare(file, 7 - rank);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private static final Square[] array = {
            Square.a1, Square.b1, Square.c1, Square.d1, Square.e1, Square.f1, Square.g1, Square.h1,
            Square.a2, Square.b2, Square.c2, Square.d2, Square.e2, Square.f2, Square.g2, Square.h2,
            Square.a3, Square.b3, Square.c3, Square.d3, Square.e3, Square.f3, Square.g3, Square.h3,
            Square.a4, Square.b4, Square.c4, Square.d4, Square.e4, Square.f4, Square.g4, Square.h4,
            Square.a5, Square.b5, Square.c5, Square.d5, Square.e5, Square.f5, Square.g5, Square.h5,
            Square.a6, Square.b6, Square.c6, Square.d6, Square.e6, Square.f6, Square.g6, Square.h6,
            Square.a7, Square.b7, Square.c7, Square.d7, Square.e7, Square.f7, Square.g7, Square.h7,
            Square.a8, Square.b8, Square.c8, Square.d8, Square.e8, Square.f8, Square.g8, Square.h8};

    public static Square getSquare(int file, int rank) {
        if (file < 0 || rank < 0) {
            return null;
        }
        if (file > 7 || rank > 7) {
            return null;
        }
        return array[rank * 8 + file];
    }

    public static Square getSquareByIdx(int idx) {
        if (idx < 0 || idx > 64) {
            return null;
        }
        return array[idx];
    }

    private static final long[] KING_JUMPS = {
            0x0000000000000302L,
            0x0000000000000705L,
            0x0000000000000E0AL,
            0x0000000000001C14L,
            0x0000000000003828L,
            0x0000000000007050L,
            0x000000000000E0A0L,
            0x000000000000C040L,
            0x0000000000030203L,
            0x0000000000070507L,
            0x00000000000E0A0EL,
            0x00000000001C141CL,
            0x0000000000382838L,
            0x0000000000705070L,
            0x0000000000E0A0E0L,
            0x0000000000C040C0L,
            0x0000000003020300L,
            0x0000000007050700L,
            0x000000000E0A0E00L,
            0x000000001C141C00L,
            0x0000000038283800L,
            0x0000000070507000L,
            0x00000000E0A0E000L,
            0x00000000C040C000L,
            0x0000000302030000L,
            0x0000000705070000L,
            0x0000000E0A0E0000L,
            0x0000001C141C0000L,
            0x0000003828380000L,
            0x0000007050700000L,
            0x000000E0A0E00000L,
            0x000000C040C00000L,
            0x0000030203000000L,
            0x0000070507000000L,
            0x00000E0A0E000000L,
            0x00001C141C000000L,
            0x0000382838000000L,
            0x0000705070000000L,
            0x0000E0A0E0000000L,
            0x0000C040C0000000L,
            0x0003020300000000L,
            0x0007050700000000L,
            0x000E0A0E00000000L,
            0x001C141C00000000L,
            0x0038283800000000L,
            0x0070507000000000L,
            0x00E0A0E000000000L,
            0x00C040C000000000L,
            0x0302030000000000L,
            0x0705070000000000L,
            0x0E0A0E0000000000L,
            0x1C141C0000000000L,
            0x3828380000000000L,
            0x7050700000000000L,
            0xE0A0E00000000000L,
            0xC040C00000000000L,
            0x0203000000000000L,
            0x0507000000000000L,
            0x0A0E000000000000L,
            0x141C000000000000L,
            0x2838000000000000L,
            0x5070000000000000L,
            0xA0E0000000000000L,
            0x40C0000000000000L};

    private static final long[] KNIGHT_JUMPS = {
            0x0000000000020400L,
            0x0000000000050800L,
            0x00000000000A1100L,
            0x0000000000142200L,
            0x0000000000284400L,
            0x0000000000508800L,
            0x0000000000A01000L,
            0x0000000000402000L,
            0x0000000002040004L,
            0x0000000005080008L,
            0x000000000A110011L,
            0x0000000014220022L,
            0x0000000028440044L,
            0x0000000050880088L,
            0x00000000A0100010L,
            0x0000000040200020L,
            0x0000000204000402L,
            0x0000000508000805L,
            0x0000000A1100110AL,
            0x0000001422002214L,
            0x0000002844004428L,
            0x0000005088008850L,
            0x000000A0100010A0L,
            0x0000004020002040L,
            0x0000020400040200L,
            0x0000050800080500L,
            0x00000A1100110A00L,
            0x0000142200221400L,
            0x0000284400442800L,
            0x0000508800885000L,
            0x0000A0100010A000L,
            0x0000402000204000L,
            0x0002040004020000L,
            0x0005080008050000L,
            0x000A1100110A0000L,
            0x0014220022140000L,
            0x0028440044280000L,
            0x0050880088500000L,
            0x00A0100010A00000L,
            0x0040200020400000L,
            0x0204000402000000L,
            0x0508000805000000L,
            0x0A1100110A000000L,
            0x1422002214000000L,
            0x2844004428000000L,
            0x5088008850000000L,
            0xA0100010A0000000L,
            0x4020002040000000L,
            0x0400040200000000L,
            0x0800080500000000L,
            0x1100110A00000000L,
            0x2200221400000000L,
            0x4400442800000000L,
            0x8800885000000000L,
            0x100010A000000000L,
            0x2000204000000000L,
            0x0004020000000000L,
            0x0008050000000000L,
            0x00110A0000000000L,
            0x0022140000000000L,
            0x0044280000000000L,
            0x0088500000000000L,
            0x0010A00000000000L,
            0x0020400000000000L};

    private static final long[] PAWN_WHITE_CAPTURE_JUMPS = {
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000002L,
            0x0000000000000005L,
            0x000000000000000AL,
            0x0000000000000014L,
            0x0000000000000028L,
            0x0000000000000050L,
            0x00000000000000A0L,
            0x0000000000000040L,
            0x0000000000000200L,
            0x0000000000000500L,
            0x0000000000000A00L,
            0x0000000000001400L,
            0x0000000000002800L,
            0x0000000000005000L,
            0x000000000000A000L,
            0x0000000000004000L,
            0x0000000000020000L,
            0x0000000000050000L,
            0x00000000000A0000L,
            0x0000000000140000L,
            0x0000000000280000L,
            0x0000000000500000L,
            0x0000000000A00000L,
            0x0000000000400000L,
            0x0000000002000000L,
            0x0000000005000000L,
            0x000000000A000000L,
            0x0000000014000000L,
            0x0000000028000000L,
            0x0000000050000000L,
            0x00000000A0000000L,
            0x0000000040000000L,
            0x0000000200000000L,
            0x0000000500000000L,
            0x0000000A00000000L,
            0x0000001400000000L,
            0x0000002800000000L,
            0x0000005000000000L,
            0x000000A000000000L,
            0x0000004000000000L,
            0x0000020000000000L,
            0x0000050000000000L,
            0x00000A0000000000L,
            0x0000140000000000L,
            0x0000280000000000L,
            0x0000500000000000L,
            0x0000A00000000000L,
            0x0000400000000000L,
            0x0002000000000000L,
            0x0005000000000000L,
            0x000A000000000000L,
            0x0014000000000000L,
            0x0028000000000000L,
            0x0050000000000000L,
            0x00A0000000000000L,
            0x0040000000000000L};

    private static final long[] PAWN_BLACK_CAPTURE_JUMPS = {
            0x0000000000000200L,
            0x0000000000000500L,
            0x0000000000000A00L,
            0x0000000000001400L,
            0x0000000000002800L,
            0x0000000000005000L,
            0x000000000000A000L,
            0x0000000000004000L,
            0x0000000000020000L,
            0x0000000000050000L,
            0x00000000000A0000L,
            0x0000000000140000L,
            0x0000000000280000L,
            0x0000000000500000L,
            0x0000000000A00000L,
            0x0000000000400000L,
            0x0000000002000000L,
            0x0000000005000000L,
            0x000000000A000000L,
            0x0000000014000000L,
            0x0000000028000000L,
            0x0000000050000000L,
            0x00000000A0000000L,
            0x0000000040000000L,
            0x0000000200000000L,
            0x0000000500000000L,
            0x0000000A00000000L,
            0x0000001400000000L,
            0x0000002800000000L,
            0x0000005000000000L,
            0x000000A000000000L,
            0x0000004000000000L,
            0x0000020000000000L,
            0x0000050000000000L,
            0x00000A0000000000L,
            0x0000140000000000L,
            0x0000280000000000L,
            0x0000500000000000L,
            0x0000A00000000000L,
            0x0000400000000000L,
            0x0002000000000000L,
            0x0005000000000000L,
            0x000A000000000000L,
            0x0014000000000000L,
            0x0028000000000000L,
            0x0050000000000000L,
            0x00A0000000000000L,
            0x0040000000000000L,
            0x0200000000000000L,
            0x0500000000000000L,
            0x0A00000000000000L,
            0x1400000000000000L,
            0x2800000000000000L,
            0x5000000000000000L,
            0xA000000000000000L,
            0x4000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L,
            0x0000000000000000L};
}
