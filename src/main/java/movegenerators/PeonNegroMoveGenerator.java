package movegenerators;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

import chess.Color;
import chess.Pieza;
import chess.Square;

public class PeonNegroMoveGenerator extends PeonAbstractMoveGenerator {
	
	public PeonNegroMoveGenerator() {
		super(Color.NEGRO);
	}


	@Override
	protected Square getCasilleroSaltoSimple(Square casillero) {
		return Square.getSquare(casillero.getFile(), casillero.getRank() - 1);
	}

	@Override
	protected Square getCasilleroSaltoDoble(Square casillero) {
		return casillero.getRank() == 6 ? Square.getSquare(casillero.getFile(), 4) : null;
	}

	@Override
	protected Square getCasilleroAtaqueIzquirda(Square casillero) {
		return Square.getSquare(casillero.getFile() - 1, casillero.getRank() - 1);
	}
	
	@Override
	protected Square getCasilleroAtaqueDerecha(Square casillero) {
		return Square.getSquare(casillero.getFile() + 1, casillero.getRank() - 1);
	}	

	@Override
	protected Entry<Square, Pieza> getCapturaPeonPasante(Square peonPasanteSquare) {
		return new SimpleImmutableEntry<Square, Pieza>(Square.getSquare(peonPasanteSquare.getFile(), 3), Pieza.PEON_BLANCO);
	}	
}
