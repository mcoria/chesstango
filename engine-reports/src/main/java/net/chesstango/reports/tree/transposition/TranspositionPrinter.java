package net.chesstango.reports.tree.transposition;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class TranspositionPrinter implements Printer {
    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private TranspositionModel transpositionModel;

    @Override
    public TranspositionPrinter print() {
        out.println("Transposition Statistics");

        // Marco superior de la tabla
        out.printf(" ________");
        out.printf("___________");
        out.printf("___________");
        out.printf("\n");


        // Nombre de las columnas
        out.printf("| Move   ");
        out.printf("| HITS     ");
        out.printf("| REPLACES ");
        out.printf("|\n");

        // Cuerpo
        transpositionModel.transpositionModelDetail.forEach(row -> {
            out.printf("|   %s ", row.move);
            out.printf("| %8d ", row.hits);
            out.printf("| %8d ", row.replaces);
            out.printf("|\n");
        });

        // Totales
        out.printf("|--------");
        out.printf("|----------");
        out.printf("|----------");
        out.printf("|\n");

        // Nombre de las columnas
        out.printf("| SUM    ");
        out.printf("| %8d ", transpositionModel.hitsTotal);
        out.printf("| %8d ", transpositionModel.replacesTotal);
        out.printf("|\n");

        // Marco inferior de la tabla
        out.printf(" --------");
        out.printf("-----------");
        out.printf("-----------");
        out.printf("\n");

        return this;
    }
}
