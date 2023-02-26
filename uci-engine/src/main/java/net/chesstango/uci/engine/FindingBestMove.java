package net.chesstango.uci.engine;

import net.chesstango.board.moves.Move;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdIsReady;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.requests.CmdUci;
import net.chesstango.uci.protocol.responses.RspBestMove;

class FindingBestMove implements ZondaState {
    private final EngineTango engineTango;
    private UCIEncoder uciEncoder = new UCIEncoder();

    FindingBestMove(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        throw new RuntimeException("Nop, I'm not ready");
    }

    @Override
    public void do_go(CmdGo cmdGo) {
    }

    @Override
    public void do_stop() {
        engineTango.searchMove.stopSearching();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
    }

    public void findBestMove(CmdGo cmdGo) {
        // TODO: for the moment we are cheating
        Move selectedMove = null;

        if (CmdGo.GoType.INFINITE.equals(cmdGo.getGoType())) {
            selectedMove = engineTango.searchMove.searchBestMove(engineTango.game).getBestMove();
        } else if (CmdGo.GoType.DEPTH.equals(cmdGo.getGoType())) {
            selectedMove = engineTango.searchMove.searchBestMove(engineTango.game, cmdGo.getDepth() + 2).getBestMove();
        } else {
            throw new RuntimeException("go subtype not implemented yet");
        }

        engineTango.responseOutputStream.accept(new RspBestMove(uciEncoder.encode(selectedMove)));

        engineTango.currentState = new Ready(engineTango);
    }
}
