package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.service.ServiceElement;
import net.chesstango.uci.service.ServiceVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Session implements ServiceElement {
    private final SearchMove searchMove;
    private final List<SearchMoveResult> moveResultList = new ArrayList<>();
    private String initialFENPosition;
    private Game game;

    public Session(SearchMove searchMove) {
        this.searchMove = searchMove;
    }

    public void setInitialFENPosition(String initialFENPosition) {
        this.initialFENPosition = initialFENPosition;
    }

    public String getInitialFENPosition() {
        return initialFENPosition;
    }

    protected void executeMoves(List<String> moves) {
        game = FENDecoder.loadGame(initialFENPosition);
        if (moves != null && !moves.isEmpty()) {
            UCIEncoder uciEncoder = new UCIEncoder();
            for (String moveStr : moves) {
                boolean findMove = false;
                for (Move move : game.getPossibleMoves()) {
                    String encodedMoveStr = uciEncoder.encode(move);
                    if (encodedMoveStr.equals(moveStr)) {
                        game.executeMove(move);
                        findMove = true;
                        break;
                    }
                }
                if (!findMove) {
                    throw new RuntimeException("No move found " + moveStr);
                }
            }
        }
    }

    public SearchMoveResult searchBestMove() {
        SearchMoveResult searchBestMove = searchMove.searchBestMove(game);
        moveResultList.add(searchBestMove);
        return searchBestMove;
    }

    public SearchMoveResult searchBestMove(int depth) {
        SearchMoveResult searchBestMove = searchMove.searchBestMove(game, depth);
        moveResultList.add(searchBestMove);
        return searchBestMove;
    }

    public List<SearchMoveResult> getMoveResultList() {
        return moveResultList;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        serviceVisitor.visit(this);
    }
}
