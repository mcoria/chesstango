module net.chesstango.uci {
    exports net.chesstango.uci.protocol;
    exports net.chesstango.uci.protocol.requests;
    exports net.chesstango.uci.protocol.responses;
    exports net.chesstango.uci.protocol.stream;
    exports net.chesstango.uci.protocol.stream.strings;
    exports net.chesstango.uci.protocol.requests.go;

    requires static lombok;
}