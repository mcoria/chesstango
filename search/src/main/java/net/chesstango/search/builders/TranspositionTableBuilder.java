package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.smart.pv.model.PVWalkerFromTT;
import net.chesstango.search.smart.statistics.transposition.*;
import net.chesstango.search.smart.transposition.*;
import net.chesstango.search.smart.transposition.listeners.TTListener;
import net.chesstango.search.smart.transposition.visitors.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Corias
 */
public class TranspositionTableBuilder implements SearchObjectBuilder<TranspositionTableBuilder> {
    private TTListener ttListener;

    /**
     * Implementation TTable filters
     */
    @Getter
    private TTableArrayPrimitives tTableImp;

    /**
     * Front-end TTable filters
     */
    private TTable tTableNode;
    private TTable tTableHeadComparator;
    private TTable tTableTailComparator;
    private TTable tTablePV;

    /**
     * Debug operations filters
     */
    private TTableNodeDebug tTableNodeDebug;
    private TTableComparatorHeadDebug tTableComparatorHeadDebug;
    private TTableComparatorTailDebug tTableComparatorTailDebug;
    private TTablePVDebug tTablePVDebug;

    /**
     * Statistics operations filters
     */
    private TTableStatisticsNodeCollector tTableNodeCollector;
    private TTableStatisticsComparatorCollector tTableComparatorCollector;
    private TTableStatisticsPVCollector tTablePVCollector;

    /**
     * Statistics model
     */
    private TTableCounters tTableCounters;
    private TTableStatisticsFillPercentageCollector tTableStatisticsFillPercentageCollector;

    private PVWalkerFromTT pvWalkerFromTT;

    private ListenerMediator listenerMediator;

    private boolean withDebugSearchTree;
    private boolean withStatistics;

    private int hashSizeKB;
    private int staleAge;

    public TranspositionTableBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public TranspositionTableBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public TranspositionTableBuilder withHashSize(int hashSizeKB) {
        this.hashSizeKB = hashSizeKB;
        return this;
    }

    public TranspositionTableBuilder withStaleAge(int staleAge) {
        this.staleAge = staleAge;
        return this;
    }

    @Override
    public TranspositionTableBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        this.listenerMediator = listenerMediator;
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
        ttListener.setTTable(tTableImp);

        listenerMediator.accept(new LinkTTableNodeVisitor(tTableNode));

        listenerMediator.accept(new LinkTTableHeadComparatorVisitor(tTableHeadComparator));

        listenerMediator.accept(new LinkTTableTailComparatorVisitor(tTableTailComparator));

        listenerMediator.accept(new LinkTTableImpVisitor(tTableImp));

        listenerMediator.accept(new LinkPVWalkerFromTTVisitor(pvWalkerFromTT));
    }

    private void buildObjects() {
        tTableImp = new TTableArrayPrimitives(staleAge, hashSizeKB);
        ttListener = new TTListener();
        pvWalkerFromTT = new PVWalkerFromTT();

        if (withDebugSearchTree) {
            tTableNodeDebug = new TTableNodeDebug();
            tTableComparatorHeadDebug = new TTableComparatorHeadDebug();
            tTableComparatorTailDebug = new TTableComparatorTailDebug();
            tTablePVDebug = new TTablePVDebug();
        }

        if (withStatistics) {
            tTableCounters = new TTableCounters();
            tTableNodeCollector = new TTableStatisticsNodeCollector(tTableCounters);
            tTableComparatorCollector = new TTableStatisticsComparatorCollector(tTableCounters);
            tTablePVCollector = new TTableStatisticsPVCollector(tTableCounters);
            tTableStatisticsFillPercentageCollector = new TTableStatisticsFillPercentageCollector(tTableCounters, tTableImp);
        }
    }

    private void setupListenerMediator() {
        listenerMediator.add(ttListener);

        listenerMediator.add(tTableImp);

        if (tTableNodeDebug != null) {
            listenerMediator.add(tTableNodeDebug);
        }
        if (tTableComparatorHeadDebug != null) {
            listenerMediator.add(tTableComparatorHeadDebug);
        }
        if(tTableComparatorTailDebug!=null){
            listenerMediator.add(tTableComparatorTailDebug);
        }
        if(tTablePVDebug!=null){
            listenerMediator.add(tTablePVDebug);
        }
        if (tTableCounters != null) {
            listenerMediator.add(tTableCounters);
        }
        if (tTableNodeCollector != null) {
            listenerMediator.add(tTableNodeCollector);
        }
        if (tTableComparatorCollector != null) {
            listenerMediator.add(tTableComparatorCollector);
        }
        if (tTablePVCollector != null) {
            listenerMediator.add(tTablePVCollector);
        }
        if (tTableStatisticsFillPercentageCollector != null) {
            listenerMediator.add(tTableStatisticsFillPercentageCollector);
        }
        if (pvWalkerFromTT != null) {
            listenerMediator.add(pvWalkerFromTT);
        }
    }

    private void createChains() {
        tTableNode = linkChain(tTableNodeDebug, tTableNodeCollector, tTableImp);
        tTableHeadComparator = linkChain(tTableComparatorHeadDebug, tTableComparatorCollector, tTableImp);
        tTableTailComparator = linkChain(tTableComparatorTailDebug, tTableComparatorCollector, tTableImp);
        tTablePV = linkChain(tTablePVDebug, tTablePVCollector, tTableImp);

        if (pvWalkerFromTT != null) {
            pvWalkerFromTT.setTTable(tTablePV);
        }
    }

    private TTable linkChain(TTable... tTables) {
        List<TTable> chain = Arrays
                .stream(tTables)
                .filter(Objects::nonNull)
                .toList();

        for (int i = 0; i < chain.size() - 1; i++) {
            TTable currentFilter = chain.get(i);
            TTable next = chain.get(i + 1);

            switch (currentFilter) {
                case TTableNodeDebug tableDebug -> tableDebug.setTTable(next);

                case TTableComparatorHeadDebug tableDebug -> tableDebug.setTTable(next);

                case TTableComparatorTailDebug tableDebug -> tableDebug.setTTable(next);

                case TTablePVDebug tableDebug -> tableDebug.setTTable(next);

                case TTableStatisticsNodeCollector tableStatisticsCollector -> tableStatisticsCollector.setTTable(next);

                case TTableStatisticsComparatorCollector tableStatisticsComparatorCollector ->
                        tableStatisticsComparatorCollector.setTTable(next);

                case TTableStatisticsPVCollector tableStatisticsPVCollector ->
                        tableStatisticsPVCollector.setTTable(next);

                case null -> throw new RuntimeException(String.format("filter %d is null", i));

                default -> throw new RuntimeException("filter not found: " + currentFilter.getClass().getSimpleName());
            }
        }
        return chain.getFirst();
    }
}
