package net.chesstango.board.representations.pgn;

import net.chesstango.board.representations.move.SANDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mauricio Coria
 */
public class PGNDecoder {

    private static final Pattern headerPattern = Pattern.compile("\\[(\\w*) \"(.*)\"\\]");


    public List<PGNGame> decodeGames(BufferedReader bufferReader) throws IOException {
        List<PGNGame> result = new ArrayList<>();
        PGNGame game;
        while ((game = decodeGame(bufferReader)) != null) {
            result.add(game);
        }
        return result;
    }

    public PGNGame decodeGame(BufferedReader bufferReader) throws IOException {
        PGNGame pgnGame = decodeHeader(bufferReader);
        if (pgnGame == null) {
            return null;
        }
        pgnGame.setMoveList(decodeMovesList(bufferReader));
        return pgnGame;
    }

    protected PGNGame decodeHeader(BufferedReader bufferReader) throws IOException {
        PGNGame result = new PGNGame();
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
