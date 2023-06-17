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
    private BitBoard bitBoard;
    protected final MoveGeneratorResult[] pseudoMoves = new MoveGeneratorResult[64];
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

        for (int i = 0; i < 64; i++) {
            MoveGeneratorResult pseudoMove = pseudoMoves[i];
            if (pseudoMove != null) {
                if ((pseudoMove.getAffectedByPositions() & affectedPositionsByMove) != 0) {
                    if (trackRemoved) {
                        removedPseudoMoves.push(pseudoMove);
                        currentRemovedPseudoMovePositions |= pseudoMove.getFrom().getSquare().getBitPosition();
                    }
                    pseudoMoves[i] = null;
                }
            }
        }

        if (trackRemoved) {
            removedPseudoMovesPositions.push(currentRemovedPseudoMovePositions);
        }

        affectedPositionsByMove = 0;
    }

    public void setBitBoard(BitBoard bitBoard) {
        this.bitBoard = bitBoard;
    }
}
