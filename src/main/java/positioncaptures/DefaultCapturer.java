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

public class DefaultCapturer implements Capturer {
	
	private DummyBoard dummyBoard = null; 
	private ColorBoard colorBoard = null;
	private MoveGeneratorStrategy strategy = null;
	
	public DefaultCapturer(DummyBoard dummyBoard, ColorBoard colorBoard, MoveGeneratorStrategy strategy) {
		this.dummyBoard = dummyBoard;
		this.colorBoard = colorBoard;
		this.strategy = strategy;
	}

	//TODO: Esto hay que reimplementarlo, buscar los peones, caballos; alfiles; torres y reinas que podrian capturar la posicion
	// EN VEZ DE RECORRER TODO EL TABLERO EN BUSCA DE LAS PIEZAS QUE PUEDEN CAPTURAR LA POSICION
	/*
	 * Observar que este método itera las posiciones en base a boardCache.
	 * Luego obtiene la posicion de dummyBoard.
	 * Esto implica que boardCache necesita estar actualizado en todo momento. 
	 */
	/* (non-Javadoc)
	 * @see positioncaptures.Capturer#positionCaptured(chess.Color, chess.Square)
	 */
	@Override
	public PosicionPieza positionCaptured(Color color, Square square){
		for (Iterator<PosicionPieza> iterator = dummyBoard.iterator(colorBoard.getPosiciones(color)); iterator.hasNext();) {
			PosicionPieza origen = iterator.next();
			Pieza currentPieza = origen.getValue();
			MoveGenerator moveGenerator = strategy.getMoveGenerator(currentPieza);
			if(moveGenerator.puedeCapturarPosicion(origen, square)){
				return origen;
			}
		}
		return null;
	}
	
}
