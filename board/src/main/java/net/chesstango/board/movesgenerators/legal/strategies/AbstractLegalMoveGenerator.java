package net.chesstango.board.movesgenerators.legal.strategies;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractLegalMoveGenerator implements LegalMoveGenerator {

    protected final ChessPositionReader positionReader;
    protected final MoveGenerator pseudoMovesGenerator;

    protected final MoveFilter filter;

    public AbstractLegalMoveGenerator(ChessPositionReader positionReader,
                                      MoveGenerator strategy,
                                      MoveFilter filter) {
        this.positionReader = positionReader;
        this.pseudoMovesGenerator = strategy;
        this.filter = filter;
    }

    protected MoveList getPseudoMoves(PiecePositioned origen) {
        MoveGeneratorResult generatorResult = pseudoMovesGenerator.generatePseudoMoves(origen);
        return generatorResult.getPseudoMoves();
    }

    protected MoveList getPseudoMoves(Square origenSquare) {
        return getPseudoMoves(positionReader.getPosition(origenSquare));
    }

    protected void addEnPassantMoves(MoveList moves) {
        final Iterable<Move> pseudoMoves = pseudoMovesGenerator.generateEnPassantPseudoMoves();
        filterMoveCollection(pseudoMoves, moves);
    }

    protected void filterMoveCollection(Iterable<Move> moveCollectionToFilter, MoveList collectionToAdd) {
        if (moveCollectionToFilter != null) {
            for (Move move : moveCollectionToFilter) {
                filter(move, collectionToAdd);
            }
        }
    }

    protected void filter(Move move, MoveList collectionToAdd) {
        if (move.filter(filter)) {
            collectionToAdd.add(move);
        }
    }


    protected Square getCurrentKingSquare() {
        return positionReader.getKingSquare(positionReader.getCurrentTurn());
    }

}