package net.chesstango.engine;

import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.service.ServiceElement;
import net.chesstango.uci.service.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Tango implements ServiceElement {

    protected final SearchMove searchMove;

    private List<Session> sessions = new ArrayList<>();

    private Session currentSession;

    public Tango(SearchMove searchMove) {
        this.searchMove = searchMove;
    }

    public void newGame() {
        currentSession = new Session(searchMove);
        sessions.add(currentSession);
    }

    public void setPosition(String fen, List<String> moves) {
        if (currentSession == null) {
            newGame();
        }
        if (currentSession.getInitialPosition() == null) {
            currentSession.setInitialPosition(fen);
        } else if (!currentSession.getInitialPosition().equals(fen)) {
            newGame();
            currentSession.setInitialPosition(fen);
        }

        currentSession.executeMoves(moves);
    }


    public SearchMoveResult searchBestMove() {
        return currentSession.searchBestMove();
    }

    public SearchMoveResult searchBestMove(int depth) {
        return currentSession.searchBestMove(depth);
    }

    public void stopSearching() {
        searchMove.stopSearching();
    }

    public List<Session> getSessions() {
        return sessions;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        if (currentSession != null) {
            currentSession.accept(visitor);
        }
    }
}
