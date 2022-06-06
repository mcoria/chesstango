package chess.ai.imp.smart;

import chess.ai.BestMoveFinder;
import chess.board.PiecePositioned;
import chess.board.moves.Move;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractSmart implements BestMoveFinder {

    protected int evaluation;

    protected boolean keepProcessing = true;

    @Override
    public int getEvaluation() {
        return this.evaluation;
    }

    @Override
    public void stopProcessing() {
        keepProcessing = false;
    }
}
