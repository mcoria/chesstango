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

/**
 * @author Mauricio Coria
 *
 */
public class GameState {

	public enum Status {
		NO_CHECK(true),
		CHECK(true),
		MATE(false),
		DRAW(false);

		private final boolean inProgress;

		Status(boolean inProgress) {
			this.inProgress = inProgress;
		}

		public boolean isInProgress(){
			return inProgress;
		}
	}

	private AnalyzerResult analyzerResult;
	private MoveContainerReader legalMoves;
	private Move selectedMove;
	private Status status;

	private final Deque<GameStateNode> stackGameStateNode = new ArrayDeque<GameStateNode>();

	public Move getSelectedMove() {
		return selectedMove;
	}

	public void setSelectedMove(Move selectedMove) {
		this.selectedMove = selectedMove;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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
		public Status status;
	}
}