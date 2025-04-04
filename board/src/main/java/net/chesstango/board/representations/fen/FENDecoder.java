package net.chesstango.board.representations.fen;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.builders.PositionBuilder;
import net.chesstango.board.builders.GameBuilder;

/**
 * @author Mauricio Coria
 */
public class FENDecoder {
    public static final String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final PositionBuilder<?> positionBuilder;

    public FENDecoder(PositionBuilder<?> positionBuilder) {
        this.positionBuilder = positionBuilder;
    }

    public void parseFEN(String fenString) {
        parseFEN(FEN.of(fenString));
    }

    public void parseFEN(FEN fen) {
        parsePiecePlacement(fen.getPiecePlacement());

        positionBuilder.withEnPassantSquare(parseEnPassantSquare(fen.getEnPassantSquare()));

        positionBuilder.withTurn(parseTurn(fen.getActiveColor()));

        if (isCastlingWhiteQueenAllowed(fen.getCastingsAllowed())) {
            positionBuilder.withCastlingWhiteQueenAllowed(true);
        }

        if (isCastlingWhiteKingAllowed(fen.getCastingsAllowed())) {
            positionBuilder.withCastlingWhiteKingAllowed(true);
        }

        if (isCastlingBlackQueenAllowed(fen.getCastingsAllowed())) {
            positionBuilder.withCastlingBlackQueenAllowed(true);
        }

        if (isCastlingBlackKingAllowed(fen.getCastingsAllowed())) {
            positionBuilder.withCastlingBlackKingAllowed(true);
        }

        positionBuilder.withHalfMoveClock(fen.getHalfMoveClock() == null ? 0 : Integer.parseInt(fen.getHalfMoveClock()));

        positionBuilder.withFullMoveClock(fen.getFullMoveClock() == null ? 1 : Integer.parseInt(fen.getFullMoveClock()));
    }

    public void parsePiecePlacement(String piecePlacement) {
        Piece[][] piezas = parsePieces(piecePlacement);
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Square square = Square.getSquare(file, rank);
                Piece piece = piezas[rank][file];
                if (piece != null) {
                    positionBuilder.withPiece(square, piece);
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
            System.arraycopy(rankPiezas, 0, tablero[currentRank], 0, 8);
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
            fileNumber = switch (file) {
                case 'a' -> 0;
                case 'b' -> 1;
                case 'c' -> 2;
                case 'd' -> 3;
                case 'e' -> 4;
                case 'f' -> 5;
                case 'g' -> 6;
                case 'h' -> 7;
                default -> throw new RuntimeException("Invalid FEV code");
            };
            result = Square.getSquare(fileNumber, rankNumber);
        }
        return result;
    }

    protected Color parseTurn(String activeColor) {
        char colorChar = activeColor.charAt(0);
        Color turno = switch (colorChar) {
            case 'w' -> Color.WHITE;
            case 'b' -> Color.BLACK;
            default -> throw new RuntimeException("Unknown FEN code " + activeColor);
        };
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
        return loadGame(FEN.of(fen));
    }

    public static Game loadGame(FEN fen) {
        GameBuilder builder = new GameBuilder();

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(fen);

        return builder.getChessRepresentation();
    }


}
