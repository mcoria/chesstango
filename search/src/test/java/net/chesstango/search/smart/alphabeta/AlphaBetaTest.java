package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.SearchResult;
import net.chesstango.search.gamegraph.GameMock;
import net.chesstango.search.gamegraph.GameMockLoader;
import net.chesstango.search.gamegraph.MockEvaluator;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.QuiescenceNull;
import net.chesstango.search.smart.alphabeta.listeners.SetGameEvaluator;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.visitors.SetGameVisitor;
import net.chesstango.search.visitors.SetDepthVisitor;
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

    private AlphaBetaFacade alphaBetaFacade;

    private SearchListenerMediator searchListenerMediator;

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
        SetGameEvaluator setGameEvaluator = new SetGameEvaluator();

        alphaBeta.setNext(alphaBetaFlowControl);
        alphaBeta.setMoveSorter(moveSorter);


        alphaBetaFlowControl.setTerminalNode(new AlphaBetaEvaluation());
        alphaBetaFlowControl.setHorizonNode(terminal);
        alphaBetaFlowControl.setTerminalNode(terminal);
        alphaBetaFlowControl.setInteriorNode(alphaBeta);

        quiescence.setGameEvaluator(evaluator);
        terminal.setEvaluator(evaluator);

        setGameEvaluator.setEvaluator(evaluator);

        this.searchListenerMediator = new SearchListenerMediator();

        this.alphaBetaFacade = new AlphaBetaFacade();
        this.alphaBetaFacade.setAlphaBetaFilter(alphaBeta);

        this.searchListenerMediator.addAllAcceptor(List.of(alphaBeta, moveSorter, alphaBetaFlowControl, setGameEvaluator, alphaBetaFacade));
        this.acceptors = List.of(alphaBeta, quiescence, moveSorter, alphaBetaFlowControl, setGameEvaluator, alphaBetaFacade);
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

        searchListenerMediator.triggerBeforeSearch();

        searchListenerMediator.accept(new SetDepthVisitor(depth));

        searchListenerMediator.triggerBeforeSearchByDepth();

        alphaBetaFacade.search();

        SearchResultByDepth searchResultByDepth = new SearchResultByDepth(depth);

        searchListenerMediator.triggerAfterSearchByDepth(searchResultByDepth);

        SearchResult searchResult = new SearchResult().addSearchResultByDepth(searchResultByDepth);

        searchListenerMediator.triggerAfterSearch(searchResult);

        return searchResult;
    }
}
