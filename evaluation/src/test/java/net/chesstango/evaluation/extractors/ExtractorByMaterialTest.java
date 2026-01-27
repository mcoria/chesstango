package net.chesstango.evaluation.extractors;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class ExtractorByMaterialTest {

    private ExtractorByMaterial extractorByMaterial;
    private Map<String, Integer> featuresMap;

    @BeforeEach
    public void setup() {
        extractorByMaterial = new ExtractorByMaterial();
        featuresMap = new HashMap<>();
    }

    @Test
    public void testExtractInitialPosition() {
        Game game = Game.from(FEN.START_POSITION);

        extractorByMaterial.extractFeatures(game, featuresMap);

        assertEquals(2, featuresMap.get(Piece.ROOK_WHITE.toString()));
        assertEquals(2, featuresMap.get(Piece.KNIGHT_WHITE.toString()));
        assertEquals(2, featuresMap.get(Piece.BISHOP_WHITE.toString()));
        assertEquals(1, featuresMap.get(Piece.QUEEN_WHITE.toString()));
        assertEquals(8, featuresMap.get(Piece.PAWN_WHITE.toString()));

        assertEquals(2, featuresMap.get(Piece.ROOK_BLACK.toString()));
        assertEquals(2, featuresMap.get(Piece.KNIGHT_BLACK.toString()));
        assertEquals(2, featuresMap.get(Piece.BISHOP_BLACK.toString()));
        assertEquals(1, featuresMap.get(Piece.QUEEN_BLACK.toString()));
        assertEquals(8, featuresMap.get(Piece.PAWN_BLACK.toString()));
    }
}
