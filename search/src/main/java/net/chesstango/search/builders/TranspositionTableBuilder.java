package net.chesstango.search.builders;

import net.chesstango.search.Acceptor;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableCounters;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatisticsComparatorCollector;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatisticsListener;
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
public class TranspositionTableBuilder {
    private final TTListener transpositionTablesListener;

    private TTable mapImp;

    private TTableDebug mapNodeDebug;

    private TTableDebug mapComparatorDebug;

    private TTableCounters tTableCounters;
    private TTableStatisticsNodeCollector mapNodeCollector;
    private TTableStatisticsComparatorCollector mapComparatorCollector;
    private TTableStatisticsListener tTableStatisticsListener;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;
    private boolean withStatistics;

    private TTable nodeMap;
    private TTable comparatorMap;

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

    public TranspositionTableBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public TranspositionTableBuilder withTranspositionTableSize(int hashSize) {
        this.hashSize = hashSize;
        return this;
    }

    public void build() {
        buildObjects();

        setupListenerMediator();

        createChains();
    }

    private void buildObjects() {
        mapImp = new TTableArrayPrimitives(hashSize);

        if (withDebugSearchTree) {
            mapNodeDebug = new TTableDebug();
            mapComparatorDebug = new TTableDebug();
        }

        if (withStatistics) {
            tTableCounters = new TTableCounters();

            mapNodeCollector = new TTableStatisticsNodeCollector(tTableCounters);

            mapComparatorCollector = new TTableStatisticsComparatorCollector(tTableCounters);

            tTableStatisticsListener = new TTableStatisticsListener(tTableCounters, mapImp);
        }
    }

    private void setupListenerMediator() {
        searchListenerMediator.add(transpositionTablesListener);

        if (mapImp instanceof Acceptor maxMapImpAcceptor) {
            searchListenerMediator.add(maxMapImpAcceptor);
        }
        if (mapNodeDebug != null) {
            searchListenerMediator.add(mapNodeDebug);
        }
        if (mapComparatorDebug != null) {
            searchListenerMediator.add(mapComparatorDebug);
        }
        if (tTableCounters != null) {
            searchListenerMediator.add(tTableCounters);
        }
        if (mapNodeCollector != null) {
            searchListenerMediator.add(mapNodeCollector);
        }
        if (mapComparatorCollector != null) {
            searchListenerMediator.add(mapComparatorCollector);
        }
        if (tTableStatisticsListener != null) {
            searchListenerMediator.add(tTableStatisticsListener);
        }
    }

    private void createChains() {
        nodeMap = linkChain(mapNodeDebug, mapNodeCollector, mapImp);
        comparatorMap = linkChain(mapComparatorDebug, mapComparatorCollector, mapImp);
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

    public void link() {
        transpositionTablesListener.setTTable(mapImp);

        searchListenerMediator.accept(new LinkTTableNodeVisitor(nodeMap));
        searchListenerMediator.accept(new LinkTTableComparatorVisitor(comparatorMap));

        // TTPVReader will not be considering for statistics purposes.
        searchListenerMediator.accept(new LinkTTableImpVisitor(mapImp));
    }
}
