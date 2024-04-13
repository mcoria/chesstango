module net.chesstango.tools {
    exports net.chesstango.tools.search.reports.nodes;
    exports net.chesstango.tools.search.reports.evaluation;
    exports net.chesstango.tools.search.reports.pv;
    exports net.chesstango.tools.tuning.fitnessfunctions;
    exports net.chesstango.tools;


    requires net.chesstango.evaluation;
    requires net.chesstango.board;
    requires net.chesstango.search;

    requires com.fasterxml.jackson.databind;
    requires org.slf4j;
    requires static lombok;
    requires io.jenetics.base;
    requires net.chesstango.engine;

}