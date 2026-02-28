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
@Measurement(iterations = 20, time = 5000, timeUnit = TimeUnit.MILLISECONDS)
public class TTableArrayLoad {

    private static final int ARRAY_SIZE = 1024 * 512;

    private TTable tTable;
    private TranspositionEntry transposition;
    private Random random;

    private static final TranspositionBound[] BOUNDS = TranspositionBound.values();
    private static final int BOUNDS_SIZE = BOUNDS.length;
    private long loadCalls;
    private long founds;

    @Setup(Level.Trial)
    public void setUp() {
        random = new Random();
        tTable = new TTableArrayPrimitives();
        //tTable = new TTableArrayObj();
        //tTable = new TTableMap();
        transposition = new TranspositionEntry();
    }

    /**
     * La tabla se encuentra con datos al 50%
     */
    @Setup(Level.Iteration)
    public void fillTranspositionTable() {
        loadCalls = 0;
        founds = 0;
        tTable.clear();
        for (int i = 0; i < ARRAY_SIZE; i += 2) {
            transposition.setHash(i);
            transposition.setDraft(random.nextInt());
            transposition.setMove((short) random.nextInt());
            transposition.setValue(random.nextInt());
            transposition.setBound(BOUNDS[random.nextInt(BOUNDS_SIZE)]);
            tTable.save(transposition);
        }
    }

    @Benchmark
    public void benchmarkLoad_6pct(Blackhole blackhole) {
        boolean found = tTable.load(random.nextLong((ARRAY_SIZE * 100) / (6 * 2)), transposition);

        loadCalls++;
        if (found) {
            founds++;
        }

        // Pass the result to the blackhole to prevent dead code elimination
        blackhole.consume(found);
    }

    @Benchmark
    public void benchmarkLoad_9pct(Blackhole blackhole) {
        boolean found = tTable.load(random.nextLong((ARRAY_SIZE * 100) / (9 * 2)), transposition);

        loadCalls++;
        if (found) {
            founds++;
        }

        // Pass the result to the blackhole to prevent dead code elimination
        blackhole.consume(found);
    }

    @TearDown(Level.Iteration)
    public void teardown() {
        System.out.printf("Load calls: %d, founds: %d (%d%%). ", loadCalls, founds, founds * 100 / loadCalls);
    }
}