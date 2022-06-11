package chess.uci.engine;

import chess.uci.protocol.UCIInputStream;
import chess.uci.protocol.UCIOutputStream;
import chess.uci.protocol.requests.*;

/**
 * @author Mauricio Coria
 *
 */
public interface Engine {

    void main();

    void do_uci(CmdUci cmdUci);

    void do_isReady(CmdIsReady cmdIsReady);

    void do_go(CmdGo cmdGo);

    void do_position_fen(CmdPositionFen cmdPositionFen);

    void do_position_startpos(CmdPositionStart cmdPositionStart);

    void do_quit(CmdQuit cmdQuit);

    void do_setOption(CmdSetOption cmdSetOption);

    void do_stop(CmdStop cmdStop);

    void do_newGame(CmdUciNewGame cmdUciNewGame);

    void setInputStream(UCIInputStream input);

    void setOutputStream(UCIOutputStream output);

}
