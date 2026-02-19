package net.chesstango.reports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

/**
 *
 * @author Mauricio Coria
 */
public class ReportToFile {
    private final Path directory;

    public ReportToFile(Path directory) {
        this.directory = directory;
    }

    /**
     * Saves report to file; returns instance for chaining
     */
    public ReportToFile save(String fileName, Report report) {
        Path searchSummaryPath = directory.resolve(fileName);
        try (PrintStream out = new PrintStream(new FileOutputStream(searchSummaryPath.toFile()), true)) {
            report.printReport(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return this;
    }
}
