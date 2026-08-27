package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.search.SearchListenerMediator;
import net.chesstango.search.smart.killermoves.KillerMoves;
import net.chesstango.search.smart.killermoves.KillerMovesDebug;
import net.chesstango.search.smart.killermoves.KillerMovesTable;
import net.chesstango.search.smart.killermoves.visitors.LinkKillerMovesVisitor;

/**
 * @author Mauricio Corias
 */
public class KillerMoveBuilder implements SearchObjectBuilder<KillerMoveBuilder> {

    /**
     * Back-end killer moves
     */
    @Getter
    private final KillerMovesTable killerMovesTableImp;

    private KillerMovesDebug killerMovesDebug;

    /**
     * Front-end killer moves
     */
    private KillerMoves killerMoves;

    private SearchListenerMediator searchListenerMediator;

    private boolean withDebugSearchTree;

    public KillerMoveBuilder() {
        killerMovesTableImp = new KillerMovesTable();
    }

    public KillerMoveBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    @Override
    public KillerMoveBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
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
        searchListenerMediator.accept(new LinkKillerMovesVisitor(killerMoves));
    }

    private void buildObjects() {
        if (withDebugSearchTree) {
            killerMovesDebug = new KillerMovesDebug();
        }
    }

    private void setupListenerMediator() {
        searchListenerMediator.add(killerMovesTableImp);

        if (killerMovesDebug != null) {
            searchListenerMediator.add(killerMovesDebug);
        }
    }

    private void createChains() {
        killerMoves = killerMovesTableImp;
        if (killerMovesDebug != null) {
            killerMovesDebug.setImp(killerMovesTableImp);
            killerMoves = killerMovesDebug;
        }
    }


}
