package net.chesstango.uci.engine;

import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdIsReady;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.requests.CmdUci;

/**
 * @author Mauricio Coria
 */
interface TangoState {

    void do_uci(CmdUci cmdUci);

    void do_isReady(CmdIsReady cmdIsReady);

    void do_go(CmdGo cmdGo);

    void do_stop();

    void do_position(CmdPosition cmdPosition);
}
