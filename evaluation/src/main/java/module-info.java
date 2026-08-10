module net.chesstango.evaluation {
    exports net.chesstango.evaluation;
    exports net.chesstango.evaluation.evaluators;

    requires net.chesstango.gardel;
    requires net.chesstango.board;
    requires tools.jackson.databind;

    requires static lombok;
}