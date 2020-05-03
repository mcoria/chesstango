package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import iterators.SquareIterator;
import movegenerators.CardinalMoveGenerator;
import movegenerators.MoveFilter;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;
import movegenerators.PeonAbstractMoveGenerator;
import movegenerators.ReyAbstractMoveGenerator;


public class Board {
	
	private MoveFilter defaultFilter = (Move move) -> filterMove(move);
	
	private MoveGeneratorStrategy strategy = null; 
	
	private BoardState boardState = null;
	
	private BoardCache boardCache = null;
	
	private DummyBoard dummyBoard = null;
	
	private MoveCache moveCache = null;

	public Board(DummyBoard dummyBoard, BoardState boardState) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.boardCache = new BoardCache(this.dummyBoard);
		this.strategy = new MoveGeneratorStrategy(this);
		this.moveCache = new MoveCache();
	}
	
	public Collection<Move> getLegalMoves() {
		Collection<Square> pinnedSquares = null;
		
		Color turnoActual = boardState.getTurnoActual();

		boolean isKingInCheck = isKingInCheck();
		
		if(! isKingInCheck ){
			Square kingSquare = boardCache.getKingSquare(turnoActual);
			ReyAbstractMoveGenerator reyMoveGenerator = strategy.getReyMoveGenerator(turnoActual);
			pinnedSquares = reyMoveGenerator.getPinned(kingSquare);
		}
		
		Collection<Move> moves = createContainer();
		

		for (SquareIterator iterator = boardCache.iteratorSquare(turnoActual); iterator.hasNext();) {
				
			//boardCache.validarCacheSqueare(dummyBoard);
			
			Square origenSquare = iterator.next();
			
			//assert turnoActual.equals(origen.getValue().getColor());

			Collection<Move> pseudoMoves = moveCache.getPseudoMoves(origenSquare);

			if (pseudoMoves == null) {
				
				PosicionPieza origen = dummyBoard.getPosicion(origenSquare);
				
				MoveGenerator moveGenerator = strategy.getMoveGenerator(origen.getValue());

				moveGenerator.generatePseudoMoves(origen);
				
				pseudoMoves = moveGenerator.getMoveContainer();

				if(moveGenerator.saveMovesInCache()){
					moveCache.setPseudoMoves(origen.getKey(), pseudoMoves);
					moveCache.setAffectedBy(origen.getKey(), moveGenerator.getAffectedBy());
				}
			}
			
			if( isKingInCheck || pinnedSquares.contains(origenSquare) ){
				for (Move move : pseudoMoves) {
					/*
					if(! origen.equals(move.getFrom()) ){
						throw new RuntimeException("Que paso?!?!?");
					}
					*/
					
					//assert  origen.equals(move.getFrom());
					
					if(this.filterMove(move)){
						moves.add(move);
					}
				}
				
			} else {
				moves.addAll(pseudoMoves);
			}
			
			//boardCache.validarCacheSqueare(dummyBoard);
			
		}

		return moves;
	}

	public boolean isKingInCheck() {
		Color turno = boardState.getTurnoActual();
		Square kingSquare = boardCache.getKingSquare(turno);

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
	
	protected boolean isPositionCaptured(Color color, Square square){
		return positionCaptured(color, square) != null;
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
	
	
	private boolean filterMove(Move move) {
		boolean result = false;
		
		//boardCache.validarCacheSqueare(dummyBoard);
				
		move.executeMove(this.boardCache);
		
		// Habria que preguntar si aquellos para los cuales su situacion cambió ahora pueden capturar al rey. 
		if(! this.isKingInCheck() ) {
			result = true;
		}
		
		move.undoMove(this.boardCache);
		
		//boardCache.validarCacheSqueare(dummyBoard);
		
		return result;
	}

	///////////////////////////// START Move execution Logic /////////////////////////////		
	public void execute(Move move) {
		//boardCache.validarCacheSqueare(dummyBoard);
		
		move.executeMove(dummyBoard);

		move.executeMove(boardCache);

		move.executeMove(moveCache);
		
		move.executeMove(boardState);
		
		//boardCache.validarCacheSqueare(dummyBoard);		
	}


	public void undo(Move move) {
		//boardCache.validarCacheSqueare(dummyBoard);
		
		move.undoMove(boardState);		
		
		move.undoMove(moveCache);
		
		move.undoMove(boardCache);
		
		move.undoMove(dummyBoard);
		
		//boardCache.validarCacheSqueare(dummyBoard);
	}
	///////////////////////////// END Move execution Logic /////////////////////////////
	
	
	public void settupMoveGenerator(MoveGenerator moveGenerator) {
		moveGenerator.setTablero(this.dummyBoard);
		
		if (moveGenerator instanceof PeonAbstractMoveGenerator) {
			PeonAbstractMoveGenerator generator = (PeonAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
			
		} else if (moveGenerator instanceof ReyAbstractMoveGenerator) {
			ReyAbstractMoveGenerator generator = (ReyAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
			generator.setPositionCaptured((Color color, Square square) -> isPositionCaptured(color, square));
			generator.setBoardCache(this.boardCache);
			
		} else if(moveGenerator instanceof CardinalMoveGenerator){
			CardinalMoveGenerator generator = (CardinalMoveGenerator) moveGenerator;
			generator.setBoardCache(this.boardCache);
		}
	}
	
	
	@Override
	public String toString() {
	    return this.dummyBoard.toString();
	}

	public DummyBoard getDummyBoard() {
		return this.dummyBoard;
	}
	
	public BoardState getBoardState() {
		return boardState;
	}
	
	public MoveFilter getDefaultFilter(){
		return defaultFilter;
	}	

	private static <T> Collection<T> createContainer(){
		return new ArrayList<T>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (T move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
	}	
}
