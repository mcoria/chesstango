package chess.board.legalmovesgenerators.strategies;

import java.util.Collection;

import chess.board.Color;
import chess.board.Square;
import chess.board.analyzer.AnalyzerResult;
import chess.board.iterators.Cardinal;
import chess.board.iterators.square.SquareIterator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.MoveCastling;
import chess.board.moves.MoveContainer;
import chess.board.position.ChessPositionReader;
import chess.board.pseudomovesgenerators.MoveGenerator;

//TODO: deberiamos contabilizar aquellas piezas que se exploraron en busca de movimientos validos y no producieron resultados validos.
//      de esta forma cuando se busca en getLegalMovesNotKing() no volver a filtrar los mismos movimientos

/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveGenerator extends AbstractLegalMoveGenerator {

	private static final int CAPACITY_MOVE_CONTAINER = 70;

	public NoCheckLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator strategy, MoveFilter filter) {
		super(positionReader, strategy, filter);
	}

	@Override
	public Collection<Move> getLegalMoves(AnalyzerResult analysis) {
		Collection<Move> moves = new MoveContainer<Move>(CAPACITY_MOVE_CONTAINER);
		
		getLegalMovesNotKing(analysis, moves);
		
		getLegalMovesKing(moves);
		
		getEnPassantMoves(moves);
		
		getCastlingMoves(moves);
		
		return moves;
	}

	//TODO: Incluso este metodo lo podemos dividirlo en 2, aquellos pinned y aquellos no pinned
	protected Collection<Move> getLegalMovesNotKing(AnalyzerResult analysis, Collection<Move> moves) {
		final Color turnoActual = this.positionReader.getTurnoActual();

		long pinnedSquares = analysis.getPinnedSquares();
		
		for (SquareIterator iterator = this.positionReader.iteratorSquareWhitoutKing(turnoActual); iterator.hasNext();) {

			Square origenSquare = iterator.next();

			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);
			
			
			long currentPiecePosition = origenSquare.getPosicion();
					
			if ( (pinnedSquares & currentPiecePosition) != 0 ) {

				Cardinal threatDirection = analysis.getThreatDirection(origenSquare);
				
				pseudoMoves.forEach(move -> {
					Cardinal moveDirection = move.getMoveDirection();
					if (moveBlocksThreat(threatDirection, moveDirection)) {
						moves.add(move);
					}
				});
				

			} else {

				// TODO: implementar una clase contenedora de movimientos
				moves.addAll(pseudoMoves);
				
				//pseudoMoves.forEach(move -> moves.add(move));
				
			}

		}

		return moves;
	}
	
	protected void getCastlingMoves(Collection<Move> moves) {
		Collection<MoveCastling> pseudoMoves = pseudoMovesGenerator.generateCastlingPseudoMoves();
		long capturedPositionsOponente = this.getCapturedPositionsOponente();
		for (MoveCastling move : pseudoMoves) {
			long posicionesRey = (move.getRookMove().getTo().getKey().getPosicion())
					| (move.getTo().getKey().getPosicion());
			if ((capturedPositionsOponente & posicionesRey) == 0) {
				moves.add(move);
			}
		}
	}	


	private boolean moveBlocksThreat(Cardinal threatDirection, Cardinal moveDirection) {
		if(moveDirection != null){
			switch (threatDirection) {
			case Norte:
			case Sur:
				if (Cardinal.Norte.equals(moveDirection) || Cardinal.Sur.equals(moveDirection)) {
					return true;
				}			
				break;
			case Este:
			case Oeste:
				if (Cardinal.Este.equals(moveDirection) || Cardinal.Oeste.equals(moveDirection)) {
					return true;
				}				
				break;
			case NorteEste:
			case SurOeste:
				if (Cardinal.NorteEste.equals(moveDirection) || Cardinal.SurOeste.equals(moveDirection)) {
					return true;
				}				
				break;
			case NorteOeste:
			case SurEste:
				if (Cardinal.NorteOeste.equals(moveDirection) || Cardinal.SurEste.equals(moveDirection)) {
					return true;
				}				
				break;
			default:
				throw new RuntimeException("Falta direccion");
			}

		}
		return false;
	}


	protected Collection<Move> getLegalMovesKing(Collection<Move> moves) {		
		Square 	kingSquare = getCurrentKingSquare();
		
		Collection<Move> pseudoMovesKing = getPseudoMoves(kingSquare);

		filterMoveCollection(pseudoMovesKing, moves);
		
		return moves;
	}
	

}
