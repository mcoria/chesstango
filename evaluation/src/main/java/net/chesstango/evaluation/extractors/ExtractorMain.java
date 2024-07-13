package net.chesstango.evaluation.extractors;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.representations.epd.EpdEntry;
import net.chesstango.board.representations.epd.EpdReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameFeatures;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class ExtractorMain {
    public static void main(String[] args) {
        new ExtractorMain().extract();
    }

    private final List<GameFeatures> featureExtractors = new ArrayList<>();

    public ExtractorMain() {
        featureExtractors.add(new ExtractorByMaterial());
    }

    private void extract() {
        List<String> featuresList = new LinkedList<>();

        //extractFeaturesFromEDPFile(featuresList, "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-all-w.epd", "WHITE_WON");
        //extractFeaturesFromEDPFile(featuresList, "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-all-b.epd", "BLACK_WON");

        extractFeaturesFromEDPFile(featuresList, "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\Texel\\eval-tunner-white.txt", "WHITE_WON");
        extractFeaturesFromEDPFile(featuresList, "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\Texel\\eval-tunner-black.txt", "BLACK_WON");
        extractFeaturesFromEDPFile(featuresList, "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\Texel\\eval-tunner-draw.txt", "DRAW");

        whiteToFile(featuresList);
    }

    private void extractFeaturesFromEDPFile(final List<String> featuresList, final String fileName, final String gameResultString) {
        EpdReader epdReader = new EpdReader();

        Stream<EpdEntry> epdEntryStream = epdReader.readEdpFile(fileName);

        epdEntryStream.forEach(epdEntry -> {
            Map<String, Integer> features = new HashMap<>();
            for (GameFeatures extractor : featureExtractors) {
                Game game = FENDecoder.loadGame(epdEntry.fen);
                extractor.extractFeatures(game, features);
            }
            Game game = FENDecoder.loadGame(epdEntry.fen);
            featuresList.add(convertToLine(game.getChessPosition().toString(), features, gameResultString));
        });
    }

    private void whiteToFile(List<String> featuresList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("eval-tunner-features.epd"))) {
            for (String featuresStr : featuresList) {
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
