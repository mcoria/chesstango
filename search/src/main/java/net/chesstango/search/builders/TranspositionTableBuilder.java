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

import static net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT.TableType.MAX_MAP;
import static net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT.TableType.MIN_MAP;

/**
 * @author Mauricio Corias
 */
public class TranspositionTableBuilder {
    private TTable maxMapImp;
    private TTable minMapImp;

    private final TTListener transpositionTablesListener;

    private TTableDebug maxMapNodeDebug;
    private TTableDebug minMapNodeDebug;

    private TTableDebug maxMapComparatorDebug;
    private TTableDebug minMapComparatorDebug;

    private TTableCounters tTableCounters;
    private TTableStatisticsNodeCollector maxMapNodeCollector;
    private TTableStatisticsNodeCollector minMapNodeCollector;
    private TTableStatisticsComparatorCollector maxMapComparatorCollector;
    private TTableStatisticsComparatorCollector minMapComparatorCollector;
    private TTableStatisticsListener tTableStatisticsListener;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;
    private boolean withStatistics;

    private TTable maxNodeMap;
    private TTable minNodeMap;

    private TTable maxComparatorMap;
    private TTable minComparatorMap;

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
        maxMapImp = new TTableArrayPrimitives(hashSize);

        minMapImp = new TTableArrayPrimitives(hashSize);

        if (withDebugSearchTree) {
            maxMapNodeDebug = new TTableDebug(MAX_MAP);
            minMapNodeDebug = new TTableDebug(MIN_MAP);

            maxMapComparatorDebug = new TTableDebug(MAX_MAP);
            minMapComparatorDebug = new TTableDebug(MIN_MAP);
        }
        if (withStatistics) {
            tTableCounters = new TTableCounters();
            maxMapNodeCollector = new TTableStatisticsNodeCollector(tTableCounters);
            minMapNodeCollector = new TTableStatisticsNodeCollector(tTableCounters);

            maxMapComparatorCollector = new TTableStatisticsComparatorCollector(tTableCounters);
            minMapComparatorCollector = new TTableStatisticsComparatorCollector(tTableCounters);

            tTableStatisticsListener = new TTableStatisticsListener(tTableCounters, maxMapImp, minMapImp);
        }
    }

    private void setupListenerMediator() {
        searchListenerMediator.add(transpositionTablesListener);

        if (maxMapImp instanceof Acceptor maxMapImpAcceptor) {
            searchListenerMediator.add(maxMapImpAcceptor);
        }
        if (minMapImp instanceof Acceptor minMapImpAcceptor) {
            searchListenerMediator.add(minMapImpAcceptor);
        }
        if (maxMapNodeDebug != null) {
            searchListenerMediator.add(maxMapNodeDebug);
        }
        if (minMapNodeDebug != null) {
            searchListenerMediator.add(minMapNodeDebug);
        }
        if (maxMapComparatorDebug != null) {
            searchListenerMediator.add(maxMapComparatorDebug);
        }
        if (minMapComparatorDebug != null) {
            searchListenerMediator.add(minMapComparatorDebug);
        }
        if (tTableCounters != null) {
            searchListenerMediator.add(tTableCounters);
        }
        if (maxMapNodeCollector != null) {
            searchListenerMediator.add(maxMapNodeCollector);
        }
        if (minMapNodeCollector != null) {
            searchListenerMediator.add(minMapNodeCollector);
        }
        if (maxMapComparatorCollector != null) {
            searchListenerMediator.add(maxMapComparatorCollector);
        }
        if (minMapComparatorCollector != null) {
            searchListenerMediator.add(minMapComparatorCollector);
        }
        if (tTableStatisticsListener != null) {
            searchListenerMediator.add(tTableStatisticsListener);
        }
    }

    private void createChains() {
        maxNodeMap = linkChain(maxMapNodeDebug, maxMapNodeCollector, maxMapImp);
        minNodeMap = linkChain(minMapNodeDebug, minMapNodeCollector, minMapImp);

        maxComparatorMap = linkChain(maxMapComparatorDebug, maxMapComparatorCollector, maxMapImp);
        minComparatorMap = linkChain(minMapComparatorDebug, minMapComparatorCollector, minMapImp);
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
        transpositionTablesListener.setMaxMap(maxMapImp);
        transpositionTablesListener.setMinMap(minMapImp);

        searchListenerMediator.accept(new LinkTTableNodeVisitor(maxNodeMap, minNodeMap));
        searchListenerMediator.accept(new LinkTTableComparatorVisitor(maxComparatorMap, minComparatorMap));

        // TTPVReader will not be considering for statistics purposes.
        searchListenerMediator.accept(new LinkTTableImpVisitor(maxMapImp, minMapImp));
    }
}
