package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.manager.SearchManager;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.service.ServiceElement;
import net.chesstango.uci.service.ServiceVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Session implements ServiceElement {
    private final List<SearchMoveResult> searches = new ArrayList<>();

    private Game game;

    public Game getGame() {
        return game;
    }

    public void setPosition(String fen, List<String> moves) {
        UCIEncoder uciEncoder = new UCIEncoder();
        game = FENDecoder.loadGame(fen);
        if (moves != null && !moves.isEmpty()) {
            for (String moveStr : moves) {
                Move move = uciEncoder.selectMove(game.getPossibleMoves(), moveStr);
                if (move == null) {
                    throw new RuntimeException(String.format("No move found %s", moveStr));
                }
                game.executeMove(move);
            }
        }
    }

    public String getInitialFen() {
        return game == null ? null : game.getInitialFen();
    }

    /**
     * Devuelve el resultado de las busquedas efectuadas durante el juego.
     *
     * @return
     */
    public List<SearchMoveResult> getSearches() {
        return searches;
    }

    public void addResult(SearchMoveResult result) {
        searches.add(result);
    }

    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        serviceVisitor.visit(this);
    }
}
