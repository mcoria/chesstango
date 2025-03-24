package net.chesstango.board.moves.generators.legal.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.LegalMoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.moves.imp.MoveCommand;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractLegalMoveGenerator implements LegalMoveGenerator {

    protected final ChessPositionReader positionReader;
    protected final MoveGenerator pseudoMovesGenerator;

    protected final LegalMoveFilter filter;

    public AbstractLegalMoveGenerator(ChessPositionReader positionReader,
                                      MoveGenerator strategy,
                                      LegalMoveFilter filter) {
        this.positionReader = positionReader;
        this.pseudoMovesGenerator = strategy;
        this.filter = filter;
    }

    protected MoveList<MoveCommand> getPseudoMoves(PiecePositioned origen) {
        MoveGeneratorResult generatorResult = pseudoMovesGenerator.generatePseudoMoves(origen);
        return generatorResult.getPseudoMoves();
    }

    protected MoveList<MoveCommand> getPseudoMoves(Square origenSquare) {
        return getPseudoMoves(positionReader.getPosition(origenSquare));
    }

    protected void getEnPassantMoves(MoveContainer<MoveCommand> moves) {
        final MovePair<MoveCommand> pseudoMoves = pseudoMovesGenerator.generateEnPassantPseudoMoves();
        filterMovePair(pseudoMoves, moves);
    }

    protected void filterMovePair(MovePair<MoveCommand> movePairToFilter, MoveContainer<MoveCommand> collectionToAdd) {
        if (movePairToFilter != null) {
            final MoveCommand first = movePairToFilter.getFirst();
            final MoveCommand second = movePairToFilter.getSecond();

            if (first != null) {
                filter(first, collectionToAdd);
            }

            if (second != null) {
                filter(second, collectionToAdd);
            }
        }
    }

    protected void filterMoveCollection(Iterable<MoveCommand> moveCollectionToFilter, MoveContainer<MoveCommand> collectionToAdd) {
        if (moveCollectionToFilter != null) {
            for (MoveCommand move : moveCollectionToFilter) {
                filter(move, collectionToAdd);
            }
        }
    }

    protected void filter(MoveCommand move, MoveContainer<MoveCommand> collectionToAdd) {
        if (move.isLegalMove(filter)) {
            collectionToAdd.add(move);
        }
    }


    protected Square getCurrentKingSquare() {
        return positionReader.getKingSquare(positionReader.getCurrentTurn());
    }

}