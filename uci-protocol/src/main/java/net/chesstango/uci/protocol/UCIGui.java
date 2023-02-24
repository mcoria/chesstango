package net.chesstango.uci.protocol;

import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;

/**
 * @author Mauricio Coria
 */
public interface UCIGui {
    void received_uciOk(RspUciOk rspUciOk);

    void received_id(RspId rspId);

    void received_readyOk(RspReadyOk rspReadyOk);

    void received_bestMove(RspBestMove rspBestMove);
}
