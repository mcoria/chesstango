/**
 * 
 */
package chess.board;

import chess.board.analyzer.AnalyzerResult;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;

import java.util.ArrayDeque;
import java.util.Deque;

public class GameState {

	public enum GameStatus {
		IN_PROGRESS,
		CHECK,
		MATE,
		DRAW
	}

	private AnalyzerResult analyzerResult;
	private MoveContainerReader legalMoves;
	private Move selectedMove;
	private GameState.GameStatus status;

	private final Deque<GameState.Node> stackNode = new ArrayDeque<GameState.Node>();

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
		GameState.Node node = new Node();
		node.movimientoSeleccionado = this.selectedMove;
		node.analyzerResult = this.analyzerResult;
		node.status = this.status;
		node.legalMoves = this.legalMoves;
		
		stackNode.push(node);
		
		this.selectedMove = null;
		this.analyzerResult = null;
		this.status = null;
		this.legalMoves = null;
	}

	public void pop() {
		GameState.Node node = stackNode.pop();
		this.selectedMove = node.movimientoSeleccionado;
		this.analyzerResult = node.analyzerResult;
		this.status = node.status;	
		this.legalMoves = node.legalMoves;
	}

	private static class Node {
		private AnalyzerResult analyzerResult;
		private MoveContainerReader legalMoves;
		private Move movimientoSeleccionado;
		private GameState.GameStatus status;
	}
}