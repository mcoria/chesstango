package net.chesstango.search.reports;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class SearchSummarySaver {

    private SearchSummaryModel reportModel;

    public void print(PrintStream out) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, reportModel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public SearchSummarySaver withSearchSummaryModel(SearchSummaryModel searchSummaryModel) {
        this.reportModel = searchSummaryModel;
        return this;
    }

}
