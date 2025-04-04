package net.chesstango.board.representations.epd;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.LANDecoder;
import net.chesstango.board.representations.move.SANDecoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * This class reads Extended Position Description files.
 *
 * @author Mauricio Coria
 */
public class EPDDecoder {
    /**
     * Decode line components
     */
    private static final Pattern edpLinePattern = Pattern.compile("(?<fen>.*/.*/.*/.*/.*\\s+[wb]\\s+([KQkq]{1,4}|-)\\s+(\\w\\d|-))\\s+" +
            "(\\s*bm\\s+(?<bestmoves>[^;]*);" +
            "|\\s*am\\s+(?<avoidmoves>[^;]*);" +
            "|\\s*sm\\s+(?<suppliedmove>[^;]*);" +
            "|\\s*c0\\s+\"(?<comment0>[^\"]+)\";" +
            "|\\s*c1\\s+\"(?<comment1>[^\"]+)\";" +
            "|\\s*c2\\s+\"(?<comment2>[^\"]+)\";" +
            "|\\s*c3\\s+\"(?<comment3>[^\"]+)\";" +
            "|\\s*c4\\s+\"(?<comment4>[^\"]+)\";" +
            "|\\s*c5\\s+\"(?<comment5>[^\"]+)\";" +
            "|\\s*c6\\s+\"(?<comment6>[^\"]+)\";" +
            "|\\s*id\\s+\"(?<id>[^\"]+)\";" +
            "|[^;]+;)*"
    );

    public Stream<EPD> readEdpFile(String filename) {
        return readEdpFile(Paths.get(filename));

    }

    public Stream<EPD> readEdpFile(Path filePath) {
        if (!Files.exists(filePath)) {
            System.err.printf("file not found: %s\n", filePath.getFileName());
            throw new RuntimeException(String.format("file not found: %s", filePath.getFileName()));
        }

        System.out.println("Reading suite " + filePath);

        try (InputStream in = new FileInputStream(filePath.toFile())) {
            return readEdpInputStream(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Stream<EPD> readEdpInputStream(InputStream in) throws IOException {
        Stream.Builder<EPD> epdEntryStreamBuilder = Stream.builder();
        try (InputStreamReader inputStreamReader = new InputStreamReader(in);
             BufferedReader rr = new BufferedReader(inputStreamReader)) {

            String line;

            while ((line = rr.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("#") && !line.isEmpty()) {
                    try {
                        EPD entry = readEdpLine(line);
                        epdEntryStreamBuilder.add(entry);
                    } catch (RuntimeException e) {
                        System.err.printf("Error decoding: %s\n", line);
                        e.printStackTrace(System.err);
                    }
                }
            }
            return epdEntryStreamBuilder.build();
        }
    }

    public EPD readEdpLine(String line) {
        EPD epd = new EPD();
        epd.setText(line);

        Matcher matcher = edpLinePattern.matcher(line);
        if (matcher.matches()) {
            epd.setFenWithoutClocks(FEN.of(matcher.group("fen")));
            if (matcher.group("suppliedmove") != null) {
                String suppliedMove = matcher.group("suppliedmove");
                epd.setSuppliedMoveStr(suppliedMove);
            }
            if (matcher.group("bestmoves") != null) {
                String bestMovesString = matcher.group("bestmoves");
                epd.setBestMovesStr(bestMovesString);
            }
            if (matcher.group("avoidmoves") != null) {
                String avoidMovesString = matcher.group("avoidmoves");
                epd.setAvoidMovesStr(avoidMovesString);
            }
            if (matcher.group("id") != null) {
                epd.setId(matcher.group("id"));
            }
            if (matcher.group("comment0") != null) {
                String comment0 = matcher.group("comment0");
                epd.setC0(comment0);
            }
            if (matcher.group("comment1") != null) {
                String comment1 = matcher.group("comment1");
                epd.setC1(comment1);
            }
            if (matcher.group("comment2") != null) {
                String comment2 = matcher.group("comment2");
                epd.setC2(comment2);
            }
            if (matcher.group("comment3") != null) {
                String comment3 = matcher.group("comment3");
                epd.setC3(comment3);
            }
            if (matcher.group("comment4") != null) {
                String comment4 = matcher.group("comment4");
                epd.setC4(comment4);
            }
            if (matcher.group("comment5") != null) {
                String comment5 = matcher.group("comment5");
                epd.setC5(comment5);
            }
            if (matcher.group("comment6") != null) {
                String comment6 = matcher.group("comment6");
                epd.setC6(comment6);
            }
        }

        return epd;
    }

}
