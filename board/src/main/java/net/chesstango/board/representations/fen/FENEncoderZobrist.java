package net.chesstango.board.representations.fen;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.builders.AbstractPositionBuilder;

/**
 * @author Mauricio Coria
 */
public class FENEncoderZobrist extends AbstractPositionBuilder<String> {

    @Override
    public String getChessRepresentation() {
        StringBuilder stringBuilder = new StringBuilder(70);

        getPiecePlacement(stringBuilder).append(' ');

        getTurno(stringBuilder).append(' ');

        getEnroques(stringBuilder).append(' ');

        getEnPassantZobrist(stringBuilder);

        return stringBuilder.toString();
    }

    public StringBuilder getTurno(StringBuilder stringBuilder) {
        return Color.WHITE.equals(turn) ? stringBuilder.append('w') : stringBuilder.append('b');
    }

    public StringBuilder getPiecePlacement(StringBuilder stringBuilder) {
        for (int i = 7; i >= 0; i--) {
            codePiecePlacementRank(board[i], stringBuilder);
            if (i > 0) {
                stringBuilder.append('/');
            }
        }
        return stringBuilder;
    }

    private StringBuilder getEnPassantZobrist(StringBuilder stringBuilder) {
        if (enPassantSquare == null) {
            stringBuilder.append('-');
        } else {
            if (Color.WHITE.equals(turn)) {
                if (enPassantSquare.getFile() - 1 >= 0 && board[4][enPassantSquare.getFile() - 1] == Piece.PAWN_WHITE
                        || enPassantSquare.getFile() + 1 < 8 && board[4][enPassantSquare.getFile() + 1] == Piece.PAWN_WHITE) {
                    stringBuilder.append(enPassantSquare);
                } else {
                    stringBuilder.append('-');
                }
            } else {
                if (enPassantSquare.getFile() - 1 >= 0 && board[3][enPassantSquare.getFile() - 1] == Piece.PAWN_BLACK
                        || enPassantSquare.getFile() + 1 < 8 && board[3][enPassantSquare.getFile() + 1] == Piece.PAWN_BLACK) {
                    stringBuilder.append(enPassantSquare);
                } else {
                    stringBuilder.append('-');
                }
            }
        }
        return stringBuilder;
    }

    public StringBuilder getEnroques(StringBuilder stringBuilder) {
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
        char result = switch (piece) {
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
        return result;
    }

    public static String encodeGame(Game game) {
        FENEncoderZobrist encoder = new FENEncoderZobrist();
        game.getChessPosition().constructChessPositionRepresentation(encoder);
        return encoder.getChessRepresentation();
    }

}
