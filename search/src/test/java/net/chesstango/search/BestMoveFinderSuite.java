package net.chesstango.search;

import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EDPReader;
import net.chesstango.board.representations.SANEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class BestMoveFinderSuite {

    private static final int DEFAULT_MAX_DEPTH = 8;

    public static void main(String[] args) {
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\40H-EPD-databases\\mate-all.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\STS\\wac-2018.epd");
    }

    private static void execute(String filename) {
        List<String> failedSuites = new ArrayList<String>();
        BestMoveFinderSuite suite = new BestMoveFinderSuite(DEFAULT_MAX_DEPTH);

        EDPReader reader = new EDPReader();
        List<EDPReader.EDPEntry> edpEntries = reader.readEdpFile(filename);

        for (EDPReader.EDPEntry edpEntry: edpEntries) {
            if (suite.run(edpEntry) == false) {
                failedSuites.add(edpEntry.fen);
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
    }

    private final ChessFactory chessFactory;
    private final int depth;
    public BestMoveFinderSuite(int depth) {
        this(new ChessFactory(), depth);
    }
    public BestMoveFinderSuite(ChessFactory chessFactory, int depth) {
        this.chessFactory = chessFactory;
        this.depth = depth;
    }

    protected boolean run(EDPReader.EDPEntry edpEntry) {
        SearchMove moveFinder = new DefaultSearchMove();

        SearchMoveResult searchResult = moveFinder.searchBestMove(edpEntry.game, depth);

        Move bestMove = searchResult.getBestMove();

        boolean result = edpEntry.bestMoves.contains(bestMove);

        if(result){
            System.out.printf("Success %s\n", edpEntry.fen);
        } else {
            SANEncoder sanEncoder = new SANEncoder();
            System.out.printf("Fail '%s', expected %s, best moves found %s \n", edpEntry.fen,
                    edpEntry.bestMovesString,
                    searchResult.getBestMoveOptions().stream().map(move -> sanEncoder.encode(move, edpEntry.game.getPossibleMoves())).collect(Collectors.toList()));
        }



        return result;
    }

}
