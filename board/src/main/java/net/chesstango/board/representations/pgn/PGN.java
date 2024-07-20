package net.chesstango.board.representations.pgn;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SANDecoder;

import java.util.ArrayList;
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

    /**
     * Cada entrada EPD representa la posicion y el movimiento ejecutado
     *
     * @return
     */
    public Stream<EPD> toEPD() {
        SANDecoder sanDecoder = new SANDecoder();

        Stream.Builder<EPD> fenStreamBuilder = Stream.builder();

        Game game = FENDecoder.loadGame(getFen() == null ? FENDecoder.INITIAL_FEN : getFen());
        game.threefoldRepetitionRule(false);
        game.fiftyMovesRule(false);

        List<EPD> epdList = new ArrayList<>(getMoveList().size());
        int lastClock = 0;

        for (String moveStr : getMoveList()) {
            MoveContainerReader legalMoves = game.getPossibleMoves();
            Move legalMoveToExecute = sanDecoder.decode(moveStr, legalMoves);

            if (legalMoveToExecute != null) {
                EPD epd = new EPD();

                epd.setFenWithoutClocks(game.getCurrentFEN());

                epd.setId(String.format("%s", Long.toHexString(game.getChessPosition().getZobristHash())));

                if (event != null) {
                    epd.setC0(String.format("event='%s'", event));
                }
                if (site != null) {
                    epd.setC1(String.format("site='%s'", site));
                }
                if (date != null) {
                    epd.setC2(String.format("date='%s'", date));
                }
                if (white != null) {
                    epd.setC3(String.format("white='%s'", white));
                }
                if (black != null) {
                    epd.setC4(String.format("black='%s'", black));
                }
                if (result != null) {
                    epd.setC5(String.format("result='%s'", result));
                }

                lastClock = game.getChessPosition().getFullMoveClock();

                epd.setC6(String.format("clock=%d", lastClock));

                epd.setSuppliedMoveStr(moveStr);
                epd.setSuppliedMove(legalMoveToExecute);

                epdList.add(epd);

                game.executeMove(legalMoveToExecute);
            } else {
                throw new RuntimeException(String.format("[%s] %s is not in the list of legal moves for %s", getEvent(), moveStr, game.getCurrentFEN().toString()));
            }
        }

        for (EPD epd : epdList) {
            epd.setC7(String.format("totalClock=%d", lastClock));
            fenStreamBuilder.add(epd);
        }

        return fenStreamBuilder.build();
    }
}
