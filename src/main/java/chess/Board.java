package chess;

import java.util.Iterator;

import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.LegalMoveCalculator;
import movegenerators.CardinalMoveGenerator;
import movegenerators.MoveGenerator;
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
	
	private DefaultLegalMoveCalculator defaultMoveCalculator = null;

	public Board(DummyBoard dummyBoard, BoardState boardState) {
		this.dummyBoard = dummyBoard;
		this.boardCache = new BoardCache(this.dummyBoard);
		this.boardState = boardState;
		this.strategy = new MoveGeneratorStrategy(this);
		this.moveCache = new MoveCache();
		
		this.defaultMoveCalculator = new DefaultLegalMoveCalculator(dummyBoard, boardState, boardCache, strategy, (Color color, Square square) -> isPositionCaptured(color, square));
	}
	
	public BoardResult getBoardResult() {
		BoardAnalyzer analyzer = new BoardAnalyzer(dummyBoard, boardState, boardCache, strategy);
		
		analyzer.analyze();
		
		LegalMoveCalculator calculator = selectCalculator(analyzer);
		
		
		BoardResult result = new BoardResult();
		result.setKingInCheck(analyzer.isKingInCheck());
		result.setLegalMoves(calculator.getLegalMoves(analyzer));

		return result;
	}
	
	private LegalMoveCalculator selectCalculator(BoardAnalyzer analyzer) {
		return defaultMoveCalculator;
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

	///////////////////////////// START Move execution Logic /////////////////////////////		
	public void execute(Move move) {
		// boardCache.validarCacheSqueare(dummyBoard);

		move.executeMove(dummyBoard);

		move.executeMove(boardCache);

		move.executeMove(moveCache);

		move.executeMove(boardState);

		// boardCache.validarCacheSqueare(dummyBoard);
	}

	public void undo(Move move) {
		// boardCache.validarCacheSqueare(dummyBoard);

		move.undoMove(boardState);

		move.undoMove(moveCache);

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
}
