module net.chesstango.evaluation {
    exports net.chesstango.evaluation;
    exports net.chesstango.evaluation.evaluators;
    requires net.chesstango.board;

    requires static lombok;
    requires com.google.gson;
}