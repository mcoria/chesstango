package net.chesstango.uci.engine;

import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;

class WaitCmdGo implements ZondaState {
    private final EngineTango engineTango;

    WaitCmdGo(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_go(CmdGo cmdGo) {
        FindingBestMove findingBestMove = new FindingBestMove(engineTango);
        engineTango.currentState =  findingBestMove;
        if (engineTango.executor != null) {
            engineTango.executor.execute(() -> findingBestMove.findBestMove(cmdGo));
        } else {
            findingBestMove.findBestMove(cmdGo);
        }
    }

    @Override
    public void do_stop() {
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {

    }
}
