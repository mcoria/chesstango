package net.chesstango.board.internal.moves.generators.legal.nocheck;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.internal.moves.generators.legal.AbstractLegalMoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.position.PositionReader;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;

//TODO: deberiamos contabilizar aquellas piezas que se exploraron en busca de movimientos validos y no producieron resultados validos.
//      de esta forma cuando se busca en getLegalMovesNotKing() no volver a filtrar los mismos movimientos

/**
 * @author Mauricio Coria
 */
public class NoCheckLegalMoveGenerator extends AbstractLegalMoveGenerator {

    public NoCheckLegalMoveGenerator(PositionReader positionReader,
                                     MoveGenerator strategy,
                                     LegalMoveFilter filter) {
        super(positionReader, strategy, filter);
    }

    @Override
    public MoveContainerReader<Move> getLegalMoves(AnalyzerResult analysis) {
        final Square kingSquare = getCurrentKingSquare();

        final Color currentTurn = positionReader.getCurrentTurn();

        final long currentTurnPositions = positionReader.getPositions(currentTurn);

        final long pinnedSquares = analysis.getPinnedSquares();

        final long kingPosition = kingSquare.bitPosition();

        final long safePositions = currentTurnPositions & ~pinnedSquares & ~kingPosition;

        MoveContainer<Move> moves = new MoveContainer<>(Long.bitCount(safePositions));

        collectLegalMovesNotKingNotPinned(safePositions, moves);

        collectLegalMovesNotKingPinned(analysis.getPinnedPositionCardinals(), moves);

        collectLegalMovesKing(analysis.getSafeKingPositions(), moves);

        collectEnPassantMoves(moves);

        collectCastlingMoves(moves);

        return moves;
    }


    protected void collectLegalMovesNotKingNotPinned(long safePositions, MoveContainer<Move> moves) {
        for (Iterator<PiecePositioned> iterator = positionReader.iterator(safePositions); iterator.hasNext(); ) {

            PiecePositioned origen = iterator.next();

            MoveList<PseudoMove> pseudoMoves = getPseudoMoves(origen);

            moves.add(pseudoMoves);
        }
    }

    protected void collectLegalMovesNotKingPinned(List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals, MoveContainer<Move> moves) {
        for (AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal> pinnedPositionCardinal : pinnedPositionCardinals) {
            PiecePositioned from = pinnedPositionCardinal.getKey();
            MoveList<PseudoMove> pseudoMoves = getPseudoMoves(from);
            for (PseudoMove pseudoMove : pseudoMoves) {
                if (NoCheckLegalMoveGenerator.moveBlocksThreat(pinnedPositionCardinal.getValue(), pseudoMove.getMoveDirection())) {
                    moves.add(pseudoMove);
                }
            }
        }
    }


    protected void collectLegalMovesKing(long safeKingPositions, MoveContainer<Move> moves) {
        Square kingSquare = getCurrentKingSquare();

        MoveList<PseudoMove> pseudoMovesKing = getPseudoMoves(kingSquare);

        for (PseudoMove pseudoMove : pseudoMovesKing) {
            Square toSquare = pseudoMove.getTo().getSquare();
            if ((toSquare.bitPosition() & safeKingPositions) != 0) {
                moves.add(pseudoMove);
            }
        }
    }


    protected void collectCastlingMoves(MoveContainer<Move> moves) {
        final MovePair<PseudoMove> pseudoMoves = pseudoMovesGenerator.generateCastlingPseudoMoves();
        filterMovePair(pseudoMoves, moves);
    }

    public static boolean moveBlocksThreat(Cardinal threatDirection, Cardinal moveDirection) {
        if (moveDirection != null) {
            return threatDirection.equals(moveDirection) || threatDirection.equals(moveDirection.getOpposite());
        }
        return false;
    }


}
