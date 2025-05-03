package net.chesstango.board.representations.fen;

import net.chesstango.board.Piece;
import net.chesstango.board.representations.AbstractPositionBuilder;

/**
 * @author Mauricio Coria
 */
public class FENBuilder extends AbstractPositionBuilder<FEN> {

    @Override
    public FEN getPositionRepresentation() {
        String piecePlacement = getPiecePlacement();
        String activeColor = getTurno();
        String castingsAllowed = getEnroques();
        String enPassantSquare = getEnPassant();
        String halfMoveClock = getHalfMoveClock();
        String fullMoveClock = getFullMoveClock();

        return new FEN(piecePlacement,
                activeColor,
                castingsAllowed,
                enPassantSquare,
                halfMoveClock,
                fullMoveClock);
    }

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
        for (int i = 7; i >= 0; i--) {
            codePiecePlacementRank(board[i], stringBuilder);
            if (i > 0) {
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

    String codePiecePlacementRank(Piece[] piezas, StringBuilder stringBuilder) {
        int vacios = 0;
        for (Piece pieza : piezas) {
            if (pieza == null) {
                vacios++;
            } else {
                if (vacios > 0) {
                    stringBuilder.append(vacios);
                    vacios = 0;
                }
                stringBuilder.append(getCode(pieza));
            }
        }

        if (vacios > 0) {
            stringBuilder.append(vacios);
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

    private char getCode(Piece piece) {
        return switch (piece) {
            case ROOK_BLACK -> 'r';
            case KNIGHT_BLACK -> 'n';
            case QUEEN_BLACK -> 'q';
            case KING_BLACK -> 'k';
            case PAWN_BLACK -> 'p';
            case BISHOP_BLACK -> 'b';
            case ROOK_WHITE -> 'R';
            case KNIGHT_WHITE -> 'N';
            case QUEEN_WHITE -> 'Q';
            case KING_WHITE -> 'K';
            case PAWN_WHITE -> 'P';
            case BISHOP_WHITE -> 'B';
            default -> throw new RuntimeException("Falta pieza");
        };
    }

}
