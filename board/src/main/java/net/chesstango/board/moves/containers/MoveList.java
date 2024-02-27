package net.chesstango.board.moves.containers;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveList implements MoveContainerReader {
    private boolean hasQuietMoves = true;

    private final List<Move> moveListImp;

    public MoveList() {
        this.moveListImp = new LinkedList<>();
    }

    public MoveList(int count) {
        this.moveListImp = new ArrayList<>(count);
    }

    public boolean add(Move move) {
        if (hasQuietMoves && !move.isQuiet()) {
            hasQuietMoves = false;
        }
        return moveListImp.add(move);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (Move move : this) {
            buffer.append(move.toString()).append("\n");
        }
        return buffer.toString();
    }

    @Override
    public int size() {
        return moveListImp.size();
    }

    @Override
    public boolean isEmpty() {
        return moveListImp.isEmpty();
    }

    public boolean hasQuietMoves() {
        return hasQuietMoves;
    }

    @Override
    public boolean contains(Move move) {
        return moveListImp.contains(move);
    }

    @Override
    public Iterator<Move> iterator() {
        return moveListImp.iterator();
    }
}
