package net.chesstango.reports.engine;

import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchByOpenBookResult;
import net.chesstango.engine.SearchByTablebaseResult;
import net.chesstango.engine.SearchByTreeResult;
import net.chesstango.engine.SearchResponse;

import java.util.LinkedList;
import java.util.List;

import static net.chesstango.reports.engine.SearchManagerModel.MoveType.*;

/**
 *
 * @author Mauricio Coria
 */
public class SearchManagerModel {
    public enum MoveType {
        OpenBook,
        Tablebase,
        Tree
    }

    String searchesName;

    long searches;

    int searchByOpenBookCounter;
    int searchByTreeCounter;
    int searchByTablebaseCounter;

    List<SearchManagerModelDetail> moveDetail = new LinkedList<>();

    static class SearchManagerModelDetail {
        String move;
        MoveType type;
    }


    public static SearchManagerModel collectStatics(String reportTitle, List<SearchResponse> searchResponses) {
        SearchManagerModel model = new SearchManagerModel();
        model.searchesName = reportTitle;

        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

        searchResponses.forEach(searchResponse -> {

            SearchManagerModelDetail searchManagerModelDetail = new SearchManagerModelDetail();

            if (searchResponse instanceof SearchByOpenBookResult) {
                searchManagerModelDetail.type = OpenBook;
                model.searchByOpenBookCounter++;
            } else if (searchResponse instanceof SearchByTablebaseResult) {
                searchManagerModelDetail.type = Tablebase;
                model.searchByTablebaseCounter++;
            } else if (searchResponse instanceof SearchByTreeResult) {
                searchManagerModelDetail.type = Tree;
                model.searchByTreeCounter++;
            }
            model.searches++;

            searchManagerModelDetail.move = simpleMoveEncoder.encode(searchResponse.getMove());

            model.moveDetail.add(searchManagerModelDetail);
        });

        return model;
    }
}
