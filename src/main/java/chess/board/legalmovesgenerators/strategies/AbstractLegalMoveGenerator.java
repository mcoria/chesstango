package chess.board.legalmovesgenerators.strategies;

import java.util.Collection;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.square.SquareIterator;
import chess.board.legalmovesgenerators.LegalMoveGenerator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.containsers.MovePair;
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

	protected Collection<Move> getPseudoMoves(PiecePositioned origen) {
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

	protected void getEnPassantMoves(Collection<Move> moves) {
		final MovePair pseudoMoves = pseudoMovesGenerator.generateEnPassantPseudoMoves();
		if(pseudoMoves != null){
			final Move first = pseudoMoves.getFirst();
			final Move second = pseudoMoves.getSecond();

			if(first != null) {
				filter(first, moves);
			}

			if(second != null) {
				filter(second, moves);
			}
		}
	}
	
	protected void filterMoveCollection(Collection<? extends Move> moveCollectionToFilter, Collection<Move> collectionToAdd){
		for (Move move : moveCollectionToFilter) {
			filter(move, collectionToAdd);
		}
	}

	protected void filter(Move move, Collection<Move> collectionToAdd) {
		if(move.filter(filter)){
			collectionToAdd.add(move);
		}
	}


	protected Square getCurrentKingSquare() {
		return positionReader.getKingSquare(positionReader.getTurnoActual());
	}	

}