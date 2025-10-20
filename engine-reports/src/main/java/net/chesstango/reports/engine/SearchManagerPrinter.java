package net.chesstango.reports.engine;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.PrintStream;

/**
 *
 * @author Mauricio Coria
 */
public class SearchManagerPrinter {
    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private SearchManagerModel reportModel;

    public void print() {
        out.print("Search Manager Statistics\n");

        // Marco superior de la tabla
        out.printf(" ____________");
        out.printf("____________");
        out.printf("____________");
        out.printf("___________");
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Move      ");
        out.printf("| OpenBook  ");
        out.printf("|   Tree    ");
        out.printf("| Tablebase ");
        out.printf("|\n");

        // Cuerpo
        for (SearchManagerModel.SearchManagerModelDetail moveDetail : reportModel.moveDetail) {
            out.printf("|      %4s ", moveDetail.move);

            out.printf("|         %1s ", moveDetail.type == SearchManagerModel.MoveType.OpenBook ? "X" : "");
            out.printf("|         %1s ", moveDetail.type == SearchManagerModel.MoveType.Tree ? "X" : "");
            out.printf("|         %1s ", moveDetail.type == SearchManagerModel.MoveType.Tablebase ? "X" : "");

            out.printf("|\n");
        }

        // Totales
        out.printf("|-----------");
        out.printf("|-----------");
        out.printf("|-----------");
        out.printf("|-----------");
        out.printf("|\n");

        out.printf("| SUM       ");
        out.printf("| %9d ", reportModel.searchByOpenBookCounter);
        out.printf("| %9d ", reportModel.searchByTreeCounter);
        out.printf("| %9d ", reportModel.searchByTablebaseCounter);
        out.printf("|\n");


        // Marco inferior de la tabla
        out.printf(" -----------");
        out.printf("------------");
        out.printf("------------");
        out.printf("------------");
        out.printf("\n\n");
    }
}
