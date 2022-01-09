package chess.positioncaptures;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.iterators.pieceplacement.PiecePlacementIterator;
import chess.position.ColorBoard;
import chess.position.PiecePlacement;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;

/*
 * Observar que esta clase itera las posiciones en base a colorBoard y luego obtiene la posicion de dummyBoard.
 * Esto implica que colorBoard necesita estar actualizado en todo momento. 
 */

/**
 * @author Mauricio Coria
 *
 */
public class IteratorCapturer implements Capturer {
	
	private final PiecePlacement dummyBoard; 
	private final ColorBoard colorBoard;
	private final MoveGeneratorStrategy strategy;
	
	public IteratorCapturer(PiecePlacement dummyBoard, ColorBoard colorBoard, MoveGeneratorStrategy strategy) {
		this.dummyBoard = dummyBoard;
		this.colorBoard = colorBoard;
		this.strategy = strategy;
	}


	/* (non-Javadoc)
	 * @see positioncaptures.Capturer#positionCaptured(chess.Color, chess.Square)
	 */
	@Override
	public boolean positionCaptured(Color color, Square square){
		for (PiecePlacementIterator iterator = dummyBoard.iterator(colorBoard.getPosiciones(color)); iterator.hasNext();) {
			PiecePositioned origen = iterator.next();
			Piece currentPieza = origen.getValue();
			//MoveGenerator moveGenerator = currentPieza.getMoveGenerator(strategy);
			MoveGenerator moveGenerator = currentPieza.getMoveGenerator(strategy);
			if(moveGenerator.puedeCapturarPosicion(origen, square)){
				return true;
			}
		}
		return false;
	}
	
}
