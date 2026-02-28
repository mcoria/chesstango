package net.chesstango.reports.search.transposition;

import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatistics;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TranspositionModelTest {

    /**
     * Test class for the TranspositionModel's collectStatistics method.
     * <p>
     * Ensures that statistics related to transposition tables are accurately
     * collected and aggregated from a list of SearchResult objects.
     */

    @Test
    void testCollectStatisticsWithSingleSearchResult() {
        // Arrange
        TTableStatistics mockStats = mock(TTableStatistics.class);
        when(mockStats.reads()).thenReturn(100L);
        when(mockStats.readHits()).thenReturn(60L);
        when(mockStats.writes()).thenReturn(50L);
        when(mockStats.updates()).thenReturn(25L);
        when(mockStats.overWrites()).thenReturn(10L);


        SearchResult mockSearchResult = mock(SearchResult.class);
        when(mockSearchResult.getTTableStatistics()).thenReturn(mockStats);
        when(mockSearchResult.getId()).thenReturn("TestId1");
        when(mockSearchResult.getBestMove()).thenReturn(null);

        List<SearchResult> searchResults = Collections.singletonList(mockSearchResult);

        TranspositionModel model = new TranspositionModel();

        // Act
        model.collectStatistics("TestGroup", searchResults);

        // Assert
        assertEquals("TestGroup", model.searchGroupName);
        assertEquals(1, model.searches);
        assertEquals(100L, model.readsTotal);
        assertEquals(60L, model.readHitsTotal);
        assertEquals(60, model.readHitPercentageTotal);
        assertEquals(50L, model.writesTotal);
        assertEquals(25L, model.updatesTotal);
        assertEquals(50, model.updatesPercentageTotal);
        assertEquals(10L, model.overWritesTotal);
        assertEquals(20, model.overWritesPercentageTotal);

        TranspositionModel.TranspositionModelDetail detail = model.transpositionModelDetail.getFirst();
        assertEquals("TestId1", detail.id);
        assertEquals(100L, detail.reads);
        assertEquals(60L, detail.readHits);
        assertEquals(60, detail.readHitPercentage);
        assertEquals(50L, detail.writes);
        assertEquals(25L, detail.updates);
        assertEquals(50, detail.updatesPercentage);
        assertEquals(10L, detail.overWrites);
        assertEquals(20, detail.overWritePercentage);
    }

    @Test
    void testCollectStatisticsWithNoSearchResults() {
        // Arrange
        TranspositionModel model = new TranspositionModel();
        List<SearchResult> searchResults = new ArrayList<>();

        // Act
        model.collectStatistics("EmptyGroup", searchResults);

        // Assert
        assertEquals("EmptyGroup", model.searchGroupName);
        assertEquals(0, model.searches);
        assertEquals(0L, model.readsTotal);
        assertEquals(0L, model.readHitsTotal);
        assertEquals(0, model.readHitPercentageTotal);
        assertEquals(0L, model.writesTotal);
        assertEquals(0L, model.updatesTotal);
        assertEquals(0, model.updatesPercentageTotal);
        assertEquals(0L, model.overWritesTotal);
        assertEquals(0, model.overWritesPercentageTotal);
        assertEquals(0, model.transpositionModelDetail.size());
    }

    @Test
    void testCollectStatisticsWithMultipleSearchResults() {
        // Arrange
        TTableStatistics stats1 = mock(TTableStatistics.class);
        when(stats1.reads()).thenReturn(200L);
        when(stats1.readHits()).thenReturn(100L);
        when(stats1.writes()).thenReturn(60L);
        when(stats1.updates()).thenReturn(30L);
        when(stats1.overWrites()).thenReturn(15L);

        TTableStatistics stats2 = mock(TTableStatistics.class);
        when(stats2.reads()).thenReturn(300L);
        when(stats2.readHits()).thenReturn(150L);
        when(stats2.writes()).thenReturn(80L);
        when(stats2.updates()).thenReturn(40L);
        when(stats2.overWrites()).thenReturn(20L);


        SearchResult result1 = mock(SearchResult.class);
        when(result1.getTTableStatistics()).thenReturn(stats1);
        when(result1.getId()).thenReturn("TestId1");
        when(result1.getBestMove()).thenReturn(null);

        SearchResult result2 = mock(SearchResult.class);
        when(result2.getTTableStatistics()).thenReturn(stats2);
        when(result2.getId()).thenReturn("TestId2");
        when(result2.getBestMove()).thenReturn(null);

        List<SearchResult> searchResults = List.of(result1, result2);

        TranspositionModel model = new TranspositionModel();

        // Act
        model.collectStatistics("MultiGroup", searchResults);

        // Assert
        assertEquals("MultiGroup", model.searchGroupName);
        assertEquals(2, model.searches);
        assertEquals(500L, model.readsTotal);
        assertEquals(250L, model.readHitsTotal);
        assertEquals(50, model.readHitPercentageTotal);
        assertEquals(140L, model.writesTotal);
        assertEquals(70L, model.updatesTotal);
        assertEquals(50, model.updatesPercentageTotal);
        assertEquals(35L, model.overWritesTotal);
        assertEquals(25, model.overWritesPercentageTotal);

        TranspositionModel.TranspositionModelDetail detail1 = model.transpositionModelDetail.get(0);
        assertEquals("TestId1", detail1.id);
        assertEquals(200L, detail1.reads);
        assertEquals(100L, detail1.readHits);
        assertEquals(50, detail1.readHitPercentage);
        assertEquals(60L, detail1.writes);
        assertEquals(30L, detail1.updates);
        assertEquals(50, detail1.updatesPercentage);
        assertEquals(15L, detail1.overWrites);
        assertEquals(25, detail1.overWritePercentage);

        TranspositionModel.TranspositionModelDetail detail2 = model.transpositionModelDetail.get(1);
        assertEquals("TestId2", detail2.id);
        assertEquals(300L, detail2.reads);
        assertEquals(150L, detail2.readHits);
        assertEquals(50, detail2.readHitPercentage);
        assertEquals(80L, detail2.writes);
        assertEquals(40L, detail2.updates);
        assertEquals(50, detail2.updatesPercentage);
        assertEquals(20L, detail2.overWrites);
        assertEquals(25, detail2.overWritePercentage);
    }
}