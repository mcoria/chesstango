package net.chesstango.board.representations.pgn;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNDecoder {

    private Pattern headerPattern = Pattern.compile("\\[(\\w*) \"(.*)\"\\]");

    private Pattern moveListPattern = Pattern.compile("\\d+.\\s?([^.]+)\\s", Pattern.MULTILINE);

    public List<PGNGame> decodeGames(BufferedReader bufferReader) throws IOException {
        List<PGNGame> result = new ArrayList<>();
        PGNGame game;
        while ((game = decodeGame(bufferReader)) != null) {
            result.add(game);
        }
        return result;
    }

    public PGNGame decodeGame(BufferedReader bufferReader) throws IOException {
        PGNGame.PGNHeader header = decodeHeader(bufferReader);
        if(header == null){
            return null;
        }
        List<String> moveList = decodeMovesList(bufferReader);
        return new PGNGame(header, moveList);
    }

    public PGNGame.PGNHeader decodeHeader(BufferedReader bufferReader) throws IOException {
        PGNGame.PGNHeader result = new PGNGame.PGNHeader();
        String line;
        while ((line = bufferReader.readLine()) != null) {
            if ("".equals(line.trim())) {
                break;
            }
            Matcher headerMather = headerPattern.matcher(line);
            if (headerMather.find()) {
                String headerName = headerMather.group(1);
                String headerText = headerMather.group(2);
                switch (headerName) {
                    case "Event":
                        result.setEvent(headerText);
                        break;
                    case "Site":
                        result.setSite(headerText);
                        break;
                    case "Date":
                        result.setDate(headerText);
                        break;
                    case "Round":
                        result.setRound(headerText);
                        break;
                    case "White":
                        result.setWhite(headerText);
                        break;
                    case "Black":
                        result.setBlack(headerText);
                        break;
                    case "Result":
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

    public List<String> decodeMovesList(BufferedReader bufferReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferReader.readLine()) != null) {
            if ("".equals(line.trim())) {
                break;
            }
            stringBuilder.append(line.trim());
        }
        ;
        return decodeMovesList(stringBuilder.toString());
    }

    public List<String> decodeMovesList(String moveListStr) {
        List<String> result = new ArrayList<>();
        final Matcher matcher = moveListPattern.matcher(moveListStr);
        while (matcher.find()) {
            String moveStr = matcher.group(1);
            result.add(moveStr);
        }
        return result;
    }
}
