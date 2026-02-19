package net.chesstango.reports.search.transposition;

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
        out.printf("_______________");
        out.printf("_____________________");
        out.printf("_______________");
        out.printf("_____________________");
        out.printf("\n");


        // Nombre de las columnas
        out.printf("| Move   ");
        out.printf("| Reads        ");
        out.printf("| Read Hits          ");
        out.printf("| Writes       ");
        out.printf("| OverWrites         ");
        out.printf("|\n");

        // Cuerpo
        transpositionModel.transpositionModelDetail.forEach(row -> {
            out.printf("|   %s ", row.move);
            out.printf("| %12d ", row.reads);
            out.printf("| %12d (%2d%%) ", row.readHits, row.readHitPercentage);
            out.printf("| %12d ", row.writes);
            out.printf("| %12d (%2d%%) ", row.overWrites, row.overWritePercentage);
            out.printf("|\n");
        });

        // Totales
        out.printf("|--------");
        out.printf("|--------------");
        out.printf("|--------------------");
        out.printf("|--------------");
        out.printf("|--------------------");
        out.printf("|\n");

        // Nombre de las columnas
        out.printf("| SUM    ");
        out.printf("| %12d ", transpositionModel.readsTotal);
        out.printf("| %12d (%2d%%) ", transpositionModel.readHitsTotal, transpositionModel.readHitPercentageTotal);
        out.printf("| %12d ", transpositionModel.writesTotal);
        out.printf("| %12d (%2d%%) ", transpositionModel.overWritesTotal, transpositionModel.overWritePercentageTotal);
        out.printf("|\n");

        // Marco inferior de la tabla
        out.printf(" --------");
        out.printf("---------------");
        out.printf("---------------------");
        out.printf("---------------");
        out.printf("---------------------");
        out.printf("\n");

        return this;
    }
}
