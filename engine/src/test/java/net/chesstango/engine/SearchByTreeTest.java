package net.chesstango.engine;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.Search;
import net.chesstango.search.SearchBuilder;
import net.chesstango.search.smart.alphabeta.egtb.visitors.LinkEndGameTableBaseVisitor;
import net.chesstango.search.smart.alphabeta.transposition.visitors.SetTTableHashSizeVisitor;
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
    private SearchBuilder searchBuilder;

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
        when(tangoFactory.createSearchBuilder()).thenReturn(searchBuilder);
        when(searchBuilder.build()).thenReturn(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        verify(tangoFactory).createSearchBuilder();
        verify(searchBuilder).build();

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

        when(tangoFactory.createSearchBuilder()).thenReturn(searchBuilder);
        when(searchBuilder.withGameEvaluator(any(Evaluator.class))).thenReturn(searchBuilder);
        when(searchBuilder.build()).thenReturn(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        verify(tangoFactory).createSearchBuilder();
        verify(searchBuilder).withGameEvaluator(evaluator);
        verify(searchBuilder).build();

        assertEquals(search, searchByTree.getSearch());
    }

    @Test
    public void test_setSyzygy() {
        when(tangoFactory.createSearchBuilder()).thenReturn(searchBuilder);
        when(searchBuilder.build()).thenReturn(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        searchByTree.setSyzygy(syzygy);

        verify(tangoFactory).createSearchBuilder();
        verify(searchBuilder).build();
        verify(tangoFactory).createSyzygyTableBaseAdapter(eq(syzygy));
        verify(search).accept(any(LinkEndGameTableBaseVisitor.class));

        assertEquals(search, searchByTree.getSearch());
    }

    @Test
    public void test_setHashSize() {
        when(tangoFactory.createSearchBuilder()).thenReturn(searchBuilder);
        when(searchBuilder.build()).thenReturn(search);

        SearchByTree searchByTree = new SearchByTree(tangoFactory, config);

        searchByTree.setHashSize(64);

        verify(tangoFactory).createSearchBuilder();
        verify(searchBuilder).build();
        verify(search).accept(any(SetTTableHashSizeVisitor.class));

        assertEquals(search, searchByTree.getSearch());
    }

}
