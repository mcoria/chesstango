package net.chesstango.uci.engine.states;

import lombok.Setter;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchListener;
import net.chesstango.engine.Tango;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspInfo;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
public class SearchingState implements UCIEngine, SearchListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private final UciTango uciTango;
    private final Tango tango;

    @Setter
    private ReadyState readyState;

    public SearchingState(UciTango uciTango, Tango tango) {
        this.uciTango = uciTango;
        this.tango = tango;
        tango.setSearchListener(this);
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        uciTango.reply(this, new RspReadyOk());
    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_go(CmdGo cmdGo) {
    }

    @Override
    public void do_stop(CmdStop cmdStop) {
        tango.stopSearching();
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
        tango.stopSearching();
        uciTango.changeState(new EndState());
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        throw new RuntimeException("Unable to process position command. Tango is still searching.");
    }


    @Override
    public void searchInfo(SearchResultByDepth searchResultByDepth) {
        String pv = simpleMoveEncoder.encodeMoves(searchResultByDepth.getPrincipalVariation().stream().map(PrincipalVariation::move).toList());

        String infoStr = String.format("depth %d seldepth %d pv %s", searchResultByDepth.getDepth(), searchResultByDepth.getDepth(), pv);

        uciTango.reply(this, new RspInfo(infoStr));
    }


    @Override
    public void searchFinished(SearchResult searchResult) {
        String selectedMoveStr = simpleMoveEncoder.encode(searchResult.getBestMove());

        uciTango.reply(readyState, new RspBestMove(selectedMoveStr));
    }
}
