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

    private TTable tTable;
    private TranspositionEntry transposition;
    private Random random;

    private static final TranspositionBound[] BOUNDS = TranspositionBound.values();
    private static final int BOUNDS_SIZE = BOUNDS.length;

    @Setup(Level.Trial)
    public void setUp() {
        random = new Random();
        tTable = new TTableArrayPrimitives();
        //tTable = new TTableMap();
        transposition = new TranspositionEntry();
    }

    @Setup(Level.Iteration)
    public void clearTranspositionTable() {
        tTable.clear();
    }

    @Setup(Level.Invocation)
    public void setUpTransposition() {
        transposition.setHash(random.nextLong());
        transposition.setDraft(random.nextInt());
        transposition.setMove((short) random.nextInt());
        transposition.setValue(random.nextInt());
        transposition.setBound(BOUNDS[random.nextInt(BOUNDS_SIZE)]);
    }

    @Benchmark
    public void benchmarkSaves(Blackhole blackhole) {
        TTable.SaveResult result = tTable.save(transposition);

        // Pass the result to the blackhole to prevent dead code elimination
        blackhole.consume(result);
    }
}