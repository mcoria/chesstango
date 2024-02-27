package net.chesstango.board.movesgenerators.legal.strategies;

import net.chesstango.board.Square;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 */
public class CheckLegalMoveGenerator extends AbstractLegalMoveGenerator {

    public CheckLegalMoveGenerator(ChessPositionReader positionReader,
                                   MoveGenerator strategy,
                                   MoveFilter filter) {
        super(positionReader, strategy, filter);
    }

    @Override
    public MoveList getLegalMoves(AnalyzerResult analysis) {
        MoveList moveContainer = new MoveList();

        addBySquareMoves(moveContainer);

        addEnPassantMoves(moveContainer);

        return moveContainer;
    }

    protected void addBySquareMoves(MoveList moveContainer) {
        for (SquareIterator iterator = positionReader.iteratorSquare(positionReader.getCurrentTurn()); iterator.hasNext(); ) {

            Square origenSquare = iterator.next();

            Iterable<Move> pseudoMoves = getPseudoMoves(origenSquare);

            filterMoveCollection(pseudoMoves, moveContainer);
        }
    }
}
