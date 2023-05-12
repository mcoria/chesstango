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
    private final List<SearchMoveResult> searches = new ArrayList<>();

    private final UCIEncoder uciEncoder = new UCIEncoder();

    private Game game;

    public void setPosition(String fen, List<String> moves) {
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

    public String goInfinite(SearchMove searchMove) {
        SearchMoveResult bestMoveFound = searchMove.searchInfinite(game);
        searches.add(bestMoveFound);
        return uciEncoder.encode(bestMoveFound.getBestMove());
    }

    public String goDepth(SearchMove searchMove, int depth) {
        SearchMoveResult bestMoveFound = searchMove.searchUpToDepth(game, depth);
        searches.add(bestMoveFound);
        return uciEncoder.encode(bestMoveFound.getBestMove());
    }

    public String goMoveTime(SearchMove searchMove, int timeOut) {
        SearchMoveResult bestMoveFound = searchMove.searchUpToTime(game, timeOut);
        searches.add(bestMoveFound);
        return uciEncoder.encode(bestMoveFound.getBestMove());
    }

    /**
     * Devuelve el resultado de las busquedas efectuadas durante el juego.
     *
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
