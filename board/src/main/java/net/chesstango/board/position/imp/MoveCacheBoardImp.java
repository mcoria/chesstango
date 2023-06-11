package net.chesstango.board.position.imp;

import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;
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

    protected MoveGeneratorResult[] pseudoMoves = new MoveGeneratorResult[64];
    protected long[] affects = new long[64];

	private final Deque<MoveGeneratorResult> clearedSquares = new ArrayDeque<>();
    private int currentClearedSquaresCounter = 0;

	private final Deque<Integer> clearedSquaresCounters = new ArrayDeque<>();
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
		clearPseudoMoves(affects[key.toIdx()] | (pseudoMoves[key.toIdx()] != null ? key.getBitPosition() : 0), trackCleared);
	}

    @Override
	public void clearPseudoMoves(Square key1, Square key2, boolean trackCleared) {
		clearPseudoMoves(affects[key1.toIdx()] | (pseudoMoves[key1.toIdx()] != null ? key1.getBitPosition() : 0)
				                  | affects[key2.toIdx()] | (pseudoMoves[key2.toIdx()] != null ? key2.getBitPosition() : 0), trackCleared);
	}

	@Override
	public void clearPseudoMoves(Square key1, Square key2, Square key3, boolean trackCleared) {
		clearPseudoMoves(affects[key1.toIdx()] | (pseudoMoves[key1.toIdx()] != null ? key1.getBitPosition() : 0)
				                 | affects[key2.toIdx()] | (pseudoMoves[key2.toIdx()] != null ? key2.getBitPosition() : 0)
				                 | affects[key3.toIdx()] | (pseudoMoves[key3.toIdx()] != null ? key3.getBitPosition() : 0), trackCleared);
	}

	@Override
	public void clearPseudoMoves(Square key1, Square key2, Square key3, Square key4, boolean trackCleared) {
		clearPseudoMoves(affects[key1.toIdx()] | (pseudoMoves[key1.toIdx()] != null ? key1.getBitPosition() : 0)
				                 | affects[key2.toIdx()] | (pseudoMoves[key2.toIdx()] != null ? key2.getBitPosition() : 0)
				                 | affects[key3.toIdx()] | (pseudoMoves[key3.toIdx()] != null ? key3.getBitPosition() : 0)
				                 | affects[key4.toIdx()] | (pseudoMoves[key4.toIdx()] != null ? key4.getBitPosition() : 0), trackCleared);
	}

    @Override
    public void clearPseudoMoves(final long clearSquares, final boolean trackCleared) {
        final long keyRemoved = ~clearSquares;

        long affectsBySquares = 0;

        long positions = clearSquares;
        while(positions != 0){
            long posicionLng = Long.lowestOneBit(positions);
            int i = Long.numberOfTrailingZeros(posicionLng);
            MoveGeneratorResult pseudoMove = pseudoMoves[i];
            if (pseudoMove != null) {
                affectsBySquares |= pseudoMove.getAffectedByPositions();
                if(trackCleared){
                    clearedSquares.push(pseudoMove);
                    currentClearedSquaresCounter++;
                }
                pseudoMoves[i] = null;
            }
            positions &= ~posicionLng;
        }

        while(affectsBySquares != 0) {
            long posicionLng = Long.lowestOneBit(affectsBySquares);
            int i = Long.numberOfTrailingZeros(posicionLng);
            affects[i] &= keyRemoved;
            affectsBySquares &= ~posicionLng;
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
        for(int i = 0; i < currentClearedSquaresCounter; i++){
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
