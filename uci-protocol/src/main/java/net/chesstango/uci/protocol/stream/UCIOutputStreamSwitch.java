package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIMessage;

import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class UCIOutputStreamSwitch implements UCIOutputStream {

    private UCIOutputStream output;

    private final Predicate<UCIMessage> predicateCondition;

    private final Runnable execute;

    public UCIOutputStreamSwitch(Predicate<UCIMessage> predicateCondition, Runnable execute) {
        this.predicateCondition = predicateCondition;
        this.execute = execute;
    }

    public UCIOutputStreamSwitch setOutputStream(UCIOutputStream output) {
        this.output = output;
        return this;
    }

    @Override
    public void accept(UCIMessage message) {
        output.accept(message);
        if (predicateCondition.test(message)) {
            execute.run();
        }
    }
}
