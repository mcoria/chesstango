package net.chesstango.search.smart;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.SearchResult;
import net.chesstango.search.gamegraph.GameMock;
import net.chesstango.search.gamegraph.GameMockLoader;
import net.chesstango.search.gamegraph.MockEvaluator;
import net.chesstango.search.smart.core.filters.AlphaBeta;
import net.chesstango.search.smart.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.quiescence.QuiescenceNull;
import net.chesstango.search.smart.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.sorters.NodeMoveSorter;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.visitors.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
@Disabled
public class AlphaBetaTest {

    private MockEvaluator evaluator;

    private SearchByDepthImp searchByDepthImp;

    private ListenerMediator listenerMediator;

    private List<Acceptor> acceptors;

    @BeforeEach
    public void setup() {
        NodeMoveSorter moveSorter = new NodeMoveSorter();
        moveSorter.setMoveComparator(new DefaultMoveComparator());

        evaluator = new MockEvaluator();

        AlphaBeta alphaBeta = new AlphaBeta();
        AlphaBetaEvaluation terminal = new AlphaBetaEvaluation();
        AlphaBetaFlowControl alphaBetaFlowControl = new AlphaBetaFlowControl();
        QuiescenceNull quiescence = new QuiescenceNull();
        SetGameToEvaluator setGameToEvaluator = new SetGameToEvaluator();

        alphaBeta.setNext(alphaBetaFlowControl);
        alphaBeta.setMoveSorter(moveSorter);


        alphaBetaFlowControl.setTerminalNode(new AlphaBetaEvaluation());
        alphaBetaFlowControl.setQuiescenceNode(terminal);
        alphaBetaFlowControl.setTerminalNode(terminal);
        alphaBetaFlowControl.setInteriorNode(alphaBeta);

        quiescence.setGameEvaluator(evaluator);
        terminal.setEvaluator(evaluator);

        setGameToEvaluator.setEvaluator(evaluator);

        this.listenerMediator = new ListenerMediator();

        this.searchByDepthImp = new SearchByDepthImp();
        this.searchByDepthImp.setNext(alphaBeta);

        this.listenerMediator.addAll(List.of(alphaBeta, moveSorter, alphaBetaFlowControl, setGameToEvaluator, searchByDepthImp));
        this.acceptors = List.of(alphaBeta, quiescence, moveSorter, alphaBetaFlowControl, setGameToEvaluator, searchByDepthImp);
    }

    @Test
    public void whiteTurn1Ply() {
        GameMock game = GameMockLoader.loadFromFile("WhiteTurn1Ply.json");

        SearchResult searchResult = search(game, 1);

        Move bestMove = searchResult.getBestMove();

        assertNotNull(searchResult);
        assertEquals(Square.b1, bestMove.getFrom().square());
        assertEquals(Square.c3, bestMove.getTo().square());
        assertEquals(1, searchResult.getBestEvaluation());
        assertEquals(3, evaluator.getNodesEvaluated());
        assertEquals(3, game.getNodesVisited());
    }

    @Test
    public void blackTurn1Ply() {
        GameMock game = GameMockLoader.loadFromFile("BlackTurn1Ply.json");

        SearchResult searchResult = search(game, 1);

        Move bestMove = searchResult.getBestMove();

        assertNotNull(searchResult);
        assertEquals(Square.b8, bestMove.getFrom().square());
        assertEquals(Square.c6, bestMove.getTo().square());
        assertEquals(-1, searchResult.getBestEvaluation());
        assertEquals(3, evaluator.getNodesEvaluated());
        assertEquals(3, game.getNodesVisited());
    }

    @Test
    public void whiteTurn2Ply() {
        GameMock game = GameMockLoader.loadFromFile("WhiteTurn2Ply.json");

        SearchResult searchResult = search(game, 2);

        Move bestMove = searchResult.getBestMove();

        assertNotNull(searchResult);
        assertEquals(Square.d2, bestMove.getFrom().square());
        assertEquals(Square.d4, bestMove.getTo().square());
        assertEquals(5, searchResult.getBestEvaluation());
        assertEquals(6, evaluator.getNodesEvaluated());
        assertEquals(8, game.getNodesVisited());
    }

    @Test
    public void blackTurn2Ply() {
        GameMock game = GameMockLoader.loadFromFile("BlackTurn2Ply.json");

        SearchResult searchResult = search(game, 2);

        Move bestMove = searchResult.getBestMove();

        assertNotNull(searchResult);
        assertEquals(Square.d7, bestMove.getFrom().square());
        assertEquals(Square.d5, bestMove.getTo().square());
        assertEquals(14, searchResult.getBestEvaluation());
        assertEquals(9, evaluator.getNodesEvaluated());
        assertEquals(12, game.getNodesVisited());
    }

    private SearchResult search(GameMock game, int depth) {
        SetGameVisitor setGameVisitor = new SetGameVisitor(game);
        acceptors.forEach(acceptor -> acceptor.accept(setGameVisitor));

        listenerMediator.triggerBeforeSearch();

        SearchResultByDepth searchResultByDepth =  searchByDepthImp.search(depth);

        listenerMediator.accept(new CollectSearchResultByDepthVisitor(searchResultByDepth));

        listenerMediator.accept(new DistributeSearchResultByDepthVisitor(searchResultByDepth));

        listenerMediator.triggerAfterSearch();

        SearchResult searchResult = new SearchResult().addSearchResultByDepth(searchResultByDepth);

        listenerMediator.accept(new CollectSearchResultVisitor(searchResult));

        listenerMediator.accept(new DistributeSearchResultVisitor(searchResult));

        return searchResult;
    }
}
