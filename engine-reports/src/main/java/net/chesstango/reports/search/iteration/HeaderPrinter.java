package net.chesstango.reports.search.iteration;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
class HeaderPrinter implements Printer {
    @Setter
    @Accessors(chain = true)
    private IterationEvaluationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public HeaderPrinter print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("IterationEvaluationReport   : %s%n%n", reportModel.searchGroupName);
        out.printf("Searches                    : %10d%n", reportModel.searches);
        out.printf("Max Iteration               : %10d%n", reportModel.maxIteration);
        out.printf("Evaluation Std Dev  Avg     : %10d%n", reportModel.evaluationStdDevAvg);
        out.printf("%n");

        return this;
    }

}

