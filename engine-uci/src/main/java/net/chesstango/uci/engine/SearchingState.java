package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchListener;
import net.chesstango.engine.Tango;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspInfo;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * This class represents a specific state in the State design pattern for the UCI engine's lifecycle.
 * In this state, the engine is actively searching for the best move. The behavior of the engine related
 * to the search process is encapsulated here. It overrides methods to handle commands such as stopping
 * the search and transition to appropriate states. Once the search finishes, it transitions back to the
 * ReadyState or other states as defined in the UciTango lifecycle.
 *
 * @author Mauricio Coria
 */
class SearchingState implements UCIEngine, SearchListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private final UciTango uciTango;
    private final Tango tango;

    @Setter
    private ReadyState readyState;

    SearchingState(UciTango uciTango, Tango tango) {
        this.uciTango = uciTango;
        this.tango = tango;
        tango.setSearchListener(this);
    }

    @Override
    public void do_uci(ReqUci cmdUci) {
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
        uciTango.reply(this, new RspReadyOk());
    }

    @Override
    public void do_newGame(ReqUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_go(ReqGo cmdGo) {
    }

    @Override
    public void do_stop(ReqStop cmdStop) {
        tango.stopSearching();
    }

    @Override
    public void do_quit(ReqQuit cmdQuit) {
        tango.stopSearching();
        uciTango.changeState(new EndState());
    }

    @Override
    public void do_position(ReqPosition cmdPosition) {
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
