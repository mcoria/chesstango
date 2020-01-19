package chess;

import java.util.Map;
import java.util.Set;

import movegenerators.AlfilMoveGenerator;
import movegenerators.CaballoMoveGenerator;
import movegenerators.MoveGenerator;
import movegenerators.PeonBlancoMoveGenerator;
import movegenerators.PeonNegroMoveGenerator;
import movegenerators.ReinaMoveGenerator;
import movegenerators.ReyBlancoMoveGenerator;
import movegenerators.ReyNegroMoveGenerator;
import movegenerators.TorreMoveGenerator;

public enum Pieza implements MoveGenerator {
	PEON_BLANCO(new PeonBlancoMoveGenerator()),
	PEON_NEGRO(new PeonNegroMoveGenerator()),
	
	TORRE_BLANCO(new TorreMoveGenerator(Color.BLANCO)),
	TORRE_NEGRO(new TorreMoveGenerator(Color.NEGRO)),
	
	CABALLO_BLANCO(new CaballoMoveGenerator(Color.BLANCO)),
	CABALLO_NEGRO(new CaballoMoveGenerator(Color.NEGRO)),
	
	ALFIL_BLANCO(new AlfilMoveGenerator(Color.BLANCO)),
	ALFIL_NEGRO(new AlfilMoveGenerator(Color.NEGRO)),
	
	REINA_BLANCO(new ReinaMoveGenerator(Color.BLANCO)),
	REINA_NEGRO(new ReinaMoveGenerator(Color.NEGRO)),
	
	REY_BLANCO(new ReyBlancoMoveGenerator()),
	REY_NEGRO(new ReyNegroMoveGenerator());
	
	private MoveGenerator generator;
	
	private Pieza(MoveGenerator generator) {
		this.generator = generator;
	}
	
	private boolean isBlanco(){
		return this.equals(PEON_BLANCO) || 
				this.equals(TORRE_BLANCO) || 
				this.equals(CABALLO_BLANCO) || 
				this.equals(ALFIL_BLANCO) || 
				this.equals(REINA_BLANCO) || 
				this.equals(REY_BLANCO);
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
	public Set<Move> getLegalMoves(DummyBoard board, BoardState boardState, Map.Entry<Square, Pieza> origen) {
		return generator.getLegalMoves(board, boardState, origen);
	}

	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Map.Entry<Square, Pieza> origen, Square kingSquare) {
		return generator.puedeCapturarRey(dummyBoard, origen, kingSquare);
	}	
}
