package net.chesstango.engine;

import net.chesstango.search.SearchMove;
import net.chesstango.uci.service.ServiceElement;
import net.chesstango.uci.service.ServiceVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class Tango implements ServiceElement {

    protected final SearchMove searchMove;

    private List<Session> sessions = new ArrayList<>();

    private Session currentSession;

    public Tango(SearchMove searchMove) {
        this.searchMove = searchMove;
    }

    public void newGame() {
        searchMove.reset();
        currentSession = new Session();
        sessions.add(currentSession);
    }

    public void setPosition(String fen, List<String> moves) {
        if (currentSession == null || currentSession!=null && !Objects.equals(fen, currentSession.getInitialFen() )) {
            newGame();
        }
        currentSession.setPosition(fen, moves);
    }


    public String goInfinite() {
        return currentSession.goInfinite(searchMove);
    }

    public String goDepth(int depth) {
        return currentSession.goDepth(searchMove, depth);
    }

    public String goMoveTime(int timeOut) {
        return currentSession.goMoveTime(searchMove, timeOut);
    }

    public void stopSearching() {
        searchMove.stopSearching();
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
}
