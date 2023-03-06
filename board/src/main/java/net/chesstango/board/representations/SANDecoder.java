package net.chesstango.board.representations;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.moves.imp.CastlingBlackKingMove;
import net.chesstango.board.moves.imp.CastlingBlackQueenMove;
import net.chesstango.board.moves.imp.CastlingWhiteKingMove;
import net.chesstango.board.moves.imp.CastlingWhiteQueenMove;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.chesstango.board.Piece.PAWN_BLACK;
import static net.chesstango.board.Piece.PAWN_WHITE;

/**
 * @author Mauricio Coria
 * <p>
 * <SAN move descriptor piece moves>   ::= <Piece symbol>[<from file>|<from rank>|<from square>]['x']<to square>
 * <SAN move descriptor pawn captures> ::= 			      <from file>[<from rank>]               'x' <to square>[<promoted to>]
 * <SAN move descriptor pawn push>     ::= 														     <to square>[<promoted to>]
 */
public class SANDecoder {
    private Pattern movePattern = Pattern.compile("(?<piecemove>(?<piece>[RNBQK])(?<piecefrom>[a-h]|[1-8]|[a-h][1-8])?x?(?<pieceto>[a-h][1-8]))|" +
            "(?<pawncapture>(?<pawncapturefile>[a-h])[1-8]?x(?<pawncaptureto>[a-h][1-8])=?(?<pawncapturepromotion>[RNBQ]?))|" +
            "(?<pawnpush>(?<pawnto>[a-h][1-8])=?(?<pawnpushpromotion>[RNBQ]?))|" +
            "(?<queencasting>O-O-O)|(?<kingcastling>O-O)"
    );

    public Move decode(String moveStr, Iterable<Move> possibleMoves) {
        final Matcher matcher = movePattern.matcher(moveStr);
        if (matcher.matches()) {
            if (matcher.group("piecemove") != null) {
                return decodePieceMove(matcher, possibleMoves);
            } else if (matcher.group("pawnpush") != null) {
                return decodePawnPush(matcher, possibleMoves);
            } else if (matcher.group("pawncapture") != null) {
                return decodePawnCapture(matcher, possibleMoves);
            } else if (matcher.group("queencasting") != null) {
                return searchQueenCastling(possibleMoves);
            } else if (matcher.group("kingcastling") != null) {
                return searchKingCastling(possibleMoves);
            }
        }
        return null;
    }

    private Move searchKingCastling(Iterable<Move> possibleMoves) {
        for (Move move : possibleMoves) {
            if (move instanceof CastlingWhiteKingMove || move instanceof CastlingBlackKingMove) {
                return move;
            }
        }
        return null;
    }

    private Move searchQueenCastling(Iterable<Move> possibleMoves) {
        for (Move move : possibleMoves) {
            if (move instanceof CastlingWhiteQueenMove || move instanceof CastlingBlackQueenMove) {
                return move;
            }
        }
        return null;
    }

    private Move decodePawnPush(Matcher matcher, Iterable<Move> possibleMoves) {
        String pawnto = matcher.group("pawnto");
        String pawnpushpromotion = matcher.group("pawnpushpromotion");

        if (pawnpushpromotion.equals("")) {
            switch (pawnto) {
                case "a1":
                case "b1":
                case "c1":
                case "d1":
                case "e1":
                case "f1":
                case "g1":
                case "h1":
                case "a8":
                case "b8":
                case "c8":
                case "d8":
                case "e8":
                case "f8":
                case "g8":
                case "h8":
                    pawnpushpromotion = "Q";
            }
        }

        for (Move move : possibleMoves) {
            if (PAWN_WHITE.equals(move.getFrom().getPiece()) || PAWN_BLACK.equals(move.getFrom().getPiece())) {
                Square toSquare = move.getTo().getSquare();
                if (pawnto.equals(toSquare.toString())) {
                    if (!pawnpushpromotion.equals("") && move instanceof MovePromotion) {
                        MovePromotion movePromotion = (MovePromotion) move;
                        if (pawnpushpromotion.equals(getPieceCode(movePromotion.getPromotion()))) {
                            return movePromotion;
                        }
                    } else {
                        return move;
                    }
                }
            }
        }
        return null;
    }

    private Move decodePawnCapture(Matcher matcher, Iterable<Move> possibleMoves) {
        String pawncapturefile = matcher.group("pawncapturefile");
        String pawncaptureto = matcher.group("pawncaptureto");
        String pawncapturepromotion = matcher.group("pawncapturepromotion");
        for (Move move : possibleMoves) {
            if (PAWN_WHITE.equals(move.getFrom().getPiece()) || PAWN_BLACK.equals(move.getFrom().getPiece())) {
                Square fromSquare = move.getFrom().getSquare();
                Square toSquare = move.getTo().getSquare();
                if (pawncapturefile.equals(fromSquare.getFileChar())) {
                    if (pawncaptureto.equals(toSquare.toString())) {
                        if (!pawncapturepromotion.equals("") && move instanceof MovePromotion) {
                            MovePromotion movePromotion = (MovePromotion) move;
                            if (pawncapturepromotion.equals(getPieceCode(movePromotion.getPromotion()))) {
                                return movePromotion;
                            }
                        } else {
                            return move;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Move decodePieceMove(Matcher matcher, Iterable<Move> possibleMoves) {
        String piece = matcher.group("piece");
        String piecefrom = matcher.group("piecefrom");
        String pieceto = matcher.group("pieceto");
        for (Move move : possibleMoves) {
            Piece thePiece = move.getFrom().getPiece();
            if (!PAWN_WHITE.equals(thePiece) && !PAWN_BLACK.equals(thePiece) && piece.equals(getPieceCode(move.getFrom().getPiece()))) {
                Square fromSquare = move.getFrom().getSquare();
                Square toSquare = move.getTo().getSquare();
                if (piecefrom == null || piecefrom !=null && (piecefrom.equals(fromSquare.getFileChar()) || piecefrom.equals(fromSquare.getRankChar()) || piecefrom.equals(fromSquare.toString()))) {
                    if (pieceto.equals(toSquare.toString())) {
                        return move;
                    }
                }
            }
        }
        return null;
    }

    private String getPieceCode(Piece piece) {
        switch (piece) {
            case PAWN_WHITE:
            case PAWN_BLACK:
                throw new RuntimeException("You should not call this method with pawn");
            case ROOK_WHITE:
            case ROOK_BLACK:
                return "R";
            case KNIGHT_WHITE:
            case KNIGHT_BLACK:
                return "N";
            case BISHOP_WHITE:
            case BISHOP_BLACK:
                return "B";
            case QUEEN_WHITE:
            case QUEEN_BLACK:
                return "Q";
            case KING_WHITE:
            case KING_BLACK:
                return "K";
            default:
                throw new RuntimeException("Falta pieza");
        }
    }
}
