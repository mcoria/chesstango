package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.gardel.fen.FENParser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class MoveSelectorTest {

    @Test
    public void selectMoveTest01() {
        Game game = Game.fromFEN("r4rk1/1pp2ppp/p2b1n2/3pp3/8/PPNbPN2/3P1PPP/R1B1K2R b KQ - 0 14");

        MoveContainerReader<? extends Move> possibleMovesMoves = game.getPossibleMoves();

        List<Move> moves = new ArrayList<>();

        possibleMovesMoves.forEach(moves::add);

        Move selectedMove = MoveSelector.selectMove(game.getPosition().getCurrentTurn(), moves);

        assertEquals(Square.d3, selectedMove.getFrom().getSquare());
        assertEquals(Square.b1, selectedMove.getTo().getSquare());
    }

    @Test
    public void selectMoveTest01_mirror() {
        Game game = Game.fromFEN("r4rk1/1pp2ppp/p2b1n2/3pp3/8/PPNbPN2/3P1PPP/R1B1K2R b KQ - 0 14");

        Game gameMirror = game.mirror();

        MoveContainerReader<? extends Move> possibleMovesMoves = gameMirror.getPossibleMoves();

        List<Move> moves = new ArrayList<>();

        possibleMovesMoves.forEach(moves::add);

        Move selectedMove = MoveSelector.selectMove(gameMirror.getPosition().getCurrentTurn(), moves);

        assertEquals(Square.d6, selectedMove.getFrom().getSquare());
        assertEquals(Square.b8, selectedMove.getTo().getSquare());
    }


    @Test
    public void selectMoveTest02() {
        Game game = Game.fromFEN("4k3/8/8/8/8/8/1p4p1/RN2K3 b Q - 0 1");

        MoveContainerReader<? extends Move> possibleMovesMoves = game.getPossibleMoves();

        List<Move> moves = new ArrayList<>();

        possibleMovesMoves.forEach(moves::add);

        Move selectedMove = MoveSelector.selectMove(game.getPosition().getCurrentTurn(), moves);

        assertEquals(Square.b2, selectedMove.getFrom().getSquare());
        assertEquals(Square.a1, selectedMove.getTo().getSquare());
        assertInstanceOf(MovePromotion.class, selectedMove);
        assertEquals(Piece.QUEEN_BLACK, ((MovePromotion) selectedMove).getPromotion());

    }

    @Test
    public void selectMoveTest02_mirror() {
        Game game = Game.fromFEN("4k3/8/8/8/8/8/1p4p1/RN2K3 b Q - 0 1");

        Game gameMirror = game.mirror();

        MoveContainerReader<? extends Move> possibleMovesMoves = gameMirror.getPossibleMoves();

        List<Move> moves = new ArrayList<>();

        possibleMovesMoves.forEach(moves::add);

        Move selectedMove = MoveSelector.selectMove(gameMirror.getPosition().getCurrentTurn(), moves);

        assertEquals(Square.b7, selectedMove.getFrom().getSquare());
        assertEquals(Square.a8, selectedMove.getTo().getSquare());
        assertInstanceOf(MovePromotion.class, selectedMove);
        assertEquals(Piece.QUEEN_WHITE, ((MovePromotion) selectedMove).getPromotion());

    }

}
