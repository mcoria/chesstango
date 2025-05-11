package net.chesstango.board.representations.pgn;

import net.chesstango.board.representations.move.SANDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class PGNStringDecoder {

    private static final Pattern headerPattern = Pattern.compile("\\[(\\w*) \"(.*)\"\\]");

    public Stream<PGN> decodePGNs(InputStream inputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferReader = new BufferedReader(inputStreamReader)
        ) {
            return decodePGNs(bufferReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Stream<PGN> decodePGNs(BufferedReader bufferReader) throws IOException {
        Stream.Builder<PGN> pgnStreamBuilder = Stream.builder();

        PGN game;
        while ((game = decodePGN(bufferReader)) != null) {
            pgnStreamBuilder.add(game);
        }
        
        return pgnStreamBuilder.build();
    }

    public PGN decodePGN(BufferedReader bufferReader) throws IOException {
        PGN pgn = decodePGNHeaders(bufferReader);
        if (pgn == null) {
            return null;
        }
        pgn.setMoveList(decodePGNBody(bufferReader));
        return pgn;
    }

    protected PGN decodePGNHeaders(BufferedReader bufferReader) throws IOException {
        PGN result = new PGN();
        String line;
        while ((line = bufferReader.readLine()) != null) {
            if ("".equals(line.trim())) {
                break;
            }
            Matcher headerMather = headerPattern.matcher(line);
            if (headerMather.find()) {
                String headerName = headerMather.group(1).toUpperCase();
                String headerText = headerMather.group(2);
                switch (headerName) {
                    case "EVENT":
                        result.setEvent(headerText);
                        break;
                    case "SITE":
                        result.setSite(headerText);
                        break;
                    case "DATE":
                        result.setDate(headerText);
                        break;
                    case "ROUND":
                        result.setRound(headerText);
                        break;
                    case "WHITE":
                        result.setWhite(headerText);
                        break;
                    case "BLACK":
                        result.setBlack(headerText);
                        break;
                    case "FEN":
                        result.setFen(headerText);
                        break;
                    case "RESULT":
                        result.setResult(headerText);
                        break;
                }
            }
        }
        if (result.getEvent() == null) {
            return null;
        }
        return result;
    }

    protected List<String> decodePGNBody(BufferedReader bufferReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferReader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                break;
            }
            stringBuilder.append(line.trim());
            stringBuilder.append(" ");
        }

        return decodePGNBody(stringBuilder.toString());
    }

    protected List<String> decodePGNBody(String moveListStr) {
        List<String> result = new ArrayList<>();
        final Matcher matcher = SANDecoder.movePattern.matcher(moveListStr);
        while (matcher.find()) {
            String moveStr = matcher.group(0);
            result.add(moveStr);
        }
        return result;
    }

}
