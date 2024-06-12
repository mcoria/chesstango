module net.chesstango.tools {
    exports net.chesstango.tools;
    exports net.chesstango.tools.tuning.fitnessfunctions;
    exports net.chesstango.tools.tuning.geneticproviders;
    exports net.chesstango.tools.search.reports.pv;
    exports net.chesstango.tools.search.reports.nodes;
    exports net.chesstango.tools.search.reports.evaluation;

    requires net.chesstango.board;
    requires net.chesstango.engine;
    requires net.chesstango.evaluation;
    requires net.chesstango.search;
    requires net.chesstango.uci.arena;
    requires net.chesstango.uci.engine;

    requires com.fasterxml.jackson.databind;
    requires org.slf4j;
    requires io.jenetics.base;
    requires org.apache.commons.pool2;

    requires static lombok;

    opens net.chesstango.tools.search.reports.summary to com.fasterxml.jackson.databind;

}