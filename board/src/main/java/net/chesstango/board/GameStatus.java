package net.chesstango.board;

import lombok.Getter;

/**
 * @author Mauricio Coria
 */
@Getter
public enum GameStatus {
    NO_CHECK(true),
    CHECK(true),
    MATE(false),
    STALEMATE(false),
    DRAW_BY_FIFTY_RULE(false),
    DRAW_BY_FOLD_REPETITION(false);

    private final boolean inProgress;

    GameStatus(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isFinalStatus() {
        return !isInProgress();
    }

    public boolean isCheck() {
        return CHECK.equals(this);
    }
}
