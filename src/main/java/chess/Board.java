package chess;

import java.util.Collection;

import layers.ColorBoard;
import layers.DummyBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.LegalMoveCalculator;
import movegenerators.CardinalMoveGenerator;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;
import movegenerators.PeonAbstractMoveGenerator;
import movegenerators.ReyAbstractMoveGenerator;
import positioncaptures.DefaultCapturer;


public class Board {

	// Al final del dia estas son dos representaciones distintas del tablero. Uno con mas informacion que el otro.
	//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. Me imagino un tablero con X y O para representar los distintos colores.
	private DummyBoard dummyBoard = null; 
	private ColorBoard colorBoard = null;
	
	// Esta es una capa mas de informacion del tablero
	private MoveCache moveCache = null;
	
	private BoardState boardState = null;
	
	private MoveGeneratorStrategy strategy = null;
	
	private DefaultLegalMoveCalculator defaultMoveCalculator = null;
	
	private BoardAnalyzer analyzer = null; 
	
	private DefaultCapturer capturer = null;

	public Board(DummyBoard dummyBoard, BoardState boardState) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.colorBoard = new ColorBoard(dummyBoard);
		this.strategy = new MoveGeneratorStrategy(this);
		this.moveCache = new MoveCache();
		this.capturer = new DefaultCapturer(dummyBoard, colorBoard, strategy);
		
		this.analyzer = new BoardAnalyzer(this, dummyBoard, boardState, colorBoard, strategy);
		this.defaultMoveCalculator = new DefaultLegalMoveCalculator(this, dummyBoard, boardState, colorBoard, strategy, (Square square) -> isPositionCaptured(square));
	}
	
	public BoardResult getBoardResult() {
		analyzer.analyze();
		
		LegalMoveCalculator calculator = selectCalculator(analyzer);
		boolean check = analyzer.isKingInCheck();
		Collection<Move> moves = calculator.getLegalMoves(analyzer);
		
		BoardResult result = new BoardResult();
		result.setKingInCheck(check);
		result.setLegalMoves(moves);

		return result;
	}
	
	private LegalMoveCalculator selectCalculator(BoardAnalyzer analyzer) {
		return defaultMoveCalculator;
	}

	protected boolean isPositionCaptured(Square square){
		return capturer.positionCaptured(boardState.getTurnoActual().opositeColor(), square) != null;
	}	

	///////////////////////////// START Move execution Logic /////////////////////////////		
	public void execute(Move move) {
		// boardCache.validarCacheSqueare(dummyBoard);

		move.executeMove(dummyBoard);

		move.executeMove(colorBoard);

		move.executeMove(moveCache);

		move.executeMove(boardState);

		// boardCache.validarCacheSqueare(dummyBoard);
	}

	public void undo(Move move) {
		// boardCache.validarCacheSqueare(dummyBoard);

		move.undoMove(boardState);

		move.undoMove(moveCache);

		move.undoMove(colorBoard);

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
			generator.setPositionCaptured((Square square) -> isPositionCaptured(square));
			generator.setKingInCheck(() -> this.analyzer.isKingInCheck());
			generator.setBoardCache(this.colorBoard);
			
		} else if(moveGenerator instanceof CardinalMoveGenerator){
			CardinalMoveGenerator generator = (CardinalMoveGenerator) moveGenerator;
			generator.setBoardCache(this.colorBoard);
		}
	}
	
	public Square getKingSquare() {
		return Color.BLANCO.equals(boardState.getTurnoActual()) ? colorBoard.getSquareKingBlancoCache() : colorBoard.getSquareKingNegroCache();
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
