package net.chesstango.uci.engine;

import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;

interface ZondaState {

    void do_go(CmdGo cmdGo);

    void do_stop();

    void do_position(CmdPosition cmdPosition);
}
