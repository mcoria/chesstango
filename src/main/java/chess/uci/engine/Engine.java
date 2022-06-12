package chess.uci.engine;

import chess.uci.protocol.stream.UCIInputStream;
import chess.uci.protocol.stream.UCIOutputStream;
import chess.uci.protocol.requests.*;

/**
 * @author Mauricio Coria
 *
 */
public interface Engine {

    void mainReadRequestLoop();

    void do_uci(CmdUci cmdUci);

    void do_isReady(CmdIsReady cmdIsReady);

    void do_go(CmdGo cmdGo);

    void do_position(CmdPosition cmdPosition);

    void do_quit(CmdQuit cmdQuit);

    void do_setOption(CmdSetOption cmdSetOption);

    void do_stop(CmdStop cmdStop);

    void do_newGame(CmdUciNewGame cmdUciNewGame);

    void setInputStream(UCIInputStream input);

    void setOutputStream(UCIOutputStream output);

}
