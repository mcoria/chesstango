package net.chesstango.uci.arena.matchtypes;

import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.go.CmdGoFast;
import net.chesstango.uci.protocol.responses.RspBestMove;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Mauricio Coria
 */
public class MatchByClock implements MatchType {
    private final int inc;
    private int wTime;
    private int bTime;

    public MatchByClock(int time, int inc) {
        this.wTime = time;
        this.bTime = time;
        this.inc = inc;
    }


    @Override
    public RspBestMove retrieveBestMoveFromController(EngineController currentTurn, boolean isWhite) {
        CmdGo cmdGo = new CmdGoFast()
                .setWTime(wTime)
                .setBTime(bTime)
                .setWInc(inc)
                .setBInc(inc);

        Instant start = Instant.now();

        RspBestMove bestMove = currentTurn.send_CmdGo(cmdGo);

        long timeElapsed = Duration.between(start, Instant.now()).toMillis();

        if (isWhite) {
            wTime -= (int) timeElapsed;
            wTime += inc;
            if (wTime < 0) {
                throw new RuntimeException("White time out");
            }
        } else {
            bTime -= (int) timeElapsed;
            bTime += inc;
            if (bTime < 0) {
                throw new RuntimeException("Black time out");
            }
        }

        return bestMove;
    }
}
