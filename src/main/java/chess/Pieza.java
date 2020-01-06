package chess;

import java.util.Set;

import movegenerators.MoveGenerator;

public enum Pieza implements MoveGenerator {
	PEON_BLANCO(null),
	PEON_NEGRO(null),
	
	TORRE_BLANCO(null),
	TORRE_NEGRO(null),
	
	CABALLO_BLANCO(null),
	CABALLO_NEGRO(null),
	
	ALFIL_BLANCO(null),
	ALFIL_NEGRO(null),
	
	REINA_BLANCO(null),
	REINA_NEGRO(null),
	
	REY_BLANCO(null),
	REY_NEGRO(null);
	
	private MoveGenerator generator;
	
	private Pieza(MoveGenerator generator) {
		this.generator = generator;
	}
	
	public boolean isBlanco(){
		return this.equals(PEON_BLANCO) || 
				this.equals(TORRE_BLANCO) || 
				this.equals(CABALLO_BLANCO) || 
				this.equals(ALFIL_BLANCO) || 
				this.equals(REINA_BLANCO) || 
				this.equals(REY_BLANCO);
	}
	
	public boolean isNegro(){
		return !isBlanco();
	}
	
	public Color getColor(){
		if(isBlanco()) 
			return Color.BLANCO;
		else
			return Color.NEGRO;
	}
	
	public boolean isPeon(){
		return this.equals(PEON_BLANCO) || this.equals(PEON_NEGRO);
	}
	
	public boolean isTorre(){
		return this.equals(TORRE_BLANCO) || this.equals(TORRE_NEGRO);
	}
	
	public boolean isCaballo(){
		return this.equals(CABALLO_BLANCO) || this.equals(CABALLO_NEGRO);
	}	
	
	public boolean isAlfil(){
		return this.equals(ALFIL_BLANCO) || this.equals(ALFIL_NEGRO);
	}	
	
	public boolean isReina(){
		return this.equals(REINA_BLANCO) || this.equals(REINA_NEGRO);
	}	
	
	public boolean isRey(){
		return this.equals(REY_BLANCO) || this.equals(REY_NEGRO);
	}

	@Override
	public Set<Move> getLegalMoves(Board board, Square currentSquare) {
		return generator.getLegalMoves(board, currentSquare);
	}
	
	@Override
	public Set<Move> getPseudoMoves(DummyBoard tablero, Square casillero) {
		return generator.getPseudoMoves(tablero, casillero);
	}

	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Square casillero, Square kingSquare) {
		return generator.puedeCapturarRey(dummyBoard, casillero, kingSquare);
	}	
}
