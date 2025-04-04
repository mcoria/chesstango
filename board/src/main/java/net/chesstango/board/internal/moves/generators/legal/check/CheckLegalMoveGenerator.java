package net.chesstango.board.internal.moves.generators.legal.check;

import net.chesstango.board.Square;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.internal.moves.generators.legal.AbstractLegalMoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 */
public class CheckLegalMoveGenerator extends AbstractLegalMoveGenerator {

    public CheckLegalMoveGenerator(ChessPositionReader positionReader,
                                   MoveGenerator strategy,
                                   LegalMoveFilter filter) {
        super(positionReader, strategy, filter);
    }

    @Override
    public MoveContainerReader<Move> getLegalMoves(AnalyzerResult analysis) {
        MoveContainer<Move> moves = new MoveContainer<>();

        collectBySquareMoves(moves);

        collectEnPassantMoves(moves);

        return moves;
    }

    protected void collectBySquareMoves(MoveContainer<Move> moves) {
        for (SquareIterator iterator = positionReader.iteratorSquare(positionReader.getCurrentTurn()); iterator.hasNext(); ) {

            Square origenSquare = iterator.next();

            MoveList<PseudoMove> pseudoMoves = getPseudoMoves(origenSquare);

            filterMoveCollection(pseudoMoves, moves);
        }
    }
}
