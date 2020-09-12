package chess;

import java.util.Iterator;

import layers.ColorBoard;
import layers.DummyBoard;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;

public class BoardAnalyzer {
	
	private Board board = null;
	
	// Al final del dia estas son dos representaciones distintas del tablero
	private DummyBoard dummyBoard = null; 
	
	private ColorBoard colorBoard = null;
	
	private BoardState boardState = null;	
	
	private MoveGeneratorStrategy strategy = null;
	
	private boolean isKingInCheck;
	
	private PosicionPieza currentChecker;	

	public BoardAnalyzer(Board board, DummyBoard dummyBoard, BoardState boardState, ColorBoard colorBoard,
			MoveGeneratorStrategy strategy) {
		this.board = board;
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.colorBoard = colorBoard;
		this.strategy = strategy;
	}

	public void analyze() {
		this.currentChecker = null;
		this.isKingInCheck = calculateKingInCheck();
	}
	
	private boolean calculateKingInCheck() {
		Square kingSquare = board.getKingSquare();		
		
		Color opositeColor = boardState.getTurnoActual().opositeColor();
		
		for (Iterator<PosicionPieza> iterator = dummyBoard.iterator(colorBoard.getPosiciones(opositeColor)); iterator.hasNext();) {
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
