package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import movegenerators.MoveFilter;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;
import movegenerators.PeonAbstractMoveGenerator;
import movegenerators.ReyAbstractMoveGenerator;


public class Board {
	
	private MoveFilter defaultFilter = (Move move) -> filterMove(move);
	
	private MoveFilter moveKingFilter = (Move move) -> filterMoveKing(move);
	
	private MoveGeneratorStrategy strategy = null; 
	
	private BoardState boardState = null;
	
	private BoardCache boardCache = null;
	
	private DummyBoard dummyBoard = null;

	public Board(DummyBoard dummyBoard, BoardState boardState) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.boardCache = new BoardCache(this.dummyBoard);
		this.strategy = new MoveGeneratorStrategy(this);
	}
	
	public Collection<Move> getLegalMoves(){
		Collection<Move> moves = createMoveContainer();
		Color turnoActual = boardState.getTurnoActual();
		//for (Iterator<PosicionPieza> iterator = dummyBoard.iterator(boardCache.iteratorSquare(turnoActual)); iterator.hasNext();) {
		for (Iterator<PosicionPieza> iterator = dummyBoard.iterator(boardCache.getPosiciones(turnoActual)); iterator.hasNext();) {
			PosicionPieza origen = iterator.next();
			Pieza currentPieza = origen.getValue();
			MoveGenerator moveGenerator = strategy.getMoveGenerator(currentPieza);
			moveGenerator.generateMoves(origen, moves);
		}
		return moves;
	}

	public boolean isKingInCheck() {
		Color turno = boardState.getTurnoActual();
		Square kingSquare = boardCache.getKingSquare(turno);
		return positionCaptured(turno.opositeColor(), kingSquare);
	}
	
	
	// Habria que preguntar si aquellos para los cuales su situacion cambió pueden ahora pueden capturar al rey. 
	/* (non-Javadoc)
	 * @see chess.PositionCaptured#sepuedeCapturarReyEnSquare(chess.Color, chess.Square)
	 */
	protected boolean positionCaptured(Color color, Square square){
		//for (Iterator<PosicionPieza> iterator = dummyBoard.iterator(boardCache.iteratorSquare(color)); iterator.hasNext();) {
		for (Iterator<PosicionPieza> iterator = dummyBoard.iterator(boardCache.getPosiciones(color)); iterator.hasNext();) {
			PosicionPieza origen = iterator.next();
			Pieza currentPieza = origen.getValue();
			if(currentPieza != null){
				if(color.equals(currentPieza.getColor())){
					MoveGenerator moveGenerator = this.strategy.getMoveGenerator(currentPieza);
					if(moveGenerator.puedeCapturarRey(origen, square)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
	 * NO HACE FALA UTILIZAR ESTE FILTRO CUANDO ES MOVIMEINTO DE REY
	 */
	private boolean filterMove(Move move) {
		boolean result = false;
				
		move.executeMove(this.dummyBoard);
		
		// Habria que preguntar si aquellos para los cuales su situacion cambió pueden ahora pueden capturar al rey. 
		if(! this.isKingInCheck() ) {
			result = true;
		}
		
		move.undoMove(this.dummyBoard);
		
		return result;
	}
	
	/*
	 * Este movimiento es utilizado para filtrar movimientos de rey, se settea el cache para movimientos de rey
	 */
	private boolean filterMoveKing(Move move) {
		boolean result = false;
				
		move.executeMove(this.dummyBoard);
		
		move.executeMove(this.boardCache);
		
		// Habria que preguntar si aquellos para los cuales su situacion cambió pueden ahora pueden capturar al rey. 
		if(! this.isKingInCheck() ) {
			result = true;
		}
		
		move.undoMove(this.dummyBoard);
		
		move.undoMove(this.boardCache);		
		
		return result;
	}

	///////////////////////////// START Move execution Logic /////////////////////////////		
	public void execute(Move move) {
		move.executeMove(this.dummyBoard);

		move.executeMove(boardCache);
		
		//boardCache.validarCacheSqueare(this);
		
		move.executeMove(boardState);
		
		//assert validarSquares(squareBlancos, Color.BLANCO) && validarSquares(squareNegros, Color.NEGRO);
	}


	public void undo(Move move) {
		move.undoMove(this.dummyBoard);

		move.undoMove(boardCache);	
		
		//boardCache.validarCacheSqueare(this);
		
		move.undoMove(boardState);
		
		//assert validarSquares(squareBlancos, Color.BLANCO) && validarSquares(squareNegros, Color.NEGRO);
	}
	///////////////////////////// END Move execution Logic /////////////////////////////
	
	
	public void settupMoveGenerator(MoveGenerator moveGenerator) {
		moveGenerator.setTablero(this.dummyBoard);
		
		if (moveGenerator instanceof PeonAbstractMoveGenerator) {
			PeonAbstractMoveGenerator generator = (PeonAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
			moveGenerator.setFilter(defaultFilter);
			
		} else if (moveGenerator instanceof ReyAbstractMoveGenerator) {
			ReyAbstractMoveGenerator generator = (ReyAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
			generator.setFilter(moveKingFilter);
			generator.setPositionCaptured((Color color, Square square) -> positionCaptured(color, square));
			
		} else {
			moveGenerator.setFilter(defaultFilter);
		}
	}
	
	
	@Override
	public String toString() {
	    return this.dummyBoard.toString();
	}
	
	private static Collection<Move> createMoveContainer(){
		return new ArrayList<Move>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (Move move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
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

}
