package net.chesstango.uci.protocol.responses;

import net.chesstango.uci.protocol.UCIGui;
import net.chesstango.uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 */
public class RspBestMove implements UCIResponse {

    private final String bestMove;
    private final String ponderMove;

    public RspBestMove(String bestMove) {
        this.bestMove = bestMove;
        this.ponderMove = null;
    }

    public RspBestMove(String bestMove, String ponderMove) {
        this.bestMove = bestMove;
        this.ponderMove = ponderMove;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.Response;
    }

    @Override
    public void execute(UCIGui executor) {
        executor.do_bestMove(this);
    }

    @Override
    public UCIResponseType getResponseType() {
        return UCIResponseType.BESTMOVE;
    }

    @Override
    public String toString() {
        return "bestmove " + bestMove + (ponderMove == null ? "" : " ponder " + ponderMove);
    }

    public String getBestMove() {
        return bestMove;
    }

    public String getPonderMove() {
        return ponderMove;
    }
}
