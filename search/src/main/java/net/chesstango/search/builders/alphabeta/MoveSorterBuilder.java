package net.chesstango.search.builders.alphabeta;

import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.features.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.features.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.features.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparatorQ;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparatorQ;
import net.chesstango.search.smart.sorters.MoveComparator;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterBuilder {
    private final NodeMoveSorter nodeMoveSorter;
    private final QuietComparator quietComparator;
    private final DefaultMoveComparator defaultMoveComparator;
    private SearchListenerMediator searchListenerMediator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionHeadMoveComparatorQ transpositionHeadMoveComparatorQ;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private TranspositionTailMoveComparatorQ transpositionTailMoveComparatorQ;
    private MoveSorterDebug moveSorterDebug;
    private EvaluatorCacheDebug gameEvaluatorCacheDebug;
    private EvaluatorCacheRead evaluatorCacheRead;
    private GameEvaluatorCacheComparator gameEvaluatorCacheComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private KillerMoveComparator killerMoveComparator;
    private MvvLvaComparator mvvLvaComparator;
    private PromotionComparator promotionComparator;
    private PrincipalVariationComparator principalVariationComparator;

    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withKillerMoveSorter;
    private boolean withRecaptureSorter;
    private boolean withMvvLva;

    public MoveSorterBuilder() {
        this.nodeMoveSorter = new NodeMoveSorter();
        this.quietComparator = new QuietComparator();
        this.recaptureMoveComparator = new RecaptureMoveComparator();
        this.defaultMoveComparator = new DefaultMoveComparator();
    }


    public MoveSorterBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public MoveSorterBuilder withGameEvaluatorCache(EvaluatorCacheRead evaluatorCacheRead) {
        this.evaluatorCacheRead = evaluatorCacheRead;
        return this;
    }

    public MoveSorterBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public MoveSorterBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public MoveSorterBuilder withKillerMoveSorter() {
        this.withKillerMoveSorter = true;
        return this;
    }

    public MoveSorterBuilder withRecaptureSorter() {
        this.withRecaptureSorter = true;
        return this;
    }

    public MoveSorterBuilder withMvvLva() {
        this.withMvvLva = true;
        return this;
    }

    public MoveSorter build() {
        buildObjects();

        setupListenerMediator();


        MoveSorter moveSorter = nodeMoveSorter;
        nodeMoveSorter.setMoveComparator(createComparatorChain());

        if (moveSorterDebug != null) {
            moveSorterDebug.setMoveSorterImp(moveSorter);
            moveSorter = moveSorterDebug;
        }

        return moveSorter;
    }

    private void buildObjects() {
        if (withTranspositionTable) {
            transpositionHeadMoveComparator = new TranspositionHeadMoveComparator();
            transpositionHeadMoveComparatorQ = new TranspositionHeadMoveComparatorQ();

            transpositionTailMoveComparator = new TranspositionTailMoveComparator();
            transpositionTailMoveComparatorQ = new TranspositionTailMoveComparatorQ();

            principalVariationComparator = new PrincipalVariationComparator();
        }

        if (withDebugSearchTree) {
            moveSorterDebug = new MoveSorterDebug();
            gameEvaluatorCacheDebug = new EvaluatorCacheDebug();
            gameEvaluatorCacheDebug.setEvaluatorCacheRead(evaluatorCacheRead);
        }

        if (evaluatorCacheRead != null) {
            gameEvaluatorCacheComparator = new GameEvaluatorCacheComparator();
            if (withDebugSearchTree) {
                gameEvaluatorCacheComparator.setEvaluatorCacheRead(gameEvaluatorCacheDebug);
            } else {
                gameEvaluatorCacheComparator.setEvaluatorCacheRead(evaluatorCacheRead);
            }
        }

        if (withKillerMoveSorter) {
            killerMoveComparator = new KillerMoveComparator();
        }

        if (withRecaptureSorter) {
            recaptureMoveComparator = new RecaptureMoveComparator();
        }

        if (withMvvLva) {
            mvvLvaComparator = new MvvLvaComparator();
        }

        promotionComparator = new PromotionComparator();
    }

    private void setupListenerMediator() {
        searchListenerMediator.add(nodeMoveSorter);

        if (transpositionHeadMoveComparator != null) {
            searchListenerMediator.addAcceptor(transpositionHeadMoveComparator);
        }

        if (transpositionHeadMoveComparatorQ != null) {
            searchListenerMediator.addAcceptor(transpositionHeadMoveComparatorQ);
        }

        if (transpositionTailMoveComparator != null) {
            searchListenerMediator.addAcceptor(transpositionTailMoveComparator);
        }

        if (transpositionTailMoveComparatorQ != null) {
            searchListenerMediator.addAcceptor(transpositionTailMoveComparatorQ);
        }

        if (principalVariationComparator != null) {
            searchListenerMediator.add(principalVariationComparator);
        }

        if (recaptureMoveComparator != null) {
            searchListenerMediator.add(recaptureMoveComparator);
        }

        if (gameEvaluatorCacheComparator != null) {
            searchListenerMediator.add(gameEvaluatorCacheComparator);
        }

        if (moveSorterDebug != null) {
            searchListenerMediator.addAcceptor(moveSorterDebug);
        }

        if (gameEvaluatorCacheDebug != null) {
            searchListenerMediator.addAcceptor(gameEvaluatorCacheDebug);
        }

        if (killerMoveComparator != null) {
            searchListenerMediator.addAcceptor(killerMoveComparator);
        }

        /*
        if (quietComparator != null) {
            smartListenerMediator.add(quietComparator);
        }
         */
    }


    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        if (withTranspositionTable) {
            chain.add(principalVariationComparator);
            chain.add(transpositionHeadMoveComparator);
            chain.add(transpositionTailMoveComparator);
        }

        chain.add(quietComparator);

        MoveComparator chainTail = buildChainTail();

        quietComparator.setNoQuietNext(buildNoQuietNext(chainTail));

        quietComparator.setQuietNext(buildQuietNext(chainTail));

        return linkChain(chain);
    }


    private MoveComparator buildNoQuietNext(MoveComparator chainTail) {
        List<MoveComparator> chain = new LinkedList<>();

        if (withTranspositionTable) {
            chain.add(transpositionHeadMoveComparatorQ);
            chain.add(transpositionTailMoveComparatorQ);
        }

        chain.add(promotionComparator);

        if (recaptureMoveComparator != null) {
            chain.add(recaptureMoveComparator);
        }

        if (mvvLvaComparator != null) {
            chain.add(mvvLvaComparator);
        }

        chain.add(chainTail);

        return linkChain(chain);
    }

    private MoveComparator buildQuietNext(MoveComparator chainTail) {
        List<MoveComparator> chain = new LinkedList<>();

        if (killerMoveComparator != null) {
            chain.add(killerMoveComparator);
        }

        chain.add(chainTail);

        return linkChain(chain);
    }

    private MoveComparator buildChainTail() {
        List<MoveComparator> chain = new LinkedList<>();

        if (gameEvaluatorCacheComparator != null) {
            chain.add(gameEvaluatorCacheComparator);
        }

        chain.add(defaultMoveComparator);

        return linkChain(chain);
    }

    private MoveComparator linkChain(List<MoveComparator> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            MoveComparator currentComparator = chain.get(i);
            MoveComparator next = chain.get(i + 1);

            switch (currentComparator) {
                case TranspositionHeadMoveComparator headMoveComparator -> headMoveComparator.setNext(next);
                case TranspositionTailMoveComparator tailMoveComparator -> tailMoveComparator.setNext(next);
                case TranspositionHeadMoveComparatorQ headMoveComparatorQ  -> headMoveComparatorQ.setNext(next);
                case TranspositionTailMoveComparatorQ tailMoveComparatorQ  -> tailMoveComparatorQ.setNext(next);
                case RecaptureMoveComparator recaptureMoveComparator -> recaptureMoveComparator.setNext(next);
                case GameEvaluatorCacheComparator gameEvaluatorCacheComparator -> gameEvaluatorCacheComparator.setNext(next);
                case KillerMoveComparator moveComparator -> moveComparator.setNext(next);
                case MvvLvaComparator lvaComparator -> lvaComparator.setNext(next);
                case PromotionComparator comparator -> comparator.setNext(next);
                case PrincipalVariationComparator variationComparator -> variationComparator.setNext(next);
                case null, default -> throw new RuntimeException("Unknow MoveComparator");
            }
        }
        return chain.getFirst();
    }
}
