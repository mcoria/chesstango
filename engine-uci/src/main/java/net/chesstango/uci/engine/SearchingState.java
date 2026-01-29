package net.chesstango.uci.engine;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchListener;
import net.chesstango.engine.SearchResponse;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.*;
import net.chesstango.goyeneche.responses.UCIResponse;

/**
 * This class represents a specific state in the State design pattern for the UCI engine's lifecycle.
 * In this state, the engine is actively searching for the best move. The behavior of the engine related
 * to the search process is encapsulated here. It overrides methods to handle commands such as stopping
 * the search and transition to appropriate states. Once the search finishes, it transitions back to the
 * ReadyState or other states as defined in the UciTango lifecycle.
 *
 * @author Mauricio Coria
 */
@Slf4j
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
        log.warn("Unable to process uci command. Tango is still searching.");
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
        log.warn("Unable to process option command. Tango is still searching.");
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
        uciTango.reply(this, UCIResponse.readyok());
    }

    @Override
    public void do_stop(ReqStop cmdStop) {
        uciTango.stopSearching();
    }

    @Override
    public void do_quit(ReqQuit cmdQuit) {
        uciTango.stopSearching();
        uciTango.changeState(new EndState());
    }

    @Override
    public void do_position(ReqPosition cmdPosition) {
        log.warn("Unable to process position command. Tango is still searching.");
    }

    @Override
    public void searchStarted() {
        log.debug("Search started");
    }

    @Override
    public void searchInfo(String searchInfo) {
        uciTango.reply(this, UCIResponse.info(searchInfo));
    }


    @Override
    public void searchFinished(SearchResponse searchResult) {
        String selectedMoveStr = simpleMoveEncoder.encode(searchResult.move());

        uciTango.reply(readyState, UCIResponse.bestMove(selectedMoveStr));
    }
}
