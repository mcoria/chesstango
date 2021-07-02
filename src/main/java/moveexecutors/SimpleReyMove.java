package moveexecutors;

import chess.PosicionPieza;

public class SimpleReyMove extends AbstractKingMove {

	
	public SimpleReyMove(PosicionPieza from, PosicionPieza to) {
		super(new SimpleMove(from, to));
	}	
	
	public SimpleReyMove(AbstractMove move) {
		super(move);
	}

	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SimpleReyMove){
			return true;
		}
		return false;
	}

}
