package net.chesstango.engine.polyglot;

import java.io.Closeable;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MappedPolyglotBook implements PolyglotBook, Closeable {
    private final static int ENTRY_SIZE = 16;
    private FileChannel fileChannel;
    private MappedByteBuffer mappedByteBuffer;
    private int maxEntry = 0;

    @Override
    public void load(Path pathToRead) {
        try {

            fileChannel = (FileChannel) Files.newByteChannel(pathToRead, EnumSet.of(StandardOpenOption.READ));

            maxEntry = ((int) fileChannel.size() / ENTRY_SIZE) - 1;

            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PolyglotEntry> search(long key) {
        List<PolyglotEntry> polyglotEntryList = null;

        int idx = -1;

        if (getKey(0) == key) {
            idx = 0;
        } else if (getKey(maxEntry) == key) {
            idx = maxEntry;
        } else {
            idx = findIndex(key, 0, maxEntry);
        }

        if (getKey(idx) == key) {
            int previousIdx = idx - 1;
            while (previousIdx >= 0 && getKey(previousIdx) == key) {
                idx--;
                previousIdx--;
            }

            polyglotEntryList = new ArrayList<>();

            while (getKey(idx) == key) {

                int moveAndWeight = mappedByteBuffer.getInt(idx * ENTRY_SIZE + 8);

                int weight = moveAndWeight & 0b00000000_00000000_11111111_11111111;

                int toFile = ((moveAndWeight & 0b00000000_00000111_00000000_00000000) >>> (16));
                int toRank = ((moveAndWeight & 0b00000000_00111000_00000000_00000000) >>> (16 + 3));

                int fromFile = ((moveAndWeight & 0b00000001_11000000_00000000_00000000) >>> (16 + 6));
                int fromRank = ((moveAndWeight & 0b00001110_00000000_00000000_00000000) >>> (16 + 9));

                PolyglotEntry entry = new PolyglotEntry(key, fromFile, fromRank, toFile, toRank, weight);

                polyglotEntryList.add(entry);

                idx++;
            }
        }

        return polyglotEntryList;
    }

    @Override
    public boolean isLoaded() {
        return fileChannel != null;
    }

    @Override
    public void close() throws IOException {
        if (fileChannel != null) {
            fileChannel.close();
        }
    }

    private int findIndex(long key, int lowerBoundIdx, int upperBoundIdx) {
        if (lowerBoundIdx + 1 == upperBoundIdx) {
            return lowerBoundIdx;
        }

        int middleIdx = (lowerBoundIdx + upperBoundIdx) / 2;

        long middleKey = getKey(middleIdx);

        if (middleKey == key) {
            return middleIdx;
        } else if (isMiddleKeyGreater(key, middleKey)) {
            return findIndex(key, lowerBoundIdx, middleIdx);
        } else {
            return findIndex(key, middleIdx, upperBoundIdx);
        }
    }

    private long getKey(int idx) {
        return mappedByteBuffer.getLong(idx * ENTRY_SIZE);
    }

    private boolean isMiddleKeyGreater(long key, long otherKey) {
        while (key != 0 && otherKey != 0) {
            long highKeyBit = Long.highestOneBit(key);
            long highOtherKeyBit = Long.highestOneBit(otherKey);

            if (highKeyBit != highOtherKeyBit) {
                if (Long.numberOfTrailingZeros(highOtherKeyBit) > Long.numberOfTrailingZeros(highKeyBit)) {
                    return true;
                } else {
                    return false;
                }
            }

            key &= ~highKeyBit;
            otherKey &= ~highOtherKeyBit;
        }

        return false;
    }

}
