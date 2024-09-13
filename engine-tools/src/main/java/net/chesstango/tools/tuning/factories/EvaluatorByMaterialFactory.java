package net.chesstango.tools.tuning.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
@Getter
public class EvaluatorByMaterialFactory implements GameEvaluatorFactory {
    private static final Logger logger = LoggerFactory.getLogger(EvaluatorByMaterialFactory.class);

    private final String key;

    private final int[] pieceValues;

    public EvaluatorByMaterialFactory(int[] pieceValues) {

        this.pieceValues = pieceValues;

        int computedKey = Arrays.hashCode(pieceValues);

        //this.key = String.format("%s", Long.toUnsignedString(computedKey, 16));
        this.key = String.format("%08x", computedKey);
    }

    @Override
    public Evaluator createGameEvaluator() {
        return new EvaluatorByMaterial(pieceValues[0], pieceValues[1], pieceValues[2], pieceValues[3], pieceValues[4]);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getRepresentation() {
        EvaluatorByMaterial.Tables obj = new EvaluatorByMaterial.Tables(key,
                pieceValues[0], pieceValues[1], pieceValues[2], pieceValues[3], pieceValues[4]);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
