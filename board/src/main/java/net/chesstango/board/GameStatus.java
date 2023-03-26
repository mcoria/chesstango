package net.chesstango.board;

/**
 * @author Mauricio Coria
 */
public enum GameStatus {
    NO_CHECK(true),
    CHECK(true),
    MATE(false),
    DRAW(false),
    DRAW_BY_FIFTY_RULE(false),
    DRAW_BY_FOLD_REPETITION(false);

    private final boolean inProgress;

    GameStatus(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public boolean isFinalStatus() {
        return !isInProgress();
    }
}
