package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.*;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.builders.alphabeta.MoveSorterBuilder;
import net.chesstango.search.smart.features.killermoves.KillerMovesTable;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.transposition.TTableMap;
import net.chesstango.search.smart.features.transposition.TTable;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractNodeSorterTest {
    protected final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
    protected SearchByCycleContext cycleContext;
    protected SearchByDepthContext depthContext;

    protected SearchListenerMediator searchListenerMediator;
    protected MoveSorterBuilder moveSorterBuilder;
    protected TTable maxMap;
    protected TTable minMap;
    protected TTable qMaxMap;
    protected TTable qMinMap;
    protected KillerMovesTable killerMovesTable;
    protected Game game;

    @BeforeEach
    public void setup() {
        game = createGame();

        searchListenerMediator = new SearchListenerMediator();

        cycleContext = new SearchByCycleContext(game);

        maxMap = new TTableMap();
        minMap = new TTableMap();
        qMaxMap = new TTableMap();
        qMinMap = new TTableMap();
        cycleContext.setMaxMap(maxMap);
        cycleContext.setMinMap(minMap);
        cycleContext.setQMaxMap(qMaxMap);
        cycleContext.setQMinMap(qMinMap);

        killerMovesTable = new KillerMovesTable();
        cycleContext.setKillerMoves(killerMovesTable);

        depthContext = new SearchByDepthContext(getMaxSearchPly());

        moveSorterBuilder = new MoveSorterBuilder()
                .withSmartListenerMediator(searchListenerMediator);
    }

    protected abstract Game createGame();

    protected abstract int getMaxSearchPly();

    protected List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(simpleMoveEncoder.encode(move));
        }
        return sortedMovesStr;
    }


    protected Move createMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal, boolean quiet) {
        return new Move() {
            @Override
            public PiecePositioned getFrom() {
                return from;
            }

            @Override
            public PiecePositioned getTo() {
                return to;
            }

            @Override
            public void executeMove() {
            }

            @Override
            public void undoMove() {
            }

            @Override
            public Cardinal getMoveDirection() {
                return cardinal;
            }

            @Override
            public boolean isQuiet() {
                return quiet;
            }

            @Override
            public long getZobristHash(ChessPosition chessPosition) {
                return 0;
            }

            @Override
            public void doMove(BitBoardWriter bitBoard) {

            }

            @Override
            public void undoMove(BitBoardWriter bitBoard) {

            }

            @Override
            public void doMove(ChessPosition chessPosition) {

            }

            @Override
            public void undoMove(ChessPosition chessPosition) {

            }

            @Override
            public void doMove(MoveCacheBoardWriter moveCache) {

            }

            @Override
            public void undoMove(MoveCacheBoardWriter moveCache) {

            }

            @Override
            public void doMove(PositionStateWriter positionState) {

            }

            @Override
            public void undoMove(PositionStateWriter positionStateWriter) {

            }

            @Override
            public void doMove(SquareBoardWriter squareBoard) {

            }

            @Override
            public void undoMove(SquareBoardWriter squareBoard) {

            }

            @Override
            public void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader) {

            }

            @Override
            public void undoMove(ZobristHashWriter hash) {

            }
        };
    }
}
