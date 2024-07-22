package net.chesstango.tools.tuning.geneticproviders;

import io.jenetics.*;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import net.chesstango.evaluation.evaluators.EvaluatorImp06;
import net.chesstango.tools.tuning.factories.EvaluatorImp06Factory;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class GPEvaluatorImp06 implements GeneticProvider {
    private static final int CONSTRAINT_MAX_VALUE = 1000;

    private static final IntRange weighRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);
    private static final IntRange tableRange = IntRange.of(-CONSTRAINT_MAX_VALUE, CONSTRAINT_MAX_VALUE);

    @Override
    public Factory<Genotype<IntegerGene>> getGenotypeFactory() {
        return Genotype.of(
                IntegerChromosome.of(weighRange, 3),  // weighs
                IntegerChromosome.of(tableRange, 64), // mgPawnTbl
                IntegerChromosome.of(tableRange, 64), // mgKnightTbl
                IntegerChromosome.of(tableRange, 64), // mgBishopTbl
                IntegerChromosome.of(tableRange, 64), // mgRookTbl
                IntegerChromosome.of(tableRange, 64), // mgQueenTbl
                IntegerChromosome.of(tableRange, 64), // mgKingTbl
                IntegerChromosome.of(tableRange, 64), // egPawnTbl
                IntegerChromosome.of(tableRange, 64), // egKnightTbl
                IntegerChromosome.of(tableRange, 64), // egBishopTbl
                IntegerChromosome.of(tableRange, 64), // egRookTbl
                IntegerChromosome.of(tableRange, 64), // egQueenTbl
                IntegerChromosome.of(tableRange, 64)  // egKingTbl
        );
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {

        List<Phenotype<IntegerGene, Long>> phenoList = List.of(
                createPhenotype("{\"id\":\"81c517b4\",\"weighs\":[647,760,222],\"mgPawnTbl\":[734,-146,-265,-853,816,257,-209,809,203,761,895,-327,675,936,508,335,-45,-272,304,306,-739,-527,-727,775,431,-162,-557,-179,138,-263,-929,12,727,-468,-1000,-377,-904,494,-361,-948,-593,-858,947,964,-199,809,-418,595,-337,238,-841,-214,-613,-679,176,-637,13,2,-921,110,91,928,-317,157],\"mgKnightTbl\":[-838,404,266,566,413,853,905,152,829,913,578,-379,-216,83,-739,534,173,-834,591,-358,-24,-475,-545,-903,-430,516,409,266,351,-798,-967,-445,526,-150,283,789,-859,232,-931,53,-916,-485,-977,-420,-322,736,-976,422,-102,319,-773,222,768,-979,-73,162,246,-154,-110,214,-695,957,-646,965],\"mgBishopTbl\":[-170,-533,426,861,-673,720,-520,575,119,539,-339,900,683,525,798,748,750,500,-894,-461,-42,-13,950,606,960,75,-261,-334,-377,-452,-780,983,418,-98,-571,370,-96,-179,612,-121,-927,555,139,-742,218,-145,-866,-240,924,-87,-343,-320,3,423,600,57,-217,267,300,977,42,633,418,-543],\"mgRookTbl\":[-420,-728,142,920,969,227,-554,164,476,157,-721,-611,-652,726,187,538,-814,351,255,-529,283,-521,573,-704,-605,860,288,-347,-114,28,767,440,-995,-752,-836,757,747,955,789,34,323,-53,807,-304,789,500,-870,636,231,54,-606,992,491,204,956,-863,-948,625,-8,-31,-3,995,132,-657],\"mgQueenTbl\":[-643,-468,717,-765,-894,-20,245,151,993,-218,-343,148,-355,938,334,709,136,-353,-780,868,898,550,22,344,-854,-324,-803,537,876,-335,-549,-230,367,484,-224,918,-784,514,124,226,475,-611,-225,-793,509,102,877,-116,-573,-571,-712,281,402,896,-537,-8,583,-253,-534,278,-284,-87,458,-76],\"mgKingTbl\":[-124,-613,-843,546,436,-816,-777,925,742,114,448,502,398,-625,-264,-504,-883,-128,233,901,-661,-441,-100,442,906,876,798,836,575,807,-305,763,-574,66,-971,-365,-415,546,-938,351,-108,-950,665,-708,-830,-454,-419,-127,-826,-288,-526,-821,-47,452,225,152,714,174,-856,333,-607,-598,-195,-423],\"egPawnTbl\":[-598,938,-312,-269,-53,272,-669,-634,-114,8,-644,115,-574,597,159,372,902,-824,398,-526,160,-965,245,-596,-884,487,-997,-216,8,-393,188,-485,609,-477,-432,-433,-82,396,-26,-421,972,-562,-920,-509,-611,-301,990,70,-528,-75,115,-95,-852,-966,611,132,403,-369,-389,-65,931,269,787,355],\"egKnightTbl\":[-324,59,967,980,997,-337,859,406,-335,75,-117,174,-542,-385,-555,-693,335,-494,-803,-186,798,-140,-306,-765,241,912,-992,993,-7,88,264,-925,-421,627,-685,-766,-999,675,-938,156,322,402,860,-160,-744,629,968,49,-376,505,764,-204,-872,501,63,-693,74,911,186,458,-578,-146,727,394],\"egBishopTbl\":[-914,419,-526,-662,-495,383,-168,-165,256,-765,967,437,-703,-528,-107,-845,-382,51,988,680,477,-307,-167,812,-437,857,-9,-71,751,371,290,539,788,-159,632,-957,-853,-712,-373,353,57,67,-506,275,-956,-94,8,309,-737,533,881,418,-405,960,-966,598,-171,826,899,125,-774,297,35,166],\"egRookTbl\":[-2,997,-741,30,-177,-169,-697,-761,589,302,-467,810,729,-238,-890,-719,769,648,892,676,-71,-969,-467,-466,506,606,-543,856,-948,-467,-413,329,551,-470,160,-15,328,-510,363,-812,925,189,-520,817,-189,-264,4,918,125,-432,-672,-872,959,817,-727,-636,-968,-275,-554,-22,-880,-932,115,970],\"egQueenTbl\":[823,529,-460,-755,79,-862,-943,-569,-220,-185,-578,-77,-803,-945,20,394,257,-157,419,-943,-416,-292,306,-628,-142,511,495,930,874,-604,459,-169,376,932,417,607,842,-547,-132,-903,-818,-709,65,367,503,648,-619,774,-393,-805,-156,-322,626,900,690,-885,-453,298,263,170,622,-85,564,-393],\"egKingTbl\":[739,91,823,-804,-359,61,895,985,849,-104,-69,-556,-323,329,-244,824,-94,436,-581,758,772,-517,977,-254,-851,810,-988,166,265,32,354,642,968,-900,-70,748,654,-442,963,-227,-99,-234,-516,235,45,-360,56,-768,-962,-986,-272,34,965,-407,-84,754,696,-553,-198,-506,458,-475,-37,588]}")
        );

        ISeq<Phenotype<IntegerGene, Long>> population = ISeq.of(phenoList);

        return EvolutionStart.of(population, 1);
    }


    @Override
    public GameEvaluatorFactory createGameEvaluatorFactors(Genotype<IntegerGene> genotype) {
        int[] weighs = chromosomeToArray(genotype.get(0));

        int[] mgPawnTbl = chromosomeToArray(genotype.get(1));
        int[] mgKnightTbl = chromosomeToArray(genotype.get(2));
        int[] mgBishopTbl = chromosomeToArray(genotype.get(3));
        int[] mgRookTbl = chromosomeToArray(genotype.get(4));
        int[] mgQueenTbl = chromosomeToArray(genotype.get(5));
        int[] mgKingTbl = chromosomeToArray(genotype.get(6));

        int[] egPawnTbl = chromosomeToArray(genotype.get(7));
        int[] egKnightTbl = chromosomeToArray(genotype.get(8));
        int[] egBishopTbl = chromosomeToArray(genotype.get(9));
        int[] egRookTbl = chromosomeToArray(genotype.get(10));
        int[] egQueenTbl = chromosomeToArray(genotype.get(11));
        int[] egKingTbl = chromosomeToArray(genotype.get(12));

        return new EvaluatorImp06Factory(weighs,
                mgPawnTbl, mgKnightTbl, mgBishopTbl, mgRookTbl, mgQueenTbl, mgKingTbl,
                egPawnTbl, egKnightTbl, egBishopTbl, egRookTbl, egQueenTbl, egKingTbl);
    }

    private int[] chromosomeToArray(Chromosome<IntegerGene> chromosome) {
        IntegerChromosome intChromosome = chromosome.as(IntegerChromosome.class);
        return intChromosome.toArray();
    }

    /**
     * Este es el proceso inverso para cargar una poblacion inicial dado los factores
     *
     * @return
     */
    private Phenotype<IntegerGene, Long> createPhenotype(String dump) {
        EvaluatorImp06.Tables tables = EvaluatorImp06.readValues(dump);

        List<IntegerGene> weighs = Arrays.stream(tables.weighs()).mapToObj(val -> IntegerGene.of(val, weighRange)).toList();
        List<IntegerGene> mgPawnTbl = Arrays.stream(tables.mgPawnTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgKnightTbl = Arrays.stream(tables.mgKnightTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgBishopTbl = Arrays.stream(tables.mgBishopTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgRookTbl = Arrays.stream(tables.mgRookTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgQueenTbl = Arrays.stream(tables.mgQueenTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgKingTbl = Arrays.stream(tables.mgKingTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egPawnTbl = Arrays.stream(tables.egPawnTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egKnightTbl = Arrays.stream(tables.egKnightTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egBishopTbl = Arrays.stream(tables.egBishopTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egRookTbl = Arrays.stream(tables.egRookTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egQueenTbl = Arrays.stream(tables.egQueenTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egKingTbl = Arrays.stream(tables.egKingTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();

        return Phenotype.of(
                Genotype.of(
                        IntegerChromosome.of(weighs),
                        IntegerChromosome.of(mgPawnTbl),
                        IntegerChromosome.of(mgKnightTbl),
                        IntegerChromosome.of(mgBishopTbl),
                        IntegerChromosome.of(mgRookTbl),
                        IntegerChromosome.of(mgQueenTbl),
                        IntegerChromosome.of(mgKingTbl),
                        IntegerChromosome.of(egPawnTbl),
                        IntegerChromosome.of(egKnightTbl),
                        IntegerChromosome.of(egBishopTbl),
                        IntegerChromosome.of(egRookTbl),
                        IntegerChromosome.of(egQueenTbl),
                        IntegerChromosome.of(egKingTbl)
                ), 1);
    }


}