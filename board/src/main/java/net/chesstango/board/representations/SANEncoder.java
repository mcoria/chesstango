package net.chesstango.board.representations;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
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
            Move rookMove = moveCastling.getRookMove();
            Square rookFromSquare = rookMove.getFrom().getSquare();
            if(rookFromSquare.getFile() == 0){
                return "O-O-O";
            } else {
                return "O-O";
            }
        } else {
            return encodeNotCastlingMove(move, possibleMoves);
        }
    }

    public String encodeNotCastlingMove(Move move, Iterable<Move>  possibleMoves) {
        StringBuilder sb = new StringBuilder();


        PiecePositioned from = move.getFrom();
        PiecePositioned to = move.getTo();
        Piece piece = from.getPiece();
        boolean capture = to.getPiece() == null ? false : true;

        if(Piece.PAWN_WHITE.equals(piece) || Piece.PAWN_BLACK.equals(piece)){
            if(capture){
                sb.append(getFile(from));
            }
        } else {
            sb.append(getPieceCode(piece));
            sb.append(solveAmbiguityFrom(move, possibleMoves));
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

    private String solveAmbiguityFrom(Move move, Iterable<Move>  possibleMoves) {
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
                return Character.toString(getFile(from));
            } else if(rankAmb == false){
                return Integer.toString (from.getSquare().getRank() + 1);
            } else{
                return from.getSquare().toString();
            }
        }

        return "";
    }

    private char getFile(PiecePositioned position) {
        switch (position.getSquare().getFile()){
            case 0:
                return 'a';
            case 1:
                return 'b';
            case 2:
                return 'c';
            case 3:
                return 'd';
            case 4:
                return 'e';
            case 5:
                return 'f';
            case 6:
                return 'g';
            case 7:
                return 'h';
        }
        throw new RuntimeException("Imposible");
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
