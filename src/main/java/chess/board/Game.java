package chess.board;

import java.util.Collection;

import chess.board.analyzer.PositionAnalyzer;
import chess.board.moves.Move;
import chess.board.position.ChessPosition;
import chess.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 */
public class Game {
	
	private final ChessPosition chessPosition;
	
	private final GameState gameState;
	
	private final PositionAnalyzer analyzer;	
	
	public Game(ChessPosition chessPosition, PositionAnalyzer analyzer, GameState gameState){
		this.chessPosition = chessPosition;
		this.analyzer = analyzer;
		this.gameState = gameState;
	}

	public GameState.GameStatus executeMove(Square from, Square to) {
		if (GameState.GameStatus.IN_PROGRESS.equals(gameState.getStatus()) || GameState.GameStatus.JAQUE.equals(gameState.getStatus())) {
			Move move = getMove(from, to);
			if (move != null) {
				return executeMove(move);
			} else {
				throw new RuntimeException("Invalid move: " + from.toString() + " " + to.toString());
			}
		} else {
			throw new RuntimeException("Invalid game state");
		}
	}
	

	public GameState.GameStatus executeMove(Move move) {
		//assert(boardPila.getPossibleMoves().contains(move));
		
		gameState.setSelectedMove(move);
		
		gameState.push();
		
		chessPosition.acceptForExecute(move);
		
		return analyzer.updateGameStatus();
	}


	public GameState.GameStatus undoMove() {
		gameState.pop();
		
		Move lastMove = gameState.getSelectedMove();
		
		chessPosition.acceptForUndo(lastMove);
		
		return getGameStatus();
	}
	
	public Move getMove(Square from, Square to) {
		for (Move move : getPossibleMoves() ) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return move;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return chessPosition.toString();
	}

	public Collection<Move> getPossibleMoves() {
		return gameState.getLegalMoves();
	}

	public GameState.GameStatus getGameStatus() {
		return gameState.getStatus();
	}
	
	public void init() {
		chessPosition.init();
		analyzer.updateGameStatus();
	}
	
	public ChessPositionReader getChessPositionReader(){
		return chessPosition;
	}
	
}
