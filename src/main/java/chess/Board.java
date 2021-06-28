package chess;

import java.util.Collection;

import builder.ChessBuilder;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.LegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;
import movegenerators.MoveGeneratorStrategy;


public class Board {

	// PosicionPiezaBoard y ColorBoard son representaciones distintas del tablero. Uno con mas informacion que la otra.
	//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. Me imagino un tablero con X y O para representar los distintos colores.
	private PosicionPiezaBoard dummyBoard = null;
	private ColorBoard colorBoard = null;
	private KingCacheBoard kingCacheBoard = null;	
	private MoveCacheBoard moveCache = null;
	private BoardState boardState = null;
	
	private BoardAnalyzer analyzer = null;
	
	private DefaultLegalMoveCalculator defaultMoveCalculator = null;
	private NoCheckLegalMoveCalculator noCheckLegalMoveCalculator = null;

	public Board(PosicionPiezaBoard dummyBoard, BoardState boardState, ChessBuilder chessBuilder) {
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

	public PosicionPiezaBoard getDummyBoard() {
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


	public void execute(Move move) {
		//colorBoard.validar(dummyBoard);
		//moveCache.validar();

		move.executeMove(this.dummyBoard);

		move.executeMove(this.colorBoard);

		move.executeMove(this.moveCache);
		
		//TODO: reemplazar por double dispatcher
		if(move instanceof KingMove){
			((KingMove)move).executeMove(this.kingCacheBoard);
		}

		move.executeMove(this.boardState);	

		//moveCache.validar();
		//colorBoard.validar(dummyBoard);
		
	}

	public void undo(Move move) {
		//colorBoard.validar(dummyBoard);
		//moveCache.validar();

		move.undoMove(this.boardState);
		
		//TODO: reemplazar por double dispatcher
		if(move instanceof KingMove){
			((KingMove)move).undoMove(this.kingCacheBoard);
		}

		move.undoMove(this.moveCache);

		move.undoMove(this.colorBoard);

		move.undoMove(this.dummyBoard);

		//moveCache.validar();
		//colorBoard.validar(dummyBoard);
		
	}
	
}
