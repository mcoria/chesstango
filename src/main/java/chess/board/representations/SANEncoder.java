package chess.board.representations;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
import chess.board.moves.MoveCastling;
import chess.board.moves.MovePromotion;
import chess.board.moves.containers.MoveContainerReader;

/**
 * @author Mauricio Coria
 *
 * <SAN move descriptor piece moves>   ::= <Piece symbol>[<from file>|<from rank>|<from square>]['x']<to square>
 * <SAN move descriptor pawn captures> ::= 			   <from file>[<from rank>]               'x' <to square>[<promoted to>]
 * <SAN move descriptor pawn push>     ::= 														  <to square>[<promoted to>]
 *
 */
public class SANEncoder {

    public String encode(Move move, MoveContainerReader possibleMoves) {
        if(move instanceof MoveCastling){
            MoveCastling moveCastling = (MoveCastling) move;
            Move rookMove = moveCastling.getRookMove();
            Square rookFromSquare = rookMove.getFrom().getKey();
            if(rookFromSquare.getFile() == 0){
                return "O-O-O";
            } else {
                return "O-O";
            }
        } else {
            return encodeNotCastlingMove(move, possibleMoves);
        }
    }

    public String encodeNotCastlingMove(Move move, MoveContainerReader possibleMoves) {
        StringBuilder sb = new StringBuilder();


        PiecePositioned from = move.getFrom();
        PiecePositioned to = move.getTo();
        Piece piece = from.getValue();
        boolean capture = to.getValue() == null ? false : true;

        if(Piece.PAWN_WHITE.equals(piece) || Piece.PAWN_BLACK.equals(piece)){
            if(capture){
                sb.append(getFile(from));
            }
        } else {
            sb.append(gePiecetCode(piece));
            sb.append(solveAmbiguityFrom(move, possibleMoves));
        }

        if(capture){
            sb.append("x");
        }

        sb.append(to.getKey());

        if(move instanceof MovePromotion){
            MovePromotion movePromotion = (MovePromotion) move;
            sb.append(gePiecetCode(movePromotion.getPromotion()));
        }

        return sb.toString();
    }

    private String solveAmbiguityFrom(Move move, MoveContainerReader possibleMoves) {
        PiecePositioned from = move.getFrom();
        PiecePositioned to = move.getTo();
        Piece piece = from.getValue();

        boolean solveAmb = false;
        boolean fileAmb = false;
        boolean rankAmb = false;

        for (Move aMove: possibleMoves){
            PiecePositioned aMoveFrom = aMove.getFrom();
            PiecePositioned aMoveTo = aMove.getTo();
            Piece aMovePiece = aMoveFrom.getValue();
            if(!aMoveFrom.equals(from) && aMoveTo.equals(to) && aMovePiece.equals(piece)){
                solveAmb = true;
                if(aMoveFrom.getKey().getFile() == to.getKey().getFile() ){
                    fileAmb = true;
                }
                if(aMoveFrom.getKey().getRank() == to.getKey().getRank() ){
                    rankAmb = true;
                }
            }
        };

        if(solveAmb){
            if(fileAmb == false){
                return Character.toString(getFile(from));
            } else if(rankAmb == false){
                return Integer.toString (from.getKey().getRank() + 1);
            } else{
                return from.getKey().toString();
            }
        }

        return "";
    }

    private char getFile(PiecePositioned position) {
        switch (position.getKey().getFile()){
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


    private char gePiecetCode(Piece piece) {
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
