package chess;

import java.util.Set;

import iterators.CardinalSquareIterator.Cardinal;
import movegenerators.CardinalMoveGenerator;
import movegenerators.DummyMoveGenerator;
import movegenerators.MoveGenerator;
import movegenerators.PeonMoveGenerator;
import movegenerators.ReyMoveGenerator;

public enum Pieza implements MoveGenerator {
	PEON_BLANCO(new PeonMoveGenerator(Color.BLANCO)),
	PEON_NEGRO(new PeonMoveGenerator(Color.NEGRO)),
	
	TORRE_BLANCO(new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur})),
	TORRE_NEGRO(new CardinalMoveGenerator(Color.NEGRO, new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur})),
	
	CABALLO_BLANCO(new DummyMoveGenerator()),
	CABALLO_NEGRO(new DummyMoveGenerator()),
	
	ALFIL_BLANCO(new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste})),
	ALFIL_NEGRO(new CardinalMoveGenerator(Color.NEGRO, new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste})),
	
	REINA_BLANCO(new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste, Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur})),
	REINA_NEGRO(new CardinalMoveGenerator(Color.NEGRO, new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste, Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur})),
	
	REY_BLANCO(new ReyMoveGenerator(Color.BLANCO)),
	REY_NEGRO(new ReyMoveGenerator(Color.NEGRO));
	
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
