package chess;

import java.util.Iterator;

import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;

public class BoardAnalyzer {
	
	// Al final del dia estas son dos representaciones distintas del tablero
	private DummyBoard dummyBoard = null; 
	private BoardCache boardCache = null;
	
	private BoardState boardState = null;	
	
	private MoveGeneratorStrategy strategy = null;
	
	private boolean isKingInCheck;
	
	protected IsPositionCaptured positionCaptured = (Square square) -> false;

	public BoardAnalyzer(DummyBoard dummyBoard, BoardState boardState, BoardCache boardCache,
			MoveGeneratorStrategy strategy) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.boardCache = boardCache;
		this.strategy =strategy;
	}

	public void analyze() {
		this.isKingInCheck = calculateKingInCheck();
		
	}
	
	public boolean isKingInCheck() {
		return isKingInCheck;
	}
	
	private boolean calculateKingInCheck() {
		Color turno = boardState.getTurnoActual();
		Square kingSquare = boardCache.getKingSquare();

		PosicionPieza checker = boardCache.getLastChecker();

		// Si no existe checker, recalculamos
		if (checker == null) {
			checker = positionCaptured(turno.opositeColor(), kingSquare);
		} else {
			// Si existe checker pero es del mismo color que el turno
			// O si la posicion de checker fué capturada por una de nuestras fichas			
			if (turno.equals(checker.getValue().getColor()) || boardCache.isColor(turno, checker.getKey())) {
				// recalculamos...
				checker = positionCaptured(turno.opositeColor(), kingSquare);
			} else {
				// Si checker sigue estando en la misma posicion
				if (checker.equals(dummyBoard.getPosicion(checker.getKey()))) {
					// Checker todavia puede capturar al Rey...
					MoveGenerator moveGenerator = strategy.getMoveGenerator(checker.getValue());
					if (!moveGenerator.puedeCapturarPosicion(checker, kingSquare)) {
						// Pero no puede capturar...
						checker = positionCaptured(turno.opositeColor(), kingSquare);
					}
				} else {
					// Checker todavia puede capturar al Rey...
					checker = positionCaptured(turno.opositeColor(), kingSquare);
				}
			}
		}

		boardCache.setLastChecker(checker);

		return checker != null;
	}
	
	/*
	 * Observar que este método itera las posiciones en base a boardCache.
	 * Luego obtiene la posicion de dummyBoard.
	 * Esto implica que boardCache necesita estar actualizado en todo momento. 
	 */
	protected PosicionPieza positionCaptured(Color color, Square square){
		for (Iterator<PosicionPieza> iterator = dummyBoard.iterator(boardCache.getPosiciones(color)); iterator.hasNext();) {
			PosicionPieza origen = iterator.next();
			Pieza currentPieza = origen.getValue();
			//if(currentPieza != null){
			MoveGenerator moveGenerator = this.strategy.getMoveGenerator(currentPieza);
			if(moveGenerator.puedeCapturarPosicion(origen, square)){
				return origen;
			}
			//} else {
			//	throw new RuntimeException("El cache quedó desactualizado");
			//}
		}
		return null;
	}
	
}
