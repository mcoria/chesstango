package net.chesstango.board.representations.fen;

import net.chesstango.board.Game;
import net.chesstango.board.builders.GameBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mauricio Coria
 */
public class FENParser {
    public static final String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private static final Pattern fenPattern = Pattern.compile("(?<piecePlacement>([rnbqkpRNBQKP12345678]{1,8}/){7}[rnbqkpRNBQKP12345678]{1,8})\\s+" +
            "(?<activeColor>[wb])\\s+" +
            "(?<castingsAllowed>([KQkq]{1,4}|-))\\s+" +
            "(?<enPassantSquare>(\\w\\d|-))\\s+" +
            "(?<halfMoveClock>[0-9]*)\\s+" +
            "(?<fullMoveClock>[0-9]*)\\s*");


    public FEN parseFEN(String fen) {
        String fenString = fen.trim();

        Matcher matcher = fenPattern.matcher(fenString);
        if (!matcher.matches()) {
            throw new RuntimeException("Invalid fen input string");
        }

        String piecePlacement = matcher.group("piecePlacement");
        String activeColor = matcher.group("activeColor");
        String castingsAllowed = matcher.group("castingsAllowed");
        String enPassantSquare = matcher.group("enPassantSquare");
        String halfMoveClock = matcher.group("halfMoveClock");
        String fullMoveClock = matcher.group("fullMoveClock");

        return new FEN(piecePlacement, activeColor, castingsAllowed, enPassantSquare, halfMoveClock, fullMoveClock);
    }


    public static Game loadGame(String fen) {
        return loadGame(FEN.of(fen));
    }

    public static Game loadGame(FEN fen) {
        GameBuilder builder = new GameBuilder();

        FENExporter fenExporter = new FENExporter(builder);

        fenExporter.exportFEN(fen);

        return builder.getChessRepresentation();
    }


}
