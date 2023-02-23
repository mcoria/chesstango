package net.chesstango.uci.protocol;

import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIGui {
    void receive_uciOk(RspUciOk rspUciOk);

    void receive_id(RspId rspId);

    void receive_readyOk(RspReadyOk rspReadyOk);

    void receive_bestMove(RspBestMove rspBestMove);
}
