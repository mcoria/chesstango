package positioncaptures;

import java.util.Iterator;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import layers.ColorBoard;
import layers.DummyBoard;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;

/*
 * Observar que esta clase itera las posiciones en base a colorBoard y luego obtiene la posicion de dummyBoard.
 * Esto implica que colorBoard necesita estar actualizado en todo momento. 
 */
public class IteratorCapturer implements Capturer {
	
	private DummyBoard dummyBoard = null; 
	private ColorBoard colorBoard = null;
	private MoveGeneratorStrategy strategy = null;
	
	public IteratorCapturer(DummyBoard dummyBoard, ColorBoard colorBoard, MoveGeneratorStrategy strategy) {
		this.dummyBoard = dummyBoard;
		this.colorBoard = colorBoard;
		this.strategy = strategy;
	}


	/* (non-Javadoc)
	 * @see positioncaptures.Capturer#positionCaptured(chess.Color, chess.Square)
	 */
	@Override
	public boolean positionCaptured(Color color, Square square){
		for (Iterator<PosicionPieza> iterator = dummyBoard.iterator(colorBoard.getPosiciones(color)); iterator.hasNext();) {
			PosicionPieza origen = iterator.next();
			Pieza currentPieza = origen.getValue();
			//MoveGenerator moveGenerator = currentPieza.getMoveGenerator(strategy);
			MoveGenerator moveGenerator = currentPieza.getMoveGenerator(strategy);
			if(moveGenerator.puedeCapturarPosicion(origen, square)){
				return true;
			}
		}
		return false;
	}
	
}
