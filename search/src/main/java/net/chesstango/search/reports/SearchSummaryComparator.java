package net.chesstango.search.reports;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class SearchSummaryComparator {

    public static void main(String[] args) {
        /*
        SearchSummaryComparator summaryComparator = new SearchSummaryComparator("depth-5-v0.0.14-2023-08-24-14-36");
        summaryComparator.addSession("depth-4-v0.0.14-2023-08-24-14-12");
        summaryComparator.addSession("depth-5-v0.0.15-SNAPSHOT-2023-08-24-08-10-withArrayTT_1024_128-withSort");
         */

        SearchSummaryComparator summaryComparator = new SearchSummaryComparator("depth-5-2023-08-27-23-55-nada");
        summaryComparator.addSession("depth-5-2023-08-27-23-56-IterativeDeepening");
        summaryComparator.addSession("depth-5-2023-08-28-00-09-IterativeDeepening-TT");
        summaryComparator.addSession("depth-5-2023-08-28-00-09-IterativeDeepening-TT-MoveSorter");
        //summaryComparator.addSession("depth-7-2023-08-27-12-36");

        /*
        summaryComparator.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-w1.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-b1.epd");

        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-w2.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-b2.epd");
        */

        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd");
        //summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd");

        /*
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS1.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS2.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS3.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS4.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS5.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS6.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS7.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS8.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS9.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS10.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS11.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS12.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS13.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS14.epd");
        summaryComparator.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS15.epd");
        */
    }

    private final String baseLineSessionID;
    private final List<String> searchSessions = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Path suiteParentDirectory;
    private String suiteName;
    private SearchSummaryModel baseLineSearchSummary;
    private List<SearchSummaryModel> searchSummaryList;

    public SearchSummaryComparator(String baseLineSessionID) {
        this.baseLineSessionID = baseLineSessionID;
    }

    private void execute(String suiteFile) {
        loadSearchSummaries(suiteFile);
        printReport(System.out);
    }

    private void printReport(PrintStream out) {
        SearchSummaryDiffReportModel reportModel = SearchSummaryDiffReportModel.createModel(suiteName, baseLineSearchSummary, searchSummaryList);

        new SearchSummaryDiffReport()
                .withSummaryDiffReportModel(reportModel)
                .printReport(out);
    }


    private void loadSearchSummaries(String suiteFile) {
        Path suitePath = Paths.get(suiteFile);

        if (!Files.exists(suitePath)) {
            System.err.printf("file not found: %s\n", suiteFile);
            return;
        }

        suiteName = suitePath.getFileName().toString();

        suiteParentDirectory = suitePath.getParent();

        baseLineSearchSummary = loadSearchSummary(baseLineSessionID);

        if (baseLineSearchSummary == null) {
            System.err.printf("baseLineSearchSummary not found: %s\n", suiteName);
            return;
        }

        searchSummaryList = searchSessions.stream()
                .map(this::loadSearchSummary)
                .filter(Objects::nonNull)
                .toList();
    }

    private SearchSummaryModel loadSearchSummary(String sessionID) {

        Path searchSummaryPath = suiteParentDirectory.resolve(sessionID).resolve(String.format("%s.json", suiteName));

        if (!Files.exists(searchSummaryPath)) {
            System.err.printf("file not found: %s\n", searchSummaryPath);
            return null;
        }

        try {
            return objectMapper.readValue(searchSummaryPath.toFile(), SearchSummaryModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addSession(String sessionID) {
        searchSessions.add(sessionID);
    }
}
