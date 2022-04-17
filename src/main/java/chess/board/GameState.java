/**
 * 
 */
package chess.board;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import chess.board.analyzer.AnalyzerResult;
import chess.board.moves.Move;

public class GameState {
	private AnalyzerResult analyzerResult;
	private Collection<Move> legalMoves;
	private Move selectedMove;
	private GameState.GameStatus status;
	
	private static class Node {
		private AnalyzerResult analyzerResult;
		private Collection<Move> legalMoves;
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
	
	public Collection<Move> getLegalMoves() {
		return legalMoves;
	}

	public void setLegalMoves(Collection<Move> legalMoves) {
		this.legalMoves = legalMoves;
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

	public AnalyzerResult getAnalyzerResult() {
		return analyzerResult;
	}

	public void setAnalyzerResult(AnalyzerResult analyzerResult) {
		this.analyzerResult = analyzerResult;
	}
}