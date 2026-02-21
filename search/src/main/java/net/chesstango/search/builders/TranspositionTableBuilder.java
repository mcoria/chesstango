package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.statistics.transposition.TTableStatisticsCollector;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TTableArray;
import net.chesstango.search.smart.features.transposition.TTableDebug;

import static net.chesstango.search.smart.features.debug.model.DebugOperationTT.TableType.*;

/**
 * @author Mauricio Corias
 */
public class TranspositionTableBuilder {
    private TTableArray maxMapImp;
    private TTableArray minMapImp;
    private TTableArray qMaxMapImp;
    private TTableArray qMinMapImp;

    private TTableDebug maxMapDebug;
    private TTableDebug minMapDebug;
    private TTableDebug qMaxMapDebug;
    private TTableDebug qMinMapDebug;


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
        maxMapImp = new TTableArray();
        minMapImp = new TTableArray();
        qMaxMapImp = new TTableArray();
        qMinMapImp = new TTableArray();

        if (withDebugSearchTree) {
            maxMapDebug = new TTableDebug(MAX_MAP);
            minMapDebug = new TTableDebug(MIN_MAP);
            qMaxMapDebug = new TTableDebug(MAX_MAP_Q);
            qMinMapDebug = new TTableDebug(MIN_MAP_Q);
        }

        if (withStatistics) {
            maxMapCollector = new TTableStatisticsCollector();
            minMapCollector = new TTableStatisticsCollector();
            qMaxMapCollector = new TTableStatisticsCollector();
            qMinMapCollector = new TTableStatisticsCollector();

        }
    }

    private void setupListenerMediator() {
        if (maxMapDebug != null) {
            searchListenerMediator.addAcceptor(maxMapDebug);
        }
        if (minMapDebug != null) {
            searchListenerMediator.addAcceptor(minMapDebug);
        }
        if (qMaxMapDebug != null) {
            searchListenerMediator.addAcceptor(qMaxMapDebug);
        }
        if (qMinMapDebug != null) {
            searchListenerMediator.addAcceptor(qMinMapDebug);
        }

        if (maxMapCollector != null) {
            searchListenerMediator.addAcceptor(maxMapCollector);
        }
        if (minMapCollector != null) {
            searchListenerMediator.addAcceptor(minMapCollector);
        }
        if (qMaxMapCollector != null) {
            searchListenerMediator.addAcceptor(qMaxMapCollector);
        }
        if (qMinMapCollector != null) {
            searchListenerMediator.addAcceptor(qMinMapCollector);
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
