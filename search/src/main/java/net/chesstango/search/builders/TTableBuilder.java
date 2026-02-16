package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TTableArray;
import net.chesstango.search.smart.features.transposition.TTableDebug;

import static net.chesstango.search.smart.features.debug.model.DebugOperationTT.TableType.*;

/**
 * @author Mauricio Corias
 */
public class TTableBuilder {
    private TTableArray maxMapImp;
    private TTableArray minMapImp;
    private TTableArray qMaxMapImp;
    private TTableArray qMinMapImp;

    private TTableDebug maxMapDebug;
    private TTableDebug minMapDebug;
    private TTableDebug qMaxMapDebug;
    private TTableDebug qMinMapDebug;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;

    @Getter
    private TTable maxMap;

    @Getter
    private TTable minMap;

    @Getter
    private TTable qMaxMap;

    @Getter
    private TTable qMinMap;


    public TTableBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public TTableBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
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
    }

    private void createChain() {
        maxMap = linkChain(maxMapDebug, maxMapImp);
        minMap = linkChain(minMapDebug, minMapImp);
        qMaxMap = linkChain(qMaxMapDebug, qMaxMapImp);
        qMinMap = linkChain(qMinMapDebug, qMinMapImp);
    }

    private TTable linkChain(TTableDebug tableDebug, TTable tableImp) {
        TTable result = tableImp;
        if (tableDebug != null) {
            tableDebug.setTTable(result);
            result = tableDebug;
        }
        return result;
    }
}
