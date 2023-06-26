package net.chesstango.search.polyglot;

import net.chesstango.board.Square;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class SimplePolyglotBook implements PolyglotBook {

    private final static int ENTRY_SIZE = 16;
    private Map<Long, List<Long>> entries;

    @Override
    public void load(Path path) {
        entries = new HashMap<>();
        byte[] bytes = new byte[ENTRY_SIZE];
        try (InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ)) {
            while (inputStream.read(bytes) == ENTRY_SIZE) {
                addEntry(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PolyglotEntry> search(long key) {
        List<PolyglotEntry> polyglotEntryList = null;

        List<Long> rawEntries = entries.get(key);
        if (rawEntries != null) {
            polyglotEntryList = new ArrayList<>();

            for (long rawEntry : rawEntries) {
                int toFile = (int) ((rawEntry & 0b00000000_00000111_00000000_00000000_00000000_00000000_00000000_00000000L) >> (48));
                int toRow = (int) ((rawEntry & 0b00000000_00111000_00000000_00000000_00000000_00000000_00000000_00000000L) >> (48 + 3));

                int fromFile = (int) ((rawEntry & 0b00000001_11000000_00000000_00000000_00000000_00000000_00000000_00000000L) >> (48 + 6));
                int fromRow = (int) ((rawEntry & 0b00001110_00000000_00000000_00000000_00000000_00000000_00000000_00000000L) >> (48 + 9));

                int weight = (int) ((rawEntry & 0b00000000_00000000_11111111_11111111_00000000_00000000_00000000_00000000L) >> (32));

                Square to = Square.getSquare(toFile, toRow);
                Square from = Square.getSquare(fromFile, fromRow);

                PolyglotEntry entry = new PolyglotEntry(to ,from, weight);

                polyglotEntryList.add(entry);
            }
        }

        return polyglotEntryList;
    }

    private void addEntry(byte[] bytes) {
        long key = convertToLong(new byte[]{bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]});

        List<Long> valueList = entries.computeIfAbsent(key, k -> new ArrayList<>());

        valueList.add(convertToLong(new byte[]{bytes[8], bytes[9], bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]}));
    }

    private static long convertToLong(byte[] bytes) {
        long value = 0l;
        for (byte b : bytes) {
            value = (value << 8) + (b & 255);
        }
        return value;
    }

}
