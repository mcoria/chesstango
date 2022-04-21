package chess.board.pseudomovesgenerators.strategies;

import java.util.Iterator;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.square.CardinalSquareIterator;
import chess.board.moves.Move;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractCardinalMoveGenerator extends AbstractMoveGenerator {
	
	/*
	 * Factory Method
	 */
	protected abstract Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);

	/*
	 * Factory Method
	 */	
	protected abstract Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);		
	
	private final Cardinal[] directions;

	public AbstractCardinalMoveGenerator(Color color, Cardinal[] directions) {
		super(color);
		this.directions = directions;
	}

	//TODO: explorar streams
	@Override
	public void generateMovesPseudoMoves(PiecePositioned origen) {
		for (Cardinal cardinal : this.directions) {
			getPseudoMoves(origen, cardinal);
		}
	}
	
	/*
	 * Template method
	 */	
	protected void getPseudoMoves(PiecePositioned from, Cardinal cardinal) {
		Square squareFrom = from.getKey();
		Iterator<Square> iterator = new CardinalSquareIterator(squareFrom, cardinal);
		while (iterator.hasNext()) {
			Square to = iterator.next();
			this.result.affectedByContainerAdd(to);
			this.result.capturedPositionsContainerAdd(to);
			Color colorDestino = colorBoard.getColor(to);
			if (colorDestino == null) {
				Move move = createSimpleMove(from, piecePlacement.getPosicion(to));
				result.moveContainerAdd(move);
			} else if (color.opositeColor().equals(colorDestino)) {
				Move move = createCaptureMove(from, piecePlacement.getPosicion(to));
				result.moveContainerAdd(move);
				break;
			} else { // if(color.equals(pieza.getColor())){
				break;
			}
		}
	}

}
