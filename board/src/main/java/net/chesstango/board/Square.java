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
    private final String fileChar;
    private final String rankChar;

    Square(int file, int rank) {
        this.file = file;
        this.rank = rank;
        this.bitPosition = 1L << toIdx();
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
        return rank * 8 + file;
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

    public static Square getSquare(int idx) {
        if (idx < 0 || idx > 64) {
            return null;
        }
        return array[idx];
    }

    public Square getMirrorSquare() {
        return getSquare(file, 7 - rank);
    }
}
