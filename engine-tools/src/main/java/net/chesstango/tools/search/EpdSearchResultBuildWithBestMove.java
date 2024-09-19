package net.chesstango.tools.search;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SANEncoder;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EpdSearchResultBuildWithBestMove implements EpdSearchResultBuilder {

    private static final SANEncoder sanEncoder = new SANEncoder();

    @Override
    public EpdSearchResult apply(EPD epd, SearchResult searchResult) {
        Game game = FENDecoder.loadGame(epd.getFenWithoutClocks());

        Move bestMove = searchResult.getBestMove();

        String bestMoveAlgNotation = sanEncoder.encodeAlgebraicNotation(bestMove, game.getPossibleMoves());

        return new EpdSearchResult(epd, searchResult)
                .setSearchSuccess(epd.isMoveSuccess(bestMove))
                .setBestMoveFound(bestMoveAlgNotation)
                .setDepthAccuracyPct(calculateAccuracy(epd, searchResult.getSearchResultByDepths()));
    }


    private int calculateAccuracy(EPD epd, List<SearchResultByDepth> searchResultByDepths) {
        if (!searchResultByDepths.isEmpty()) {
            long successCounter = searchResultByDepths
                    .stream()
                    .map(SearchResultByDepth::getBestMove)
                    .filter(epd::isMoveSuccess)
                    .count();
            return (int) (successCounter * 100 / searchResultByDepths.size());
        }
        return 0;
    }
}
