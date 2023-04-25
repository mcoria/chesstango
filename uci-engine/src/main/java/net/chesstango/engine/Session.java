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
    private final List<SearchMoveResult> searches = new ArrayList<>();
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
                Move move = uciEncoder.selectMove(game.getPossibleMoves(), moveStr);
                if(move == null){
                    throw new RuntimeException(String.format("No move found %s", moveStr));
                }
                game.executeMove(move);
            }
        }
    }

    public SearchMoveResult searchBestMove() {
        SearchMoveResult searchBestMove = searchMove.searchBestMove(game);
        searches.add(searchBestMove);
        return searchBestMove;
    }

    public SearchMoveResult searchBestMove(int depth) {
        SearchMoveResult searchBestMove = searchMove.searchBestMove(game, depth);
        searches.add(searchBestMove);
        return searchBestMove;
    }

    /**
     * Revuelve el resultado de las busquedas efectuadas durante el juego.
     * @return
     */
    public List<SearchMoveResult> getSearches() {
        return searches;
    }


    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        serviceVisitor.visit(this);
    }
}
