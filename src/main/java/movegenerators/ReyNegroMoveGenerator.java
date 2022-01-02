package movegenerators;

import chess.CachePosiciones;
import chess.Color;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.EnroqueNegroReyMove;
import moveexecutors.EnroqueNegroReynaMove;
import moveexecutors.Move;

/**
 * @author Mauricio Coria
 *
 */
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
		
		//this.saveMovesInCache =  ! (this.boardState.isEnroqueNegroReinaPermitido() ||  this.boardState.isEnroqueNegroReyPermitido()) ;
		
		if (this.boardState.isEnroqueNegroReinaPermitido()){
			result.affectedByContainerAdd(INTERMEDIO_TORRE_REYNA_SQUARE);
			result.affectedByContainerAdd(DESTINO_REYNA_SQUARE);
			result.affectedByContainerAdd(INTERMEDIO_REY_REYNA_SQUARE);		
			result.affectedByContainerAdd(Square.a8); //La posicion de la torre
			if(puedeEnroqueReina(	origen, 
								CachePosiciones.REY_NEGRO, 
								CachePosiciones.TORRE_NEGRO_REYNA,
								INTERMEDIO_TORRE_REYNA_SQUARE, 
								DESTINO_REYNA_SQUARE, 
								INTERMEDIO_REY_REYNA_SQUARE)) {
				result.moveContainerAdd(new EnroqueNegroReynaMove());
			}
		}
			
			
		if (this.boardState.isEnroqueNegroReyPermitido()){
			result.affectedByContainerAdd(INTERMEDIO_REY_REY_SQUARE);
			result.affectedByContainerAdd(DESTINO_REY_SQUARE);
			result.affectedByContainerAdd(Square.h8); //La posicion de la torre
			if(puedeEnroqueRey(	origen, 
								CachePosiciones.REY_NEGRO, 
								CachePosiciones.TORRE_NEGRO_REY,
								DESTINO_REY_SQUARE, 
								INTERMEDIO_REY_REY_SQUARE)) {
				result.moveContainerAdd(new EnroqueNegroReyMove());
			}
		}
	}

	//TODO: agregar test case (cuando el rey se mueve pierde enroque) y agregar validacion en state 
	@Override
	protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
		return this.moveFactory.createSimpleReyMoveNegro(origen, destino);
	}

	@Override
	protected Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return this.moveFactory.createCaptureReyMoveNegro(origen, destino);
	}	

}
