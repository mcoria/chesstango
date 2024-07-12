package net.chesstango.board.representations.fen;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.builders.AbstractChessPositionBuilder;

/**
 * @author Mauricio Coria
 */
public class FENEncoderWithoutClocks extends AbstractChessPositionBuilder<String> {

    @Override
    public String getChessRepresentation() {
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
        for (int i = 0; i < piezas.length; i++) {
            if (piezas[i] == null) {
                vacios++;
            } else {
                if (vacios > 0) {
                    stringBuilder.append(vacios);
                    vacios = 0;
                }
                stringBuilder.append(getCode(piezas[i]));
            }
        }

        if (vacios > 0) {
            stringBuilder.append(vacios);
        }

        return stringBuilder;
    }

    private char getCode(Piece piece) {
        char result;
        switch (piece) {
            case ROOK_BLACK:
                result = 'r';
                break;
            case KNIGHT_BLACK:
                result = 'n';
                break;
            case QUEEN_BLACK:
                result = 'q';
                break;
            case KING_BLACK:
                result = 'k';
                break;
            case PAWN_BLACK:
                result = 'p';
                break;
            case BISHOP_BLACK:
                result = 'b';
                break;
            case ROOK_WHITE:
                result = 'R';
                break;
            case KNIGHT_WHITE:
                result = 'N';
                break;
            case QUEEN_WHITE:
                result = 'Q';
                break;
            case KING_WHITE:
                result = 'K';
                break;
            case PAWN_WHITE:
                result = 'P';
                break;
            case BISHOP_WHITE:
                result = 'B';
                break;
            default:
                throw new RuntimeException("Falta pieza");
        }
        return result;
    }

}
