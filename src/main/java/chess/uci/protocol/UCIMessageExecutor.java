package chess.uci.protocol;

import chess.uci.protocol.requests.*;
import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspId;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.RspUciOk;

public interface UCIMessageExecutor {
    void do_uci(CmdUci cmdUci);

    void do_setOption(CmdSetOption cmdSetOption);

    void do_isReady(CmdIsReady cmdIsReady);

    void do_newGame(CmdUciNewGame cmdUciNewGame);

    void do_position(CmdPosition cmdPosition);

    void do_go(CmdGo cmdGo);

    void do_stop(CmdStop cmdStop);

    void do_quit(CmdQuit cmdQuit);


    void receive_uciOk(RspUciOk rspUciOk);

    void receive_id(RspId rspId);

    void receive_readyOk(RspReadyOk rspReadyOk);

    void receive_bestMove(RspBestMove rspBestMove);
}
