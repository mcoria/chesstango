package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.representations.fen.FENDecoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mauricio Coria
 */
public class EDPReader {

    public List<EDPEntry> readEdpFile(String filename) {
        List<EDPEntry> edpEntries = new ArrayList<>();
        try {
            System.out.println("Reading suite " + filename);

            InputStream instr = new FileInputStream(filename);

            // reading the files with buffered reader
            InputStreamReader inputStreamReader = new InputStreamReader(instr);

            BufferedReader rr = new BufferedReader(inputStreamReader);

            String line;

            // outputting each line of the file.
            while ((line = rr.readLine()) != null) {
                if (!line.startsWith("#")) {
                    try {
                        EDPEntry entry = readEdpLine(line);
                        edpEntries.add(entry);
                    } catch (RuntimeException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return edpEntries;
    }

    public EDPEntry readEdpLine(String line) {
        EDPEntry edpEntry = parseLine(line);
        edpEntry.game = FENDecoder.loadGame(edpEntry.fen);

        String[] bestMoves = edpEntry.bestMovesString.split(" ");

        for (int i = 0; i < bestMoves.length; i++) {
            Move move = decodeMove(bestMoves[i], edpEntry.game.getPossibleMoves());
            if (move != null) {
                edpEntry.bestMoves.add(move);
            } else {
                throw new RuntimeException(String.format("Unable to decode %s", bestMoves[i]));
            }
        }
        return edpEntry;
    }

    private Pattern edpLinePattern = Pattern.compile("(?<fen>.*/.*/.*/.*/.*) bm (?<bestmoves>[^;]*);.*");

    protected EDPEntry parseLine(String line) {
        EDPEntry edpParsed = new EDPEntry();
        edpParsed.text = line;

        Matcher matcher = edpLinePattern.matcher(line);
        if (matcher.matches()) {
            edpParsed.fen = matcher.group("fen");
            edpParsed.bestMovesString = matcher.group("bestmoves");
            edpParsed.bestMoves = new ArrayList<>();
        }

        return edpParsed;
    }

    public static class EDPEntry {
        public String text;

        public String fen;
        public String bestMovesString;
        public List<Move> bestMoves;

        public Game game;
    }


    /**
     * DECODE MOVE
     */
    private Pattern edpMovePattern = Pattern.compile("(" +
            "(?<piecemove>(?<piece>[RNBQK]?)((?<from>[a-h][1-8])|(?<fromfile>[a-h]))?[-x]?(?<to>[a-h][1-8]))|" +
            "(?<promotion>(?<promotionfrom>[a-h][1-8])[-x](?<promotionto>[a-h][1-8])(?<promotionpiece>[RNBQK]))|" +
            "(?<queencaslting>O-O-O)|" +
            "(?<kingcastling>O-O)" +
            ")\\+?");

    public Move decodeMove(String moveStr, Iterable<Move> possibleMoves) {
        final Matcher matcher = edpMovePattern.matcher(moveStr);
        if (matcher.matches()) {
            if (matcher.group("piecemove") != null) {
                return decodePieceMove(matcher, possibleMoves);
            } else if (matcher.group("queencaslting") != null) {
                return searchQueenCastling(possibleMoves);
            } else if (matcher.group("kingcastling") != null) {
                return searchKingCastling(possibleMoves);
            } else if (matcher.group("promotion") != null) {
                return decodePromotion(matcher, possibleMoves);
            }
        }
        return null;
    }

    protected Move decodePieceMove(Matcher matcher, Iterable<Move> possibleMoves) {
        String pieceStr = matcher.group("piece");
        String fromStr = matcher.group("from");
        String fromFileStr = matcher.group("fromfile");
        String toStr = matcher.group("to");
        for (Move move : possibleMoves) {
            if (pieceStr != null && pieceStr.length() == 1) {
                if (move.getFrom().getPiece().isPawn()) {
                    continue;
                }
                if (!getPieceCode(move.getFrom().getPiece()).equals(pieceStr)) {
                    continue;
                }
            } else {
                if (!move.getFrom().getPiece().isPawn()) {
                    continue;
                }
            }
            if (fromStr != null) {
                if (!move.getFrom().getSquare().toString().equals(fromStr)) {
                    continue;
                }
            }
            if (fromFileStr != null) {
                if (!move.getFrom().getSquare().getFileChar().equals(fromFileStr)) {
                    continue;
                }
            }
            if (move.getTo().getSquare().toString().equals(toStr)) {
                return move;
            }

        }
        return null;
    }

    protected Move decodePromotion(Matcher matcher, Iterable<Move> possibleMoves) {
        String promotionpieceStr = matcher.group("promotionpiece");
        String fromStr = matcher.group("promotionfrom");
        String toStr = matcher.group("promotionto");
        for (Move move : possibleMoves) {
            if (move instanceof MovePromotion) {
                MovePromotion movePromotion = (MovePromotion) move;
                if (move.getFrom().getSquare().toString().equals(fromStr) && move.getTo().getSquare().toString().equals(toStr) && getPieceCode(movePromotion.getPromotion()).equals(promotionpieceStr)) {
                    return move;
                }
            }
        }
        return null;
    }

    private Move searchKingCastling(Iterable<Move> possibleMoves) {
        for (Move move : possibleMoves) {
            if (move instanceof MoveCastling && move.getTo().getSquare().getFile() == 6) {
                return move;
            }
        }
        return null;
    }

    private Move searchQueenCastling(Iterable<Move> possibleMoves) {
        for (Move move : possibleMoves) {
            if (move instanceof MoveCastling && move.getTo().getSquare().getFile() == 2) {
                return move;
            }
        }
        return null;
    }

    private String getPieceCode(Piece piece) {
        switch (piece) {
            case PAWN_WHITE:
            case PAWN_BLACK:
                throw new RuntimeException("You should not call this method with pawn");
            case ROOK_WHITE:
            case ROOK_BLACK:
                return "R";
            case KNIGHT_WHITE:
            case KNIGHT_BLACK:
                return "N";
            case BISHOP_WHITE:
            case BISHOP_BLACK:
                return "B";
            case QUEEN_WHITE:
            case QUEEN_BLACK:
                return "Q";
            case KING_WHITE:
            case KING_BLACK:
                return "K";
            default:
                throw new RuntimeException("Falta pieza");
        }
    }
}
