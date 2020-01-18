package movegenerators;

import java.util.Map;
import java.util.Set;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import iterators.SaltoSquareIterator;

public class ReyNegroMoveGenerator extends SaltoMoveGenerator {
	
	protected static final Square TORRE_BLANCA_REYNA_SQUARE = Square.a1;
	protected static final Square TORRE_BLANCA_REY_SQUARE = Square.h1;
	
	public ReyNegroMoveGenerator() {
		super(Color.NEGRO, SaltoSquareIterator.SALTOS_REY);
	}
	
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, BoardState boardState, Map.Entry<Square, Pieza> origen){
		assert(Pieza.REINA_NEGRO.equals(origen.getValue()));
		Set<Move> moves = getPseudoMoves(dummyBoard, origen);
		if(Square.e8.equals(origen.getKey())){
			if(boardState.isEnroqueBlancoReinaPermitido()){
				if(Pieza.TORRE_BLANCO.equals(dummyBoard.getPieza(TORRE_BLANCA_REYNA_SQUARE))){
					if(dummyBoard.isEmtpy(Square.c1) && dummyBoard.isEmtpy(Square.d1)){
						if( !dummyBoard.sepuedeCapturarReyEnSquare(Color.BLANCO, Square.e8) && 
							!dummyBoard.sepuedeCapturarReyEnSquare(Color.BLANCO, Square.d1) ){
							moves.add(Move.ENROQUE_BLANCO_REYNA);
						}
					}			
				}
			}
			
			if(boardState.isEnroqueBlancoReyPermitido()){
				if(Pieza.TORRE_BLANCO.equals(dummyBoard.getPieza(TORRE_BLANCA_REY_SQUARE))){
					if(dummyBoard.isEmtpy(Square.f1) && dummyBoard.isEmtpy(Square.g1)){
						if( !dummyBoard.sepuedeCapturarReyEnSquare(Color.BLANCO, Square.e8) && 
							!dummyBoard.sepuedeCapturarReyEnSquare(Color.BLANCO, Square.f1) ){
							moves.add(Move.ENROQUE_BLANCO_REY);
						}
					}			
				}
			}			
		}
		
		return moves;
	}

}
