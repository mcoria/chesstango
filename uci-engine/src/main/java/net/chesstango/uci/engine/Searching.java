package net.chesstango.uci.engine;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchListener;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspInfo;

import java.util.List;

/**
 * @author Mauricio Coria
 */
class Searching implements UCIEngine, SearchListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
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
    public void searchInfo(SearchByDepthResult searchByDepthResult) {
        String pv = simpleMoveEncoder.encodeMoves(searchByDepthResult.getPrincipalVariation().stream().map(PrincipalVariation::move).toList());

        String infoStr = String.format("depth %d seldepth %d pv %s", searchByDepthResult.getDepth(), searchByDepthResult.getDepth(), pv);

        uciTango.reply(new RspInfo(infoStr));
    }


    @Override
    public void searchFinished(SearchMoveResult searchMoveResult) {
        String selectedMoveStr = simpleMoveEncoder.encode(searchMoveResult.getBestMove());

        synchronized (uciTango.engineExecutor) {
            uciTango.reply(new RspBestMove(selectedMoveStr));

            uciTango.currentState = uciTango.readyState;
        }
    }
}
