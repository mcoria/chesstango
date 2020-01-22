package chess;

import java.util.Collection;
import java.util.Map;

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
	PEON_BLANCO(Color.BLANCO, new PeonBlancoMoveGenerator()),
	PEON_NEGRO(Color.NEGRO, new PeonNegroMoveGenerator()),
	
	TORRE_BLANCO(Color.BLANCO, new TorreMoveGenerator(Color.BLANCO)),
	TORRE_NEGRO(Color.NEGRO, new TorreMoveGenerator(Color.NEGRO)),
	
	CABALLO_BLANCO(Color.BLANCO, new CaballoMoveGenerator(Color.BLANCO)),
	CABALLO_NEGRO(Color.NEGRO, new CaballoMoveGenerator(Color.NEGRO)),
	
	ALFIL_BLANCO(Color.BLANCO, new AlfilMoveGenerator(Color.BLANCO)),
	ALFIL_NEGRO(Color.NEGRO, new AlfilMoveGenerator(Color.NEGRO)),
	
	REINA_BLANCO(Color.BLANCO, new ReinaMoveGenerator(Color.BLANCO)),
	REINA_NEGRO(Color.NEGRO, new ReinaMoveGenerator(Color.NEGRO)),
	
	REY_BLANCO(Color.BLANCO, new ReyBlancoMoveGenerator()),
	REY_NEGRO(Color.NEGRO, new ReyNegroMoveGenerator());
	
	private MoveGenerator generator;
	private Color color;
	
	private Pieza(Color color, MoveGenerator generator) {
		this.generator = generator;
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}

	@Override
	public Collection<Move> getLegalMoves(DummyBoard board, BoardState boardState, Map.Entry<Square, Pieza> origen) {
		return generator.getLegalMoves(board, boardState, origen);
	}

	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Map.Entry<Square, Pieza> origen, Square kingSquare) {
		return generator.puedeCapturarRey(dummyBoard, origen, kingSquare);
	}
	
	public static Pieza getRey(Color color){
		switch (color) {
		case  BLANCO:
			return REY_BLANCO;
		case  NEGRO:
			return REY_NEGRO;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
}
