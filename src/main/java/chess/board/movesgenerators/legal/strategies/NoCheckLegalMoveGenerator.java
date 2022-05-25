package chess.board.movesgenerators.legal.strategies;

import java.util.Collection;
import java.util.Iterator;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.analyzer.AnalyzerResult;
import chess.board.iterators.Cardinal;
import chess.board.movesgenerators.legal.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;
import chess.board.moves.containers.MoveContainer;
import chess.board.moves.containers.MoveList;
import chess.board.moves.containers.MovePair;
import chess.board.position.ChessPositionReader;
import chess.board.movesgenerators.pseudo.MoveGenerator;

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
	public MoveContainerReader getLegalMoves(AnalyzerResult analysis) {
		final Square kingSquare = getCurrentKingSquare();

		final Color turnoActual = this.positionReader.getCurrentTurn();

		final long posicionesTurnoActual =  this.positionReader.getPositions(turnoActual);

		final long pinnedSquares = analysis.getPinnedSquares();

		final long posicionRey = kingSquare.getPosicion();

		long safePositions = posicionesTurnoActual & ~pinnedSquares & ~posicionRey;

		MoveContainer moves = new MoveContainer(Long.bitCount(safePositions));

		getLegalMovesNotKingNotPinned(safePositions, moves);

		getLegalMovesNotKingPinned(analysis, moves);
		
		getLegalMovesKing(moves);
		
		getEnPassantMoves(moves);
		
		getCastlingMoves(moves);
		
		return moves;
	}


	protected MoveContainer getLegalMovesNotKingNotPinned(long posicionesSafe, MoveContainer moves) {

		for (Iterator<PiecePositioned> iterator = this.positionReader.iterator(posicionesSafe); iterator.hasNext();) {

			PiecePositioned origen = iterator.next();

			MoveList pseudoMoves = getPseudoMoves(origen);

			moves.add(pseudoMoves);
		}

		return moves;
	}

	protected MoveContainer getLegalMovesNotKingPinned(AnalyzerResult analysis, MoveContainer moves) {
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


	protected MoveContainer getLegalMovesKing(MoveContainer moves) {
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

	protected void getCastlingMoves(MoveContainer moves) {
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
