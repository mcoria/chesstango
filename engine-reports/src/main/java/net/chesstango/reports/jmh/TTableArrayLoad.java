package net.chesstango.reports.jmh;

import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TTableArray;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionBound;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;
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
public class TTableArrayLoad {

    private TTable tTable;
    private TranspositionEntry transposition;
    private Random random;
    private long hash;

    private static final TranspositionBound[] BOUNDS = TranspositionBound.values();
    private static final int BOUNDS_SIZE = BOUNDS.length;

    @Setup(Level.Trial)
    public void setUp() {
        random = new Random();
        tTable = new TTableArray();
        //tTable = new TTableMap();
        transposition = new TranspositionEntry();
    }

    @Setup(Level.Iteration)
    public void clearTranspositionTable() {
        tTable.clear();
        for (int i = 0; i < 1024 * 10; i++) {
            transposition.setHash(random.nextLong());
            transposition.setDraft(random.nextInt());
            transposition.setMove((short) random.nextInt());
            transposition.setValue(random.nextInt());
            transposition.setBound(BOUNDS[random.nextInt(BOUNDS_SIZE)]);
        }
    }

    @Setup(Level.Invocation)
    public void setUpTransposition() {
        hash = random.nextLong();
    }

    @Benchmark
    public void benchmarkLoad(Blackhole blackhole) {
        boolean found = tTable.load(hash, transposition);

        // Pass the result to the blackhole to prevent dead code elimination
        blackhole.consume(found);
    }
}