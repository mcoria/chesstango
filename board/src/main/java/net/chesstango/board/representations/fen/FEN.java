package net.chesstango.board.representations.fen;

import lombok.Getter;
import net.chesstango.board.builders.ChessPositionBuilder;
import net.chesstango.board.position.ChessPosition;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mauricio Coria
 */
@Getter
public final class FEN {

    private final String fen;

    private final String piecePlacement;

    private final String activeColor;

    private final String castingsAllowed;

    private final String enPassantSquare;

    private final String halfMoveClock;

    private final String fullMoveClock;


    public static final Pattern fenPattern = Pattern.compile("(?<piecePlacement>([rnbqkpRNBQKP12345678]{1,8}/){7}[rnbqkpRNBQKP12345678]{1,8})\\s+" +
            "(?<activeColor>[wb])\\s+" +
            "(?<castingsAllowed>([KQkq]{1,4}|-))\\s+" +
            "(?<enPassantSquare>(\\w\\d|-))(\\s*|\\s+" +
            "(?<halfMoveClock>[0-9]*)\\s+" +
            "(?<fullMoveClock>[0-9]*)\\s*)");

    public FEN(String fen) {
        this.fen = fen.trim();

        Matcher matcher = fenPattern.matcher(this.fen);
        if (!matcher.matches()) {
            throw new RuntimeException("Invalid fen input string");
        }

        this.piecePlacement = matcher.group("piecePlacement");
        this.activeColor = matcher.group("activeColor");
        this.castingsAllowed = matcher.group("castingsAllowed");
        this.enPassantSquare = matcher.group("enPassantSquare");
        this.halfMoveClock = matcher.group("halfMoveClock");
        this.fullMoveClock = matcher.group("fullMoveClock");
    }

    FEN(String piecePlacement,
        String activeColor,
        String castingsAllowed,
        String enPassantSquare,
        String halfMoveClock,
        String fullMoveClock) {

        this.piecePlacement = piecePlacement;
        this.activeColor = activeColor;
        this.castingsAllowed = castingsAllowed;
        this.enPassantSquare = enPassantSquare;
        this.halfMoveClock = halfMoveClock;
        this.fullMoveClock = fullMoveClock;
        this.fen = String.format("%s %s %s %s %s %s", piecePlacement, activeColor, castingsAllowed, enPassantSquare, halfMoveClock, fullMoveClock);
    }


    @Override
    public String toString() {
        return fen;
    }

    @Override
    public int hashCode() {
        return this.fen.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FEN fen1)) return false;
        return Objects.equals(fen, fen1.fen);
    }

    public ChessPosition toChessPosition() {
        ChessPositionBuilder builder = new ChessPositionBuilder();

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(fen);

        return builder.getChessRepresentation();
    }
}
