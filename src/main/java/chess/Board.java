package chess;

import java.util.Collection;

import chess.builder.ChessBuilder;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.moves.Move;
import chess.parsers.FENCoder;


/**
 * @author Mauricio Coria
 *
 */
public class Board {

	// PosicionPiezaBoard y ColorBoard son representaciones distintas del tablero. Uno con mas informacion que la otra.
	protected PosicionPiezaBoard dummyBoard = null;
	protected ColorBoard colorBoard = null;
	protected KingCacheBoard kingCacheBoard = null;	
	protected MoveCacheBoard moveCache = null;
	protected BoardState boardState = null;
	
	private BoardAnalyzer analyzer = null;

	public BoardStatus getBoardStatus() {
		return analyzer.getBoardStatus();
	}
	
	public Collection<Move> getLegalMoves() {
		return analyzer.getLegalMoves();
	}	

	public void execute(Move move) {
		move.executeMove(this);
	}

	public void executeMove(Move move) {
		move.executeMove(this.dummyBoard);

		move.executeMove(this.colorBoard);

		move.executeMove(this.moveCache);

		move.executeMove(this.boardState);	
		
	}
	
	
	//TODO: hay que reflotar la idea del MoveKing interface
	public void executeKingMove(Move move) {
		executeMove(move);
		
		move.executeMove(this.kingCacheBoard);
		
	}	

	public void undo(Move move) {
		move.undoMove(this);
		
	}

	public void undoMove(Move move) {
		move.undoMove(this.boardState);

		move.undoMove(this.moveCache);

		move.undoMove(this.colorBoard);

		move.undoMove(this.dummyBoard);
		
	}
	

	public void undoKingMove(Move move) {
		undoMove(move);

		move.undoMove(this.kingCacheBoard);
	}	
	
	
	public void constructBoardRepresentation(ChessBuilder builder){		
		builder.withTurno(boardState.getTurnoActual());
		builder.withEnroqueBlancoReinaPermitido(boardState.isEnroqueBlancoReinaPermitido());
		builder.withEnroqueBlancoReyPermitido(boardState.isEnroqueBlancoReyPermitido());
		builder.withEnroqueNegroReinaPermitido(boardState.isEnroqueNegroReinaPermitido());
		builder.withEnroqueNegroReyPermitido(boardState.isEnroqueNegroReyPermitido());
		builder.withPeonPasanteSquare(boardState.getPeonPasanteSquare());
		
		for(PosicionPieza pieza: dummyBoard){
			builder.withPieza(pieza.getKey(), pieza.getValue());
		}
	}
	
	
	@Override
	public String toString() {
		FENCoder coder = new FENCoder();
		
		constructBoardRepresentation(coder);
		
	    return this.dummyBoard.toString() + "\n" + this.boardState.toString() + "\n" + this.kingCacheBoard.toString() + "\n" + coder.getFEN();
	}

	public void setDummyBoard(PosicionPiezaBoard dummyBoard) {
		this.dummyBoard = dummyBoard;
	}

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
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


	public void setAnalyzer(BoardAnalyzer analyzer) {
		this.analyzer = analyzer;
	}
	
}
