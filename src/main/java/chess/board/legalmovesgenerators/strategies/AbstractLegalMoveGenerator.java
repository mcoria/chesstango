package chess.board.legalmovesgenerators.strategies;

import java.util.Collection;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.square.SquareIterator;
import chess.board.legalmovesgenerators.LegalMoveGenerator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainer;
import chess.board.moves.containers.MoveList;
import chess.board.moves.containers.MovePair;
import chess.board.position.ChessPositionReader;
import chess.board.pseudomovesgenerators.MoveGenerator;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;

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
		return getPseudoMoves(positionReader.getPosicion(origenSquare));
	}

	//TODO: este metodo no tien buena performance
	protected long getCapturedPositionsOponente(){
		final Color turnoActual = this.positionReader.getTurnoActual();

		long posicionesCapturadas = 0;
		
		for (SquareIterator iterator = this.positionReader.iteratorSquare( turnoActual.oppositeColor() ); iterator.hasNext();) {

			Square origenSquare = iterator.next();

			MoveGeneratorResult generatorResult = pseudoMovesGenerator.generatePseudoMoves(positionReader.getPosicion(origenSquare));	
			
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
		return positionReader.getKingSquare(positionReader.getTurnoActual());
	}	

}