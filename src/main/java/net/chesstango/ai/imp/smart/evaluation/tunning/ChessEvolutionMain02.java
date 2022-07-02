package net.chesstango.ai.imp.smart.evaluation.tunning;

import io.jenetics.*;
import io.jenetics.engine.Constraint;
import io.jenetics.util.Factory;
import io.jenetics.util.IntRange;
import net.chesstango.ai.imp.smart.IterativeDeeping;
import net.chesstango.ai.imp.smart.MinMaxPruning;
import net.chesstango.ai.imp.smart.evaluation.imp.GameEvaluatorImp02;
import net.chesstango.uci.arbiter.EngineController;
import net.chesstango.uci.arbiter.imp.EngineControllerImp;
import net.chesstango.uci.engine.imp.EngineTango;

/**
 * @author Mauricio Coria
 */
public class ChessEvolutionMain02 implements GeneticProvider  {
    private static int CONSTRAINT_MAX_VALUE = 1000;

    private final IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

    @Override
    public Factory getGenotypeFactory() {
        return Genotype.of(IntegerChromosome.of(geneRange, 2));
    }

    @Override
    public String getKeyGenesString(Genotype<IntegerGene> genotype) {
        Chromosome<IntegerGene> chromo1 = genotype.get(0);

        IntegerGene gene1 = chromo1.get(0);
        int gene1Value = gene1.intValue();

        IntegerGene gene2 = chromo1.get(1);
        int gene2Value = gene2.intValue();

        return gene1Value + "|" + gene2Value;
    }

    @Override
    public EngineController createTango(Genotype<IntegerGene> genotype) {
        Chromosome<IntegerGene> chromo1 = genotype.get(0);

        IntegerGene gene1 = chromo1.get(0);
        int gene1Value = gene1.intValue();

        IntegerGene gene2 = chromo1.get(1);
        int gene2Value = gene2.intValue();

        EngineController tango = new EngineControllerImp(new EngineTango(new IterativeDeeping(new MinMaxPruning(new GameEvaluatorImp02(gene1Value, gene2Value)))).disableAsync());
        tango.send_CmdUci();
        tango.send_CmdIsReady();
        return tango;
    }

    @Override
    public void printGeneAndPoints(Genotype<IntegerGene> genotype, long points) {
        Chromosome<IntegerGene> chromo1 = genotype.get(0);

        IntegerGene gene1 = chromo1.get(0);
        int gene1Value = gene1.intValue();

        IntegerGene gene2 = chromo1.get(1);
        int gene2Value = gene2.intValue();

        System.out.println("Evaluacion con gene1=[" + gene1Value + "] gene2=[" + gene2Value + "] ; puntos = [" + points + "]");
    }

    @Override
    public Constraint getPhenotypeConstraint() {
        return phenotypeConstraint;
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

            return (gene1Value +  gene2Value) % CONSTRAINT_MAX_VALUE == 0 ;
        }

        @Override
        public Phenotype<IntegerGene, Long> repair(Phenotype<IntegerGene, Long> phenotype, long generation) {
            Genotype<IntegerGene> genotype = phenotype.genotype();

            Chromosome<IntegerGene> chromo1 = genotype.get(0);

            IntegerGene gene1 = chromo1.get(0);
            int gene1Value = gene1.intValue() % CONSTRAINT_MAX_VALUE;

            int gene2Value = CONSTRAINT_MAX_VALUE - gene1Value;


            IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);
            Phenotype<IntegerGene, Long> newPhenotype = Phenotype.of(Genotype.of(IntegerChromosome.of(
                    IntegerGene.of(gene1Value, geneRange ),
                    IntegerGene.of(gene2Value, geneRange )
            )), generation);

            return newPhenotype;
        }
    };
}