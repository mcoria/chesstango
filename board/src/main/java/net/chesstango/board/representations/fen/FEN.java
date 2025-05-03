package net.chesstango.board.representations.fen;

import lombok.Getter;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Getter
public final class FEN {

    private final String piecePlacement;

    private final String activeColor;

    private final String castingsAllowed;

    private final String enPassantSquare;

    private final String halfMoveClock;

    private final String fullMoveClock;

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
    }

    public static FEN of(String fenString) {
        FENParser parser = new FENParser();
        return parser.parseFEN(fenString);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s %s", piecePlacement, activeColor, castingsAllowed, enPassantSquare, halfMoveClock, fullMoveClock);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FEN fen)) return false;
        return Objects.equals(piecePlacement, fen.piecePlacement) && Objects.equals(activeColor, fen.activeColor) && Objects.equals(castingsAllowed, fen.castingsAllowed) && Objects.equals(enPassantSquare, fen.enPassantSquare) && Objects.equals(halfMoveClock, fen.halfMoveClock) && Objects.equals(fullMoveClock, fen.fullMoveClock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piecePlacement, activeColor, castingsAllowed, enPassantSquare, halfMoveClock, fullMoveClock);
    }

}
