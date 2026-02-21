package net.chesstango.reports;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class PrinterTxtTable implements Printer {
    private final int numberOfColumns;

    private String[] titles;

    private List<String[]> rows;

    private String[] bottomRow;

    private PrintStream out;

    public PrinterTxtTable(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
        this.rows = new LinkedList<>();
    }

    public PrinterTxtTable setTitles(String... titles) {
        if (titles.length != numberOfColumns) {
            throw new IllegalArgumentException("Titles must have " + numberOfColumns + " columns, found " + titles.length + " columns" );
        }
        this.titles = titles;
        return this;
    }

    public PrinterTxtTable addRow(String... row) {
        if (row.length != numberOfColumns) {
            throw new IllegalArgumentException("Row must have " + numberOfColumns + " columns, found " + row.length + " columns");
        }
        this.rows.add(row);
        return this;
    }

    public PrinterTxtTable setBottomRow(String... row) {
        if (row.length != numberOfColumns) {
            throw new IllegalArgumentException("Row must have " + numberOfColumns + " columns, found " + row.length + " columns");
        }
        this.bottomRow = row;
        return this;
    }


    @Override
    public PrinterTxtTable setOut(PrintStream out) {
        this.out = out;
        return this;
    }

    @Override
    public PrinterTxtTable print() {
        int[] columnWidths = calculateColumnWidths();

        // Marco superior de la tabla
        out.printf(" %s", "_".repeat(columnWidths[0] + 2));
        for (int i = 1; i < numberOfColumns; i++) {
            out.print("_".repeat(columnWidths[i] + 3));
        }
        out.printf("%n");

        // Títulos
        out.print("|");
        for (int i = 0; i < numberOfColumns; i++) {
            out.printf(" %-" + columnWidths[i] + "s |", titles[i]);
        }
        out.printf("%n");

        // Línea separadora
        out.print("|");
        for (int i = 0; i < numberOfColumns; i++) {
            out.print("-".repeat(columnWidths[i] + 2));
            out.print("|");
        }
        out.printf("%n");

        for (String[] row : rows) {
            // Datos
            out.print("|");
            for (int i = 0; i < numberOfColumns; i++) {
                out.printf(" %" + columnWidths[i] + "s |", row[i]);
            }
            out.printf("%n");
        }

        if (bottomRow != null) {
            // Línea separadora
            out.print("|");
            for (int i = 0; i < numberOfColumns; i++) {
                out.print("-".repeat(columnWidths[i] + 2));
                out.print("|");
            }
            out.printf("%n");

            // Datos
            out.print("|");
            for (int i = 0; i < numberOfColumns; i++) {
                out.printf(" %" + columnWidths[i] + "s |", bottomRow[i]);
            }
            out.printf("%n");
        }

        // Marco inferior de la tabla
        out.printf(" %s", "-".repeat(columnWidths[0] + 2));
        for (int i = 1; i < numberOfColumns; i++) {
            out.print("-".repeat(columnWidths[i] + 3));
        }
        out.printf("%n");

        return this;
    }

    private int[] calculateColumnWidths() {
        int[] columnWidths = Arrays.stream(titles).mapToInt(String::length).toArray();

        rows.forEach(row -> {
            for (int i = 0; i < numberOfColumns; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        });

        if (bottomRow != null) {
            for (int i = 0; i < numberOfColumns; i++) {
                columnWidths[i] = Math.max(columnWidths[i], bottomRow[i].length());
            }
        }

        return columnWidths;
    }
}
