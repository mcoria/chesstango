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

/**
 * @author Mauricio Coria
 */
public class PGNDecoder {

    private static final Pattern headerPattern = Pattern.compile("\\[(\\w*) \"(.*)\"\\]");

    public List<PGN> decodeGames(InputStream inputStream) {

        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferReader = new BufferedReader(inputStreamReader);
        ) {
            return decodeGames(bufferReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<PGN> decodeGames(BufferedReader bufferReader) throws IOException {
        List<PGN> result = new ArrayList<>();
        PGN game;
        while ((game = decodeGame(bufferReader)) != null) {
            result.add(game);
        }
        return result;
    }

    public PGN decodeGame(BufferedReader bufferReader) throws IOException {
        PGN pgn = decodeHeader(bufferReader);
        if (pgn == null) {
            return null;
        }
        pgn.setMoveList(decodeMovesList(bufferReader));
        return pgn;
    }

    protected PGN decodeHeader(BufferedReader bufferReader) throws IOException {
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

    protected List<String> decodeMovesList(BufferedReader bufferReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferReader.readLine()) != null) {
            if ("".equals(line.trim())) {
                break;
            }
            stringBuilder.append(line.trim());
            stringBuilder.append(" ");
        }

        return decodeMovesList(stringBuilder.toString());
    }

    protected List<String> decodeMovesList(String moveListStr) {
        List<String> result = new ArrayList<>();
        final Matcher matcher = SANDecoder.movePattern.matcher(moveListStr);
        while (matcher.find()) {
            String moveStr = matcher.group(0);
            result.add(moveStr);
        }
        return result;
    }

}
