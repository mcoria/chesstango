package net.chesstango.uci.protocol;

import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;

/**
 * @author Mauricio Coria
 */
public interface UCIGui {
    void do_uciOk(RspUciOk rspUciOk);

    void do_id(RspId rspId);

    void do_readyOk(RspReadyOk rspReadyOk);

    void do_bestMove(RspBestMove rspBestMove);
}
