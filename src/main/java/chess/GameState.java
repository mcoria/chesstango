/**
 * 
 */
package chess;

import java.util.ArrayDeque;
import java.util.Deque;

import chess.analyzer.AnalyzerResult;
import chess.moves.Move;

public class GameState {
	private AnalyzerResult analyzerResult; 
	private Move selectedMove;
	private GameState.GameStatus status;
	
	private static class Node {
		private AnalyzerResult analyzerResult; 
		private Move movimientoSeleccionado;
		private GameState.GameStatus status;		
	}
	
	public static enum GameStatus {
		IN_PROGRESS,
		JAQUE,
		JAQUE_MATE, 
		TABLAS		
	}

	private Deque<GameState.Node> stackNode = new ArrayDeque<GameState.Node>();

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

	public void push() {
		GameState.Node node = new Node();
		node.movimientoSeleccionado = this.selectedMove;
		node.analyzerResult = this.analyzerResult;
		node.status = this.status;
		
		stackNode.push(node);
		
		this.selectedMove = null;
		this.analyzerResult = null;
		this.status = null;
	}

	public void pop() {
		GameState.Node node = stackNode.pop();
		this.selectedMove = node.movimientoSeleccionado;
		this.analyzerResult = node.analyzerResult;
		this.status = node.status;		
	}

	public AnalyzerResult getAnalyzerResult() {
		return analyzerResult;
	}

	public void setAnalyzerResult(AnalyzerResult analyzerResult) {
		this.analyzerResult = analyzerResult;
	}
}