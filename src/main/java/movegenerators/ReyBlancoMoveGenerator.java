package movegenerators;

import chess.CachePosiciones;
import chess.Color;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.EnroqueBlancoReyMove;
import moveexecutors.EnroqueBlancoReynaMove;

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
		
		//ver KiwipeteTest.test_d5d6_h3g2
		this.saveMovesInCache =  ! (this.boardState.isEnroqueBlancoReinaPermitido() ||  this.boardState.isEnroqueBlancoReyPermitido()) ;
		
		//this.saveMovesInCache = true;
		
		if (this.boardState.isEnroqueBlancoReinaPermitido() && 
			puedeEnroqueReina(	this.tablero, 
								origen, 
								CachePosiciones.REY_BLANCO, 
								CachePosiciones.TORRE_BLANCA_REYNA,
								INTERMEDIO_TORRE_REYNA_SQUARE, 
								DESTINO_REYNA_SQUARE, 
								INTERMEDIO_REY_REYNA_SQUARE)) {
			//result.affectedByContainerAdd(Square.a1);
			//result.affectedByContainerAdd(INTERMEDIO_TORRE_REYNA_SQUARE);
			//result.affectedByContainerAdd(DESTINO_REYNA_SQUARE);
			//result.affectedByContainerAdd(INTERMEDIO_REY_REYNA_SQUARE);
			result.moveContainerAdd(new EnroqueBlancoReynaMove());
			//this.saveMovesInCache = false;
		}
		
		
		if (this.boardState.isEnroqueBlancoReyPermitido() && 
			puedeEnroqueRey(this.tablero, 
							origen, 
							CachePosiciones.REY_BLANCO, 
							CachePosiciones.TORRE_BLANCA_REY,
							DESTINO_REY_SQUARE, 
							INTERMEDIO_REY_REY_SQUARE)) {
			//result.affectedByContainerAdd(INTERMEDIO_REY_REY_SQUARE);
			//result.affectedByContainerAdd(DESTINO_REY_SQUARE);
			//result.affectedByContainerAdd(Square.h1);
			result.moveContainerAdd(new EnroqueBlancoReyMove());
			//this.saveMovesInCache = false;
		}
	}

}
