package net.chesstango.ai.imp.smart.evaluation.tunning;

import io.jenetics.*;
import io.jenetics.engine.Constraint;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;
import io.jenetics.util.IntRange;
import net.chesstango.ai.imp.smart.IterativeDeeping;
import net.chesstango.ai.imp.smart.MinMaxPruning;
import net.chesstango.ai.imp.smart.evaluation.imp.GameEvaluatorImp;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.arbiter.EngineController;
import net.chesstango.uci.arbiter.Match;
import net.chesstango.uci.arbiter.imp.EngineControllerImp;
import net.chesstango.uci.engine.imp.EngineProxy;
import net.chesstango.uci.engine.imp.EngineTango;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class ChessEvolutionMain {
    private static int CONSTRAINT_MAX_VALUE = 10000;
    private static int POPULATION_SIZE = 20;
    private static int LIMIT = 1000;

    private static ObjectPool<EngineController> pool;
    private static ExecutorService executor;

    private List<String> fenList;
    private Map<String, Long> gameMemory = new HashMap<>();

    public ChessEvolutionMain(List<String> fenList) {
        this.fenList = fenList;
    }

    public static void main(String[] args) {
        executor = Executors.newFixedThreadPool(4);
        pool = new GenericObjectPool<>(new EngineControllerProxyFactory());
        new ChessEvolutionMain(Arrays.asList(FENDecoder.INITIAL_FEN, "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20", "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7", "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5")).findGenotype();
        pool.close();
        executor.shutdown();
    }

    private void findGenotype() {
        // 1) Define the genotype (factory) suitable for the problem.
        IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

        Factory<Genotype<IntegerGene>> genotypeFactory =
                Genotype.of(IntegerChromosome.of(geneRange, 3));

        // 3) Create the execution environment.
        Engine<IntegerGene, Long> engine = Engine.builder(this::fitness, genotypeFactory)
                .selector(new EliteSelector<>(4))
                .constraint(phenotypeConstraint)
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

    private long fitness(Genotype<IntegerGene> genotype){
        Chromosome<IntegerGene> chromo1 = genotype.get(0);

        IntegerGene gene1 = chromo1.get(0);
        int gene1Value = gene1.intValue();

        IntegerGene gene2 = chromo1.get(1);
        int gene2Value = gene2.intValue();

        IntegerGene gene3 = chromo1.get(2);
        int gene3Value = gene3.intValue();

        long points = 0;

        String keyGenes = gene1Value + "|" + gene2Value + "|" + gene3Value;
        Long previousGamePoints = gameMemory.get(keyGenes);

        if (previousGamePoints == null) {

            EngineController engineZonda = createTango(gene1Value, gene2Value, gene3Value);

            List<Match.MathResult> matchResult = fitnessEval(engineZonda);

            quitTango(engineZonda);

            points += matchResult.stream().filter(result -> result.getEngineWhite() == engineZonda).mapToLong(Match.MathResult::getPoints).sum();

            points -= matchResult.stream().filter(result -> result.getEngineBlack() == engineZonda).mapToLong(Match.MathResult::getPoints).sum();

            gameMemory.put(keyGenes, points);

            System.out.println("Evaluacion con gene1=[" + gene1Value + "] gene2=[" + gene2Value + "] gene3=[" + gene3Value + "] ; puntos = [" + points + "]");

        } else {
            points = previousGamePoints;
        }

        return points;
    }

    private List<Match.MathResult> fitnessEval(EngineController engineZonda) {
        List<Match.MathResult> matchResult = null;

        try {

            EngineController engineProxy = pool.borrowObject();

            Match match = new Match(engineProxy, engineZonda, 1);

            matchResult = match.play(fenList);

            pool.returnObject(engineProxy);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return matchResult;
    }

    private EngineController createTango(int gene1, int gene2, int gene3) {
        EngineController tango = new EngineControllerImp(new EngineTango(new IterativeDeeping(new MinMaxPruning(new GameEvaluatorImp(gene1, gene2, gene3)))).disableAsync());
        tango.send_CmdUci();
        tango.send_CmdIsReady();
        return tango;
    }

    private void quitTango(EngineController tango) {
        tango.send_CmdQuit();
    }

    protected Constraint<IntegerGene, Long> phenotypeConstraint = new Constraint<IntegerGene, Long>() {
        @Override
        public boolean test(Phenotype<IntegerGene, Long> phenotype) {
            Genotype<IntegerGene> genotype = phenotype.genotype();
            Chromosome<IntegerGene> chromo1 = genotype.get(0);

            IntegerGene gene1 = chromo1.get(0);
            int gene1Value = gene1.intValue();

            IntegerGene gene2 = chromo1.get(1);
            int gene2Value = gene2.intValue();

            IntegerGene gene3 = chromo1.get(2);
            int gene3Value = gene3.intValue();

            return (gene1Value +  gene2Value + gene3Value) % CONSTRAINT_MAX_VALUE == 0 ;
        }

        @Override
        public Phenotype<IntegerGene, Long> repair(Phenotype<IntegerGene, Long> phenotype, long generation) {
            Genotype<IntegerGene> genotype = phenotype.genotype();

            Chromosome<IntegerGene> chromo1 = genotype.get(0);

            IntegerGene gene1 = chromo1.get(0);
            int gene1Value = gene1.intValue() % CONSTRAINT_MAX_VALUE;

            IntegerGene gene2 = chromo1.get(1);
            int gene2Value = gene2.intValue() % (CONSTRAINT_MAX_VALUE - gene1Value);

            int gene3Value = CONSTRAINT_MAX_VALUE - gene2Value - gene1Value;

            IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);
            Phenotype<IntegerGene, Long> newPhenotype = Phenotype.of(Genotype.of(IntegerChromosome.of(
                    IntegerGene.of(gene1Value, geneRange ),
                    IntegerGene.of(gene2Value, geneRange ),
                    IntegerGene.of(gene3Value, geneRange )
            )), generation);

            return newPhenotype;
        }
    };

    private static class EngineControllerProxyFactory extends BasePooledObjectFactory<EngineController> {

        @Override
        public EngineController create() {
            EngineProxy coreEngineProxy = new EngineProxy();
            //coreEngineProxy.setLogging(true);
            EngineController engineProxy = new EngineControllerImp(coreEngineProxy);
            engineProxy.send_CmdUci();
            engineProxy.send_CmdIsReady();

            return engineProxy;
        }

        @Override
        public PooledObject<EngineController> wrap(EngineController engineController) {
            return new DefaultPooledObject<EngineController>(engineController);
        }

        @Override
        public void destroyObject(PooledObject<EngineController> pooledController) {
            pooledController.getObject().send_CmdQuit();
        }
    }
}