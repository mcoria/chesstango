package net.chesstango.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chesstango.search.reports.summary.SummaryDiffReport;
import net.chesstango.search.reports.summary.SummaryDiffReportModel;
import net.chesstango.search.reports.summary.SummaryModel;

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
public class SummaryComparatorMain {

    public static void main(String[] args) {
        SummaryComparatorMain summaryComparatorMain = new SummaryComparatorMain("depth-5-2024-02-27-01-39-v0.0.26");
        summaryComparatorMain.addSession("depth-5-2024-04-08-22-51-CHT-253");
        summaryComparatorMain.addSession("depth-5-2024-04-09-19-29-CHT-257");
        //summaryComparatorMain.addSession("depth-5-2024-04-10-00-36-CHT-229");
        summaryComparatorMain.addSession("depth-5-2024-04-10-09-11-CHT-218");
        summaryComparatorMain.addSession("depth-5-2024-04-10-15-37-CHT-218");
        //
        //

        summaryComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-w1.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-b1.epd");

        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-w2.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-b2.epd");

        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-w3.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-b3.epd");

        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\sbd.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Nolot.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Kaufman.epd");

        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS1.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS2.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS3.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS4.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS5.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS6.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS7.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS8.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS9.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS10.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS11.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS12.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS13.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS14.epd");
        summaryComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS15.epd");
    }

    private final String baseLineSessionID;
    private final List<String> searchSessions = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Path suiteParentDirectory;
    private String suiteName;
    private SummaryModel baseLineSearchSummary;
    private List<SummaryModel> searchSummaryList;

    public SummaryComparatorMain(String baseLineSessionID) {
        this.baseLineSessionID = baseLineSessionID;
    }

    private void execute(String suiteFile) {
        loadSearchSummaries(suiteFile);
        printReport(System.out);
    }

    private void printReport(PrintStream out) {
        SummaryDiffReportModel reportModel = SummaryDiffReportModel.createModel(suiteName, baseLineSearchSummary, searchSummaryList);

        new SummaryDiffReport()
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

    private SummaryModel loadSearchSummary(String sessionID) {

        Path searchSummaryPath = suiteParentDirectory.resolve(sessionID).resolve(String.format("%s.json", suiteName));

        if (!Files.exists(searchSummaryPath)) {
            System.err.printf("file not found: %s\n", searchSummaryPath);
            return null;
        }

        try {
            return objectMapper.readValue(searchSummaryPath.toFile(), SummaryModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addSession(String sessionID) {
        searchSessions.add(sessionID);
    }
}



