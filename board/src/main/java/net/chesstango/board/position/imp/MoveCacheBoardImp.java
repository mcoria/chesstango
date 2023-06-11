package net.chesstango.board.position.imp;

import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.MoveCacheBoard;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Mauricio Coria
 * TODO: LOS TESTS DE MOVIMIENTOS DEBERIAN PROBAR LOS AFFECTS
 */
public class MoveCacheBoardImp implements MoveCacheBoard {
    protected final MoveGeneratorResult[] pseudoMoves = new MoveGeneratorResult[64];
    protected final long[] affects = new long[64];
    protected final Deque<MoveGeneratorResult> clearedSquares = new ArrayDeque<>();
    protected final Deque<Integer> clearedSquaresCounters = new ArrayDeque<>();
    protected int currentClearedSquaresCounter = 0;

    @Override
    public MoveGeneratorResult getPseudoMovesResult(Square key) {
        return pseudoMoves[key.toIdx()];
    }

    @Override
    public void setPseudoMoves(Square key, MoveGeneratorResult generatorResult) {
        pseudoMoves[key.toIdx()] = generatorResult;

        final long keyAdded = key.getBitPosition();

        long affectedByCollection = generatorResult.getAffectedByPositions();
        while (affectedByCollection != 0) {
            long posicionLng = Long.lowestOneBit(affectedByCollection);
            int idx = Long.numberOfTrailingZeros(posicionLng);
            affects[idx] |= keyAdded;
            affectedByCollection &= ~posicionLng;
        }
    }

    @Override
    public void clearPseudoMoves(Square key, boolean trackCleared) {
        clearPseudoMoves(affects[key.toIdx()] | key.getBitPosition(), trackCleared);
    }

    @Override
    public void clearPseudoMoves(Square key1, Square key2, boolean trackCleared) {
        clearPseudoMoves(affects[key1.toIdx()] | key1.getBitPosition()
                | affects[key2.toIdx()] | key2.getBitPosition(), trackCleared);
    }

    @Override
    public void clearPseudoMoves(Square key1, Square key2, Square key3, boolean trackCleared) {
        clearPseudoMoves(affects[key1.toIdx()] | key1.getBitPosition()
                | affects[key2.toIdx()] | key2.getBitPosition()
                | affects[key3.toIdx()] | key3.getBitPosition(), trackCleared);
    }

    @Override
    public void clearPseudoMoves(Square key1, Square key2, Square key3, Square key4, boolean trackCleared) {
        clearPseudoMoves(affects[key1.toIdx()] | key1.getBitPosition()
                | affects[key2.toIdx()] | key2.getBitPosition()
                | affects[key3.toIdx()] | key3.getBitPosition()
                | affects[key4.toIdx()] | key4.getBitPosition(), trackCleared);
    }

    @Override
    public void clearPseudoMoves(final long clearSquares, final boolean trackCleared) {
        long affectedByClearedSquares = 0;

        long positions = clearSquares;
        while (positions != 0) {
            long posicionLng = Long.lowestOneBit(positions);
            int i = Long.numberOfTrailingZeros(posicionLng);
            MoveGeneratorResult pseudoMove = pseudoMoves[i];
            if (pseudoMove != null) {
                affectedByClearedSquares |= pseudoMove.getAffectedByPositions();
                if (trackCleared) {
                    clearedSquares.push(pseudoMove);
                    currentClearedSquaresCounter++;
                }
                pseudoMoves[i] = null;
            }
            positions &= ~posicionLng;
        }

        while (affectedByClearedSquares != 0) {
            long posicionLng = Long.lowestOneBit(affectedByClearedSquares);
            int i = Long.numberOfTrailingZeros(posicionLng);
            affects[i] &= ~clearSquares;
            affectedByClearedSquares &= ~posicionLng;
        }
    }


    @Override
    public void pushCleared() {
        clearedSquaresCounters.push(currentClearedSquaresCounter);
        currentClearedSquaresCounter = 0;
    }

    //TODO: este metodo consume el 20% del procesamiento
    @Override
    public void popCleared() {
        for (int i = 0; i < currentClearedSquaresCounter; i++) {
            MoveGeneratorResult generatorResult = clearedSquares.pop();
            setPseudoMoves(generatorResult.getFrom().getSquare(), generatorResult);
        }
        currentClearedSquaresCounter = clearedSquaresCounters.pop();
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (MoveGeneratorResult result : pseudoMoves) {
            if (result != null) {
                buffer.append(result + "\n");
            }
        }

        return buffer.toString();
    }

}
