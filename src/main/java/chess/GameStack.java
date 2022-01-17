package chess;

import java.util.ArrayDeque;
import java.util.Deque;

import chess.Game.GameStatus;
import chess.analyzer.AnalyzerResult;
import chess.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class GameStack {
	private AnalyzerResult analyzerResult; 
	private Move movimientoSeleccionado;
	private GameStatus status;
	
	private static class Node {
		private AnalyzerResult analyzerResult; 
		private Move movimientoSeleccionado;
		private GameStatus status;		
	}
	
	private Deque<Node> stackNode = new ArrayDeque<Node>();

	public Move getMovimientoSeleccionado() {
		return movimientoSeleccionado;
	}

	public void setMovimientoSeleccionado(Move movimientoSeleccionado) {
		this.movimientoSeleccionado = movimientoSeleccionado;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public void push() {
		Node node = new Node();
		node.movimientoSeleccionado = this.movimientoSeleccionado;
		node.analyzerResult = this.analyzerResult;
		node.status = this.status;
		
		stackNode.push(node);
		
		this.movimientoSeleccionado = null;
		this.analyzerResult = null;
		this.status = null;
	}

	public void pop() {
		Node node = stackNode.pop();
		this.movimientoSeleccionado = node.movimientoSeleccionado;
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
