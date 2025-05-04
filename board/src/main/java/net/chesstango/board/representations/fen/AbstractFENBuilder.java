package net.chesstango.board.representations.fen;

import net.chesstango.board.representations.AbstractPositionBuilder;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractFENBuilder<T> extends AbstractPositionBuilder<T> {


    String getHalfMoveClock() {
        return Integer.toString(this.halfMoveClock);
    }

    String getFullMoveClock() {
        return Integer.toString(this.fullMoveClock);
    }

    String getTurno() {
        return whiteTurn ? "w" : "b";
    }

    String getPiecePlacement() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            int emptySquares = 0;
            for (int file = 0; file < 8; file++) {
                long position = 1L << rank * 8 + file;
                char piece = getChar(position);
                if (piece == ' ') {
                    emptySquares++;
                } else {
                    if (emptySquares > 0) {
                        stringBuilder.append(emptySquares);
                        emptySquares = 0;
                    }
                    stringBuilder.append(piece);
                }
            }
            if (emptySquares > 0) {
                stringBuilder.append(emptySquares);
            }
            if (rank > 0) {
                stringBuilder.append('/');
            }
        }
        return stringBuilder.toString();
    }

    String getEnPassant() {
        if (enPassantSquare != 0) {
            return enPassantSquareToString(enPassantSquare);
        }
        return "-";
    }

    String getEnroques() {
        StringBuilder stringBuilder = new StringBuilder();

        if (castlingWhiteKingAllowed) {
            stringBuilder.append('K');
        }

        if (castlingWhiteQueenAllowed) {
            stringBuilder.append('Q');
        }

        if (castlingBlackKingAllowed) {
            stringBuilder.append('k');
        }

        if (castlingBlackQueenAllowed) {
            stringBuilder.append('q');
        }

        if (!castlingWhiteKingAllowed && !castlingWhiteQueenAllowed && !castlingBlackKingAllowed && !castlingBlackQueenAllowed) {
            stringBuilder.append('-');
        }

        return stringBuilder.toString();
    }

    public static String enPassantSquareToString(long enPassantSquare) {
        int enPassantSquarePosition = Long.numberOfTrailingZeros(enPassantSquare);
        if (enPassantSquarePosition == 64) {
            return "-";
        }

        int file = enPassantSquarePosition % 8;
        int rank = enPassantSquarePosition / 8;

        char fileChar = (char) ('a' + file);
        char rankChar = rank == 0 ? '1' :
                rank == 7 ? '8' :
                        (char) ('1' + rank);

        return String.valueOf(new char[]{fileChar, rankChar});
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
