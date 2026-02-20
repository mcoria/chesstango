package net.chesstango.reports.engine;

import net.chesstango.reports.PrinterTestAbstract;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class SearchManagerPrinterTest extends PrinterTestAbstract {

    @Test
    public void testPrintReport() {
        SearchManagerModel searchManagerModel = new SearchManagerModel();

        SearchManagerModel.SearchManagerModelDetail entry = new SearchManagerModel.SearchManagerModelDetail();
        entry.move = "e2e3";
        entry.type = SearchManagerModel.MoveType.OpenBook;
        searchManagerModel.moveDetail.add(entry);

        entry = new SearchManagerModel.SearchManagerModelDetail();
        entry.move = "e2e4";
        entry.type = SearchManagerModel.MoveType.Tree;
        searchManagerModel.moveDetail.add(entry);

        entry = new SearchManagerModel.SearchManagerModelDetail();
        entry.move = "e2e5";
        entry.type = SearchManagerModel.MoveType.Tablebase;
        searchManagerModel.moveDetail.add(entry);

        SearchManagerPrinter searchManagerPrinter = new SearchManagerPrinter();
        searchManagerPrinter.setReportModel(searchManagerModel);
        //searchManagerPrinter.setOut(System.out);
        //searchManagerPrinter.print();

        assertSearchTree(searchManagerPrinter, "SearchManagerPrinterTest.txt");
    }

}
