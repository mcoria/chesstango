package movegenerators;

import chess.CachePosiciones;
import chess.Color;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.EnroqueBlancoReyMove;
import moveexecutors.EnroqueBlancoReynaMove;
import moveexecutors.Move;


/**
 * @author Mauricio Coria
 *
 */
public class ReyBlancoMoveGenerator extends ReyAbstractMoveGenerator {

	protected static final Square INTERMEDIO_TORRE_REYNA_SQUARE = Square.b1;
	protected static final Square DESTINO_REYNA_SQUARE = Square.c1;
	protected static final Square INTERMEDIO_REY_REYNA_SQUARE = Square.d1;
	
	protected static final Square INTERMEDIO_REY_REY_SQUARE = Square.f1;	
	protected static final Square DESTINO_REY_SQUARE = Square.g1;
	
	
	public ReyBlancoMoveGenerator() {
		super(Color.BLANCO);
	}
	
	@Override
	public void generateMovesPseudoMoves(PosicionPieza origen) {
		super.generateMovesPseudoMoves(origen);
		
		if (this.boardState.isEnroqueBlancoReinaPermitido()){
			result.affectedByContainerAdd(INTERMEDIO_TORRE_REYNA_SQUARE);
			result.affectedByContainerAdd(DESTINO_REYNA_SQUARE);
			result.affectedByContainerAdd(INTERMEDIO_REY_REYNA_SQUARE);
			result.affectedByContainerAdd(Square.a1); //La posicion de la torre
			if(puedeEnroqueReina(	origen, 
								CachePosiciones.REY_BLANCO, 
								CachePosiciones.TORRE_BLANCA_REYNA,
								INTERMEDIO_TORRE_REYNA_SQUARE, 
								DESTINO_REYNA_SQUARE, 
								INTERMEDIO_REY_REYNA_SQUARE)) {
				result.moveContainerAdd(new EnroqueBlancoReynaMove());
			}
		}
		
		
		if (this.boardState.isEnroqueBlancoReyPermitido() ){
			result.affectedByContainerAdd(INTERMEDIO_REY_REY_SQUARE);
			result.affectedByContainerAdd(DESTINO_REY_SQUARE);
			result.affectedByContainerAdd(Square.h1); //La posicion de la torre		
			if(puedeEnroqueRey(	origen, 
								CachePosiciones.REY_BLANCO, 
								CachePosiciones.TORRE_BLANCA_REY,
								DESTINO_REY_SQUARE, 
								INTERMEDIO_REY_REY_SQUARE)) {
				this.result.moveContainerAdd(new EnroqueBlancoReyMove());
			}
		}
	}
	
	//TODO: agregar test case (cuando el rey se mueve pierde enroque) y agregar validacion en state 
	@Override
	protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
		return this.moveFactory.createSimpleReyMoveBlanco(origen, destino);
	}

	@Override
	protected Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return this.moveFactory.createCaptureReyMoveBlanco(origen, destino);
	}	

}
