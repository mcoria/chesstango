/**
 * 
 */
package chess.board;

import chess.board.analyzer.AnalyzerResult;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class GameState {

	public enum GameStatus {
		IN_PROGRESS(false),
		CHECK(false),
		MATE(true),
		DRAW(true);

		private final boolean endGame;

		GameStatus(boolean endGame) {
			this.endGame = endGame;
		}

		public boolean isEndGame(){
			return endGame;
		}
	}

	private AnalyzerResult analyzerResult;
	private MoveContainerReader legalMoves;
	private Move selectedMove;
	private GameState.GameStatus status;

	private final Deque<GameStateNode> stackGameStateNode = new ArrayDeque<GameStateNode>();

	public Move getSelectedMove() {
		return selectedMove;
	}

	public void setSelectedMove(Move selectedMove) {
		this.selectedMove = selectedMove;
	}

	public GameState.GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameState.GameStatus status) {
		this.status = status;
	}
	
	public MoveContainerReader getLegalMoves() { return legalMoves; }

	public void setLegalMoves(MoveContainerReader legalMoves) {
		this.legalMoves = legalMoves;
	}

	public AnalyzerResult getAnalyzerResult() {
		return analyzerResult;
	}

	public void setAnalyzerResult(AnalyzerResult analyzerResult) {
		this.analyzerResult = analyzerResult;
	}

	public void push() {
		GameStateNode gameStateNode = new GameStateNode();
		gameStateNode.selectedMove = this.selectedMove;
		gameStateNode.analyzerResult = this.analyzerResult;
		gameStateNode.status = this.status;
		gameStateNode.legalMoves = this.legalMoves;
		
		stackGameStateNode.push(gameStateNode);
		
		this.selectedMove = null;
		this.analyzerResult = null;
		this.status = null;
		this.legalMoves = null;
	}

	public void pop() {
		GameStateNode gameStateNode = stackGameStateNode.pop();
		this.selectedMove = gameStateNode.selectedMove;
		this.analyzerResult = gameStateNode.analyzerResult;
		this.status = gameStateNode.status;
		this.legalMoves = gameStateNode.legalMoves;
	}

	public Iterator<GameStateNode> iterateGameStates(){
		return stackGameStateNode.descendingIterator();
	}

	public static class GameStateNode {
		public AnalyzerResult analyzerResult;
		public MoveContainerReader legalMoves;
		public Move selectedMove;
		public GameState.GameStatus status;
	}
}