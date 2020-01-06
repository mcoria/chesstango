package movegenerators;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import iterators.BoardIterator;
import iterators.CardinalSquareIterator;
import iterators.CardinalSquareIterator.Cardinal;
import iterators.SquareIterator;

public class TorreMoveGenerator extends AbstractMoveGenerator {
	
	private Color color;

	public TorreMoveGenerator(Color color) {
		this.color = color;
	}

	@Override
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, Square casillero) {
		Set<Move> moves = new HashSet<Move>();
		moves.addAll(getNortePseudoMoves(dummyBoard, casillero));
		moves.addAll(getSurPseudoMoves(dummyBoard, casillero));
		moves.addAll(getEstePseudoMoves(dummyBoard, casillero));
		moves.addAll(getOestePseudoMoves(dummyBoard, casillero));
		return moves;
	}

	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Square casillero, Square kingSquare) {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<Move> getNortePseudoMoves(DummyBoard tablero, Square casillero) {
		return getPseudoMovesFromIterator(tablero, casillero, new CardinalSquareIterator(Cardinal.Norte, casillero));
	}

	public Set<Move> getSurPseudoMoves(DummyBoard tablero, Square casillero) {
		return getPseudoMovesFromIterator(tablero, casillero, new CardinalSquareIterator(Cardinal.Sur, casillero));
	}
	
	public Set<Move> getEstePseudoMoves(DummyBoard tablero, Square casillero) {
		return getPseudoMovesFromIterator(tablero, casillero, new CardinalSquareIterator(Cardinal.Este, casillero));
	}
	
	public Set<Move> getOestePseudoMoves(DummyBoard tablero, Square casillero) {
		return getPseudoMovesFromIterator(tablero, casillero, new CardinalSquareIterator(Cardinal.Oeste, casillero));
	}
	
	public Set<Move> getPseudoMovesFromIterator(DummyBoard tablero, Square casillero, SquareIterator squareIterator ) {
		BoardIterator iterator = tablero.iterator(squareIterator);
		Set<Move> moves = new HashSet<Move>();
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> entry = iterator.next();
		    Square destino = entry.getKey();
		    Pieza pieza = entry.getValue();
		    if(pieza == null){
		    	Move move = new Move(casillero, destino);
		    	moves.add(move);
		    } else if(color.equals(pieza.getColor())){
		    	break;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	Move move = new Move(casillero, destino);
		    	moves.add(move);		    	
		    }
		}
		return moves;	
	}


}
