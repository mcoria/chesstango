package net.chesstango.search.builders;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableCounters;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatisticsComparatorCollector;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatisticsFillPercentageCollector;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatisticsNodeCollector;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TTableArrayPrimitives;
import net.chesstango.search.smart.alphabeta.transposition.TTableDebug;
import net.chesstango.search.smart.alphabeta.transposition.listeners.TTListener;
import net.chesstango.search.smart.alphabeta.transposition.visitors.LinkTTableComparatorVisitor;
import net.chesstango.search.smart.alphabeta.transposition.visitors.LinkTTableImpVisitor;
import net.chesstango.search.smart.alphabeta.transposition.visitors.LinkTTableNodeVisitor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Corias
 */
public class TranspositionTableBuilder implements SearchObjectBuilder<TranspositionTableBuilder> {
    private final TTListener transpositionTablesListener;

    /**
     * Implementation TTable filters
     */
    private TTableArrayPrimitives tTableImp;

    /**
     * Front-end TTable filters
     */
    private TTable tTableNode;
    private TTable tTableComparator;

    /**
     * Debug operations filters
     */
    private TTableDebug tTableNodeDebug;
    private TTableDebug tTableComparatorDebug;

    /**
     * Statistics operations filters
     */
    private TTableStatisticsNodeCollector tTableNodeCollector;
    private TTableStatisticsComparatorCollector tTableComparatorCollector;

    /**
     * Statistics model
     */
    private TTableCounters tTableCounters;
    private TTableStatisticsFillPercentageCollector tTableStatisticsFillPercentageCollector;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;
    private boolean withStatistics;

    private int hashSize;

    public TranspositionTableBuilder() {
        transpositionTablesListener = new TTListener();
    }

    public TranspositionTableBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public TranspositionTableBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    @Override
    public TranspositionTableBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public TranspositionTableBuilder withTranspositionTableSize(int hashSize) {
        this.hashSize = hashSize;
        return this;
    }

    @Override
    public void build() {
        buildObjects();

        setupListenerMediator();

        createChains();
    }

    @Override
    public void link() {
        transpositionTablesListener.setTTable(tTableImp);

        searchListenerMediator.accept(new LinkTTableNodeVisitor(tTableNode));

        searchListenerMediator.accept(new LinkTTableComparatorVisitor(tTableComparator));

        // TTPVReader will not be considering for statistics purposes.
        searchListenerMediator.accept(new LinkTTableImpVisitor(tTableImp));
    }

    private void buildObjects() {
        tTableImp = new TTableArrayPrimitives(hashSize);

        if (withDebugSearchTree) {
            tTableNodeDebug = new TTableDebug();
            tTableComparatorDebug = new TTableDebug();
        }

        if (withStatistics) {
            tTableCounters = new TTableCounters();
            tTableNodeCollector = new TTableStatisticsNodeCollector(tTableCounters);
            tTableComparatorCollector = new TTableStatisticsComparatorCollector(tTableCounters);
            tTableStatisticsFillPercentageCollector = new TTableStatisticsFillPercentageCollector(tTableCounters, tTableImp);
        }
    }

    private void setupListenerMediator() {
        searchListenerMediator.add(transpositionTablesListener);

        searchListenerMediator.add(tTableImp);

        if (tTableNodeDebug != null) {
            searchListenerMediator.add(tTableNodeDebug);
        }
        if (tTableComparatorDebug != null) {
            searchListenerMediator.add(tTableComparatorDebug);
        }
        if (tTableCounters != null) {
            searchListenerMediator.add(tTableCounters);
        }
        if (tTableNodeCollector != null) {
            searchListenerMediator.add(tTableNodeCollector);
        }
        if (tTableComparatorCollector != null) {
            searchListenerMediator.add(tTableComparatorCollector);
        }
        if (tTableStatisticsFillPercentageCollector != null) {
            searchListenerMediator.add(tTableStatisticsFillPercentageCollector);
        }
    }

    private void createChains() {
        tTableNode = linkChain(tTableNodeDebug, tTableNodeCollector, tTableImp);
        tTableComparator = linkChain(tTableComparatorDebug, tTableComparatorCollector, tTableImp);
    }

    private TTable linkChain(TTable... tTables) {
        List<TTable> chain = Arrays.stream(tTables).filter(Objects::nonNull).toList();

        for (int i = 0; i < chain.size() - 1; i++) {
            TTable currentFilter = chain.get(i);
            TTable next = chain.get(i + 1);

            switch (currentFilter) {
                case TTableDebug tableDebug -> tableDebug.setTTable(next);

                case TTableStatisticsNodeCollector tableStatisticsCollector -> tableStatisticsCollector.setTTable(next);

                case TTableStatisticsComparatorCollector tableStatisticsComparatorCollector ->
                        tableStatisticsComparatorCollector.setTTable(next);

                case null -> throw new RuntimeException(String.format("filter %d is null", i));

                default -> throw new RuntimeException("filter not found: " + currentFilter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }
}
