package net.chesstango.board.representations.pgn;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;

import java.util.List;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class PGN {
    private String event;
    private String site;
    private String date;
    private String round;
    private String white;
    private String black;
    private String fen;
    private String result;
    private List<String> moveList;

    @Override
    public String toString() {
        return new PGNStringEncoder().encode(this);
    }

    public Game toGame() {
        return new PGNGameEncoder().encode(this);
    }

    public static PGN of(Game game) {
        return new PGNGameDecoder().decode(game);
    }
}
