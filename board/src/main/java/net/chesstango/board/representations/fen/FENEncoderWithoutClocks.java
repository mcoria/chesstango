package net.chesstango.board.representations.fen;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.representations.AbstractPositionBuilder;

/**
 * @author Mauricio Coria
 */
public class FENEncoderWithoutClocks extends AbstractPositionBuilder<String> {

    @Override
    public String getPositionRepresentation() {
        StringBuilder stringBuilder = new StringBuilder(70);

        getPiecePlacement(stringBuilder).append(' ');

        getTurno(stringBuilder).append(' ');

        getEnroques(stringBuilder).append(' ');

        getEnPassant(stringBuilder);

        return stringBuilder.toString();
    }


    protected StringBuilder getTurno(StringBuilder stringBuilder) {
        return Color.WHITE.equals(turn) ? stringBuilder.append('w') : stringBuilder.append('b');
    }

    protected StringBuilder getPiecePlacement(StringBuilder stringBuilder) {
        for (int i = 7; i >= 0; i--) {
            codePiecePlacementRank(board[i], stringBuilder);
            if (i > 0) {
                stringBuilder.append('/');
            }
        }
        return stringBuilder;
    }

    protected StringBuilder getEnPassant(StringBuilder stringBuilder) {
        if (enPassantSquare == null) {
            stringBuilder.append('-');
        } else {
            stringBuilder.append(enPassantSquare);
        }
        return stringBuilder;
    }

    protected StringBuilder getEnroques(StringBuilder stringBuilder) {
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

        return stringBuilder;
    }

    protected StringBuilder codePiecePlacementRank(Piece[] piezas, StringBuilder stringBuilder) {
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

        return stringBuilder;
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
