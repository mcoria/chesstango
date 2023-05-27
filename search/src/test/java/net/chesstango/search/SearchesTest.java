package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.reports.SearchesReport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class SearchesTest {

    @Test
    @Disabled
    public void testSearch_01(){
        SearchMove moveFinder = new DefaultSearchMove();

        Game game = FENDecoder.loadGame("r4rk1/p1qbp1b1/2p3pp/2Pn1p2/1pQ5/5B2/PPP1NPPP/R1B2RK1 w - - 1 22");

        SearchMoveResult searchResult = moveFinder.search( game, 4);

        new SearchesReport()
                .withNodesVisitedStatics()
                .withCutoffStatics()
                .withPrincipalVariation()
                .printSearchesStatics(Arrays.asList(searchResult));
    }

    @Test
    @Disabled
    public void testSearch_02(){
        SearchMove moveFinder = new DefaultSearchMove();

        Game game = FENDecoder.loadGame("1k2r3/1pp5/4B3/1P3Q2/3q1Pp1/3n2Pp/3p3P/5R1K b - - 0 1");

        long hash = game.getChessPosition().getPositionHash();

        SearchMoveResult searchResult = moveFinder.search( game, 6);

        new SearchesReport()
                .withNodesVisitedStatics()
                .withCutoffStatics()
                .withPrincipalVariation()
                .printSearchesStatics(Arrays.asList(searchResult));
    }
}
