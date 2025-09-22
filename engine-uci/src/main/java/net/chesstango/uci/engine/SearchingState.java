package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchListener;
import net.chesstango.engine.Tango;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.*;
import net.chesstango.goyeneche.responses.UCIResponse;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

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

    @Setter
    private ReadyState readyState;

    SearchingState(UciTango uciTango) {
        this.uciTango = uciTango;
    }

    @Override
    public void do_uci(ReqUci cmdUci) {
        throw new RuntimeException("Unable to process position command. Tango is still searching.");
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
        throw new RuntimeException("Unable to process position command. Tango is still searching.");
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
        uciTango.reply(this, UCIResponse.readyok());
    }

    @Override
    public void do_stop(ReqStop cmdStop) {
        uciTango.session.stopSearching();
    }

    @Override
    public void do_quit(ReqQuit cmdQuit) {
        uciTango.session.stopSearching();
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

        uciTango.reply(this, UCIResponse.info(infoStr));
    }


    @Override
    public void searchFinished(SearchResult searchResult) {
        String selectedMoveStr = simpleMoveEncoder.encode(searchResult.getBestMove());

        uciTango.reply(readyState, UCIResponse.bestMove(selectedMoveStr));
    }
}
