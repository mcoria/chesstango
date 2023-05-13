package net.chesstango.engine;

import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.manager.SearchListener;
import net.chesstango.search.manager.SearchManager;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.service.ServiceElement;
import net.chesstango.uci.service.ServiceVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class Tango implements ServiceElement, SearchListener {
    protected final SearchManager searchManager;
    private List<Session> sessions = new ArrayList<>();
    private Session currentSession;
    private Consumer<String> searchCallBackFn;

    public Tango(SearchMove searchMove) {
        searchManager = new SearchManager()
                            .setSearchMove(searchMove)
                            .setSearchListener(this);
    }

    public void open() {
        searchManager.open();
    }

    public void close() {
        searchManager.close();
    }

    public void newGame() {
        searchManager.reset();
        currentSession = new Session();
        sessions.add(currentSession);
    }

    public void setPosition(String fen, List<String> moves) {
        if (currentSession == null || currentSession != null && !Objects.equals(fen, currentSession.getInitialFen())) {
            newGame();
        }
        currentSession.setPosition(fen, moves);
    }


    public void goInfinite() {
        searchManager.searchInfinite(currentSession.getGame());
    }

    public void goDepth(int depth) {
        searchManager.searchUpToDepth(currentSession.getGame(), depth);
    }

    public void goMoveTime(int timeOut) {
        searchManager.searchUpToTime(currentSession.getGame(), timeOut);
    }

    public void stopSearching() {
        searchManager.stopSearching();
    }

    public List<Session> getSessions() {
        return sessions;
    }

    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        serviceVisitor.visit(this);
        if (currentSession != null) {
            currentSession.accept(serviceVisitor);
        }
    }

    @Override
    public void searchStarted() {
    }

    @Override
    public void searchStopped() {
    }

    @Override
    public void searchFinished(SearchMoveResult result) {
        currentSession.addResult(result);

        searchCallBackFn.accept(new UCIEncoder().encode(result.getBestMove()));
    }

    public void setCallBack(Consumer<String> searchCallBackFn) {
        this.searchCallBackFn = searchCallBackFn;
    }
}
