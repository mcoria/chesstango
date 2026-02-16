package net.chesstango.reports.tree.transposition;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class TranspositionPrinter implements Printer {
    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private TranspositionModel transpositionModel;

    public TranspositionPrinter(PrintStream out, TranspositionModel transpositionModel) {
        this.out = out;
        this.transpositionModel = transpositionModel;
    }

    @Override
    public void setOut(PrintStream out) {
        this.out = out;
    }

    @Override
    public void print() {

    }
}
