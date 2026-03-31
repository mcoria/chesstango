package net.chesstango.engine;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.Search;
import net.chesstango.search.smart.alphabeta.egtb.visitors.SetEndGameTableBaseVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class SearchByTreeTest {
    private Config config;

    @Mock
    private TangoFactory tangoFactory;

    @Mock
    private Search search;

    @Mock
    private Evaluator evaluator;

    @Mock
    private Syzygy syzygy;


    @BeforeEach
    public void setup() {
        config = new Config();
    }

    @Test
    public void test_noOptions() {
        when(tangoFactory.createSearch()).thenReturn(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        verify(tangoFactory).createSearch();

        assertEquals(search, searchByTree.getSearch());
    }


    @Test
    public void test_withSearch() {
        config.setSearch(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        verifyNoInteractions(tangoFactory);

        assertEquals(search, searchByTree.getSearch());
    }


    @Test
    public void test_withEvaluator() {
        config.setEvaluator(evaluator);

        when(tangoFactory.createSearch(any(Evaluator.class))).thenReturn(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        verify(tangoFactory).createSearch(eq(evaluator));

        assertEquals(search, searchByTree.getSearch());
    }

    @Test
    public void test_setSyzygy() {
        when(tangoFactory.createSearch()).thenReturn(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        searchByTree.setSyzygy(syzygy);

        verify(tangoFactory).createSearch();
        verify(tangoFactory).createSyzygyTableBaseAdapter(eq(syzygy));
        verify(search).accept(any(SetEndGameTableBaseVisitor.class));

        assertEquals(search, searchByTree.getSearch());
    }

}
