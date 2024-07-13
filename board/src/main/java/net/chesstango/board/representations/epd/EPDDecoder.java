package net.chesstango.board.representations.epd;

import net.chesstango.board.Game;
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
import java.util.stream.Stream;

/**
 * This class reads Extended Position Description files.
 *
 * @author Mauricio Coria
 */
public class EPDDecoder {

    public Stream<EPD> readEdpFile(String filename) {
        return readEdpFile(Paths.get(filename));

    }

    public Stream<EPD> readEdpFile(Path filePath) {
        if (!Files.exists(filePath)) {
            System.err.printf("file not found: %s\n", filePath.getFileName());
            throw new RuntimeException(String.format("file not found: %s", filePath.getFileName()));
        }

        System.out.println("Reading suite " + filePath);

        Stream.Builder<EPD> epdEntryStreamBuilder = Stream.<EPD>builder();

        try (InputStream instr = new FileInputStream(filePath.toFile());
             InputStreamReader inputStreamReader = new InputStreamReader(instr);
             BufferedReader rr = new BufferedReader(inputStreamReader)) {

            String line;

            while ((line = rr.readLine()) != null) {
                if (!line.startsWith("#")) {
                    try {
                        EPD entry = readEdpLine(line);
                        epdEntryStreamBuilder.add(entry);
                    } catch (RuntimeException e) {
                        System.err.printf("Error decoding: %s\n", line);
                        e.printStackTrace(System.err);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return epdEntryStreamBuilder.build();
    }

    public EPD readEdpLine(String line) {
        EPD epd = parseLine(line);
        Game game = FENDecoder.loadGame(epd.getFen());

        if (epd.getBestMovesString() != null) {
            bestMovesStringToMoves(game, epd.getBestMovesString(), epd.getBestMoves());
        } else if (epd.getAvoidMoves() != null) {
            bestMovesStringToMoves(game, epd.getAvoidMovesString(), epd.getAvoidMoves());
        } else {
            throw new RuntimeException("No best move nor avoid move detected");
        }
        return epd;
    }

    private void bestMovesStringToMoves(Game game, String movesString, List<Move> moveList) {
        String[] bestMoves = movesString.split(" ");
        for (int i = 0; i < bestMoves.length; i++) {
            Move move = decodeMove(bestMoves[i], game.getPossibleMoves());
            if (move != null) {
                moveList.add(move);
            } else {
                throw new RuntimeException(String.format("Unable to find move %s", bestMoves[i]));
            }
        }
    }

    private static final Pattern edpLinePattern = Pattern.compile("(?<fen>.*/.*/.*/.*/.*\\s+[wb]\\s+([KQkq]{1,4}|-)\\s+(\\w\\d|-))\\s+(bm\\s+(?<bestmoves>[^;]*);|am\\s+(?<avoidmoves>[^;]*);|\\s*id\\s+\"(?<id>[^\"]+)\";|[^;]+;)*");

    private EPD parseLine(String line) {
        EPD edpParsed = new EPD();
        edpParsed.setText(line);

        Matcher matcher = edpLinePattern.matcher(line);
        if (matcher.matches()) {
            edpParsed.setFen(matcher.group("fen"));
            if (matcher.group("bestmoves") != null) {
                edpParsed.setBestMovesString(matcher.group("bestmoves"));
                edpParsed.setBestMoves(new ArrayList<>());
            }
            if (matcher.group("avoidmoves") != null) {
                edpParsed.setAvoidMovesString(matcher.group("avoidmoves"));
                edpParsed.setAvoidMoves(new ArrayList<>());
            }
            if (matcher.group("id") != null) {
                edpParsed.setId(matcher.group("id"));
            }
        }

        return edpParsed;
    }


    /**
     * DECODE MOVE
     */
    private static final Pattern edpMovePattern = Pattern.compile("(" +
            "(?<piecemove>(?<piece>[RNBQK]?)((?<from>[a-h][1-8])|(?<fromfile>[a-h])|(?<fromrank>[1-8]))?[-x]?(?<to>[a-h][1-8]))|" +
            "(?<promotion>(?<promotionfrom>[a-h][1-8])[-x](?<promotionto>[a-h][1-8])(?<promotionpiece>[RNBQK]))|" +
            "(?<queencaslting>O-O-O)|" +
            "(?<kingcastling>O-O)" +
            ")\\+?");

    private Move decodeMove(String moveStr, Iterable<Move> possibleMoves) {
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

    private Move decodePieceMove(Matcher matcher, Iterable<Move> possibleMoves) {
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

    private Move decodePromotion(Matcher matcher, Iterable<Move> possibleMoves) {
        String promotionpieceStr = matcher.group("promotionpiece");
        String fromStr = matcher.group("promotionfrom");
        String toStr = matcher.group("promotionto");
        for (Move move : possibleMoves) {
            if (move instanceof MovePromotion movePromotion) {
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
        return switch (piece) {
            case PAWN_WHITE, PAWN_BLACK -> throw new RuntimeException("You should not call this method with pawn");
            case ROOK_WHITE, ROOK_BLACK -> "R";
            case KNIGHT_WHITE, KNIGHT_BLACK -> "N";
            case BISHOP_WHITE, BISHOP_BLACK -> "B";
            case QUEEN_WHITE, QUEEN_BLACK -> "Q";
            case KING_WHITE, KING_BLACK -> "K";
            default -> throw new RuntimeException("Falta pieza");
        };
    }
}
