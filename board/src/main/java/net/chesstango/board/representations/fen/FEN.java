package net.chesstango.board.representations.fen;

/**
 * @author Mauricio Coria
 */
public record FEN(String piecePlacement,
                  String activeColor,
                  String castingsAllowed,
                  String enPassantSquare,
                  String halfMoveClock,
                  String fullMoveClock) {
}
