package chess.board.movesgenerators.pseudo.strategies;

import java.util.Iterator;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.bysquares.CardinalSquareIterator;
import chess.board.moves.Move;
import chess.board.movesgenerators.pseudo.MoveGeneratorResult;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractCardinalMoveGenerator extends AbstractMoveGenerator {
	
	/*
	 * Factory Method
	 */
	protected abstract Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

	/*
	 * Factory Method
	 */	
	protected abstract Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);
	
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
		Square squareFrom = from.getKey();
		Iterator<Square> iterator = new CardinalSquareIterator(squareFrom, cardinal);
		while (iterator.hasNext()) {
			Square to = iterator.next();
			result.addAffectedByPositions(to);
			result.addCapturedPositions(to);
			Color colorDestino = colorBoard.getColor(to);
			if (colorDestino == null) {
				Move move = createSimpleMove(from, piecePlacement.getPosicion(to), cardinal);
				result.addPseudoMove(move);
			} else if (color.oppositeColor().equals(colorDestino)) {
				Move move = createCaptureMove(from, piecePlacement.getPosicion(to), cardinal);
				result.addPseudoMove(move);
				break;
			} else { // if(color.equals(pieza.getColor())){
				break;
			}
		}
	}

}
