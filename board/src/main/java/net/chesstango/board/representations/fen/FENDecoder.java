package net.chesstango.board.representations.fen;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.builders.ChessPositionBuilder;
import net.chesstango.board.builders.ChessRepresentationBuilder;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.position.ChessPosition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mauricio Coria
 */
public class FENDecoder {
    public static final String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static Pattern fenPattern = Pattern.compile("(?<piecePlacement>([rnbqkpRNBQKP12345678]{1,8}/){7}[rnbqkpRNBQKP12345678]{1,8})\\s+" +
            "(?<activeColor>[wb])\\s+" +
            "(?<castingsAllowed>([KQkq]{1,4}|-))\\s+" +
            "(?<enPassantSquare>(\\w\\d|-))(\\s*|\\s+" +
            "(?<halfMoveClock>[0-9]*)\\s+" +
            "(?<fullMoveClock>[0-9]*)\\s*)");

    private final ChessRepresentationBuilder<?> chessRepresentationBuilder;

    public FENDecoder(ChessRepresentationBuilder<?> chessRepresentationBuilder) {
        this.chessRepresentationBuilder = chessRepresentationBuilder;
    }

    public void parseFEN(String input) {
        Matcher matcher = fenPattern.matcher(input);
        if (!matcher.matches()) {
            throw new RuntimeException("Invalid fen input string");
        }

        String piecePlacement = matcher.group("piecePlacement");
        String activeColor = matcher.group("activeColor");
        String castingsAllowed = matcher.group("castingsAllowed");
        String enPassantSquare = matcher.group("enPassantSquare");
        String halfMoveClock = matcher.group("halfMoveClock");
        String fullMoveClock = matcher.group("fullMoveClock");

        FEN fen = new FEN(piecePlacement,
                activeColor,
                castingsAllowed,
                enPassantSquare,
                halfMoveClock,
                fullMoveClock);

        parseFEN(fen);
    }

    public void parseFEN(FEN fen) {
        parsePiecePlacement(fen.piecePlacement());

        chessRepresentationBuilder.withEnPassantSquare(parseEnPassantSquare(fen.enPassantSquare()));

        chessRepresentationBuilder.withTurn(parseTurn(fen.activeColor()));

        if (isCastlingWhiteQueenAllowed(fen.castingsAllowed())) {
            chessRepresentationBuilder.withCastlingWhiteQueenAllowed(true);
        }

        if (isCastlingWhiteKingAllowed(fen.castingsAllowed())) {
            chessRepresentationBuilder.withCastlingWhiteKingAllowed(true);
        }

        if (isCastlingBlackQueenAllowed(fen.castingsAllowed())) {
            chessRepresentationBuilder.withCastlingBlackQueenAllowed(true);
        }

        if (isCastlingBlackKingAllowed(fen.castingsAllowed())) {
            chessRepresentationBuilder.withCastlingBlackKingAllowed(true);
        }

        chessRepresentationBuilder.withHalfMoveClock(fen.halfMoveClock() == null ? 0 : Integer.parseInt(fen.halfMoveClock()));

        chessRepresentationBuilder.withFullMoveClock(fen.fullMoveClock() == null ? 1 : Integer.parseInt(fen.fullMoveClock()));
    }

    public void parsePiecePlacement(String piecePlacement) {
        Piece[][] piezas = parsePieces(piecePlacement);
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Square square = Square.getSquare(file, rank);
                Piece piece = piezas[rank][file];
                if (piece != null) {
                    chessRepresentationBuilder.withPiece(square, piece);
                }
            }
        }
    }

    protected Piece[][] parsePieces(String piecePlacement) {
        Piece[][] tablero = new Piece[8][8];
        String[] ranks = piecePlacement.split("/");
        int currentRank = 7;
        for (int i = 0; i < 8; i++) {
            Piece[] rankPiezas = parseRank(ranks[i]);
            for (int j = 0; j < 8; j++) {
                tablero[currentRank][j] = rankPiezas[j];
            }
            currentRank--;
        }
        return tablero;
    }

    protected Piece[] parseRank(String rank) {
        Piece[] piezas = new Piece[8];
        int position = 0;
        for (int i = 0; i < rank.length(); i++) {
            char theCharCode = rank.charAt(i);
            Piece currentPieza = getCode(theCharCode);
            if (currentPieza != null) {
                piezas[position] = currentPieza;
                position++;
            } else {
                int offset = Integer.parseInt(String.valueOf(theCharCode));
                position += offset;
            }
        }
        if (position != 8) {
            throw new RuntimeException("FEN: Malformed rank: " + rank);
        }
        return piezas;
    }

    private Piece getCode(char t) {
        Piece piece = null;
        switch (t) {
            case 'r':
                piece = Piece.ROOK_BLACK;
                break;
            case 'n':
                piece = Piece.KNIGHT_BLACK;
                break;
            case 'q':
                piece = Piece.QUEEN_BLACK;
                break;
            case 'k':
                piece = Piece.KING_BLACK;
                break;
            case 'p':
                piece = Piece.PAWN_BLACK;
                break;
            case 'b':
                piece = Piece.BISHOP_BLACK;
                break;
            case 'R':
                piece = Piece.ROOK_WHITE;
                break;
            case 'N':
                piece = Piece.KNIGHT_WHITE;
                break;
            case 'Q':
                piece = Piece.QUEEN_WHITE;
                break;
            case 'K':
                piece = Piece.KING_WHITE;
                break;
            case 'P':
                piece = Piece.PAWN_WHITE;
                break;
            case 'B':
                piece = Piece.BISHOP_WHITE;
                break;
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
                break;
            default:
                throw new RuntimeException("Unknown FEN code " + t);
        }

        return piece;
    }

    protected Square parseEnPassantSquare(String pawnPasante) {
        Square result = null;
        if (!"-".equals(pawnPasante)) {
            char file = pawnPasante.charAt(0);
            char rank = pawnPasante.charAt(1);
            int fileNumber = -1;
            int rankNumber = Integer.parseInt(String.valueOf(rank)) - 1;
            switch (file) {
                case 'a':
                    fileNumber = 0;
                    break;
                case 'b':
                    fileNumber = 1;
                    break;
                case 'c':
                    fileNumber = 2;
                    break;
                case 'd':
                    fileNumber = 3;
                    break;
                case 'e':
                    fileNumber = 4;
                    break;
                case 'f':
                    fileNumber = 5;
                    break;
                case 'g':
                    fileNumber = 6;
                    break;
                case 'h':
                    fileNumber = 7;
                    break;
                default:
                    throw new RuntimeException("Invalid FEV code");
            }
            result = Square.getSquare(fileNumber, rankNumber);
        }
        return result;
    }

    protected Color parseTurn(String activeColor) {
        char colorChar = activeColor.charAt(0);
        Color turno = null;
        switch (colorChar) {
            case 'w':
                turno = Color.WHITE;
                break;
            case 'b':
                turno = Color.BLACK;
                break;
            default:
                throw new RuntimeException("Unknown FEN code " + activeColor);
        }
        return turno;
    }

    protected boolean isCastlingWhiteQueenAllowed(String castlingsAlloweds) {
        return castlingsAlloweds.contains("Q");
    }

    protected boolean isCastlingWhiteKingAllowed(String castlingsAlloweds) {
        return castlingsAlloweds.contains("K");
    }

    protected boolean isCastlingBlackQueenAllowed(String castlingsAlloweds) {
        return castlingsAlloweds.contains("q");
    }

    protected boolean isCastlingBlackKingAllowed(String castlingsAlloweds) {
        return castlingsAlloweds.contains("k");
    }


    public static Game loadGame(String fen) {
        GameBuilder builder = new GameBuilder();

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(fen);

        return builder.getChessRepresentation();
    }

    public static ChessPosition loadChessPosition(String fen) {
        ChessPositionBuilder builder = new ChessPositionBuilder();

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(fen);

        return builder.getChessRepresentation();
    }

}
