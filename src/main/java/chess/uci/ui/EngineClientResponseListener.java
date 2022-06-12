package chess.uci.ui;

import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspId;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.RspUciOk;

public interface EngineClientResponseListener {
    void receive_uciOk(RspUciOk rspUciOk);

    void receive_readyOk(RspReadyOk rspReadyOk);

    void receive_bestMove(RspBestMove rspBestMove);

    void receive_id(RspId rspId);
}
