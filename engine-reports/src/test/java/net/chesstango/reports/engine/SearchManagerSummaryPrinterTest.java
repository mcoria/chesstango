package net.chesstango.reports.engine;

import net.chesstango.reports.PrinterTestAbstract;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SearchManagerSummaryPrinterTest extends PrinterTestAbstract {

    @Test
    public void testPrintReport() {
        List<SearchManagerModel> reportModelList = new ArrayList<>();
        SearchManagerModel searchManagerModel;

        /**
         * Firt entry
         */
        searchManagerModel = new SearchManagerModel();
        searchManagerModel.searchesName = "Engine 1";
        searchManagerModel.searchByOpenBookCounter = 1;
        searchManagerModel.searchByTreeCounter = 2;
        searchManagerModel.searchByTablebaseCounter = 3;
        reportModelList.add(searchManagerModel);

        /**
         * Second entry
         */
        searchManagerModel = new SearchManagerModel();
        searchManagerModel.searchesName = "f144465d-9f28-4e92-9bdc-b43a182df4bd";
        searchManagerModel.searchByOpenBookCounter = 4;
        searchManagerModel.searchByTreeCounter = 5;
        searchManagerModel.searchByTablebaseCounter = 6;
        reportModelList.add(searchManagerModel);

        /**
         * Printer
         */

        SearchManagerSummaryPrinter searchManagerPrinter = new SearchManagerSummaryPrinter();
        searchManagerPrinter.setReportModel(reportModelList);
        //searchManagerPrinter.setOut(System.out);
        //searchManagerPrinter.print();

        assertSearchTree(searchManagerPrinter, "SearchManagerSummaryPrinterTest.txt");
    }
}
