package net.chesstango.board.position;

import net.chesstango.board.builders.ChessPositionBuilder;
import net.chesstango.gardel.fen.FEN;

/**
 * Interface representing a chess position.
 * This interface extends both ChessPositionReader and ChessPositionWriter,
 * indicating that it provides methods for reading and writing chess positions.
 * <p>
 * Chess position consists of:
 * <ul>
 * <li>Piece placement on the Board Representation</li>
 * <li>Side to move</li>
 * <li>Castling Rights</li>
 * <li>En passant target square</li>
 * <li>FullMoveClock</li>
 * <li>Halfmove Clock</li>
 * </ul>
 *
 * @author Mauricio Coria
 */
public interface Position extends PositionReader, PositionWriter {
    /**
     * Initializes the chess position.
     * This method should set up the initial state of the chess position,
     */
    void init();

    SquareBoard getSquareBoard();

    BitBoard getBitBoard();

    KingSquare getKingSquare();

    MoveCacheBoard getMoveCache();

    PositionState getPositionState();

    ZobristHash getZobrist();

    /**
     * Creates a new chess position from a FEN (Forsyth–Edwards Notation) string.
     * The FEN string describes the state of a chessboard, including piece positions,
     * active color, castling availability, en passant target squares, halfmove clock,
     * and fullmove number.
     *
     * @param fen the FEN string representing the chessboard state
     * @return a {@code Position} object representing the chessboard described by the FEN string
     */
    static Position fromFEN(String fen) {
        return from(FEN.of(fen));
    }

    /**
     * Creates a new {@code Position} object from the given FEN (Forsyth–Edwards Notation) object.
     * The method constructs a chessboard state using the FEN representation by utilizing a
     * {@code ChessPositionBuilder} and a {@code FENExporter}.
     *
     * @param fen the FEN object representing the chessboard state
     * @return a {@code Position} object representing the chessboard described by the FEN object
     */
    static Position from(FEN fen) {
        ChessPositionBuilder builder = new ChessPositionBuilder();

        fen.export(builder);

        return builder.getPositionRepresentation();
    }
}
