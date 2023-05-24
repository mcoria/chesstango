package net.chesstango.uci.protocol;

import net.chesstango.uci.protocol.responses.*;

/**
 * @author Mauricio Coria
 */
public interface UCIGui {
    void do_uciOk(RspUciOk rspUciOk);

    void do_id(RspId rspId);

    void do_readyOk(RspReadyOk rspReadyOk);

    void do_bestMove(RspBestMove rspBestMove);

    void do_info(RspInfo rspInfo);
}
