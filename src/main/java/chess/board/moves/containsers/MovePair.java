package chess.board.moves.containsers;

import chess.board.moves.Move;

public class MovePair {
    private Move first;
    private Move second;

    public Move getFirst() {
        return first;
    }

    public void setFirst(Move first) {
        this.first = first;
    }

    public Move getSecond() {
        return second;
    }

    public void setSecond(Move second) {
        this.second = second;
    }

    public int size() {
        int result = 0;
        if(first!=null){
            result++;
        }
        if(second!=null){
            result++;
        }
        return result;
    }

    public boolean contains(Move move) {
        return move.equals(first) || move.equals(second);
    }
}
