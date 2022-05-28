package chess.board;

import chess.board.analyzer.PositionAnalyzer;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;
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

	private boolean analyze;
	
	public Game(ChessPosition chessPosition, PositionAnalyzer analyzer, GameState gameState){
		this.chessPosition = chessPosition;
		this.analyzer = analyzer;
		this.gameState = gameState;
		this.analyze = true;
	}

	public void executeMove(Square from, Square to) {
		Move move = getMove(from, to);
		if (move != null) {
			executeMove(move);
		} else {
			throw new RuntimeException("Invalid move: " + from.toString() + " " + to.toString());
		}
	}

	public Move getMove(Square from, Square to) {
		for (Move move : getPossibleMoves() ) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return move;
			}
		}
		return null;
	}

	public void executeMove(Move move) {
		gameState.setSelectedMove(move);
		
		gameState.push();
		
		chessPosition.acceptForExecute(move);

		analyze = true;
	}


	public void undoMove() {
		gameState.pop();
		
		Move lastMove = gameState.getSelectedMove();
		
		chessPosition.acceptForUndo(lastMove);
	}

	public MoveContainerReader getPossibleMoves() {
		if(analyze){
			analyzer.updateGameStatus();
			analyze = false;
		}
		return gameState.getLegalMoves();
	}

	public GameState.GameStatus getGameStatus() {
		if(analyze){
			analyzer.updateGameStatus();
			analyze = false;
		}
		return gameState.getStatus();
	}

	public GameState getGameState() {
		if(analyze){
			analyzer.updateGameStatus();
			analyze = false;
		}
		return gameState;
	}
	
	public void init() {
		chessPosition.init();
	}
	
	public ChessPositionReader getChessPositionReader(){
		return chessPosition;
	}

	@Override
	public String toString() {
		return chessPosition.toString();
	}
	
}
