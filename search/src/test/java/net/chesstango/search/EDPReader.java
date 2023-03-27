package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.representations.fen.FENDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EDPReader {

    public List<EDPEntry> readEdpFile(String filename){
        List<EDPEntry> edpEntries = new ArrayList<>();
        try {
            System.out.println("Starting suite " + filename);

            List<String> failedSuites = new ArrayList<String>();

            BestMoveFinderSuite suite = new BestMoveFinderSuite(6);

            InputStream instr = suite.getClass().getClassLoader().getResourceAsStream(filename);

            // reading the files with buffered reader
            InputStreamReader strrd = new InputStreamReader(instr);

            BufferedReader rr = new BufferedReader(strrd);

            String line;

            // outputting each line of the file.
            while ((line = rr.readLine()) != null) {
                if (!line.startsWith("#")) {
                    EDPEntry entry = readEdpLine(line);
                    edpEntries.add(entry);
                }
            }

            System.out.println("Suite summary " + filename);
            if (failedSuites.isEmpty()) {
                System.out.println("\t all tests exceute sucessfully");
            } else {
                for (String suiteStr : failedSuites) {
                    System.out.println("\t test failed: " + suiteStr);
                }
            }
            System.out.println("=================");


        } catch (IOException e) {
            System.out.println(e);
        }

        return edpEntries;
    }

    public EDPEntry readEdpLine(String line) {
        EDPEntry edpEntry = parseLine(line);
        edpEntry.game =  FENDecoder.loadGame(edpEntry.fen);
        return edpEntry;
    }



    protected EDPEntry parseLine(String line) {
        EDPEntry edpParsed = new EDPEntry();
        String[] splitStrings = line.split("bm");

        edpParsed.fen = splitStrings[0].trim();
        edpParsed.bestMove = splitStrings[1].trim();
        return edpParsed;
    }

    public static class EDPEntry {
        String fen;
        String bestMove;
        Game game;
        Move expectedMove;
    }

    /*
     * TODO: similar a UCIEncoder
     */
    private String encodeMove(Move move) {
        String promotionStr = "";
        if (move instanceof MovePromotion) {
            MovePromotion movePromotion = (MovePromotion) move;
            switch (movePromotion.getPromotion()) {
                case ROOK_WHITE:
                case ROOK_BLACK:
                    promotionStr = "r";
                    break;
                case KNIGHT_WHITE:
                case KNIGHT_BLACK:
                    promotionStr = "k";
                    break;
                case BISHOP_WHITE:
                case BISHOP_BLACK:
                    promotionStr = "b";
                    break;
                case QUEEN_WHITE:
                case QUEEN_BLACK:
                    promotionStr = "q";
                    break;
                default:
                    throw new RuntimeException("Invalid promotion " + move);
            }
        }

        return move.getFrom().getSquare().toString() + move.getTo().getSquare().toString() + promotionStr;
    }

}
