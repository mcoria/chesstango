package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import net.chesstango.board.moves.Move;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractCardinalMoveGenerator extends AbstractMoveGenerator {
	
	/**
	 * Factory Method
	 */
	protected abstract Move createSimpleMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

	/**
	 * Factory Method
	 */	
	protected abstract Move createCaptureMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);
	
	private final Cardinal[] directions;

	public AbstractCardinalMoveGenerator(Color color, Cardinal[] directions) {
		super(color);
		this.directions = directions;
	}

	//TODO: explorar streams
	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned from){
		MoveGeneratorResult result = new MoveGeneratorResult(from);
		for (Cardinal cardinal : this.directions) {
			getPseudoMoves(result, cardinal);
		}
		return result;
	}
	
	/*
	 * Template method
	 */	
	protected void getPseudoMoves(MoveGeneratorResult result, Cardinal cardinal) {
		PiecePositioned from = result.getFrom();
		Square squareFrom = from.getSquare();
		Iterator<Square> iterator = new CardinalSquareIterator(squareFrom, cardinal);
		while (iterator.hasNext()) {
			Square to = iterator.next();
			result.addAffectedByPositions(to);
			result.addCapturedPositions(to);
			Color colorDestino = bitBoard.getColor(to);
			if (colorDestino == null) {
				Move move = createSimpleMove(from, squareBoard.getPosition(to), cardinal);
				result.addPseudoMove(move);
			} else if (color.oppositeColor().equals(colorDestino)) {
				Move move = createCaptureMove(from, squareBoard.getPosition(to), cardinal);
				result.addPseudoMove(move);
				break;
			} else { // if(color.equals(pieza.getColor())){
				break;
			}
		}
	}

}
