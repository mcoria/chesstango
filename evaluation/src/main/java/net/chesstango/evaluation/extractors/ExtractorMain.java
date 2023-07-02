package net.chesstango.evaluation.extractors;

import net.chesstango.board.Piece;
import net.chesstango.board.representations.EDPReader;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.GameFeatures;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author Mauricio Coria
 */
public class ExtractorMain {
    public static void main(String[] args) {
        new ExtractorMain().extract();
    }

    private void extract() {
        List<GameFeatures> featureExtractors = new ArrayList<>();
        featureExtractors.add(new ExtractorByMaterial());

        List<String> featuresList = new LinkedList<>();

        EDPReader epdReader = new EDPReader();
        //List<EDPReader.EDPEntry> edpEntryList = epdReader.readEdpFile("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-all.epd");
        List<EDPReader.EDPEntry> edpEntryList = epdReader.readEdpFile("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-all-w.epd");

        for (EDPReader.EDPEntry edpEntry : edpEntryList) {
            Map<String, Integer> features = new HashMap<>();
            for (GameFeatures extractor : featureExtractors) {
                extractor.extractFeatures(edpEntry.game, features);
            }
            featuresList.add(convertToLine(edpEntry.game.getChessPosition().toString(), features, "WHITE_WON"));
        }

        whiteToFile(featuresList);
    }

    private void whiteToFile(List<String> featuresList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("games-features.epd"))) {
            for (String featuresStr: featuresList) {
                writer.append(String.format("%s\n", featuresStr));
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertToLine(String fen, Map<String, Integer> featuresMap, String gameResult) {
        return String.format("%s;%d;%d;%d;%d;%d;%d;%d;%d;%d;%d;%s",
                fen,
                featuresMap.get(Piece.ROOK_WHITE.toString()),
                featuresMap.get(Piece.KNIGHT_WHITE.toString()),
                featuresMap.get(Piece.BISHOP_WHITE.toString()),
                featuresMap.get(Piece.QUEEN_WHITE.toString()),
                featuresMap.get(Piece.PAWN_WHITE.toString()),

                featuresMap.get(Piece.ROOK_BLACK.toString()),
                featuresMap.get(Piece.KNIGHT_BLACK.toString()),
                featuresMap.get(Piece.BISHOP_BLACK.toString()),
                featuresMap.get(Piece.QUEEN_BLACK.toString()),
                featuresMap.get(Piece.PAWN_BLACK.toString()),
                gameResult
        );
    }
}
