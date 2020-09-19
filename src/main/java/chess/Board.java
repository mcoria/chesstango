package chess;

import java.util.Collection;

import builder.ChessBuilder;
import layers.ColorBoard;
import layers.DummyBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.LegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;
import positioncaptures.Capturer;
import positioncaptures.DefaultCapturer;


public class Board {

	// Al final del dia estas son dos representaciones distintas del tablero. Uno con mas informacion que el otro.
	//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. Me imagino un tablero con X y O para representar los distintos colores.
	private DummyBoard dummyBoard = null; 
	private ColorBoard colorBoard = null;
	//TODO: No debieramos tener un layer de move generators, para evitar el case dentro de MoveGeneratorStrategy
	
	// Esta es una capa mas de informacion del tablero
	private MoveCache moveCache = null;
	
	private BoardState boardState = null;
	
	private MoveGeneratorStrategy strategy = null;
	
	private DefaultLegalMoveCalculator defaultMoveCalculator = null;
	
	private BoardAnalyzer analyzer = null; 
	
	private Capturer capturer = null;

	public Board(DummyBoard dummyBoard, BoardState boardState, ChessBuilder chessBuilder) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.colorBoard = chessBuilder.buildColorBoard();
		this.moveCache = new MoveCache();
		
		this.strategy = chessBuilder.buildMoveGeneratorStrategy();
		this.strategy.setIsKingInCheck(() -> isKingInCheck());
		this.strategy.setPositionCaptured((Square square) -> isPositionCaptured(square));
		
		this.analyzer = new BoardAnalyzer(this, dummyBoard, colorBoard, boardState, strategy);
		
		this.defaultMoveCalculator = new DefaultLegalMoveCalculator(this, dummyBoard, boardState, colorBoard, strategy, (Square square) -> isPositionCaptured(square));
		
		this.capturer = new DefaultCapturer(dummyBoard, colorBoard, strategy);
	}


	public BoardStatus getBoardStatus() {
		analyzer.analyze();
		
		LegalMoveCalculator calculator = selectCalculator(analyzer);
		boolean check = analyzer.isKingInCheck();
		Collection<Move> moves = calculator.getLegalMoves(analyzer);
		
		BoardStatus result = new BoardStatus();
		result.setKingInCheck(check);
		result.setLegalMoves(moves);

		return result;
	}
	
	public Square getKingSquare() {
		return Color.BLANCO.equals(boardState.getTurnoActual()) ? colorBoard.getSquareKingBlancoCache() : colorBoard.getSquareKingNegroCache();
	}

	protected boolean isKingInCheck() {
		return analyzer.isKingInCheck();
	}
	
	protected boolean isPositionCaptured(Square square){
		return capturer.positionCaptured(boardState.getTurnoActual().opositeColor(), square);
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

	private LegalMoveCalculator selectCalculator(BoardAnalyzer analyzer) {
		return defaultMoveCalculator;
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
