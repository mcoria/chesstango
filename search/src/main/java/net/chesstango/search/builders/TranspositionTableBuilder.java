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
    private TTable qMaxMapImp;
    private TTable qMinMapImp;

    private TTableDebug maxMapDebug;
    private TTableDebug minMapDebug;
    private TTableDebug qMaxMapDebug;
    private TTableDebug qMinMapDebug;

    private TTableCounters TTableCounters;
    private TTableStatisticsCollector maxMapCollector;
    private TTableStatisticsCollector minMapCollector;
    private TTableStatisticsCollector qMaxMapCollector;
    private TTableStatisticsCollector qMinMapCollector;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;
    private boolean withStatistics;

    @Getter
    private TTable maxMap;

    @Getter
    private TTable minMap;

    @Getter
    private TTable qMaxMap;

    @Getter
    private TTable qMinMap;


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
        qMaxMapImp = new TTableArrayPrimitives();
        qMinMapImp = new TTableArrayPrimitives();

        if (withDebugSearchTree) {
            maxMapDebug = new TTableDebug(MAX_MAP);
            minMapDebug = new TTableDebug(MIN_MAP);
            qMaxMapDebug = new TTableDebug(MAX_MAP_Q);
            qMinMapDebug = new TTableDebug(MIN_MAP_Q);
        }

        if (withStatistics) {
            TTableCounters = new TTableCounters();
            maxMapCollector = new TTableStatisticsCollector(TTableCounters);
            minMapCollector = new TTableStatisticsCollector(TTableCounters);
            qMaxMapCollector = new TTableStatisticsCollector(TTableCounters);
            qMinMapCollector = new TTableStatisticsCollector(TTableCounters);
        }
    }

    private void setupListenerMediator() {
        if (maxMapDebug != null) {
            searchListenerMediator.add(maxMapDebug);
        }
        if (minMapDebug != null) {
            searchListenerMediator.add(minMapDebug);
        }
        if (qMaxMapDebug != null) {
            searchListenerMediator.add(qMaxMapDebug);
        }
        if (qMinMapDebug != null) {
            searchListenerMediator.add(qMinMapDebug);
        }

        if (TTableCounters != null) {
            searchListenerMediator.add(TTableCounters);
        }
        if (maxMapCollector != null) {
            searchListenerMediator.add(maxMapCollector);
        }
        if (minMapCollector != null) {
            searchListenerMediator.add(minMapCollector);
        }
        if (qMaxMapCollector != null) {
            searchListenerMediator.add(qMaxMapCollector);
        }
        if (qMinMapCollector != null) {
            searchListenerMediator.add(qMinMapCollector);
        }
    }

    private void createChain() {
        maxMap = linkChain(maxMapCollector, maxMapDebug, maxMapImp);
        minMap = linkChain(minMapCollector, minMapDebug, minMapImp);
        qMaxMap = linkChain(qMaxMapCollector, qMaxMapDebug, qMaxMapImp);
        qMinMap = linkChain(qMinMapCollector, qMinMapDebug, qMinMapImp);
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
