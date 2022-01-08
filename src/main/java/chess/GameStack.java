package chess;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import chess.Game.GameStatus;
import chess.moveexecutors.Move;

/**
 * @author Mauricio Coria
 *
 */
public class GameStack {
	
	private Collection<Move> movimientosPosibles;
	private Move movimientoSeleccionado;
	private GameStatus status;
	
	private static class Node {
		private Collection<Move> movimientosPosibles;
		private Move movimientoSeleccionado;
		private GameStatus status;		
	}
	
	private Deque<Node> stackNode = new ArrayDeque<Node>();

	public Collection<Move> getMovimientosPosibles() {
		return movimientosPosibles;
	}

	public void setMovimientosPosibles(Collection<Move> movimientosPosibles) {
		this.movimientosPosibles = movimientosPosibles;
	}

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
		node.movimientosPosibles = this.movimientosPosibles;
		node.status = this.status;
		
		stackNode.push(node);
		
		this.movimientoSeleccionado = null;
		this.movimientosPosibles = null;
		this.status = null;
	}

	public void pop() {
		Node node = stackNode.pop();
		this.movimientoSeleccionado = node.movimientoSeleccionado;
		this.movimientosPosibles = node.movimientosPosibles;
		this.status = node.status;		
	}
}
