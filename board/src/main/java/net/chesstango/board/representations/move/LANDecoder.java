package net.chesstango.board.representations.move;

import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mauricio Coria
 * <p>
 * <LAN move descriptor piece moves> ::= <Piece symbol><from square>['-'|'x']<to square>
 * <LAN move descriptor pawn moves>  ::= <from square>['-'|'x']<to square>[<promoted to>]
 * <Piece symbol> ::= 'N' | 'B' | 'R' | 'Q' | 'K'
 */
public class LANDecoder {
    private static final Pattern edpMovePattern = Pattern.compile("(" +
            "(?<piecemove>(?<piece>[RNBQK]?)((?<from>[a-h][1-8])|(?<fromfile>[a-h])|(?<fromrank>[1-8]))?[-x]?(?<to>[a-h][1-8]))|" +
            "(?<pawnmove>(?<pawnfrom>[a-h][1-8])[-x](?<pawnto>[a-h][1-8])(?<promotionpiece>[RNBQK]))" +
            ")\\+?");

    public Move decode(String moveStr, Iterable<Move> possibleMoves) {
        final Matcher matcher = edpMovePattern.matcher(moveStr);
        if (matcher.matches()) {
            if (matcher.group("piecemove") != null) {
                return decodePieceMove(matcher, possibleMoves);
            } else if (matcher.group("pawnmove") != null) {
                return decodePawnMove(matcher, possibleMoves);
            }
        }
        return null;
    }

    private Move decodePieceMove(Matcher matcher, Iterable<Move> possibleMoves) {
        String pieceStr = matcher.group("piece");
        String fromStr = matcher.group("from");
        String fromFileStr = matcher.group("fromfile");
        String fromRankStr = matcher.group("fromrank");
        String toStr = matcher.group("to");
        for (Move move : possibleMoves) {
            if (pieceStr != null && pieceStr.length() == 1) {
                if (move.getFrom().getPiece().isPawn()) {
                    continue;
                }
                if (!getPieceCode(move.getFrom().getPiece()).equals(pieceStr)) {
                    continue;
                }
            } else {
                if (!move.getFrom().getPiece().isPawn()) {
                    continue;
                }
            }
            if (fromStr != null) {
                if (!move.getFrom().getSquare().toString().equals(fromStr)) {
                    continue;
                }
            }
            if (fromFileStr != null) {
                if (!move.getFrom().getSquare().getFileChar().equals(fromFileStr)) {
                    continue;
                }
            }
            if (fromRankStr != null) {
                if (!move.getFrom().getSquare().getRankChar().equals(fromRankStr)) {
                    continue;
                }
            }
            if (move.getTo().getSquare().toString().equals(toStr)) {
                return move;
            }

        }
        return null;
    }

    private Move decodePawnMove(Matcher matcher, Iterable<Move> possibleMoves) {
        String promotionPieceStr = matcher.group("promotionpiece");
        String fromStr = matcher.group("pawnfrom");
        String toStr = matcher.group("pawnto");
        for (Move move : possibleMoves) {
            if (move instanceof MovePromotion movePromotion) {
                if (move.getFrom().getSquare().toString().equals(fromStr) && move.getTo().getSquare().toString().equals(toStr) && getPieceCode(movePromotion.getPromotion()).equals(promotionPieceStr)) {
                    return move;
                }
            }
        }
        return null;
    }


    private String getPieceCode(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE, PAWN_BLACK -> throw new RuntimeException("You should not call this method with pawn");
            case ROOK_WHITE, ROOK_BLACK -> "R";
            case KNIGHT_WHITE, KNIGHT_BLACK -> "N";
            case BISHOP_WHITE, BISHOP_BLACK -> "B";
            case QUEEN_WHITE, QUEEN_BLACK -> "Q";
            case KING_WHITE, KING_BLACK -> "K";
            default -> throw new RuntimeException("Falta pieza");
        };
    }
}
