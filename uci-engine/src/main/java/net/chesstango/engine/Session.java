package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.protocol.UCIEncoder;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private final SearchMove searchMove;
    private String initialPosition;
    private Game currentGame;

    private final List<SearchMoveResult> moveResultList = new ArrayList<>();

    public Session(SearchMove searchMove) {
        this.searchMove = searchMove;
    }

    public void setInitialPosition(String initialPosition) {
        this.initialPosition = initialPosition;
    }

    public String getInitialPosition() {
        return initialPosition;
    }

    protected void executeMoves(List<String> moves) {
        currentGame = FENDecoder.loadGame(initialPosition);
        if (moves != null && !moves.isEmpty()) {
            UCIEncoder uciEncoder = new UCIEncoder();
            for (String moveStr : moves) {
                boolean findMove = false;
                for (Move move : currentGame.getPossibleMoves()) {
                    String encodedMoveStr = uciEncoder.encode(move);
                    if (encodedMoveStr.equals(moveStr)) {
                        currentGame.executeMove(move);
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
        SearchMoveResult searchBestMove = searchMove.searchBestMove(currentGame);
        moveResultList.add(searchBestMove);
        return searchBestMove;
    }

    public SearchMoveResult searchBestMove(int depth) {
        SearchMoveResult searchBestMove = searchMove.searchBestMove(currentGame, depth);
        moveResultList.add(searchBestMove);
        return searchBestMove;
    }

    public List<SearchMoveResult> getMoveResultList() {
        return moveResultList;
    }
}
