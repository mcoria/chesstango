package net.chesstango.ai.imp.smart.evaluation.tunning;

import io.jenetics.EliteSelector;
import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import net.chesstango.ai.imp.smart.IterativeDeeping;
import net.chesstango.ai.imp.smart.MinMaxPruning;
import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
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

/**
 * @author Mauricio Coria
 */
public class EvaluationMain{
    private static int POPULATION_SIZE = 5;
    private static int LIMIT = 50;
    private static ExecutorService executor;
    private static ObjectPool<EngineController> pool;
    private final GeneticProvider geneticProvider;
    private final List<String> fenList;
    private final Map<String, Long> gameMemory;

    public EvaluationMain(List<String> fenList, GeneticProvider geneticProvider) {
        this.fenList = fenList;
        this.geneticProvider = geneticProvider;
        this.gameMemory = new HashMap<>();
    }

    public static void main(String[] args) {
        executor = Executors.newFixedThreadPool(4);
        pool = new GenericObjectPool<>(new EngineControllerProxyFactory());
        new EvaluationMain(Arrays.asList(FENDecoder.INITIAL_FEN,
                "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20",
                "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7",
                "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5"),
                new GeneticProviderImp01()).findGenotype();
        pool.close();
        executor.shutdown();
    }

    private void findGenotype() {

        Engine<IntegerGene, Long> engine = Engine.builder(this::fitness, geneticProvider.getGenotypeFactory())
                .selector(new EliteSelector<>(3))
                .constraint(geneticProvider.getPhenotypeConstraint())
                .populationSize(POPULATION_SIZE)
                .executor(executor)
                .build();


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

            EngineController engineTango = createTango(genotype);

            List<Match.MathResult> matchResult = fitnessEval(engineTango);

            quitTango(engineTango);

            points += matchResult.stream().filter(result -> result.getEngineWhite() == engineTango).mapToLong(Match.MathResult::getPoints).sum();

            points -= matchResult.stream().filter(result -> result.getEngineBlack() == engineTango).mapToLong(Match.MathResult::getPoints).sum();

            gameMemory.put(keyGenes, points);

            geneticProvider.printGeneAndPoints(genotype, points);

        } else {
            points = previousGamePoints;
        }

        return points;
    }

    public EngineController createTango(Genotype<IntegerGene> genotype) {
        GameEvaluator gameEvaluator = geneticProvider.createGameEvaluator(genotype);

        EngineController tango = new EngineControllerImp(new EngineTango(new IterativeDeeping(new MinMaxPruning( gameEvaluator ))).disableAsync());

        tango.send_CmdUci();
        tango.send_CmdIsReady();

        return tango;
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
