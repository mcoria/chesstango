package net.chesstango.search.smartminmax;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AbstractSmartTest {

    private AbstractSmart abstractSmart;

    @Before
    public void setup(){
        abstractSmart = new AbstractSmart(){

            @Override
            public SearchMoveResult searchBestMove(Game game) {
                return null;
            }

            @Override
            public SearchMoveResult searchBestMove(Game game, int depth) {
                return null;
            }

            @Override
            public void setGameEvaluator(GameEvaluator evaluator) {

            }
        };
    }

    @Test
    public void selectMoveTest01(){
        Game game = FENDecoder.loadGame("r4rk1/1pp2ppp/p2b1n2/3pp3/8/PPNbPN2/3P1PPP/R1B1K2R b KQ - 0 14");

        MoveContainerReader possibleMovesMoves = game.getPossibleMoves();

        List<Move> moves = new ArrayList<>();

        possibleMovesMoves.forEach(moves::add);

        Move selectedMove = abstractSmart.selectMove(game.getChessPosition().getCurrentTurn(), moves);

        Assert.assertEquals(Square.d3, selectedMove.getFrom().getSquare());
        Assert.assertEquals(Square.b1, selectedMove.getTo().getSquare());
    }


    @Test
    public void selectMoveTest02(){
        Game game = FENDecoder.loadGame("4k3/8/8/8/8/8/1p4p1/RN2K3 b Q - 0 1");

        MoveContainerReader possibleMovesMoves = game.getPossibleMoves();

        List<Move> moves = new ArrayList<>();

        possibleMovesMoves.forEach(moves::add);

        Move selectedMove = abstractSmart.selectMove(game.getChessPosition().getCurrentTurn(), moves);

        Assert.assertEquals(Square.b2, selectedMove.getFrom().getSquare());
        Assert.assertEquals(Square.a1, selectedMove.getTo().getSquare());
        Assert.assertTrue(selectedMove instanceof MovePromotion);
        Assert.assertEquals(Piece.QUEEN_BLACK, ((MovePromotion)selectedMove).getPromotion() );

    }

    @Test
    public void selectMoveTest04(){
        Game game = FENDecoder.loadGame("rn2k3/1P4P1/8/8/8/8/8/4K3 w q - 0 1");

        MoveContainerReader possibleMovesMoves = game.getPossibleMoves();

        List<Move> moves = new ArrayList<>();

        possibleMovesMoves.forEach(moves::add);

        Move selectedMove = abstractSmart.selectMove(game.getChessPosition().getCurrentTurn(), moves);

        Assert.assertEquals(Square.b7, selectedMove.getFrom().getSquare());
        Assert.assertEquals(Square.a8, selectedMove.getTo().getSquare());
        Assert.assertTrue(selectedMove instanceof MovePromotion);
        Assert.assertEquals(Piece.QUEEN_WHITE, ((MovePromotion)selectedMove).getPromotion() );

    }
}
