package chess;

import java.util.Collection;

import builder.ChessBuilder;
import layers.ColorBoard;
import layers.DummyBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.LegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;


public class Board {

	// Dos representaciones distintas del tablero. Uno con mas informacion que la otra.
	//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. Me imagino un tablero con X y O para representar los distintos colores.
	private DummyBoard dummyBoard = null; 
	private ColorBoard colorBoard = null;
	
	// TODO: Al final del dia, esta es una capa mas de informacion
	private MoveCache moveCache = null;
	
	private BoardState boardState = null;
	
	private MoveGeneratorStrategy strategy = null;
	
	private BoardAnalyzer analyzer = null;
	
	private DefaultLegalMoveCalculator defaultMoveCalculator = null;

	public Board(DummyBoard dummyBoard, BoardState boardState, ChessBuilder chessBuilder) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.colorBoard = chessBuilder.buildColorBoard();
		this.moveCache = new MoveCache();
		
		this.strategy = chessBuilder.buildMoveGeneratorStrategy();
		this.strategy.setIsKingInCheck(() -> isKingInCheck());
		
		this.analyzer = new BoardAnalyzer(this);
		
		this.defaultMoveCalculator = new DefaultLegalMoveCalculator(dummyBoard, colorBoard, moveCache, boardState, strategy, analyzer);
	}


	public BoardStatus getBoardStatus() {
		analyzer.analyze();
		
		LegalMoveCalculator calculator = selectMoveCalculator();
		boolean check = analyzer.isKingInCheck();
		Collection<Move> moves = calculator.getLegalMoves();
		
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

	private LegalMoveCalculator selectMoveCalculator() {
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
