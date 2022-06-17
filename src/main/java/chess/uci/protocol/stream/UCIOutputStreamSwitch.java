package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

import java.io.IOException;
import java.util.function.Predicate;

public class UCIOutputStreamSwitch implements UCIOutputStream {

    private UCIOutputStream output;

    private final Predicate<UCIMessage> predicateCondition;

    private final Runnable execute;

    public UCIOutputStreamSwitch(Predicate<UCIMessage> predicateCondition, Runnable execute) {
        this.predicateCondition = predicateCondition;
        this.execute = execute;
    }

    public void setOutputStream(UCIOutputStream output){
        this.output = output;
    }

    @Override
    public void write(UCIMessage message) {
        output.write(message);
        if(predicateCondition.test(message)){
            execute.run();
        }
    }

    @Override
    public void close() throws IOException {

    }
}