package net.chesstango.board.movesgenerators.legal.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.movesgenerators.legal.LegalMoveFilter;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

//TODO: deberiamos contabilizar aquellas piezas que se exploraron en busca de movimientos validos y no producieron resultados validos.
//      de esta forma cuando se busca en getLegalMovesNotKing() no volver a filtrar los mismos movimientos

/**
 * @author Mauricio Coria
 */
public class NoCheckLegalMoveGenerator extends AbstractLegalMoveGenerator {

    private static final int CAPACITY_MOVE_CONTAINER = 70;

    public NoCheckLegalMoveGenerator(ChessPositionReader positionReader,
                                     MoveGenerator strategy,
                                     LegalMoveFilter filter) {
        super(positionReader, strategy, filter);
    }

    @Override
    public MoveContainerReader getLegalMoves(AnalyzerResult analysis) {
        final Square kingSquare = getCurrentKingSquare();

        final Color currentTurn = positionReader.getCurrentTurn();

        final long currentTurnPositions = positionReader.getPositions(currentTurn);

        final long pinnedSquares = analysis.getPinnedSquares();

        final long kingPosition = kingSquare.getBitPosition();

        //final long capturedPositions = analysis.getCapturedPositions();

        final long safePositions = currentTurnPositions & ~pinnedSquares & ~kingPosition;

        MoveContainer moves = new MoveContainer(Long.bitCount(safePositions));

        getLegalMovesNotKingNotPinned(safePositions, moves);

        getLegalMovesNotKingPinned(analysis.getPinnedPositionCardinals(), moves);

        getLegalMovesKing(analysis.getSafeKingPositions(), moves);

        getEnPassantMoves(moves);

        getCastlingMoves(moves);

        return moves;
    }


    protected MoveContainer getLegalMovesNotKingNotPinned(long safePositions, MoveContainer moves) {

        for (Iterator<PiecePositioned> iterator = positionReader.iterator(safePositions); iterator.hasNext(); ) {

            PiecePositioned origen = iterator.next();

            MoveList pseudoMoves = getPseudoMoves(origen);

            moves.add(pseudoMoves);
        }

        return moves;
    }

    protected MoveContainer getLegalMovesNotKingPinned(List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals, MoveContainer moves) {
        for (AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal> pinnedPositionCardinal : pinnedPositionCardinals) {
            PiecePositioned from = pinnedPositionCardinal.getKey();
            MoveList pseudoMoves = getPseudoMoves(from);
            for (Move pseudoMove : pseudoMoves) {
                if (NoCheckLegalMoveGenerator.moveBlocksThreat(pinnedPositionCardinal.getValue(), pseudoMove.getMoveDirection())) {
                    moves.add(pseudoMove);
                }
            }
        }
        return moves;
    }


    protected MoveContainer getLegalMovesKing(long safeKingPositions, MoveContainer moves) {
        Square kingSquare = getCurrentKingSquare();

        Collection<Move> pseudoMovesKing = getPseudoMoves(kingSquare);

        for (Move pseudoMove : pseudoMovesKing) {
            Square toSquare = pseudoMove.getTo().getSquare();
            if ((toSquare.getBitPosition() & safeKingPositions) != 0) {
                moves.add(pseudoMove);
            }
        }

        return moves;
    }


    protected void getCastlingMoves(MoveContainer moves) {
        final MovePair pseudoMoves = pseudoMovesGenerator.generateCastlingPseudoMoves();
        filterMoveCollection(pseudoMoves, moves);
    }

    public static boolean moveBlocksThreat(Cardinal threatDirection, Cardinal moveDirection) {
        if (moveDirection != null) {
            if (threatDirection.equals(moveDirection) || threatDirection.equals(moveDirection.getOpposite())) {
                return true;
            }
        }
        return false;
    }


}
