package net.chesstango.board.representations.ascii;

import net.chesstango.board.representations.AbstractPositionBuilder;
import net.chesstango.board.representations.fen.FENBuilder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class ASCIIBuilder extends AbstractPositionBuilder<String> {


    @Override
    public String getPositionRepresentation() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baos)) {
            getPiecePlacement(ps);
            getState(ps);
        }
        return baos.toString();
    }


    public void getState(PrintStream printStream) {
        printStream.println("Turn: " + String.format("%-6s", whiteTurn ? "WHITE" : "BLACK") +
                ", enPassantSquare: " + String.format("%-2s", FENBuilder.enPassantSquareToString(enPassantSquare)) +
                ", castlingWhiteQueenAllowed: " + castlingWhiteQueenAllowed +
                ", castlingWhiteKingAllowed: " + castlingWhiteKingAllowed +
                ", castlingBlackQueenAllowed: " + castlingBlackQueenAllowed +
                ", castlingBlackKingAllowed: " + castlingBlackKingAllowed);
    }

    public void getPiecePlacement(PrintStream printStream) {
        printStream.println("  -------------------------------");

        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                if (file == 0) {
                    printStream.print((rank + 1));
                }

                long position = 1L << rank * 8 + file;

                printStream.print("| " + getChar(position) + " ");

                if (file == 7) {
                    printStream.println("|");
                    printStream.println("  -------------------------------");
                }
            }
        }

        printStream.println("   a   b   c   d   e   f   g   h");

        printStream.flush();
    }

    private char getChar(long position) {
        if ((whitePositions & kingPositions & position) != 0) {
            return 'K';
        }
        if ((whitePositions & queenPositions & position) != 0) {
            return 'Q';
        }
        if ((whitePositions & rookPositions & position) != 0) {
            return 'R';
        }
        if ((whitePositions & bishopPositions & position) != 0) {
            return 'B';
        }
        if ((whitePositions & knightPositions & position) != 0) {
            return 'N';
        }
        if ((whitePositions & pawnPositions & position) != 0) {
            return 'P';
        }

        if ((blackPositions & kingPositions & position) != 0) {
            return 'k';
        }
        if ((blackPositions & queenPositions & position) != 0) {
            return 'q';
        }
        if ((blackPositions & rookPositions & position) != 0) {
            return 'r';
        }
        if ((blackPositions & bishopPositions & position) != 0) {
            return 'b';
        }
        if ((blackPositions & knightPositions & position) != 0) {
            return 'n';
        }
        if ((blackPositions & pawnPositions & position) != 0) {
            return 'p';
        }
        return ' ';
    }
}
