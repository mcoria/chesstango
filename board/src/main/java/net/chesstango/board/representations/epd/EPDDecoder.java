package net.chesstango.board.representations.epd;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.representations.fen.FEN;
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
    /**
     * Decode line components
     */
    private static final Pattern edpLinePattern = Pattern.compile("(?<fen>.*/.*/.*/.*/.*\\s+[wb]\\s+([KQkq]{1,4}|-)\\s+(\\w\\d|-))\\s+" +
            "(\\s*bm\\s+(?<bestmoves>[^;]*);" +
            "|\\s*am\\s+(?<avoidmoves>[^;]*);" +
            "|\\s*sm\\s+(?<suppliedmove>[^;]*);" +
            "|\\s*c0\\s+\"(?<comment0>[^\"]+)\";" +
            "|\\s*c1\\s+\"(?<comment1>[^\"]+)\";" +
            "|\\s*c2\\s+\"(?<comment2>[^\"]+)\";" +
            "|\\s*c3\\s+\"(?<comment3>[^\"]+)\";" +
            "|\\s*c4\\s+\"(?<comment4>[^\"]+)\";" +
            "|\\s*c5\\s+\"(?<comment5>[^\"]+)\";" +
            "|\\s*c6\\s+\"(?<comment6>[^\"]+)\";" +
            "|\\s*id\\s+\"(?<id>[^\"]+)\";" +
            "|[^;]+;)*"
    );

    public Stream<EPD> readEdpFile(String filename) {
        return readEdpFile(Paths.get(filename));

    }

    public Stream<EPD> readEdpFile(Path filePath) {
        if (!Files.exists(filePath)) {
            System.err.printf("file not found: %s\n", filePath.getFileName());
            throw new RuntimeException(String.format("file not found: %s", filePath.getFileName()));
        }

        System.out.println("Reading suite " + filePath);

        Stream.Builder<EPD> epdEntryStreamBuilder = Stream.builder();

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
        EPD epd = new EPD();
        epd.setText(line);

        Matcher matcher = edpLinePattern.matcher(line);
        if (matcher.matches()) {
            epd.setFenWithoutClocks(FEN.of(matcher.group("fen")));
            if (matcher.group("suppliedmove") != null) {
                String suppliedMove = matcher.group("suppliedmove");
                epd.setSuppliedMoveStr(suppliedMove);
            }
            if (matcher.group("bestmoves") != null) {
                String bestMovesString = matcher.group("bestmoves");
                epd.setBestMovesStr(bestMovesString);
            }
            if (matcher.group("avoidmoves") != null) {
                String avoidMovesString = matcher.group("avoidmoves");
                epd.setAvoidMovesStr(avoidMovesString);
            }
            if (matcher.group("id") != null) {
                epd.setId(matcher.group("id"));
            }
            if (matcher.group("comment0") != null) {
                String comment0 = matcher.group("comment0");
                epd.setC0(comment0);
            }
            if (matcher.group("comment1") != null) {
                String comment1 = matcher.group("comment1");
                epd.setC1(comment1);
            }
            if (matcher.group("comment2") != null) {
                String comment2 = matcher.group("comment2");
                epd.setC2(comment2);
            }
            if (matcher.group("comment3") != null) {
                String comment3 = matcher.group("comment3");
                epd.setC3(comment3);
            }
            if (matcher.group("comment4") != null) {
                String comment4 = matcher.group("comment4");
                epd.setC4(comment4);
            }
            if (matcher.group("comment5") != null) {
                String comment5 = matcher.group("comment5");
                epd.setC5(comment5);
            }
            if (matcher.group("comment6") != null) {
                String comment6 = matcher.group("comment6");
                epd.setC6(comment6);
            }
        }

        Game game = FENDecoder.loadGame(epd.getFenWithoutClocks());
        if (epd.getBestMovesStr() != null) {
            epd.setBestMoves(movesStringToMoves(game, epd.getBestMovesStr()));
        }
        if (epd.getAvoidMovesStr() != null) {
            epd.setAvoidMoves(movesStringToMoves(game, epd.getAvoidMovesStr()));
        }

        return epd;
    }

    private List<Move> movesStringToMoves(Game game, String movesString) {
        String[] bestMoves = movesString.split(" ");
        List<Move> moveList = new ArrayList<>(bestMoves.length);
        for (int i = 0; i < bestMoves.length; i++) {
            Move move = decodeMove(bestMoves[i], game.getPossibleMoves());
            if (move != null) {
                moveList.add(move);
            } else {
                throw new RuntimeException(String.format("Unable to find move %s", bestMoves[i]));
            }
        }
        return moveList;
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
        String promotionPieceStr = matcher.group("promotionpiece");
        String fromStr = matcher.group("promotionfrom");
        String toStr = matcher.group("promotionto");
        for (Move move : possibleMoves) {
            if (move instanceof MovePromotion movePromotion) {
                if (move.getFrom().getSquare().toString().equals(fromStr) && move.getTo().getSquare().toString().equals(toStr) && getPieceCode(movePromotion.getPromotion()).equals(promotionPieceStr)) {
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
