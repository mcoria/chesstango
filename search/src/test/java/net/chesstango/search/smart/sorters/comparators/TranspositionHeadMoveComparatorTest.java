package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TTableMap;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionBound;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class TranspositionHeadMoveComparatorTest {

    private TTable maxMap;
    private TTable minMap;
    private TranspositionHeadMoveComparator headMoveComparator;

    @BeforeEach
    public void setup() {
        maxMap = new TTableMap();
        minMap = new TTableMap();
        headMoveComparator = new TranspositionHeadMoveComparator();
        headMoveComparator.setNext(new DefaultMoveComparator());
    }

    @Test
    public void testInitial() {
        Game game = Game.from(FEN.START_POSITION);

        Move bestMove = null;
        for (Move move : game.getPossibleMoves()) {
            if (Square.c2.equals(move.getFrom().square()) && Square.c3.equals(move.getTo().square())) {
                bestMove = move;
            }
        }

        long hash = game.getPosition().getZobristHash();

        long bestMoveAndValue = AlphaBetaHelper.encode(bestMove, 1);
        short move = AlphaBetaHelper.decodeMove(bestMoveAndValue);
        int value = AlphaBetaHelper.decodeValue(bestMoveAndValue);

        TranspositionEntry entry = new TranspositionEntry()
                .setHash(hash)
                .setBound(TranspositionBound.EXACT)
                .setDraft(1)
                .setMove(move)
                .setValue(value);

        maxMap.save(entry);

        initMoveSorter(game);

        List<Move> movesSorted = getSortedMoves(game);
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        Move theMove = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, theMove.getFrom().piece());
        assertEquals(Square.c2, theMove.getFrom().square());
        assertEquals(Square.c3, theMove.getTo().square());
    }

    private List<Move> getSortedMoves(Game game) {
        List<Move> movesList = new LinkedList<>();

        MoveContainerReader<? extends Move> moves = game.getPossibleMoves();
        moves.forEach(movesList::add);

        MoveToHashMap moveToZobrist = new MoveToHashMap();
        headMoveComparator.beforeSort(0, moveToZobrist);
        movesList.sort(headMoveComparator.reversed());
        headMoveComparator.afterSort(0, moveToZobrist);

        return movesList;
    }

    private void initMoveSorter(Game game) {
        headMoveComparator.setGame(game);
        headMoveComparator.setMaxMap(maxMap);
        headMoveComparator.setMinMap(minMap);
    }

}
