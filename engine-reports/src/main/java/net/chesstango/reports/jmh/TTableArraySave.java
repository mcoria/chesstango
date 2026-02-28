package net.chesstango.reports.jmh;

import net.chesstango.search.smart.alphabeta.transposition.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Fork(value = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class TTableArraySave {
    private static final int ARRAY_SIZE = 1024 * 512;

    private TTable tTable;
    private TranspositionEntry transpositionRead;
    private TranspositionEntry transpositionWrite;
    private Random random;

    private static final TranspositionBound[] BOUNDS = TranspositionBound.values();
    private static final int BOUNDS_SIZE = BOUNDS.length;
    private long hashToWrite;
    private int updatedCounter;
    private int insertedCounter;
    private int overWrittenCounter;
    private int saveCounter;


    @Setup(Level.Trial)
    public void setUp() {
        random = new Random();
        tTable = new TTableArrayPrimitives();
        //tTable = new TTableArrayObj();
        //tTable = new TTableMap();
        transpositionRead = new TranspositionEntry();
        transpositionWrite = new TranspositionEntry();
    }

    @Setup(Level.Iteration)
    public void clearTranspositionTable() {
        tTable.clear();
        updatedCounter = 0;
        insertedCounter = 0;
        overWrittenCounter = 0;
        saveCounter = 0;
        for (int i = 0; i < ARRAY_SIZE; i += 2) {
            transpositionWrite.setHash(i);
            transpositionWrite.setDraft(random.nextInt());
            transpositionWrite.setMove((short) random.nextInt());
            transpositionWrite.setValue(random.nextInt());
            transpositionWrite.setBound(BOUNDS[random.nextInt(BOUNDS_SIZE)]);
            tTable.save(transpositionWrite);
        }
    }

    @Setup(Level.Invocation)
    public void takeBackup() {
        hashToWrite = random.nextLong((ARRAY_SIZE * 100) / (6 * 2));

        tTable.load(hashToWrite % ARRAY_SIZE, transpositionRead);

        transpositionWrite.setHash(hashToWrite);
        transpositionWrite.setDraft(random.nextInt());
        transpositionWrite.setMove((short) random.nextInt());
        transpositionWrite.setValue(random.nextInt());
        transpositionWrite.setBound(BOUNDS[random.nextInt(BOUNDS_SIZE)]);
    }

    @Benchmark
    public void benchmarkSaves(Blackhole blackhole) {
        TTable.SaveResult result = tTable.save(transpositionWrite);

        switch (result) {
            case INSERTED -> insertedCounter++;
            case UPDATED -> updatedCounter++;
            case OVER_WRITTEN -> overWrittenCounter++;
        }
        saveCounter++;

        // Pass the result to the blackhole to prevent dead code elimination
        blackhole.consume(result);
    }

    @TearDown(Level.Invocation)
    public void restoreBackup() {
        tTable.save(transpositionRead);
    }

    @TearDown(Level.Iteration)
    public void teardown() {
        System.out.printf("SAVED: %d, INSERTED: %d (%d%%), UPDATED: %d (%d%%), OVER_WRITTEN: %d (%d%%) ",
                saveCounter,
                insertedCounter,
                insertedCounter * 100 / saveCounter,
                updatedCounter,
                updatedCounter * 100 / saveCounter,
                overWrittenCounter,
                overWrittenCounter * 100 / saveCounter
        );
    }
}