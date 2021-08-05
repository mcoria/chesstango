package movegenerators;

import chess.CachePosiciones;
import chess.Color;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.CaptureReyMove;
import moveexecutors.EnroqueNegroReyMove;
import moveexecutors.EnroqueNegroReynaMove;
import moveexecutors.Move;
import moveexecutors.MoveDecoratorReyState;
import moveexecutors.SimpleReyMove;

public class ReyNegroMoveGenerator extends ReyAbstractMoveGenerator {

	protected static final Square INTERMEDIO_TORRE_REYNA_SQUARE = Square.b8;
	protected static final Square DESTINO_REYNA_SQUARE = Square.c8;
	protected static final Square INTERMEDIO_REY_REYNA_SQUARE = Square.d8;
	
	protected static final Square INTERMEDIO_REY_REY_SQUARE = Square.f8;	
	protected static final Square DESTINO_REY_SQUARE = Square.g8;
	
	
	public ReyNegroMoveGenerator() {
		super(Color.NEGRO);
	}
	
	@Override
	public void generateMovesPseudoMoves(PosicionPieza origen) {		
		super.generateMovesPseudoMoves(origen);
		
		this.saveMovesInCache =  ! (this.boardState.isEnroqueNegroReinaPermitido() ||  this.boardState.isEnroqueNegroReyPermitido()) ;
		
		if (this.boardState.isEnroqueNegroReinaPermitido() && 
			puedeEnroqueReina(	origen, 
								CachePosiciones.REY_NEGRO, 
								CachePosiciones.TORRE_NEGRO_REYNA,
								INTERMEDIO_TORRE_REYNA_SQUARE, 
								DESTINO_REYNA_SQUARE, 
								INTERMEDIO_REY_REYNA_SQUARE)) {
			this.result.moveContainerAdd(new EnroqueNegroReynaMove());
		}
			
			
		if (this.boardState.isEnroqueNegroReyPermitido() && 
			puedeEnroqueRey(	origen, 
								CachePosiciones.REY_NEGRO, 
								CachePosiciones.TORRE_NEGRO_REY,
								DESTINO_REY_SQUARE, 
								INTERMEDIO_REY_REY_SQUARE)) {
			this.result.moveContainerAdd(new EnroqueNegroReyMove());			
		}
	}

	@Override
	protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e8.equals(origen.getKey())){
			return new MoveDecoratorReyState(new SimpleReyMove(origen, destino), state -> {
				boardState.setEnroqueNegroReinaPermitido(false);
				boardState.setEnroqueNegroReyPermitido(false);			
			});
		}
		return new SimpleReyMove(origen, destino);
	}

	@Override
	protected Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e8.equals(origen.getKey())){
			return new MoveDecoratorReyState(new CaptureReyMove(origen, destino), state -> {
				boardState.setEnroqueNegroReinaPermitido(false);
				boardState.setEnroqueNegroReyPermitido(false);			
			});
		}
		return new CaptureReyMove(origen, destino);
	}	

}
