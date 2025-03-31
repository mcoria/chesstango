package net.chesstango.board.position.imp;

import net.chesstango.board.Square;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.position.MoveCacheBoard;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 * TODO: LOS TESTS DE MOVIMIENTOS DEBERIAN PROBAR LOS AFFECTS
 */
public class MoveCacheBoardImp implements MoveCacheBoard {
    protected final MoveGeneratorByPieceResult[] pseudoMoves = new MoveGeneratorByPieceResult[64];
    protected final Deque<List<MoveGeneratorByPieceResult>> removedPseudoMoves = new ArrayDeque<>();

    /**
     * Aquellas posiciones pseudoMoves[] != null
     */
    protected long pseudoMovesPositions = 0;

    /**
     * Affected position squares by move (do or undo)
     */
    protected long affectedPositionsByMove = 0;

    /**
     * Possible affected pseudo move positions by move (do or undo)
     */
    protected long possibleAffectedPseudoMovesPositions = 0;

    @Override
    public MoveGeneratorByPieceResult getPseudoMovesResult(Square key) {
        return pseudoMoves[key.toIdx()];
    }

    @Override
    public void setPseudoMoves(Square key, MoveGeneratorByPieceResult generatorResult) {
        if (pseudoMoves[key.toIdx()] != null) {
            throw new RuntimeException("pseudoMoves[key.toIdx()]");
        }

        pseudoMoves[key.toIdx()] = generatorResult;

        pseudoMovesPositions |= key.getBitPosition();
    }

    @Override
    public void affectedPositionsByMove(Square key) {
        affectedPositionsByMove = key.getBitPosition();
        possibleAffectedPseudoMovesPositions = possibleAffectedBySquare[key.toIdx()];
    }

    @Override
    public void affectedPositionsByMove(Square key1, Square key2) {
        affectedPositionsByMove = key1.getBitPosition() | key2.getBitPosition();
        possibleAffectedPseudoMovesPositions = possibleAffectedBySquare[key1.toIdx()] | possibleAffectedBySquare[key2.toIdx()];
    }

    @Override
    public void affectedPositionsByMove(Square key1, Square key2, Square key3) {
        affectedPositionsByMove = key1.getBitPosition() | key2.getBitPosition() | key3.getBitPosition();
        possibleAffectedPseudoMovesPositions = possibleAffectedBySquare[key1.toIdx()] | possibleAffectedBySquare[key2.toIdx()] | possibleAffectedBySquare[key3.toIdx()];
    }

    @Override
    public void affectedPositionsByMove(Square key1, Square key2, Square key3, Square key4) {
        affectedPositionsByMove = key1.getBitPosition() | key2.getBitPosition() | key3.getBitPosition() | key4.getBitPosition();
        possibleAffectedPseudoMovesPositions = possibleAffectedBySquare[key1.toIdx()] | possibleAffectedBySquare[key2.toIdx()] | possibleAffectedBySquare[key3.toIdx()] | possibleAffectedBySquare[key4.toIdx()];
    }


    @Override
    public void push() {
        syncCache(true);
    }

    @Override
    public void pop() {
        syncCache(false);

        List<MoveGeneratorByPieceResult> currentRemovedPseudoMoves = removedPseudoMoves.pop();

        currentRemovedPseudoMoves.forEach(generatorResult -> setPseudoMoves(generatorResult.getFrom().getSquare(), generatorResult));
    }

    @Override
    public long getPseudoMovesPositions() {
        return pseudoMovesPositions;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (MoveGeneratorByPieceResult result : pseudoMoves) {
            if (result != null) {
                buffer.append(result + "\n");
            }
        }

        return buffer.toString();
    }


    protected void syncCache(final boolean trackRemoved) {
        List<MoveGeneratorByPieceResult> currentRemovedPseudoMoves = null;
        if (trackRemoved) {
            currentRemovedPseudoMoves = new LinkedList<>();
        }


        long allPositions = pseudoMovesPositions & possibleAffectedPseudoMovesPositions;
        while (allPositions != 0) {
            long posicionLng = Long.lowestOneBit(allPositions);
            int i = Long.numberOfTrailingZeros(posicionLng);
            MoveGeneratorByPieceResult pseudoMove = pseudoMoves[i];
            if (pseudoMove != null) {
                Square key = pseudoMove.getFrom().getSquare();
                if ((pseudoMove.getAffectedByPositions() & affectedPositionsByMove) != 0) {
                    if (trackRemoved) {
                        currentRemovedPseudoMoves.add(pseudoMove);
                    }
                    pseudoMoves[i] = null;
                    pseudoMovesPositions &= ~key.getBitPosition();
                }
            }
            allPositions &= ~posicionLng;
        }

        if (trackRemoved) {
            removedPseudoMoves.push(currentRemovedPseudoMoves);
        }

        affectedPositionsByMove = 0;
        possibleAffectedPseudoMovesPositions = 0;
    }

    private static final long[] possibleAffectedBySquare = new long[]{
            0x81412111090707feL,
            0x2824222120f0ffdL,
            0x4048444241f1ffbL,
            0x8080888493e3ef7L,
            0x10101011927c7cefL,
            0x2020212224f8f8dfL,
            0x4041424448f0f0bfL,
            0x8182848890e0e07fL,
            0x412111090707fe07L,
            0x824222120f0ffd0fL,
            0x48444241f1ffb1fL,
            0x80888493e3ef73eL,
            0x101011927c7cef7cL,
            0x20212224f8f8dff8L,
            0x41424448f0f0bff0L,
            0x82848890e0e07fe0L,
            0x2111090707fe0707L,
            0x4222120f0ffd0f0fL,
            0x8444241f1ffb1f1fL,
            0x888493e3ef73e3eL,
            0x1011927c7cef7c7cL,
            0x212224f8f8dff8f8L,
            0x424448f0f0bff0f0L,
            0x848890e0e07fe0e0L,
            0x11090707fe070709L,
            0x22120f0ffd0f0f12L,
            0x44241f1ffb1f1f24L,
            0x88493e3ef73e3e49L,
            0x11927c7cef7c7c92L,
            0x2224f8f8dff8f824L,
            0x4448f0f0bff0f048L,
            0x8890e0e07fe0e090L,
            0x90707fe07070911L,
            0x120f0ffd0f0f1222L,
            0x241f1ffb1f1f2444L,
            0x493e3ef73e3e4988L,
            0x927c7cef7c7c9211L,
            0x24f8f8dff8f82422L,
            0x48f0f0bff0f04844L,
            0x90e0e07fe0e09088L,
            0x707fe0707091121L,
            0xf0ffd0f0f122242L,
            0x1f1ffb1f1f244484L,
            0x3e3ef73e3e498808L,
            0x7c7cef7c7c921110L,
            0xf8f8dff8f8242221L,
            0xf0f0bff0f0484442L,
            0xe0e07fe0e0908884L,
            0x7fe070709112141L,
            0xffd0f0f12224282L,
            0x1ffb1f1f24448404L,
            0x3ef73e3e49880808L,
            0x7cef7c7c92111010L,
            0xf8dff8f824222120L,
            0xf0bff0f048444241L,
            0xe07fe0e090888482L,
            0xfe07070911214181L,
            0xfd0f0f1222428202L,
            0xfb1f1f2444840404L,
            0xf73e3e4988080808L,
            0xef7c7c9211101010L,
            0xdff8f82422212020L,
            0xbff0f04844424140L,
            0x7fe0e09088848281L};
}
