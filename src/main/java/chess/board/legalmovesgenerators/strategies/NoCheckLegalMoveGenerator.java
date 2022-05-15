package chess.board.legalmovesgenerators.strategies;

import java.util.Collection;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.analyzer.AnalyzerResult;
import chess.board.iterators.Cardinal;
import chess.board.iterators.pieceplacement.PiecePlacementIterator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.MoveCastling;
import chess.board.moves.containsers.MoveContainer;
import chess.board.moves.containsers.MovePair;
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
		final Square kingSquare = getCurrentKingSquare();

		final Color turnoActual = this.positionReader.getTurnoActual();

		final long posicionesTurnoActual =  this.positionReader.getPosiciones(turnoActual);

		final long pinnedSquares = analysis.getPinnedSquares();

		final long posicionRey = kingSquare.getPosicion();

		Collection<Move> moves = new MoveContainer(CAPACITY_MOVE_CONTAINER);

		getLegalMovesNotKingNotPinned(posicionesTurnoActual & ~pinnedSquares & ~posicionRey, moves);

		getLegalMovesNotKingPinned(analysis, moves);
		
		getLegalMovesKing(moves);
		
		getEnPassantMoves(moves);
		
		getCastlingMoves(moves);
		
		return moves;
	}


	protected Collection<Move> getLegalMovesNotKingNotPinned(long posicionesSafe, Collection<Move> moves) {

		for (PiecePlacementIterator iterator = this.positionReader.iterator(posicionesSafe) ; iterator.hasNext();) {

			PiecePositioned origen = iterator.next();

			Collection<Move> pseudoMoves = getPseudoMoves(origen);

			moves.addAll(pseudoMoves);
		}

		return moves;
	}

	protected Collection<Move> getLegalMovesNotKingPinned(AnalyzerResult analysis, Collection<Move> moves) {
		analysis.getPinnedPositionCardinals().forEach( pinnedPositionCardinal -> {
			getPseudoMoves(pinnedPositionCardinal.getKey())
					.stream()
					.filter(pseudoMove -> {
						return NoCheckLegalMoveGenerator.moveBlocksThreat(pinnedPositionCardinal.getValue(),  pseudoMove.getMoveDirection() );
					} )
					.forEach(move -> moves.add(move));
		});

		return moves;
	}


	protected Collection<Move> getLegalMovesKing(Collection<Move> moves) {
		Square 	kingSquare = getCurrentKingSquare();

		Collection<Move> pseudoMovesKing = getPseudoMoves(kingSquare);

		filterMoveCollection(pseudoMovesKing, moves);

		return moves;
	}

	//TODO: Esta complicado este metodo, se pierde demasiada performance
	/*
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
	 */

	protected void getCastlingMoves(Collection<Move> moves) {
		final MovePair pseudoMoves = pseudoMovesGenerator.generateCastlingPseudoMoves();
		filterMoveCollection(pseudoMoves, moves);
	}

	public static boolean moveBlocksThreat(Cardinal threatDirection, Cardinal moveDirection) {
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
	

}
