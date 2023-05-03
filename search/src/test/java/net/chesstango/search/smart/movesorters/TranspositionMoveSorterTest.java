package net.chesstango.search.smart.movesorters;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.smart.SearchContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class TranspositionMoveSorterTest {

    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;
    private TranspositionMoveSorter moveSorter;
    @BeforeEach
    public void setup(){
        maxMap = new HashMap<>();
        minMap = new HashMap<>();
        moveSorter = new TranspositionMoveSorter();
    }

    @Test
    public void testInitial(){
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        short bestMoveEncoded = 0;
        for (Move move: game.getPossibleMoves()) {
            if(Square.c2.equals(move.getFrom().getSquare()) && Square.c3.equals(move.getTo().getSquare())){
                bestMoveEncoded = move.binaryEncoding();
                break;
            }
        }
        maxMap.put(game.getChessPosition().getPositionHash(), createTableEntry(bestMoveEncoded));

        initMoveSorter(game);

        Move move;
        List<Move> movesSorted = moveSorter.getSortedMoves();
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());
        assertEquals(Square.c2, move.getFrom().getSquare());
        assertEquals(Square.c3, move.getTo().getSquare());
    }


    private void initMoveSorter(Game game) {
        int[] visitedNodesCounters = new int[30];
        int[] expectedNodesCounters = new int[30];
        int[] visitedNodesQuiescenceCounter = new int[30];
        Set<Move>[] distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> distinctMovesPerLevel[i] = new HashSet<>() );
        Map<Long, SearchContext.TableEntry> qMaxMap = new HashMap<>();
        Map<Long, SearchContext.TableEntry> qMinMap = new HashMap<>();

        SearchContext context = new SearchContext(1,
                visitedNodesCounters,
                expectedNodesCounters,
                visitedNodesQuiescenceCounter,
                distinctMovesPerLevel,
                maxMap,
                minMap,
                qMaxMap,
                qMinMap);

        moveSorter.init(game, context);
    }

    private SearchContext.TableEntry createTableEntry(short bestMoveEncoded) {
        SearchContext.TableEntry entry = new SearchContext.TableEntry();
        long bestMoveAndValue = encodedMoveAndValue(bestMoveEncoded, 1);
        entry.bestMoveAndValue = bestMoveAndValue;
        entry.value = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & bestMoveAndValue);
        return entry;
    }

    private long encodedMoveAndValue(short move, int value){
        long encodedMoveLng = ((long) move) << 32;

        long encodedValueLng = 0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & value;

        return encodedValueLng | encodedMoveLng;
    }
}
