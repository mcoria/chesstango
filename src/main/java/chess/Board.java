package chess;

import java.util.Collection;

import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.LegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;


public class Board {

	// PosicionPiezaBoard y ColorBoard son representaciones distintas del tablero. Uno con mas informacion que la otra.
	//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. Me imagino un tablero con X y O para representar los distintos colores.
	protected PosicionPiezaBoard dummyBoard = null;
	protected ColorBoard colorBoard = null;
	protected KingCacheBoard kingCacheBoard = null;	
	protected MoveCacheBoard moveCache = null;
	protected BoardState boardState = null;
	
	private BoardAnalyzer analyzer = null;
	
	private DefaultLegalMoveCalculator defaultMoveCalculator = null;
	private NoCheckLegalMoveCalculator noCheckLegalMoveCalculator = null;

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


	//TODO: Podriamos implemetar las validaciones en una clase derivada
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

	//TODO: Podriamos implemetar las validaciones en una clase derivada	
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
	
	
	@Override
	public String toString() {
	    return this.dummyBoard.toString() + "\n" + this.boardState.toString() + "\n" + this.kingCacheBoard.toString();
	}


	public PosicionPiezaBoard getDummyBoard() {
		return dummyBoard;
	}


	public void setDummyBoard(PosicionPiezaBoard dummyBoard) {
		this.dummyBoard = dummyBoard;
	}


	public ColorBoard getColorBoard() {
		return colorBoard;
	}


	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}


	public KingCacheBoard getKingCacheBoard() {
		return kingCacheBoard;
	}


	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}


	public MoveCacheBoard getMoveCache() {
		return moveCache;
	}


	public void setMoveCache(MoveCacheBoard moveCache) {
		this.moveCache = moveCache;
	}


	public BoardState getBoardState() {
		return boardState;
	}


	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}


	public BoardAnalyzer getAnalyzer() {
		return analyzer;
	}


	public void setAnalyzer(BoardAnalyzer analyzer) {
		this.analyzer = analyzer;
	}


	public DefaultLegalMoveCalculator getDefaultMoveCalculator() {
		return defaultMoveCalculator;
	}


	public void setDefaultMoveCalculator(DefaultLegalMoveCalculator defaultMoveCalculator) {
		this.defaultMoveCalculator = defaultMoveCalculator;
	}


	public NoCheckLegalMoveCalculator getNoCheckLegalMoveCalculator() {
		return noCheckLegalMoveCalculator;
	}


	public void setNoCheckLegalMoveCalculator(NoCheckLegalMoveCalculator noCheckLegalMoveCalculator) {
		this.noCheckLegalMoveCalculator = noCheckLegalMoveCalculator;
	}	
	
}
