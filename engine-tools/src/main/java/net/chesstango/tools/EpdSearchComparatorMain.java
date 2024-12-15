package net.chesstango.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chesstango.tools.search.reports.summary.SummaryDiffReport;
import net.chesstango.tools.search.reports.summary.SummaryDiffReportModel;
import net.chesstango.tools.search.reports.summary.SummaryModel;

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
public class EpdSearchComparatorMain {

    public static void main(String[] args) {
        EpdSearchComparatorMain epdSearchComparatorMain = new EpdSearchComparatorMain("depth-5-2024-04-10-23-20-v0.0.27");
        //epsSearchComparatorMain.addSession("depth-5-2024-05-12-07-17-v0.0.28-SNAPSHOT");
        epdSearchComparatorMain.addSession("depth-5-2024-09-15-06-06-v0.0.28-SNAPSHOT"); // Con EvaluatorImp04
        epdSearchComparatorMain.addSession("depth-5-2024-09-15-07-14-v0.0.28-SNAPSHOT");
        //
        //

        epdSearchComparatorMain.execute("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-w1.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-b1.epd");

        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-w2.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-b2.epd");

        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-w3.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-b3.epd");

        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Kaufman.epd");


        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\sbd.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Nolot.epd");

        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS1.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS2.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS3.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS4.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS5.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS6.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS7.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS8.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS9.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS10.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS11.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS12.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS13.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS14.epd");
        epdSearchComparatorMain.execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS15.epd");

    }

    private final String baseLineSessionID;
    private final List<String> searchSessions = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Path suiteParentDirectory;
    private String suiteName;
    private SummaryModel baseLineSearchSummary;
    private List<SummaryModel> searchSummaryList;

    public EpdSearchComparatorMain(String baseLineSessionID) {
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



