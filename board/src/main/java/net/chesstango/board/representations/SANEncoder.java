package net.chesstango.board.representations;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MovePromotion;

/**
 * @author Mauricio Coria
 *
 * <SAN move descriptor piece moves>   ::= <Piece symbol>[<from file>|<from rank>|<from square>]['x']<to square>
 * <SAN move descriptor pawn captures> ::= 			      <from file>[<from rank>]               'x' <to square>[<promoted to>]
 * <SAN move descriptor pawn push>     ::= 														     <to square>[<promoted to>]
 *
 */
public class SANEncoder {

    public String encode(Move move, Iterable<Move> possibleMoves) {
        if(move instanceof MoveCastling){
            MoveCastling moveCastling = (MoveCastling) move;
            return encodeMoveCastling(moveCastling);
        } else if(move.getFrom().getPiece().isPawn()){
            return encodePawnMove(move, possibleMoves);
        }
        return encodePieceMove(move, possibleMoves);
    }

    public String encodePieceMove(Move move, Iterable<Move>  possibleMoves) {
        StringBuilder sb = new StringBuilder();
        PiecePositioned from = move.getFrom();
        PiecePositioned to = move.getTo();
        Piece piece = from.getPiece();
        boolean capture = to.getPiece() == null ? false : true;

        sb.append(getPieceCode(piece));
        sb.append(solvePieceAmbiguityFrom(move, possibleMoves));

        if(capture){
            sb.append("x");
        }

        sb.append(to.getSquare());

        return sb.toString();
    }

    private String encodePawnMove(Move move, Iterable<Move> possibleMoves) {
        StringBuilder sb = new StringBuilder();
        PiecePositioned from = move.getFrom();
        PiecePositioned to = move.getTo();
        boolean capture = Cardinal.Sur.equals(move.getMoveDirection()) || Cardinal.Norte.equals(move.getMoveDirection()) ? false : true;

        if(capture) {
            sb.append(from.getSquare().getFileChar());
        }

        if(capture){
            sb.append("x");
        }

        sb.append(to.getSquare());

        if(move instanceof MovePromotion){
            MovePromotion movePromotion = (MovePromotion) move;
            sb.append("=" + getPieceCode(movePromotion.getPromotion()));
        }

        return sb.toString();
    }

    private String encodeMoveCastling(MoveCastling moveCastling) {
        Square rookFromSquare = moveCastling.getRookFrom().getSquare();
        if(rookFromSquare.getFile() == 0){
            return "O-O-O";
        } else {
            return "O-O";
        }
    }


    private String solvePieceAmbiguityFrom(Move move, Iterable<Move>  possibleMoves) {
        PiecePositioned from = move.getFrom();
        PiecePositioned to = move.getTo();
        Piece piece = from.getPiece();

        boolean solveAmb = false;
        boolean fileAmb = false;
        boolean rankAmb = false;

        for (Move aMove: possibleMoves){
            PiecePositioned aMoveFrom = aMove.getFrom();
            PiecePositioned aMoveTo = aMove.getTo();
            Piece aMovePiece = aMoveFrom.getPiece();
            if(!aMoveFrom.equals(from) && aMoveTo.equals(to) && aMovePiece.equals(piece)){
                solveAmb = true;
                if(aMoveFrom.getSquare().getFile() == from.getSquare().getFile() ){
                    fileAmb = true;
                }
                if(aMoveFrom.getSquare().getRank() == from.getSquare().getRank() ){
                    rankAmb = true;
                }
            }
        };

        if(solveAmb){
            if(fileAmb == false){
                return from.getSquare().getFileChar();
            } else if(rankAmb == false){
                return from.getSquare().getRankChar();
            } else{
                return from.getSquare().toString();
            }
        }

        return "";
    }


    private char getPieceCode(Piece piece) {
        switch (piece) {
            case PAWN_WHITE:
            case PAWN_BLACK:
                throw new RuntimeException("You should not call this method with pawn");
            case ROOK_WHITE:
            case ROOK_BLACK:
                return 'R';
            case KNIGHT_WHITE:
            case KNIGHT_BLACK:
                return 'N';
            case BISHOP_WHITE:
            case BISHOP_BLACK:
                return 'B';
            case QUEEN_WHITE:
            case QUEEN_BLACK:
                return 'Q';
            case KING_WHITE:
            case KING_BLACK:
                return 'K';
            default:
                throw new RuntimeException("Falta pieza");
        }
    }

}
