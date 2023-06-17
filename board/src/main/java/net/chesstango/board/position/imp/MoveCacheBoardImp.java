package net.chesstango.board.position.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.MoveCacheBoard;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Mauricio Coria
 * TODO: LOS TESTS DE MOVIMIENTOS DEBERIAN PROBAR LOS AFFECTS
 */
public class MoveCacheBoardImp implements MoveCacheBoard {
    protected final MoveGeneratorResult[] pseudoMoves = new MoveGeneratorResult[64];

    protected long setPseudoMoves = 0;
    protected final Deque<MoveGeneratorResult> removedPseudoMoves = new ArrayDeque<>();
    protected final Deque<Long> removedPseudoMovesPositions = new ArrayDeque<>();
    protected long affectedPositionsByMove = 0;

    @Override
    public MoveGeneratorResult getPseudoMovesResult(Square key) {
        return pseudoMoves[key.toIdx()];
    }


    @Override
    public void setPseudoMoves(Square key, MoveGeneratorResult generatorResult) {
        if (pseudoMoves[key.toIdx()] != null) {
            throw new RuntimeException("pseudoMoves[key.toIdx()]");
        }

        pseudoMoves[key.toIdx()] = generatorResult;

        setPseudoMoves |= key.getBitPosition();
    }

    @Override
    public void clearPseudoMoves(Square key) {
        affectedPositionsByMove = key.getBitPosition();
    }

    @Override
    public void clearPseudoMoves(Square key1, Square key2) {
        affectedPositionsByMove = key1.getBitPosition() | key2.getBitPosition();
    }

    @Override
    public void clearPseudoMoves(Square key1, Square key2, Square key3) {
        affectedPositionsByMove = key1.getBitPosition() | key2.getBitPosition() | key3.getBitPosition();
    }

    @Override
    public void clearPseudoMoves(Square key1, Square key2, Square key3, Square key4) {
        affectedPositionsByMove = key1.getBitPosition() | key2.getBitPosition() | key3.getBitPosition() | key4.getBitPosition();
    }


    @Override
    public void push() {
        syncCache(true);
    }

    @Override
    public void pop() {
        syncCache(false);

        long currentRemovedPseudoMovePositions = removedPseudoMovesPositions.pop();

        int counter = Long.bitCount(currentRemovedPseudoMovePositions);
        for (int i = 0; i < counter; i++) {
            MoveGeneratorResult generatorResult = removedPseudoMoves.pop();
            setPseudoMoves(generatorResult.getFrom().getSquare(), generatorResult);
        }
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


    protected void syncCache(final boolean trackRemoved) {
        long currentRemovedPseudoMovePositions = 0;

        long allPositions = setPseudoMoves;

        while(allPositions != 0) {
            long posicionLng = Long.lowestOneBit(allPositions);
            int i = Long.numberOfTrailingZeros(posicionLng);
            MoveGeneratorResult pseudoMove = pseudoMoves[i];
            if (pseudoMove != null) {
                Square key = pseudoMove.getFrom().getSquare();
                if ((pseudoMove.getAffectedByPositions() & affectedPositionsByMove) != 0) {
                    if (trackRemoved) {
                        removedPseudoMoves.push(pseudoMove);
                        currentRemovedPseudoMovePositions |= key.getBitPosition();
                    }
                    pseudoMoves[i] = null;
                    setPseudoMoves &= ~key.getBitPosition();
                }
            }
            allPositions &= ~posicionLng;
        }

        if (trackRemoved) {
            removedPseudoMovesPositions.push(currentRemovedPseudoMovePositions);
        }

        affectedPositionsByMove = 0;
    }
}
