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
	
	private PosicionPieza currentChecker;	

	public BoardAnalyzer(DummyBoard dummyBoard, BoardState boardState, BoardCache boardCache,
			MoveGeneratorStrategy strategy) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.boardCache = boardCache;
		this.strategy =strategy;
	}

	public void analyze() {
		this.currentChecker = null;
		this.isKingInCheck = calculateKingInCheck();
	}
	
	private boolean calculateKingInCheck() {
		Square kingSquare = boardCache.getKingSquare();		
		
		Color opositeColor = boardState.getTurnoActual().opositeColor();
		
		for (Iterator<PosicionPieza> iterator = dummyBoard.iterator(boardCache.getPosiciones(opositeColor)); iterator.hasNext();) {
			PosicionPieza origen = iterator.next();
			Pieza currentPieza = origen.getValue();
			MoveGenerator moveGenerator = this.strategy.getMoveGenerator(currentPieza);
			if(moveGenerator.puedeCapturarPosicion(origen, kingSquare)){
				this.currentChecker = origen;
			}
		}
		
		return this.currentChecker != null;
	}
	
	public PosicionPieza getCurrentChecker() {
		return currentChecker;
	}
	
	public boolean isKingInCheck() {
		return isKingInCheck;
	}		
	
}
