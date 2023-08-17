package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
//TODO: En vez de computar los saltos podriamos ya tenerlos precargados para usar un iterador de bits
public abstract class AbstractJumpMoveGenerator extends AbstractMoveGenerator {

    /**
     * Factory Method
     */
    protected abstract Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);

    /**
     * Factory Method
     */
    protected abstract Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);

    protected abstract Iterator<Square> getSquareIterator(Square fromSquare);

    public AbstractJumpMoveGenerator(Color color) {
        super(color);
    }

    //El calculo de movimientos lo puede hacer en funcion de ColorBoard

    @Override
    public MoveGeneratorResult generatePseudoMoves(PiecePositioned from) {
        MoveGeneratorResult result = new MoveGeneratorResult(from);
        Square fromSquare = from.getSquare();
        Iterator<Square> iterator = getSquareIterator(fromSquare);
        while (iterator.hasNext()) {
            Square to = iterator.next();
            result.addAffectedByPositions(to);
            result.addCapturedPositions(to);
            Color colorDestino = bitBoard.getColor(to);
            if (colorDestino == null) {
                Move move = createSimpleMove(from, squareBoard.getPosition(to));
                result.addPseudoMove(move);
            } else if (color.oppositeColor().equals(colorDestino)) {
                Move move = createCaptureMove(from, squareBoard.getPosition(to));
                result.addPseudoMove(move);
            }
            // else if(color.equals(pieza.getColor())){
            // continue;
            // }
        }
        return result;
    }

}
