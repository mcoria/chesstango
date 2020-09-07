package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import iterators.SquareIterator;
import movegenerators.CardinalMoveGenerator;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;
import movegenerators.PeonAbstractMoveGenerator;
import movegenerators.ReyAbstractMoveGenerator;


public class Board {

	// Al final del dia estas son dos representaciones distintas del tablero
	private DummyBoard dummyBoard = null; 
	private BoardCache boardCache = null;
	
	// Esta es una capa mas de informacion del tablero
	private MoveCache moveCache = null;
	
	private BoardState boardState = null;	
	
	private MoveGeneratorStrategy strategy = null;
	
	private final boolean useMoveCache = false;

	public Board(DummyBoard dummyBoard, BoardState boardState) {
		this.dummyBoard = dummyBoard;
		this.boardCache = new BoardCache(this.dummyBoard);
		this.boardState = boardState;
		this.strategy = new MoveGeneratorStrategy(this);
		this.moveCache = new MoveCache();
		//this.checkAnalyzer = new CheckAnalyzer(dummyBoard, boardState, boardCache, strategy);
	}
	
	public BoardResult getBoardResult() {
		boolean isKingInCheck = isKingInCheck();
		Color 	turnoActual = boardState.getTurnoActual();
		Square 	kingSquare = null;
		Collection<Square> pinnedSquares = null; // Casilleros donde se encuentran piezas propias que de moverse pueden desproteger al Rey.

		if(! isKingInCheck ){
			kingSquare = boardCache.getKingSquare(turnoActual);
			ReyAbstractMoveGenerator reyMoveGenerator = strategy.getReyMoveGenerator(turnoActual);
			pinnedSquares = reyMoveGenerator.getPinnedSquare(kingSquare);
		}

		Collection<Move> moves = createContainer();
		

		for (SquareIterator iterator = boardCache.iteratorSquare(turnoActual); iterator.hasNext();) {
				
			//boardCache.validarCacheSqueare(dummyBoard);
			
			Square origenSquare = iterator.next();
			
			//assert turnoActual.equals(origen.getValue().getColor());

			Collection<Move> pseudoMoves = null;
			
			
			if (useMoveCache) {

				pseudoMoves = moveCache.getPseudoMoves(origenSquare);

				if (pseudoMoves == null) {

					PosicionPieza origen = dummyBoard.getPosicion(origenSquare);

					MoveGenerator moveGenerator = strategy.getMoveGenerator(origen.getValue());

					MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);

					pseudoMoves = generatorResult.getPseudoMoves();

					if (generatorResult.isSaveMovesInCache()) {
						moveCache.setPseudoMoves(origen.getKey(), pseudoMoves);
						moveCache.setAffectedBy(origen.getKey(), generatorResult.getAffectedBy());
					}
				}
			} else {

				PosicionPieza origen = dummyBoard.getPosicion(origenSquare);

				MoveGenerator moveGenerator = strategy.getMoveGenerator(origen.getValue());

				MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);

				pseudoMoves = generatorResult.getPseudoMoves();

			}
			
			// Si el rey esta en jaque
			// O se mueve el rey
			// O se mueve un pieza que protege al Rey
			if( isKingInCheck || pinnedSquares.contains(origenSquare) || origenSquare.equals(kingSquare)){
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
		
		BoardResult result = new BoardResult();
		result.setKingInCheck(isKingInCheck);
		result.setLegalMoves(moves);

		return result;
	}

	private boolean isKingInCheck() {
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
		// boardCache.validarCacheSqueare(dummyBoard);

		move.executeMove(dummyBoard);

		move.executeMove(boardCache);

		if (useMoveCache) {
			move.executeMove(moveCache);
		}

		move.executeMove(boardState);

		// boardCache.validarCacheSqueare(dummyBoard);
	}

	public void undo(Move move) {
		// boardCache.validarCacheSqueare(dummyBoard);

		move.undoMove(boardState);

		if (useMoveCache) {
			move.undoMove(moveCache);
		}

		move.undoMove(boardCache);

		move.undoMove(dummyBoard);

		// boardCache.validarCacheSqueare(dummyBoard);
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
