package net.chesstango.tools.tuning;

import io.jenetics.EliteSelector;
import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessByEpdSearch;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessFunction;
import net.chesstango.tools.tuning.geneticproviders.GPEvaluatorSEandImp03;
import net.chesstango.tools.tuning.geneticproviders.GeneticProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mauricio Coria
 */
public class EvalTuningJeneticsMain extends EvalTuningAbstract {
    private static final Logger logger = LoggerFactory.getLogger(EvalTuningJeneticsMain.class);
    private static final int POPULATION_SIZE = 10;
    private static final int GENERATION_LIMIT = 3;

    public static void main(String[] args) {
        //GeneticProvider geneticProvider = new GeneticProvider2FactorsGenes();
        GeneticProvider geneticProvider = new GPEvaluatorSEandImp03();
        //GeneticProvider geneticProvider = new GeneticProviderNIntChromosomes(10);

        //FitnessFunction fitnessFunction = new FitnessByMatch();
        FitnessFunction fitnessFunction = new FitnessByEpdSearch();
        //FitnessFunction fitnessFunction = new FitnessByLeastSquare();

        EvalTuningJeneticsMain main = new EvalTuningJeneticsMain(fitnessFunction, geneticProvider);

        main.doWork();
    }

    private final GeneticProvider geneticProvider;

    public EvalTuningJeneticsMain(FitnessFunction fitnessFn, GeneticProvider geneticProvider) {
        super(fitnessFn);
        this.geneticProvider = geneticProvider;
    }

    @Override
    public void doWork() {
        fitnessFn.start();

        Engine<IntegerGene, Long> engine = Engine
                .builder(this::fitness, geneticProvider.getGenotypeFactory())
                .selector(new EliteSelector<>(POPULATION_SIZE / 5))
                //.constraint(geneticProvider.getPhenotypeConstraint())
                .populationSize(POPULATION_SIZE)
                .build();

        //EvolutionStart<IntegerGene, Long> start = geneticProvider.getEvolutionStart(POPULATION_SIZE);

        Phenotype<IntegerGene, Long> result = engine
                //.stream(start)
                .stream()
                .limit(GENERATION_LIMIT)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("El mejor fenotipo encontrado = " + result.fitness());
        System.out.println("Y su genotipo = " + geneticProvider.createGameEvaluatorFactors(result.genotype()));

        dumpMemory();

        fitnessFn.stop();
    }


    private long fitness(Genotype<IntegerGene> genotype) {
        return fitness(geneticProvider.createGameEvaluatorFactors(genotype));
    }
}
