package movegenerators;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Move.MoveType;
import chess.Pieza;
import chess.Square;
import iterators.BoardIterator;
import iterators.SaltoSquareIterator;

public class ReyMoveGenerator extends AbstractMoveGenerator {
	
	protected static final Square TORRE_BLANCA_REYNA_SQUARE = Square.a1;
	
	private Color color;
	public ReyMoveGenerator(Color color) {
		this.color = color;
	}
	
	@Override
	public Set<Move> getPseudoMoves(DummyBoard tablero, Map.Entry<Square, Pieza> origen) {
		Square casillero = origen.getKey();
		BoardIterator iterator = tablero.iterator(new SaltoSquareIterator(casillero, SaltoSquareIterator.SALTOS_REY));
		Set<Move> moves = createMoveContainer();
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
		    	Move move = new Move(origen, destino, MoveType.SIMPLE);
		    	moves.add(move);
		    } else if(color.equals(pieza.getColor())){
		    	continue;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	Move move = new Move(origen, destino, MoveType.CAPTURA);
		    	moves.add(move);		    	
		    }
		}
		return moves;
	}
	
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, BoardState boardState, Map.Entry<Square, Pieza> origen){
		Set<Move> moves = getPseudoMoves(dummyBoard, origen);
		if(Pieza.REY_BLANCO.equals(origen.getValue()) && Square.e1.equals(origen.getKey())){
			if(boardState.isEnroqueBlancoReinaPermitido()){
				if(Pieza.TORRE_BLANCO.equals(dummyBoard.getPieza(TORRE_BLANCA_REYNA_SQUARE))){
					if(dummyBoard.isEmtpy(Square.b1) && dummyBoard.isEmtpy(Square.c1)){
						if( !dummyBoard.sepuedeCapturarReyEnSquare(Color.BLANCO, Square.e1) && 
							!dummyBoard.sepuedeCapturarReyEnSquare(Color.BLANCO, Square.b1) &&
							!dummyBoard.sepuedeCapturarReyEnSquare(Color.BLANCO, Square.c1) ){
							moves.add(Move.ENROQUE_TORRE_BLANCA_REYNA);
						}
					}			
				}
			}
		}
		
		return moves;
	}

}
