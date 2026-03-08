package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableCounters;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatisticsCollector;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TTableArrayPrimitives;
import net.chesstango.search.smart.alphabeta.transposition.TTableDebug;

import static net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT.TableType.*;

/**
 * @author Mauricio Corias
 */
public class TranspositionTableBuilder {
    private TTable maxMapImp;
    private TTable minMapImp;

    private TTableDebug maxMapDebug;
    private TTableDebug minMapDebug;

    private TTableCounters tTableCounters;
    private TTableStatisticsCollector maxMapCollector;
    private TTableStatisticsCollector minMapCollector;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;
    private boolean withStatistics;

    @Getter
    private TTable maxMap;

    @Getter
    private TTable minMap;


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

    public void build() {
        buildObjects();
        setupListenerMediator();
        createChain();
    }

    private void buildObjects() {
        maxMapImp = new TTableArrayPrimitives();
        minMapImp = new TTableArrayPrimitives();

        if (withDebugSearchTree) {
            maxMapDebug = new TTableDebug(MAX_MAP);
            minMapDebug = new TTableDebug(MIN_MAP);
        }

        if (withStatistics) {
            tTableCounters = new TTableCounters();
            maxMapCollector = new TTableStatisticsCollector(tTableCounters);
            minMapCollector = new TTableStatisticsCollector(tTableCounters);
        }
    }

    private void setupListenerMediator() {
        if (maxMapDebug != null) {
            searchListenerMediator.add(maxMapDebug);
        }
        if (minMapDebug != null) {
            searchListenerMediator.add(minMapDebug);
        }

        if (tTableCounters != null) {
            searchListenerMediator.add(tTableCounters);
        }
        if (maxMapCollector != null) {
            searchListenerMediator.add(maxMapCollector);
        }
        if (minMapCollector != null) {
            searchListenerMediator.add(minMapCollector);
        }
    }

    private void createChain() {
        maxMap = linkChain(maxMapCollector, maxMapDebug, maxMapImp);
        minMap = linkChain(minMapCollector, minMapDebug, minMapImp);
    }

    private TTable linkChain(TTableStatisticsCollector collector, TTableDebug tableDebug, TTable tableImp) {
        TTable result = tableImp;
        if (tableDebug != null) {
            tableDebug.setTTable(result);
            result = tableDebug;
        }
        if (collector != null) {
            collector.setTTable(result);
            result = collector;
        }
        return result;
    }
}
