package net.chesstango.reports.search.nodes;

import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled
class NodesModelTest {

    /**
     * Test ensures that collectStatistics properly initializes and aggregates data
     * when given an empty list of SearchResult.
     */
    @Test
    void testCollectStatisticsWithEmptySearchResults() {
        String reportTitle = "Test Report Title";
        List<SearchResult> emptySearchResults = Collections.emptyList();

        NodesModel result = NodesModel.collectStatistics(reportTitle, emptySearchResults);

        assertEquals(reportTitle, result.searchGroupName);
        assertEquals(0, result.searches);
        assertEquals(0, result.visitedNodesTotal);
        assertEquals(0, result.expectedNodesTotal);
        assertEquals(0, result.cutoffPercentageTotal);
        assertEquals(0, result.executedMovesTotal);
        assertEquals(0, result.visitedNodesTotalAvg);
        assertEquals(0, result.visitedRNodesAvg);
        assertEquals(0, result.visitedQNodesAvg);
        assertNotNull(result.nodesModelDetails);
        assertTrue(result.nodesModelDetails.isEmpty());
    }

    /**
     * Test ensures that collectStatistics correctly calculates all fields
     * based on a single SearchResult that includes regular and quiescence node statistics.
     */
    @Test
    void testCollectStatisticsWithSingleSearchResult() {
        String reportTitle = "Single SearchResult Report";
        SearchResult mockSearchResult = mock(SearchResult.class);
        NodeStatistics mockRegularStatistics = mock(NodeStatistics.class);
        NodeStatistics mockQuiescenceStatistics = mock(NodeStatistics.class);

        when(mockSearchResult.getId()).thenReturn("search_1");
        when(mockSearchResult.getBestMove()).thenReturn(null);
        when(mockSearchResult.getExecutedMoves()).thenReturn(15);
        when(mockSearchResult.getRegularNodeStatistics()).thenReturn(mockRegularStatistics);
        when(mockSearchResult.getQuiescenceNodeStatistics()).thenReturn(mockQuiescenceStatistics);

        int[] expectedRegularCounters = {100, 200, 300, 0, 0, 0};
        int[] visitedRegularCounters = {90, 180, 270, 0, 0, 0};
        int[] expectedQuiescenceCounters = {50, 100, 150, 0, 0, 0};
        int[] visitedQuiescenceCounters = {45, 90, 135, 0, 0, 0};

        when(mockRegularStatistics.expectedNodesCounters()).thenReturn(expectedRegularCounters);
        when(mockRegularStatistics.visitedNodesCounters()).thenReturn(visitedRegularCounters);
        when(mockQuiescenceStatistics.expectedNodesCounters()).thenReturn(expectedQuiescenceCounters);
        when(mockQuiescenceStatistics.visitedNodesCounters()).thenReturn(visitedQuiescenceCounters);

        NodesModel result = NodesModel.collectStatistics(reportTitle, List.of(mockSearchResult));

        assertEquals(reportTitle, result.searchGroupName);
        assertEquals(1, result.searches);

        assertEquals(3 * 270 + 3 * 135, result.visitedNodesTotal);
        assertEquals(3 * 300 + 3 * 150, result.expectedNodesTotal);

        assertEquals((int) (100 - ((3 * 270 + 3 * 135) * 100.0 / (3 * 300 + 3 * 150))), result.cutoffPercentageTotal);

        assertEquals(15, result.executedMovesTotal);
        assertEquals(result.visitedNodesTotal, result.visitedNodesTotalAvg);
        assertEquals(270, result.visitedRNodesAvg);
        assertEquals(135, result.visitedQNodesAvg);
        assertNotNull(result.nodesModelDetails);
        assertEquals(1, result.nodesModelDetails.size());
    }

    /**
     * Test ensures that collectStatistics correctly handles multiple SearchResult
     * objects and combines their statistics accurately.
     */
    @Test
    void testCollectStatisticsWithMultipleSearchResults() {
        String reportTitle = "Multiple SearchResults Report";

        SearchResult searchResult1 = mock(SearchResult.class);
        SearchResult searchResult2 = mock(SearchResult.class);

        NodeStatistics regularStatistics1 = mock(NodeStatistics.class);
        NodeStatistics quiescenceStatistics1 = mock(NodeStatistics.class);

        NodeStatistics regularStatistics2 = mock(NodeStatistics.class);
        NodeStatistics quiescenceStatistics2 = mock(NodeStatistics.class);

        when(searchResult1.getId()).thenReturn("search_1");
        when(searchResult1.getExecutedMoves()).thenReturn(10);
        when(searchResult1.getRegularNodeStatistics()).thenReturn(regularStatistics1);
        when(searchResult1.getQuiescenceNodeStatistics()).thenReturn(quiescenceStatistics1);

        when(searchResult2.getId()).thenReturn("search_2");
        when(searchResult2.getExecutedMoves()).thenReturn(20);
        when(searchResult2.getRegularNodeStatistics()).thenReturn(regularStatistics2);
        when(searchResult2.getQuiescenceNodeStatistics()).thenReturn(quiescenceStatistics2);

        int[] regularStatistics1Expected = {100, 50, 0};
        int[] regularStatistics1Visited = {80, 30, 0};
        int[] quiescenceStatistics1Expected = {60, 40, 0};
        int[] quiescenceStatistics1Visited = {50, 20, 0};

        int[] regularStatistics2Expected = {200, 100, 0};
        int[] regularStatistics2Visited = {150, 80, 0};
        int[] quiescenceStatistics2Expected = {90, 70, 0};
        int[] quiescenceStatistics2Visited = {80, 60, 0};

        when(regularStatistics1.expectedNodesCounters()).thenReturn(regularStatistics1Expected);
        when(regularStatistics1.visitedNodesCounters()).thenReturn(regularStatistics1Visited);
        when(quiescenceStatistics1.expectedNodesCounters()).thenReturn(quiescenceStatistics1Expected);
        when(quiescenceStatistics1.visitedNodesCounters()).thenReturn(quiescenceStatistics1Visited);

        when(regularStatistics2.expectedNodesCounters()).thenReturn(regularStatistics2Expected);
        when(regularStatistics2.visitedNodesCounters()).thenReturn(regularStatistics2Visited);
        when(quiescenceStatistics2.expectedNodesCounters()).thenReturn(quiescenceStatistics2Expected);
        when(quiescenceStatistics2.visitedNodesCounters()).thenReturn(quiescenceStatistics2Visited);

        NodesModel result = NodesModel.collectStatistics(reportTitle, List.of(searchResult1, searchResult2));

        assertEquals(reportTitle, result.searchGroupName);
        assertEquals(2, result.searches);
        assertEquals(2 * (230), result.visitedRNodesTotal);
        assertEquals((int) (100 - ((560 * 100.0 / 730))), result.cutoffPercentageTotal);
        assertEquals(30, result.executedMovesTotal);
    }
}