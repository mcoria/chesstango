package net.chesstango.uci.engine.states;

import lombok.Setter;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchListener;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspInfo;

/**
 * @author Mauricio Coria
 */
public class SearchingState implements UCIEngine, SearchListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private final UciTango uciTango;

    @Setter
    private ReadyState readyState;

    public SearchingState(UciTango uciTango) {
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
        uciTango.getTango().stopSearching();
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
        uciTango.getTango().stopSearching();
        uciTango.close();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        throw new RuntimeException("Unable to process position command. Tango is still searching.");
    }


    @Override
    public void searchInfo(SearchResultByDepth searchResultByDepth) {
        String pv = simpleMoveEncoder.encodeMoves(searchResultByDepth.getPrincipalVariation().stream().map(PrincipalVariation::move).toList());

        String infoStr = String.format("depth %d seldepth %d pv %s", searchResultByDepth.getDepth(), searchResultByDepth.getDepth(), pv);

        uciTango.reply(new RspInfo(infoStr));
    }


    @Override
    public void searchFinished(SearchResult searchResult) {
        String selectedMoveStr = simpleMoveEncoder.encode(searchResult.getBestMove());

        synchronized (uciTango.getEngineExecutor()) {
            uciTango.reply(new RspBestMove(selectedMoveStr));

            uciTango.setCurrentState(readyState);
        }
    }
}
