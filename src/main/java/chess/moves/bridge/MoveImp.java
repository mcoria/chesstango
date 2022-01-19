package chess.moves.bridge;

import chess.PiecePositioned;
import chess.legalmovesgenerators.MoveFilter;
import chess.moves.Move;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;

//TOOD: Y si en vez de PosicionPieza utilizamos Square para To?
//      La mayoria de los movimientos posibles es a square vacios
//      Tiene sentido puesto que las capturas solo son contra piezas contrarias, sin importar que pieza es.

//TODO: y se implementamos un cache de movimientos? Implementar flyweight  pattern
//TODO: implementar Factory para crear objetos, la creacion está repartida por todas partes y habria que desacoplarla de move generators 
// 		ademas con Decorartor se complicó por ejemplo: 
// 				algunos movimientos de King quitan derecho a enroque; 
//				todos los movimientos de torre que la mueven de su posicion inicial hacen perder enroque
//				toda captura a una torre que que se encuentra en si posicion inicial hacen perder enroque
//		cada vez que se cambia la jerarquia o hay algun tipo de modificacion en estos objetos las clases de pruebas necesitan ser actualizadas

//TODO: implement bridge pattern.

/**
 * @author Mauricio Coria
 *
 */
class MoveImp implements Move {
	protected final PiecePositioned from;
	protected final PiecePositioned to;
	
	private Object stateChange;
	
	public MoveImp(PiecePositioned from, PiecePositioned to) {
		this.from = from;
		this.to = to;
	}	

	@Override
	public PiecePositioned getFrom() {
		return from;
	}

	@Override
	public PiecePositioned getTo() {
		return to;
	}
	
	@Override
	public void executeMove(PiecePlacement board) {
		board.move(from, to);
	}
	
	@Override
	public void undoMove(PiecePlacement board) {
		board.setPosicion(to);							//Reestablecemos destino
		board.setPosicion(from);						//Volvemos a origen
	}	

	//TODO: implementar un decorator antes de crear el movimiento
	@Override
	public void executeMove(PositionState positionState) {
		positionState.pushState();
		positionState.rollTurno();
		positionState.setPawnPasanteSquare(null); 			// Por defecto en null y solo escribimos en SaltoDoblePawnMove		
	}
	
	@Override
	public void undoMove(PositionState positionState) {
		positionState.popState();		
	}

	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushCleared();
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), true);		
	}

	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), false);
		moveCache.popCleared();
	}
	
	@Override
	public int hashCode() {
		return from.getKey().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Move){
			Move theOther = (Move) obj;
			return from.equals(theOther.getFrom()) &&  to.equals(theOther.getTo());
		}
		return false;
	}

	@Override
	public int compareTo(Move theOther) {
		//Comparamos from
		if(this.from.getKey().getRank() > theOther.getFrom().getKey().getRank()){
			return 1;
		} else if (this.from.getKey().getRank() < theOther.getFrom().getKey().getRank()){
			return -1;
		}
		

		if(this.from.getKey().getFile() <  theOther.getFrom().getKey().getFile()){
			return 1;
		} else if(this.from.getKey().getFile() >  theOther.getFrom().getKey().getFile()){
			return -1;
		}
		
		//---------------
		//Son iguales asi que comparamos to
		if(this.to.getKey().getRank() < theOther.getTo().getKey().getRank()){
			return 1;
		} else if (this.to.getKey().getRank() > theOther.getTo().getKey().getRank()){
			return -1;
		}
		

		if(this.to.getKey().getFile() <  theOther.getTo().getKey().getFile()){
			return -1;
		} else if(this.to.getKey().getFile() >  theOther.getTo().getKey().getFile()){
			return 1;
		}
		
		//--------------- Desde y hasta coinciden, que hacemos ?
		
		return 0;
	}

	@Override
	public String toString() {
		return from.toString() + " " + to.toString() + " - " + this.getClass().getSimpleName().toString();
	}

	/* (non-Javadoc)
	 * @see chess.moves.Move#executeMove(chess.position.ChessPosition)
	 */
	@Override
	public void executeMove(ChessPosition chessPosition) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see chess.moves.Move#undoMove(chess.position.ChessPosition)
	 */
	@Override
	public void undoMove(ChessPosition chessPosition) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see chess.moves.Move#filter(chess.legalmovesgenerators.MoveFilter)
	 */
	@Override
	public boolean filter(MoveFilter filter) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see chess.moves.Move#executeMove(chess.position.ColorBoard)
	 */
	@Override
	public void executeMove(ColorBoard coloBoard) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see chess.moves.Move#undoMove(chess.position.ColorBoard)
	 */
	@Override
	public void undoMove(ColorBoard colorBoard) {
		// TODO Auto-generated method stub
		
	}
}