package net.chesstango.board.representations.pgn;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SANDecoder;

import java.util.List;
import java.util.stream.Stream;

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

    public static PGN of(Game game) {
        return new PGNGameDecoder().decode(game);
    }

    @Override
    public String toString() {
        return new PGNStringEncoder().encode(this);
    }

    public Game toGame() {
        return new PGNGameEncoder().encode(this);
    }

    public Stream<EPD> stream() {
        SANDecoder sanDecoder = new SANDecoder();

        Stream.Builder<EPD> fenStreamBuilder = Stream.builder();

        Game game = FENDecoder.loadGame(getFen() == null ? FENDecoder.INITIAL_FEN : getFen());

        getMoveList().forEach(moveStr -> {
            MoveContainerReader legalMoves = game.getPossibleMoves();
            Move legalMoveToExecute = sanDecoder.decode(moveStr, legalMoves);

            if (legalMoveToExecute != null) {
                EPD epd = new EPD();
                epd.setFen(game.getCurrentFEN());
                epd.setSuppliedMoveStr(moveStr);
                epd.setSuppliedMove(legalMoveToExecute);
                fenStreamBuilder.add(epd);

                game.executeMove(legalMoveToExecute);
            } else {
                throw new RuntimeException(String.format("[%s] %s is not in the list of legal moves for %s", getEvent(), moveStr, game.getCurrentFEN().toString()));
            }
        });

        return fenStreamBuilder.build();
    }
}
