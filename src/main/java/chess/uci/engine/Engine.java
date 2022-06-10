package chess.uci.engine;

import chess.uci.protocol.UCIInputStream;
import chess.uci.protocol.UCIOutputStream;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdIsReady;
import chess.uci.protocol.requests.CmdUci;

import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public interface Engine {

    void main();

    void do_uci(CmdUci cmdUci);

    void do_isReady(CmdIsReady cmdIsReady);

    void do_go(CmdGo cmdGo);

    void do_position_fen(String fen, List<String> moves);

    void do_position_startpos(List<String> moves);

    void do_quit();

    void do_setOptions();

    void do_stop();

    void do_newGame();

    void setInputStream(UCIInputStream input);

    void setOutputStream(UCIOutputStream output);

}
