package movecalculators;

import java.util.ArrayList;
import java.util.Collection;

import chess.BoardAnalyzer;
import chess.BoardState;
import chess.Color;
import chess.Move;
import chess.MoveCache;
import chess.PosicionPieza;
import chess.Square;
import iterators.SquareIterator;
import layers.ColorBoard;
import layers.DummyBoard;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;
import positioncaptures.Capturer;
import positioncaptures.ImprovedCapturer;

public class DefaultLegalMoveCalculator implements LegalMoveCalculator {
	// Al final del dia estas son dos representaciones distintas del tablero
	private DummyBoard dummyBoard = null; 
	private ColorBoard colorBoard = null;
	
	// Esta es una capa mas de informacion del tablero
	private MoveCache moveCache = null;		
	
	private BoardState boardState = null;	
	
	private MoveGeneratorStrategy strategy = null;
	
	protected Capturer capturer = null;
	
	public DefaultLegalMoveCalculator(DummyBoard dummyBoard, ColorBoard colorBoard, MoveCache moveCache, BoardState boardState,
			MoveGeneratorStrategy strategy) {
		this.dummyBoard = dummyBoard;
		this.colorBoard = colorBoard;
		this.moveCache = moveCache;		
		this.boardState = boardState;
		this.strategy = strategy;
		this.capturer = new ImprovedCapturer(dummyBoard);
	}	

	private Color turnoActual = null;
	private Color opositeTurnoActual = null;

	@Override
	public Collection<Move> getLegalMoves(BoardAnalyzer analyzer) {
		turnoActual = boardState.getTurnoActual();
		opositeTurnoActual = turnoActual.opositeColor();

		Collection<Move> moves = createContainer();
		

		for (SquareIterator iterator = colorBoard.iteratorSquare(turnoActual); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();
			
			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);

			// De almacenar movimientos en un cache, estos moviemientos son pseudo, es imposible almacenar movimientos legales en un cache !!!
			// Ejemplo supongamos que almacenamos movimientos de torre blanca en a5, rey blanco se encuentra en e1 y es turno blancas.
			// En movimiento anterior Reina Negra se movió desde h7 a e7 y ahora el rey blanco e1 queda en jaque.
			// Solo movimiento de torre a5 e5 es VALIDO, el resto deja al rey en Jaque
			// Esto quiere decir que una vez obtenidos todos los movimientos pseudo debemos filtrarlos SI o SI
			for (Move move : pseudoMoves) {
				if(filterMove(move)){
					moves.add(move);
				}
			}
			
			//boardCache.validarCacheSqueare(dummyBoard);
		}
		
		return moves;
	}
	
	private Collection<Move> getPseudoMoves(Square origenSquare) {
		Collection<Move> pseudoMoves = null;


		pseudoMoves = moveCache.getPseudoMoves(origenSquare);

		if (pseudoMoves == null) {

			PosicionPieza origen = dummyBoard.getPosicion(origenSquare);
			
			//Pieza pieza = origen.getValue();
			//MoveGenerator moveGenerator = pieza.getMoveGenerator(strategy);

			MoveGenerator moveGenerator = strategy.getMoveGenerator(origen.getValue());

			MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);

			pseudoMoves = generatorResult.getPseudoMoves();

			if (generatorResult.isSaveMovesInCache()) {
				moveCache.setPseudoMoves(origen.getKey(), pseudoMoves);
				moveCache.setAffectedBy(origen.getKey(), generatorResult.getAffectedBy());
			}
		}
		
		return pseudoMoves;
	}	

	private boolean filterMove(Move move) {
		boolean result = false;
		
		//boardCache.validarCacheSqueare(dummyBoard);
				
		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);
		 
		if(! capturer.positionCaptured(this.opositeTurnoActual, getCurrentKingSquare())) {
			result = true;
		}
		
		move.undoMove(this.colorBoard);
		move.undoMove(this.dummyBoard);
		
		//boardCache.validarCacheSqueare(dummyBoard);
		
		return result;
	}
	
	
	private Square getCurrentKingSquare() {
		return Color.BLANCO.equals(this.turnoActual) ? colorBoard.getSquareKingBlancoCache() : colorBoard.getSquareKingNegroCache();
	}

	private static <T> Collection<T> createContainer(){
		return new ArrayList<T>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (T move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
	}	
}
