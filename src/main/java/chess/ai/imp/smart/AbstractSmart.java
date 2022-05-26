package chess.ai.imp.smart;

import chess.ai.BestMoveFinder;
import chess.board.PiecePositioned;
import chess.board.moves.Move;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractSmart implements BestMoveFinder {

    protected Move selectMove(List<Move> moves) {
        Map<PiecePositioned, List<Move>> moveMap = new HashMap<PiecePositioned, List<Move>>();

        moves.forEach(move ->
                 moveMap.computeIfAbsent(move.getFrom(), k -> new ArrayList<Move>())
                        .add(move)
        );

        PiecePositioned[] pieces = moveMap.keySet().toArray(new PiecePositioned[moveMap.keySet().size()]);
        PiecePositioned selectedPiece = pieces[ThreadLocalRandom.current().nextInt(0, pieces.length)];

        List<Move> selectedMovesCollection = moveMap.get(selectedPiece);

        return selectedMovesCollection.get(ThreadLocalRandom.current().nextInt(0, selectedMovesCollection.size()));
    }
}
