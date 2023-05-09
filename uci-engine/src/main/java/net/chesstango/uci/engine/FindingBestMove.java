package net.chesstango.uci.engine;

import net.chesstango.board.moves.Move;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdIsReady;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.requests.CmdUci;
import net.chesstango.uci.protocol.responses.RspBestMove;

/**
 * @author Mauricio Coria
 */
class FindingBestMove implements TangoState {
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
        engineTango.tango.stopSearching();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
    }

    public void findBestMove(CmdGo cmdGo) {
        Move selectedMove = null;

        if (CmdGo.GoType.INFINITE.equals(cmdGo.getGoType())) {
            selectedMove = engineTango.tango.searchBestMove().getBestMove();
        } else if (CmdGo.GoType.DEPTH.equals(cmdGo.getGoType())) {
            selectedMove = engineTango.tango.searchBestMove(cmdGo.getDepth()).getBestMove();
        } else {
            throw new RuntimeException("go subtype not implemented yet");
        }

        engineTango.responseOutputStream.accept(new RspBestMove(uciEncoder.encode(selectedMove)));

        engineTango.currentState = new Ready(engineTango);
    }
}
