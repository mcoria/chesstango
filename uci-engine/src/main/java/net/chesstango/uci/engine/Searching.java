package net.chesstango.uci.engine;

import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchListener;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspInfo;

import java.util.List;

/**
 * @author Mauricio Coria
 */
class Searching implements UCIEngine, SearchListener {
    private final EngineTango engineTango;

    Searching(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_go(CmdGo cmdGo) {
    }

    @Override
    public void do_stop(CmdStop cmdStop) {
        engineTango.tango.stopSearching();
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
        engineTango.tango.stopSearching();
        engineTango.close();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
    }

    @Override
    public void searchStarted() {
    }

    @Override
    public void searchInfo(int depth, int selDepth, List<Move> pv) {
        StringBuilder sb = new StringBuilder();
        for (Move move : pv) {
            sb.append(String.format("%s ", UCIEncoder.encode(move)));
        }

        String info = String.format("depth %d seldepth %d pv %s", depth, selDepth, sb);

        engineTango.reply(new RspInfo(info));
    }

    @Override
    public void searchStopped() {
    }

    @Override
    public void searchFinished(SearchMoveResult searchResult) {
        String selectedMoveStr = new UCIEncoder().encode(searchResult.getBestMove());

        engineTango.reply(new RspBestMove(selectedMoveStr));

        engineTango.currentState = engineTango.readyState;
    }
}
