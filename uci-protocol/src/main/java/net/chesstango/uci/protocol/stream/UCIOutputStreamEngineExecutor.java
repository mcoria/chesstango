package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 * TODO: Aca deberiamos hacer el filtrado para asegurarnos que solo llegan requests
 */
public class UCIOutputStreamEngineExecutor implements UCIOutputStream {

    private final UCIEngine engine;

    public UCIOutputStreamEngineExecutor(UCIEngine engine) {
        this.engine = engine;
    }

    @Override
    public void accept(UCIMessage message) {
        //TODO: implementar filtrado, se me ocurre una canal para descartar
        if(message instanceof UCIRequest) {
            ((UCIRequest) message).execute(engine);
        }
    }
}
