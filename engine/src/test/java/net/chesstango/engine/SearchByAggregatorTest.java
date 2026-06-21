package net.chesstango.engine;

import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.syzygy.Syzygy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class SearchByAggregatorTest {
    private Config config;

    @Mock
    private TangoFactory tangoFactory;

    @Mock
    private SearchByOpenBook searchByOpenBook;

    @Mock
    private PolyglotBook polyglotBook;

    @Mock
    private SearchByTablebase searchByTablebase;

    @Mock
    private Syzygy syzygy;

    @Mock
    private SearchByTree searchByTree;


    @BeforeEach
    public void setup() {
        config = Config.create();
        when(tangoFactory.createSearchByProxy()).thenReturn(new SearchByProxy(), new SearchByProxy());
    }

    @Test
    public void testBuildNoOptions() {
        SearchByAggregator searchByAggregator = new SearchByAggregator(tangoFactory, config, searchByTree);

        assertNotNull(searchByAggregator);

        verify(tangoFactory, times(2)).createSearchByProxy();

        SearchByProxy searchByOpenBookProxy = searchByAggregator.getSearchByOpenBookProxy();
        assertNull(searchByOpenBookProxy.getImp());

        SearchByProxy searchByTablebaseProxy = searchByAggregator.getSearchByTablebaseProxy();
        assertNull(searchByTablebaseProxy.getImp());
    }

    @Test
    public void testBuildWithOpenBook() {
        config.setPolyglotFile(Path.of("test.bin"));
        when(tangoFactory.createPolyglotBook(any(Path.class))).thenReturn(polyglotBook);
        when(tangoFactory.createSearchByOpenBook(any(PolyglotBook.class))).thenReturn(searchByOpenBook);

        SearchByAggregator searchByAggregator = new SearchByAggregator(tangoFactory, config, searchByTree);

        assertNotNull(searchByAggregator);

        verify(tangoFactory, times(2)).createSearchByProxy();
        verify(tangoFactory).createPolyglotBook(eq(Path.of("test.bin")));

        SearchByProxy searchByOpenBookProxy = searchByAggregator.getSearchByOpenBookProxy();
        assertEquals(searchByOpenBook, searchByOpenBookProxy.getImp());

        SearchByProxy searchByTablebaseProxy = searchByAggregator.getSearchByTablebaseProxy();
        assertNull(searchByTablebaseProxy.getImp());
    }


    @Test
    public void testBuildWithSyzygyPath() {
        config.setSyzygyPath("/mnt/syzygy");
        when(tangoFactory.createSyzygy(any(String.class))).thenReturn(syzygy);
        when(tangoFactory.createSearchByTablebase(any(Syzygy.class))).thenReturn(searchByTablebase);

        SearchByAggregator searchByAggregator = new SearchByAggregator(tangoFactory, config, searchByTree);

        assertNotNull(searchByAggregator);

        verify(tangoFactory, times(2)).createSearchByProxy();
        verify(tangoFactory).createSyzygy(eq("/mnt/syzygy"));

        SearchByProxy searchByOpenBookProxy = searchByAggregator.getSearchByOpenBookProxy();
        assertNull(searchByOpenBookProxy.getImp());

        SearchByProxy searchByTablebaseProxy = searchByAggregator.getSearchByTablebaseProxy();
        assertEquals(searchByTablebase, searchByTablebaseProxy.getImp());
    }


    @Test
    public void testBuildWithBookAndSyzygyDirectory() {
        config.setPolyglotFile(Path.of("test.bin"));
        config.setSyzygyPath("/mnt/syzygy");

        when(tangoFactory.createPolyglotBook(any(Path.class))).thenReturn(polyglotBook);
        when(tangoFactory.createSearchByOpenBook(any(PolyglotBook.class))).thenReturn(searchByOpenBook);

        when(tangoFactory.createSyzygy(any(String.class))).thenReturn(syzygy);
        when(tangoFactory.createSearchByTablebase(any(Syzygy.class))).thenReturn(searchByTablebase);

        SearchByAggregator searchByAggregator = new SearchByAggregator(tangoFactory, config, searchByTree);

        assertNotNull(searchByAggregator);

        verify(tangoFactory, times(2)).createSearchByProxy();
        verify(tangoFactory).createPolyglotBook(eq(Path.of("test.bin")));

        verify(tangoFactory).createSyzygy(eq("/mnt/syzygy"));

        SearchByProxy searchByOpenBookProxy = searchByAggregator.getSearchByOpenBookProxy();
        assertEquals(searchByOpenBook, searchByOpenBookProxy.getImp());

        SearchByProxy searchByTablebaseProxy = searchByAggregator.getSearchByTablebaseProxy();
        assertEquals(searchByTablebase, searchByTablebaseProxy.getImp());
    }


}
