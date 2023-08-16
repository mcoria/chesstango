package net.chesstango.uci.engine;

import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchInfo;
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

    protected Searching(EngineTango engineTango) {
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
        throw new RuntimeException("Unable to process newgame command. Tango is still searching.");
    }

    @Override
    public void do_go(CmdGo cmdGo) {
        throw new RuntimeException("Unable to process go command. Tango is still searching.");
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
        throw new RuntimeException("Unable to process position command. Tango is still searching.");
    }


    @Override
    public void searchInfo(SearchInfo info) {
        StringBuilder sb = new StringBuilder();
        for (Move move : info.pv()) {
            sb.append(String.format("%s ", UCIEncoder.encode(move)));
        }

        String infoStr = String.format("depth %d seldepth %d pv %s", info.depth(), info.selDepth(), sb);

        engineTango.reply(new RspInfo(infoStr));
    }


    @Override
    public void searchFinished(SearchMoveResult searchResult) {
        String selectedMoveStr = UCIEncoder.encode(searchResult.getBestMove());

        synchronized (engineTango.engineExecutor) {
            engineTango.reply(new RspBestMove(selectedMoveStr));

            engineTango.currentState = engineTango.readyState;
        }
    }
}
