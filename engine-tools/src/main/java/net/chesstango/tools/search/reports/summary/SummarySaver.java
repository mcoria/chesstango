package net.chesstango.tools.search.reports.summary;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class SummarySaver {

    private SummaryModel reportModel;

    public void print(PrintStream out) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, reportModel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public SummarySaver withSearchSummaryModel(SummaryModel summaryModel) {
        this.reportModel = summaryModel;
        return this;
    }

}
