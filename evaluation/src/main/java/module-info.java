module net.chesstango.evaluation {
    exports net.chesstango.evaluation;
    exports net.chesstango.evaluation.evaluators;

    requires net.chesstango.board;
    requires com.fasterxml.jackson.databind;

    requires static lombok;
}