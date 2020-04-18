package moveexecutors;

import chess.BoardState;
import chess.PosicionPieza;
import chess.Square;

public class SaltoDoblePeonMove extends SimpleMove {
	
	private final Square peonPasanteSquare;

	public SaltoDoblePeonMove(PosicionPieza from, PosicionPieza to, Square peonPasanteSquare) {
		super(from, to);
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	@Override
	public void executeState(BoardState boardState) {
		super.executeState(boardState);
		boardState.setPeonPasanteSquare(peonPasanteSquare);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SaltoDoblePeonMove){
			return true;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "SaltoDoblePeonMove";
	}
}
