package net.chesstango.board.representations.fen;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.builders.AbstractChessPositionBuilder;

/**
 * @author Mauricio Coria
 */
public class FENEncoder extends AbstractChessPositionBuilder<FEN> {

    @Override
    public FEN getChessRepresentation() {
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

    protected String getHalfMoveClock() {
        return Integer.toString(this.halfMoveClock);
    }

    protected String getFullMoveClock() {
        return Integer.toString(this.fullMoveClock);
    }


    protected String getTurno() {
        return Color.WHITE.equals(turn) ? "w" : "b";
    }

    protected String getPiecePlacement() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            codePiecePlacementRank(board[i], stringBuilder);
            if (i > 0) {
                stringBuilder.append('/');
            }
        }
        return stringBuilder.toString();
    }

    protected String getEnPassant() {
        if (enPassantSquare != null) {
            return enPassantSquare.toString();
        }
        return "-";
    }

    protected String getEnroques() {
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

    protected String codePiecePlacementRank(Piece[] piezas, StringBuilder stringBuilder) {
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

        return stringBuilder.toString();
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
