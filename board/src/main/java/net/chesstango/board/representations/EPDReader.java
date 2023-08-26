package net.chesstango.board.representations;

import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.representations.fen.FENDecoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class reads Extended Position Description files.
 *
 * @author Mauricio Coria
 */
public class EPDReader {

    public List<EPDEntry> readEdpFile(String filename) {
        return readEdpFile(Paths.get(filename));

    }

    public List<EPDEntry> readEdpFile(Path filePath) {
        if (!Files.exists(filePath)) {
            System.err.printf("file not found: %s\n", filePath.getFileName());
            throw new RuntimeException(String.format("file not found: %s", filePath.getFileName()));
        }

        List<EPDEntry> edpEntries = new ArrayList<>();
        try {
            System.out.println("Reading suite " + filePath);

            InputStream instr = new FileInputStream(filePath.toFile());

            // reading the files with buffered reader
            InputStreamReader inputStreamReader = new InputStreamReader(instr);

            BufferedReader rr = new BufferedReader(inputStreamReader);

            String line;

            // outputting each line of the file.
            while ((line = rr.readLine()) != null) {
                if (!line.startsWith("#")) {
                    try {
                        EPDEntry entry = readEdpLine(line);
                        edpEntries.add(entry);
                    } catch (RuntimeException e) {
                        System.err.printf("Error decoding: %s\n", line);
                        e.printStackTrace(System.err);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return edpEntries;
    }

    public EPDEntry readEdpLine(String line) {
        EPDEntry EPDEntry = parseLine(line);
        EPDEntry.game = FENDecoder.loadGame(EPDEntry.fen);

        if(EPDEntry.bestMovesString != null) {
            String[] bestMoves = EPDEntry.bestMovesString.split(" ");
            for (int i = 0; i < bestMoves.length; i++) {
                Move move = decodeMove(bestMoves[i], EPDEntry.game.getPossibleMoves());
                if (move != null) {
                    EPDEntry.bestMoves.add(move);
                } else {
                    throw new RuntimeException(String.format("Unable to find move %s", bestMoves[i]));
                }
            }
        }
        return EPDEntry;
    }

    private Pattern edpLinePattern = Pattern.compile("(?<fen>.*/.*/.*/.*/.*\\s+[wb]\\s+([KQkq]{1,4}|-)\\s+(\\w\\d|-))\\s+(bm\\s+(?<bestmoves>[^;]*);|\\s*id\\s+\"(?<id>[^\"]+)\";|[^;]+;)*");

    protected EPDEntry parseLine(String line) {
        EPDEntry edpParsed = new EPDEntry();
        edpParsed.text = line;

        Matcher matcher = edpLinePattern.matcher(line);
        if (matcher.matches()) {
            edpParsed.fen = matcher.group("fen");
            if(matcher.group("bestmoves") != null) {
                edpParsed.bestMovesString = matcher.group("bestmoves");
                edpParsed.bestMoves = new ArrayList<>();
            }
            if(matcher.group("id") != null) {
                edpParsed.id = matcher.group("id");
            }
        }

        return edpParsed;
    }


    /**
     * DECODE MOVE
     */
    private Pattern edpMovePattern = Pattern.compile("(" +
            "(?<piecemove>(?<piece>[RNBQK]?)((?<from>[a-h][1-8])|(?<fromfile>[a-h])|(?<fromrank>[1-8]))?[-x]?(?<to>[a-h][1-8]))|" +
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
        String fromRankStr = matcher.group("fromrank");
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
            if (fromRankStr != null) {
                if (!move.getFrom().getSquare().getRankChar().equals(fromRankStr)) {
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
