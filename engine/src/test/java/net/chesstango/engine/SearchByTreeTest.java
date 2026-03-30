package net.chesstango.engine;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
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


    @BeforeEach
    public void setup() {
        config = new Config();
    }

    @Test
    public void testSearchByTree_noOptions() {
        when(tangoFactory.createSearch()).thenReturn(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        verify(tangoFactory).createSearch();

        assertEquals(search, searchByTree.getSearch());
    }


    @Test
    public void testSearchByTree_withSearch() {
        config.setSearch(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        verifyNoInteractions(tangoFactory);

        assertEquals(search, searchByTree.getSearch());
    }


    @Test
    public void testSearchByTree_withEvaluator() {
        config.setEvaluator(evaluator);

        when(tangoFactory.createSearch(any(Evaluator.class))).thenReturn(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        verify(tangoFactory).createSearch(eq(evaluator));

        assertEquals(search, searchByTree.getSearch());
    }

}
