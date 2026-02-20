package net.chesstango.reports;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintStream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for the PrinterTxtTable class' print method.
 * Ensures table printing functionality behaves correctly for various configurations.
 */
class PrinterTxtTableTest {

    @Test
    void testPrintEmptyTable() {
        // Arrange
        PrintStream mockOut = System.out; //Mockito.mock(PrintStream.class);
        PrinterTxtTable printer = new PrinterTxtTable(3)
                .setTitles("Col1", "Col2", "Col3")
                .setOut(mockOut);

        // Act
        printer.print();
    }

    @Test
    void testPrintTableWithRows() {
        // Arrange
        PrintStream mockOut = System.out;
        PrinterTxtTable printer = new PrinterTxtTable(2)
                .setTitles("Name", "Age")
                .addRow("Alice", "25")
                .addRow("Bob", "30")
                .setOut(mockOut);

        // Act
        printer.print();
    }

    @Test
    void testPrintTableWithDynamicColumnWidths() {
        // Arrange
        PrintStream mockOut = System.out;
        PrinterTxtTable printer = new PrinterTxtTable(2)
                .setTitles("Name", "Occupation")
                .addRow("Alice", "Software Engineer")
                .addRow("Bob", "Designer")
                .setBottomRow("TOT1", "TOT2")
                .setOut(mockOut);

        // Act
        printer.print();
    }
}