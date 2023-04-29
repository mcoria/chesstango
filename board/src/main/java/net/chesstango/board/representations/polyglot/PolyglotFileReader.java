package net.chesstango.board.representations.polyglot;

import net.chesstango.board.Square;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 *
 */
public class PolyglotFileReader {

    public Map<Long, List<MoveBookEntry>> read(){
        Map<Long, List<MoveBookEntry>> entries = new HashMap<>();

        File file = new File("C:\\Java\\projects\\chess\\chess-utils\\books\\Perfect_2021\\Perfect_2021\\BIN\\Perfect2021.bin");
        byte[] bytes = new byte[16];
        long totalBytes = file.length();
        try(FileInputStream fis = new FileInputStream(file)) {
            while (totalBytes > 0) {
                fis.read(bytes);
                totalBytes -= 16;

                addEntry(entries, bytes);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return entries;
    }

    private void addEntry(Map<Long, List<MoveBookEntry>> entries, byte[] bytes) {
        long key = convertToLong(new byte[]{bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]});

        List<MoveBookEntry> valueList = entries.computeIfAbsent(key, k -> new ArrayList<>());

        MoveBookEntry entry =  new MoveBookEntry();
        entry.value = convertToLong(new byte[]{bytes[8], bytes[9], bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]});

        //move weight learn

        int toFile = (int) ((entry.value & 0b00000000_00000111_00000000_00000000_00000000_00000000_00000000_00000000L) >> (48));
        int toRow =  (int) ((entry.value & 0b00000000_00111000_00000000_00000000_00000000_00000000_00000000_00000000L) >> (48 + 3));

        int fromFile = (int)  ((entry.value & 0b00000001_11000000_00000000_00000000_00000000_00000000_00000000_00000000L)  >> (48 + 6));
        int fromRow  = (int)  ((entry.value & 0b00001110_00000000_00000000_00000000_00000000_00000000_00000000_00000000L)  >> (48 + 9));

        int weight = (int) ((entry.value & 0b00000000_00000000_11111111_11111111_00000000_00000000_00000000_00000000L)  >> (32));

        entry.to = Square.getSquare(toFile, toRow);
        entry.from = Square.getSquare(fromFile, fromRow);
        entry.weight = weight;

        valueList.add(entry);
    }

    static long convertToLong(byte[] bytes)
    {
        long value = 0l;
        for (byte b : bytes) {
            value = (value << 8) + (b & 255);
        }
        return value;
    }

    /**
     * key    uint64
     *
     * move   uint16
     * weight uint16
     * learn  uint32
     */

    public static class MoveBookEntry {
        long value;

        Square from;
        Square to;
        byte promotion;

        int weight;

        @Override
        public String toString() {
            return String.format("%s%s %d", from, to, weight);
        }
    }

}
