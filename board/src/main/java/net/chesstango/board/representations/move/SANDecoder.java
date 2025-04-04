package net.chesstango.board.representations.move;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MovePromotion;

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
    public static final Pattern movePattern = Pattern.compile("(" +
            "(?<piecemove>(?<piece>[RNBQK])(?<piecefrom>[a-h]|[1-8]|[a-h][1-8])?x?(?<pieceto>[a-h][1-8]))|" +
            "(?<pawncapture>(?<pawncapturefile>[a-h])[1-8]?x(?<pawncaptureto>[a-h][1-8])=?(?<pawncapturepromotion>[RNBQ]?))|" +
            "(?<pawnpush>(?<pawnto>[a-h][1-8])=?(?<pawnpushpromotion>[RNBQ]?))|" +
            "(?<queencaslting>O-O-O)|" +
            "(?<kingcastling>O-O)" +
            ")[+#]?"
    );

    public Move decode(String moveStr, Iterable<? extends Move> possibleMoves) {
        final Matcher matcher = movePattern.matcher(moveStr);
        if (matcher.matches()) {
            if (matcher.group("piecemove") != null) {
                return decodePieceMove(matcher, possibleMoves);
            } else if (matcher.group("pawnpush") != null) {
                return decodePawnPush(matcher, possibleMoves);
            } else if (matcher.group("pawncapture") != null) {
                return decodePawnCapture(matcher, possibleMoves);
            } else if (matcher.group("queencaslting") != null) {
                return searchQueenCastling(possibleMoves);
            } else if (matcher.group("kingcastling") != null) {
                return searchKingCastling(possibleMoves);
            }
        }
        return null;
    }

    private Move searchKingCastling(Iterable<? extends Move> possibleMoves) {
        for (Move move : possibleMoves) {
            if (move instanceof MoveCastling && move.getTo().getSquare().getFile() == 6) {
                return move;
            }
        }
        return null;
    }

    private Move searchQueenCastling(Iterable<? extends Move> possibleMoves) {
        for (Move move : possibleMoves) {
            if (move instanceof MoveCastling && move.getTo().getSquare().getFile() == 2) {
                return move;
            }
        }
        return null;
    }

    private Move decodePawnPush(Matcher matcher, Iterable<? extends Move> possibleMoves) {
        String pawnto = matcher.group("pawnto");
        String pawnpushpromotion = matcher.group("pawnpushpromotion");

        if (pawnpushpromotion.equals("")) {
            pawnpushpromotion = switch (pawnto) {
                case "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8" ->
                        "Q";
                default -> pawnpushpromotion;
            };
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

    private Move decodePawnCapture(Matcher matcher, Iterable<? extends Move> possibleMoves) {
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

    private Move decodePieceMove(Matcher matcher, Iterable<? extends Move> possibleMoves) {
        String piece = matcher.group("piece");
        String piecefrom = matcher.group("piecefrom");
        String pieceto = matcher.group("pieceto");
        for (Move move : possibleMoves) {
            Piece thePiece = move.getFrom().getPiece();
            if (!PAWN_WHITE.equals(thePiece) && !PAWN_BLACK.equals(thePiece) && piece.equals(getPieceCode(move.getFrom().getPiece()))) {
                Square fromSquare = move.getFrom().getSquare();
                Square toSquare = move.getTo().getSquare();
                if (piecefrom == null || piecefrom.equals(fromSquare.getFileChar()) || piecefrom.equals(fromSquare.getRankChar()) || piecefrom.equals(fromSquare.toString())) {
                    if (pieceto.equals(toSquare.toString())) {
                        return move;
                    }
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
