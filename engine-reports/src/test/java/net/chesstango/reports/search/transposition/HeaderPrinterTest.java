package net.chesstango.reports.search.transposition;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintStream;

import static org.mockito.Mockito.verify;

/**
 * Tests for HeaderPrinter class, which is responsible for printing
 * formatted reports based on a TranspositionModel instance.
 */
@Disabled
class HeaderPrinterTest {

    @Test
    void testPrintWithValidModel() {
        // Arrange
        PrintStream mockPrintStream = Mockito.mock(PrintStream.class);
        TranspositionModel model = new TranspositionModel();
        model.searchGroupName = "Test Group";
        model.searches = 10;
        model.readsTotal = 100;
        model.readHitsTotal = 60;
        model.readHitPercentageTotal = 60;
        model.writesTotal = 40;
        model.overWritesTotal = 10;
        model.overWritePercentageTotal = 25;

        HeaderPrinter headerPrinter = new HeaderPrinter()
                .setOut(mockPrintStream)
                .setTranspositionModel(model);

        // Act
        headerPrinter.print();

        // Assert
        verify(mockPrintStream).print("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        verify(mockPrintStream).printf("TranspositionReport   : %s\n\n", "Test Group");
        verify(mockPrintStream).printf("Searches              : %d\n", 10);
        verify(mockPrintStream).printf("Reads                 : %d\n", 100);
        verify(mockPrintStream).printf("Read Hits             : %d (%2d%%)\n", 60, 60);
        verify(mockPrintStream).printf("Writes                : %d\n", 40);
        verify(mockPrintStream).printf("OverWrites            : %d (%2d%%)\n", 10, 25);
        verify(mockPrintStream).print("\n");
    }

    @Test
    void testPrintWithEmptyModel() {
        // Arrange
        PrintStream mockPrintStream = Mockito.mock(PrintStream.class);
        TranspositionModel model = new TranspositionModel();

        HeaderPrinter headerPrinter = new HeaderPrinter()
                .setOut(mockPrintStream)
                .setTranspositionModel(model);

        // Act
        headerPrinter.print();

        // Assert
        verify(mockPrintStream).print("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        verify(mockPrintStream).printf("TranspositionReport   : %s\n\n", null);
        verify(mockPrintStream).printf("Searches              : %d\n", 0);
        verify(mockPrintStream).printf("Reads                 : %d\n", 0);
        verify(mockPrintStream).printf("Read Hits             : %d (%2d%%)\n", 0, 0);
        verify(mockPrintStream).printf("Writes                : %d\n", 0);
        verify(mockPrintStream).printf("OverWrites            : %d (%2d%%)\n", 0, 0);
        verify(mockPrintStream).print("\n");
    }
}