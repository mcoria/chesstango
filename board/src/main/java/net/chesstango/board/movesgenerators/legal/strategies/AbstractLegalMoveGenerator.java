package net.chesstango.board.movesgenerators.legal.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Collection;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractLegalMoveGenerator implements LegalMoveGenerator {

	protected final ChessPositionReader positionReader;
	protected final MoveGenerator pseudoMovesGenerator;
	
	protected final MoveFilter filter;
	
	public AbstractLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator strategy, MoveFilter filter) {
		this.positionReader = positionReader;
		this.pseudoMovesGenerator = strategy;
		this.filter = filter;
	}

	protected MoveList getPseudoMoves(PiecePositioned origen) {
		MoveGeneratorResult generatorResult = pseudoMovesGenerator.generatePseudoMoves(origen);
		return generatorResult.getPseudoMoves();
	}

	protected Collection<Move> getPseudoMoves(Square origenSquare) {
		return getPseudoMoves(positionReader.getPosition(origenSquare));
	}

	//TODO: este metodo no tien buena performance
	protected long getCapturedPositionsOponente(){
		final Color turnoActual = this.positionReader.getCurrentTurn();

		long posicionesCapturadas = 0;
		
		for (SquareIterator iterator = this.positionReader.iteratorSquare( turnoActual.oppositeColor() ); iterator.hasNext();) {

			Square origenSquare = iterator.next();

			MoveGeneratorResult generatorResult = pseudoMovesGenerator.generatePseudoMoves(positionReader.getPosition(origenSquare));
			
			posicionesCapturadas |= generatorResult.getCapturedPositions();

		}

		return posicionesCapturadas;		
	}

	protected void getEnPassantMoves(MoveContainer moves) {
		final MovePair pseudoMoves = pseudoMovesGenerator.generateEnPassantPseudoMoves();
		filterMoveCollection(pseudoMoves, moves);
	}

	protected void filterMovePair(MovePair movePairToFilter, MoveContainer collectionToAdd) {
		if(movePairToFilter != null){
			final Move first = movePairToFilter.getFirst();
			final Move second = movePairToFilter.getSecond();

			if(first != null) {
				filter(first, collectionToAdd);
			}

			if(second != null) {
				filter(second, collectionToAdd);
			}
		}
	}

	protected void filterMoveCollection(Iterable<? extends Move> moveCollectionToFilter, MoveContainer collectionToAdd){
		if(moveCollectionToFilter != null) {
			for (Move move : moveCollectionToFilter) {
				filter(move, collectionToAdd);
			}
		}
	}

	protected void filter(Move move, MoveContainer collectionToAdd) {
		if(move.filter(filter)){
			collectionToAdd.add(move);
		}
	}


	protected Square getCurrentKingSquare() {
		return positionReader.getKingSquare(positionReader.getCurrentTurn());
	}	

}