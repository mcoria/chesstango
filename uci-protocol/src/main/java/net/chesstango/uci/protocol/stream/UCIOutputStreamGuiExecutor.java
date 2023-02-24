package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.*;

import java.io.IOException;

/**
 * @author Mauricio Coria
 */
//TODO: NO SE UTILIZA
public class UCIOutputStreamGuiExecutor implements UCIOutputStream {

    private final UCIGui executor;

    public UCIOutputStreamGuiExecutor(UCIGui executor) {
        this.executor = executor;
    }

    @Override
    public void accept(UCIMessage message) {
        ((UCIResponse) message).execute(executor);
    }

}
