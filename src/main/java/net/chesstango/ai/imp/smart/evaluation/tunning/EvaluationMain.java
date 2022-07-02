package net.chesstango.ai.imp.smart.evaluation.tunning;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.IntRange;
import net.chesstango.ai.imp.smart.IterativeDeeping;
import net.chesstango.ai.imp.smart.MinMaxPruning;
import net.chesstango.ai.imp.smart.evaluation.imp.GameEvaluatorImp01;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.arbiter.EngineController;
import net.chesstango.uci.arbiter.Match;
import net.chesstango.uci.arbiter.imp.EngineControllerImp;
import net.chesstango.uci.engine.imp.EngineTango;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class EvaluationMain{
    private static int CONSTRAINT_MAX_VALUE = 10000;
    private static int POPULATION_SIZE = 20;
    private static int LIMIT = 1000;
    private static ObjectPool<EngineController> pool;
    private final GeneticProvider geneticProvider;
    private static ExecutorService executor;
    private List<String> fenList;
    private Map<String, Long> gameMemory = new HashMap<>();

    public EvaluationMain(List<String> fenList, GeneticProvider geneticProvider) {
        this.fenList = fenList;
        this.geneticProvider = geneticProvider;
    }

    public static void main(String[] args) {
        executor = Executors.newFixedThreadPool(4);
        pool = new GenericObjectPool<>(new EngineControllerProxyFactory());
        new EvaluationMain(Arrays.asList(FENDecoder.INITIAL_FEN,
                "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20", "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7",
                "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5"), new ChessEvolutionMain01() )
                .findGenotype();
        pool.close();
        executor.shutdown();
    }

    private void findGenotype() {
        // 1) Define the genotype (factory) suitable for the problem.
        IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

        // 3) Create the execution environment.
        Engine<IntegerGene, Long> engine = Engine.builder(this::fitness, geneticProvider.getGenotypeFactory())
                .selector(new EliteSelector<>(4))
                .constraint(geneticProvider.getPhenotypeConstraint())
                .populationSize(POPULATION_SIZE)
                .executor(executor)
                .build();

        // 4) Start the execution (evolution) and collect the result.
        Phenotype<IntegerGene, Long> result = engine
                .stream()
                .limit(LIMIT)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("El mejor fenotipo encontrado = " + result.fitness());
        System.out.println("Y su genotipo = " + result.genotype());

        Set<Map.Entry<String, Long>> entrySet = gameMemory.entrySet();
        List<Map.Entry<String, Long>> entryList = entrySet.stream().collect(Collectors.toList());
        Collections.sort(entryList, Comparator.comparing(Map.Entry::getValue));
        entryList.stream().forEach( entry -> {
            System.out.println("key = [" + entry.getKey() + "]; value=[" + entry.getValue() + "]");
        });

    }

    public long fitness(Genotype<IntegerGene> genotype){
        long points = 0;

        String keyGenes =  geneticProvider.getKeyGenesString(genotype);

        Long previousGamePoints = gameMemory.get(keyGenes);

        if (previousGamePoints == null) {

            EngineController engineZonda = geneticProvider.createTango(genotype);

            List<Match.MathResult> matchResult = fitnessEval(engineZonda);

            quitTango(engineZonda);

            points += matchResult.stream().filter(result -> result.getEngineWhite() == engineZonda).mapToLong(Match.MathResult::getPoints).sum();

            points -= matchResult.stream().filter(result -> result.getEngineBlack() == engineZonda).mapToLong(Match.MathResult::getPoints).sum();

            gameMemory.put(keyGenes, points);

            geneticProvider.printGeneAndPoints(genotype, points);

        } else {
            points = previousGamePoints;
        }

        return points;
    }


    private List<Match.MathResult> fitnessEval(EngineController engineTango) {
        List<Match.MathResult> matchResult = null;
        try {

            EngineController engineProxy = pool.borrowObject();

            Match match = new Match(engineProxy, engineTango, 1);

            matchResult = match.play(fenList);

            pool.returnObject(engineProxy);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return matchResult;
    }

    private void quitTango(EngineController tango) {
        tango.send_CmdQuit();
    }


}
