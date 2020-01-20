package chess;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

public class BoardPila {
	
	private class Node {
		private Set<Move> movimientosPosibles;
		
		private Move movimientoSeleccionado;
		
		private GameStatus status;		
	}
	
	private Deque<Node> stackNode = new ArrayDeque<Node>();
	
	private Set<Move> movimientosPosibles;
	
	private Move movimientoSeleccionado;
	
	private GameStatus status;

	public Set<Move> getMovimientosPosibles() {
		return movimientosPosibles;
	}

	public void setMovimientosPosibles(Set<Move> movimientosPosibles) {
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
	}

	public void pop() {
		Node node = stackNode.pop();
		this.movimientoSeleccionado = node.movimientoSeleccionado;
		this.movimientosPosibles = node.movimientosPosibles;
		this.status = node.status;		
	}
}
