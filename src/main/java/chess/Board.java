package chess;

import java.util.Collection;

import builder.ChessBuilder;
import layers.ColorBoard;
import layers.DummyBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.LegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;


public class Board {

	// Dos representaciones distintas del tablero. Uno con mas informacion que la otra.
	//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. Me imagino un tablero con X y O para representar los distintos colores.
	private DummyBoard dummyBoard = null;
	private ColorBoard colorBoard = null;
	private KingCacheBoard kingCacheBoard = null;	
	private MoveCacheBoard moveCache = null;
	private BoardState boardState = null;
	
	private BoardAnalyzer analyzer = null;
	
	private DefaultLegalMoveCalculator defaultMoveCalculator = null;
	private NoCheckLegalMoveCalculator noCheckLegalMoveCalculator = null;

	public Board(DummyBoard dummyBoard, BoardState boardState, ChessBuilder chessBuilder) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.kingCacheBoard = chessBuilder.buildKingCacheBoard();
		this.colorBoard = chessBuilder.buildColorBoard();
		this.moveCache = new MoveCacheBoard();
		
		this.analyzer = new BoardAnalyzer(this);
		
		MoveGeneratorStrategy strategy = chessBuilder.buildMoveGeneratorStrategy();
		strategy.setIsKingInCheck(() -> analyzer.isKingInCheck());
		
		
		this.defaultMoveCalculator = new DefaultLegalMoveCalculator(this, dummyBoard, kingCacheBoard, colorBoard, moveCache, boardState, strategy);
		this.noCheckLegalMoveCalculator  = new NoCheckLegalMoveCalculator(this, dummyBoard, kingCacheBoard, colorBoard, moveCache, boardState, strategy);
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

	private LegalMoveCalculator selectMoveCalculator() {
		if(! analyzer.isKingInCheck() ){
			return noCheckLegalMoveCalculator;
		}
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


	public ColorBoard getColorBoard() {
		return colorBoard;
	}


	public KingCacheBoard getKingCacheBoard() {
		return kingCacheBoard;
	}


	public MoveCacheBoard getMoveCache() {
		return moveCache;
	}
	
}
