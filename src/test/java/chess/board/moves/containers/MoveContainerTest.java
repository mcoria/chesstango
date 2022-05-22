package chess.board.moves.containers;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
import chess.board.moves.MoveFactory;
import chess.board.moves.imp.MoveFactoryWhite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MoveContainerTest {

    private MoveContainer moveContainerImp;

    private MoveFactory factory;

    @Before
    public void setUp() throws Exception {
        moveContainerImp = new MoveContainer();
        factory = new MoveFactoryWhite();
    }

    @Test
    public void test1(){
        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e5, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, null);

        Move move1 = factory.createSimpleMove(origen, destino);
        moveContainerImp.add(move1);

        Move foundMove = null;
        for(Move move: moveContainerImp){
            if(move1.equals(move)){
                foundMove = move;
            }
        }
        Assert.assertEquals(move1, foundMove);
        Assert.assertEquals(1, moveContainerImp.size());
    }

    @Test
    public void test2(){
        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e5, Piece.ROOK_WHITE);
        PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, null);

        Move move1 = factory.createSimpleMove(origen, destino);

        MoveList moveList = new MoveList();
        moveList.add(move1);

        moveContainerImp.add(moveList);

        Move foundMove1 = null;
        for(Move move: moveContainerImp){
            if(move1.equals(move)){
                foundMove1 = move;
            }
        }
        Assert.assertEquals(move1, foundMove1);
        Assert.assertEquals(1, moveContainerImp.size());
    }

    @Test
    public void test3(){
        PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e5, Piece.ROOK_WHITE);
        PiecePositioned destino1 = PiecePositioned.getPiecePositioned(Square.e7, null);
        Move move1 = factory.createSimpleMove(origen, destino1);
        MoveList moveList1 = new MoveList();
        moveList1.add(move1);
        moveContainerImp.add(moveList1);

        PiecePositioned destino2 = PiecePositioned.getPiecePositioned(Square.e8, null);
        Move move2 = factory.createSimpleMove(origen, destino2);
        MoveList moveList2 = new MoveList();
        moveList2.add(move2);
        moveContainerImp.add(moveList2);

        Move foundMove1 = null;
        Move foundMove2= null;
        for(Move move: moveContainerImp){
            if(move1.equals(move)){
                foundMove1 = move;
            }
            if(move2.equals(move)){
                foundMove2 = move;
            }
        }

        Assert.assertEquals(move1, foundMove1);
        Assert.assertEquals(move2, foundMove2);
        Assert.assertEquals(2, moveContainerImp.size());
    }

}
