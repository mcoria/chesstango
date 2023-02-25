package net.chesstango.uci.engine;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;

class Ready implements ZondaState {

    private final EngineTango engineTango;

    Ready(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_go(CmdGo cmdGo) {
    }

    @Override
    public void do_stop() {
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        engineTango.game = CmdPosition.CmdType.STARTPOS == cmdPosition.getType() ? FENDecoder.loadGame(FENDecoder.INITIAL_FEN) : FENDecoder.loadGame(cmdPosition.getFen());
        engineTango.executeMoves(cmdPosition.getMoves());
        engineTango.currentState = new WaitCmdGo(engineTango);
    }
}
