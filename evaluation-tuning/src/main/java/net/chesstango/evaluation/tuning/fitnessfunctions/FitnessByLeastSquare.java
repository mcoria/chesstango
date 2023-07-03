package net.chesstango.evaluation.tuning.fitnessfunctions;

import io.jenetics.Chromosome;
import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import net.chesstango.evaluation.GameEvaluator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Mauricio Coria
 */
public class FitnessByLeastSquare implements FitnessFunction {

    private List<FeaturesValues> featuresValuesList;

    @Override
    public long fitness(Genotype<IntegerGene> genotype) {
        Chromosome<IntegerGene> chromosome = genotype.chromosome();

        IntegerChromosome intChromosome = chromosome.as(IntegerChromosome.class);

        int[] factors = intChromosome.toArray();

        long totalError = 0;
        for (FeaturesValues featuresValues : featuresValuesList) {
            totalError += Math.abs(featuresValues.error(factors));
        }

        return -totalError;
    }

    @Override
    public void start() {
        featuresValuesList = readFeaturesFromFile();
    }

    private List<FeaturesValues> readFeaturesFromFile() {
        List<FeaturesValues> records = new LinkedList<>();
        try (Scanner scanner = new Scanner(new File("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\Texel\\mate-all-features.epd"))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    private FeaturesValues getRecordFromLine(String line) {
        int[] features = new int[FeaturesValues.FEATURES_COUNT];
        GameResult expectedResult = null;
        int featuresCount = 0;
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(";");
            rowScanner.next(); //Skip FEN
            while (rowScanner.hasNext() && featuresCount < FeaturesValues.FEATURES_COUNT) {
                features[featuresCount] = Integer.parseInt(rowScanner.next());
                featuresCount++;
            }
            String expectedResultStr = rowScanner.next();
            if ("DRAW".equals(expectedResultStr)) {
                expectedResult = GameResult.DRAW;
            } else if ("WHITE_WON".equals(expectedResultStr)) {
                expectedResult = GameResult.WHITE_WINS;
            } else if ("BLACK_WON".equals(expectedResultStr)) {
                expectedResult = GameResult.BLACK_WINS;
            } else {
                throw new RuntimeException("Unknown result " + expectedResultStr);
            }
        }

        FeaturesValues featuresValues = new FeaturesValues();
        featuresValues.features = features;
        featuresValues.expectedResult = expectedResult;

        return featuresValues;
    }

    @Override
    public void stop() {

    }

    enum GameResult {
        WHITE_WINS,
        BLACK_WINS,
        DRAW
    }

    protected static class FeaturesValues {
        static final int FEATURES_COUNT = 10;
        int features[];
        GameResult expectedResult;

        public int error(int[] factors) {
            if (factors.length != FEATURES_COUNT) {
                throw new RuntimeException(String.format("Expected %d features", factors.length));
            }

            int evaluation = 0;

            for (int i = 0; i < FEATURES_COUNT; i++) {
                evaluation += factors[i] * features[i];
            }

            if (GameResult.WHITE_WINS.equals(expectedResult)) {
                return GameEvaluator.WHITE_WON - evaluation;
            } else if (GameResult.BLACK_WINS.equals(expectedResult)) {
                return GameEvaluator.BLACK_WON - evaluation;
            }

            return evaluation;
        }
    }
}
