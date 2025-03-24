package net.chesstango.board.moves.generators.legal.imp.nocheck;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.imp.AbstractLegalMoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.imp.MoveCommandImp;
import net.chesstango.board.position.ChessPositionReader;

import java.util.AbstractMap;
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
    public MoveContainerReader<MoveCommandImp> getLegalMoves(AnalyzerResult analysis) {
        final Square kingSquare = getCurrentKingSquare();

        final Color currentTurn = positionReader.getCurrentTurn();

        final long currentTurnPositions = positionReader.getPositions(currentTurn);

        final long pinnedSquares = analysis.getPinnedSquares();

        final long kingPosition = kingSquare.getBitPosition();

        //final long capturedPositions = analysis.getCapturedPositions();

        final long safePositions = currentTurnPositions & ~pinnedSquares & ~kingPosition;

        MoveContainer<MoveCommandImp> moves = new MoveContainer<>(Long.bitCount(safePositions));

        getLegalMovesNotKingNotPinned(safePositions, moves);

        getLegalMovesNotKingPinned(analysis.getPinnedPositionCardinals(), moves);

        getLegalMovesKing(analysis.getSafeKingPositions(), moves);

        getEnPassantMoves(moves);

        getCastlingMoves(moves);

        return moves;
    }


    protected void getLegalMovesNotKingNotPinned(long safePositions, MoveContainer<MoveCommandImp> moves) {

        for (Iterator<PiecePositioned> iterator = positionReader.iterator(safePositions); iterator.hasNext(); ) {

            PiecePositioned origen = iterator.next();

            MoveList<MoveCommandImp> pseudoMoves = getPseudoMoves(origen);

            moves.add(pseudoMoves);
        }

    }

    protected void getLegalMovesNotKingPinned(List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals, MoveContainer<MoveCommandImp> moves) {
        for (AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal> pinnedPositionCardinal : pinnedPositionCardinals) {
            PiecePositioned from = pinnedPositionCardinal.getKey();
            MoveList<MoveCommandImp> pseudoMoves = getPseudoMoves(from);
            for (MoveCommandImp pseudoMove : pseudoMoves) {
                if (NoCheckLegalMoveGenerator.moveBlocksThreat(pinnedPositionCardinal.getValue(), pseudoMove.getMoveDirection())) {
                    moves.add(pseudoMove);
                }
            }
        }
    }


    protected void getLegalMovesKing(long safeKingPositions, MoveContainer<MoveCommandImp> moves) {
        Square kingSquare = getCurrentKingSquare();

        MoveList<MoveCommandImp> pseudoMovesKing = getPseudoMoves(kingSquare);

        for (MoveCommandImp pseudoMove : pseudoMovesKing) {
            Square toSquare = pseudoMove.getTo().getSquare();
            if ((toSquare.getBitPosition() & safeKingPositions) != 0) {
                moves.add(pseudoMove);
            }
        }

    }


    protected void getCastlingMoves(MoveContainer<MoveCommandImp> moves) {
        final MovePair pseudoMoves = pseudoMovesGenerator.generateCastlingPseudoMoves();
        filterMovePair(pseudoMoves, moves);
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
