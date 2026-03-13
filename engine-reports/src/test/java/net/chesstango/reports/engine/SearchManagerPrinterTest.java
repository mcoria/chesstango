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
        entry.searchTime = 1;
        searchManagerModel.moveDetail.add(entry);

        entry = new SearchManagerModel.SearchManagerModelDetail();
        entry.move = "e2e4";
        entry.type = SearchManagerModel.MoveType.Tree;
        entry.searchTime = 2;
        searchManagerModel.moveDetail.add(entry);

        entry = new SearchManagerModel.SearchManagerModelDetail();
        entry.move = "e2e5";
        entry.type = SearchManagerModel.MoveType.Tablebase;
        entry.searchTime = 3;
        searchManagerModel.moveDetail.add(entry);

        SearchManagerPrinter searchManagerPrinter = new SearchManagerPrinter();
        searchManagerPrinter.setReportModel(searchManagerModel);

        assertSearchTree(searchManagerPrinter, "SearchManagerPrinterTest.txt");
    }

}
