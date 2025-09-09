module net.chesstango.search {
    exports net.chesstango.search;
    exports net.chesstango.search.smart;
    exports net.chesstango.search.smart.sorters;
    exports net.chesstango.search.smart.sorters.comparators;
    exports net.chesstango.search.smart.negamax;
    exports net.chesstango.search.smart.minmax;
    exports net.chesstango.search.smart.features.zobrist.listeners;
    exports net.chesstango.search.smart.features.zobrist.filters;
    exports net.chesstango.search.smart.features.transposition;
    exports net.chesstango.search.smart.features.transposition.listeners;
    exports net.chesstango.search.smart.features.transposition.filters;
    exports net.chesstango.search.smart.features.transposition.comparators;
    exports net.chesstango.search.smart.features.statistics.node;
    exports net.chesstango.search.smart.features.statistics.node.listeners;
    exports net.chesstango.search.smart.features.statistics.node.filters;
    exports net.chesstango.search.smart.features.statistics.evaluation;
    exports net.chesstango.search.smart.features.pv.listeners;
    exports net.chesstango.search.smart.features.pv.filters;
    exports net.chesstango.search.smart.features.killermoves;
    exports net.chesstango.search.smart.features.killermoves.listeners;
    exports net.chesstango.search.smart.features.killermoves.filters;
    exports net.chesstango.search.smart.features.killermoves.comparators;
    exports net.chesstango.search.smart.features.evaluator;
    exports net.chesstango.search.smart.features.evaluator.comparators;
    exports net.chesstango.search.smart.features.debug;
    exports net.chesstango.search.smart.features.debug.traps;
    exports net.chesstango.search.smart.features.debug.traps.predicates;
    exports net.chesstango.search.smart.features.debug.traps.actions;
    exports net.chesstango.search.smart.features.debug.model;
    exports net.chesstango.search.smart.features.debug.listeners;
    exports net.chesstango.search.smart.features.debug.filters;
    exports net.chesstango.search.smart.alphabeta;
    exports net.chesstango.search.smart.alphabeta.listeners;
    exports net.chesstango.search.smart.alphabeta.filters;
    exports net.chesstango.search.smart.alphabeta.filters.once;
    exports net.chesstango.search.dummy;
    exports net.chesstango.search.builders;
    exports net.chesstango.search.builders.alphabeta;
    exports net.chesstango.search.visitors;


    requires com.fasterxml.jackson.databind;
    requires net.chesstango.board;
    requires net.chesstango.evaluation;
    requires net.chesstango.gardel;
    requires static lombok;


    opens net.chesstango.search.gamegraph to com.fasterxml.jackson.databind;
}