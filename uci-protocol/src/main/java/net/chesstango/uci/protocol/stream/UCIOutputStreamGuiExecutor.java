package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIGui;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 */
public class UCIOutputStreamGuiExecutor implements UCIOutputStream {

    private final UCIGui executor;

    public UCIOutputStreamGuiExecutor(UCIGui executor) {
        this.executor = executor;
    }

    @Override
    public void accept(UCIMessage message) {
        //TODO: implementar filtrado, se me ocurre una canal para descartar
        if(message instanceof UCIResponse) {
            ((UCIResponse) message).execute(executor);
        }
    }
}
