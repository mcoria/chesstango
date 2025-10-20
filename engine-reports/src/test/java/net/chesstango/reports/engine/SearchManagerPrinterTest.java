package net.chesstango.reports.engine;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class SearchManagerPrinterTest {

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

    private void assertSearchTree(SearchManagerPrinter searchManagerPrinter, String resourceName) {
        List<String> expectedPrintChain = readResource(resourceName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);) {
            //chainPrinterVisitor.print(search, System.out);
            searchManagerPrinter.setOut(out);
            searchManagerPrinter.print();
        }

        try (InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());) {
            List<String> actualPrintChain = readInputStream(inputStream);

            assertEquals(expectedPrintChain.size(), actualPrintChain.size());

            for (int i = 0; i < actualPrintChain.size(); i++) {
                assertEquals(expectedPrintChain.get(i), actualPrintChain.get(i), "Line " + (i + 1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> readResource(String resourceName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);) {
            return readInputStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private List<String> readInputStream(InputStream inputStream) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}
