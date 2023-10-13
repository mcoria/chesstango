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
    private final UciTango uciTango;

    protected Searching(UciTango uciTango) {
        this.uciTango = uciTango;
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
        uciTango.tango.stopSearching();
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
        uciTango.tango.stopSearching();
        uciTango.close();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        throw new RuntimeException("Unable to process position command. Tango is still searching.");
    }


    @Override
    public void searchInfo(SearchInfo info) {
        StringBuilder sb = new StringBuilder();
        SearchMoveResult searchMoveResult = info.searchMoveResult();
        List<Move> pv = searchMoveResult.getPrincipalVariation();
        for (Move move : pv) {
            sb.append(String.format("%s ", UCIEncoder.encode(move)));
        }

        String infoStr = String.format("depth %d seldepth %d pv %s", searchMoveResult.getDepth(), searchMoveResult.getDepth(), sb);

        uciTango.reply(new RspInfo(infoStr));
    }


    @Override
    public void searchFinished(SearchMoveResult searchResult) {
        String selectedMoveStr = UCIEncoder.encode(searchResult.getBestMove());

        synchronized (uciTango.engineExecutor) {
            uciTango.reply(new RspBestMove(selectedMoveStr));

            uciTango.currentState = uciTango.readyState;
        }
    }
}
