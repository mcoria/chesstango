package chess;

public enum Pieza {
	PEON_BLANCO(Color.BLANCO),
	PEON_NEGRO(Color.NEGRO),
	
	TORRE_BLANCO(Color.BLANCO),
	TORRE_NEGRO(Color.NEGRO),
	
	CABALLO_BLANCO(Color.BLANCO),
	CABALLO_NEGRO(Color.NEGRO),
	
	ALFIL_BLANCO(Color.BLANCO),
	ALFIL_NEGRO(Color.NEGRO),
	
	REINA_BLANCO(Color.BLANCO),
	REINA_NEGRO(Color.NEGRO),
	
	REY_BLANCO(Color.BLANCO),
	REY_NEGRO(Color.NEGRO);
	
	private Color color;
	
	private Pieza(Color color) {
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
	
	/*

	@Override
	public Collection<Move> getLegalMoves(DummyBoard board, Map.Entry<Square, Pieza> origen) {
		return generator.getLegalMoves(board, origen);
	}

	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Map.Entry<Square, Pieza> origen, Square kingSquare) {
		return generator.puedeCapturarRey(dummyBoard, origen, kingSquare);
	}*/
	
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
