package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
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

    public List<EDPEntry> readEdpFile(String filename){
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
                    EDPEntry entry = readEdpLine(line);
                    edpEntries.add(entry);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return edpEntries;
    }

    public EDPEntry readEdpLine(String line) {
        EDPEntry edpEntry = parseLine(line);
        edpEntry.game =  FENDecoder.loadGame(edpEntry.fen);
        Move move = decodeMove(edpEntry.bestMove, edpEntry.game.getPossibleMoves());
        if(move != null){
            edpEntry.expectedMove = move;
        } else {
            throw new RuntimeException(String.format("Unable to decode %s", edpEntry.bestMove));
        }

        return edpEntry;
    }

    private Pattern edpMovePattern = Pattern.compile("[RNBQK]?(?<from>[a-h][1-8])[-x](?<to>[a-h][1-8])(?<promotion>[RNBQK]?)\\+?");
    private Move decodeMove(String bestMove, MoveContainerReader possibleMoves) {
        Matcher matcher = edpMovePattern.matcher(bestMove);
        if(matcher.matches()) {
            String fromStr = matcher.group("from");
            String toStr = matcher.group("to");
            String promotionStr = matcher.group("promotion");
            for (Move move: possibleMoves) {
                if(move.getFrom().getSquare().toString().equals(fromStr) && move.getTo().getSquare().toString().equals(toStr)){
                    if(move instanceof MovePromotion){
                        MovePromotion promotionMove = (MovePromotion) move;
                        if(getPieceCode(promotionMove.getPromotion()).equals(promotionStr)){
                            return move;
                        }
                    } else {
                        return move;
                    }
                }
            }
        }
        return null;
    }


    private Pattern edpLinePattern = Pattern.compile("(?<fen>.*/.*/.*/.*/.*) bm (?<bestmove>[^;]*);.*");
    protected EDPEntry parseLine(String line) {
        EDPEntry edpParsed = new EDPEntry();

        Matcher matcher = edpLinePattern.matcher(line);

        if(matcher.matches()){
            edpParsed.fen = matcher.group("fen");
            edpParsed.bestMove = matcher.group("bestmove");
        }

        return edpParsed;
    }

    public static class EDPEntry {
        public String fen;
        public String bestMove;
        public Game game;
        public Move expectedMove;
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
