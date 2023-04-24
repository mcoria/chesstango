package net.chesstango.uci.arena.reports;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.pgn.PGNEncoder;
import net.chesstango.engine.Session;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class CutoffReports {

    public void printTangoStatics(List<EngineController> enginesOrder, List<GameResult> matchResult) {
        List<ReportModel> reportRows = new ArrayList<>();

        enginesOrder.forEach(engineController -> {
            matchResult.stream().filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null).map(result -> collectStatics(result, result.getSessionWhite())).forEach(reportRows::add);

            matchResult.stream().filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null).map(result -> collectStatics(result, result.getSessionBlack())).forEach(reportRows::add);

        });

        reportRows.forEach(this::print);
    }

    private void print(ReportModel report) {
        //System.out.printf("%s\n", report.pgnGame);

        System.out.println("\n Cutoff per search level (higher is better)");

        // Marco superior de la tabla
        System.out.printf(" ________");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("___________"));
        System.out.printf("\n");

        // Nombre de las columnas
        System.out.printf("| Move   ");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| Level %2d ", depth + 1));
        System.out.printf("|\n");

        // Cuerpo
        for (ReportRowMoveDetail moveDetail: report.moveDetails) {
            System.out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("| %6d %% ", moveDetail.cuttoffPercentages[depth]));
            System.out.printf("|\n");
        }

        // Marco inferior de la tabla
        System.out.printf(" --------");
        IntStream.range(0, report.maxSearchLevel).forEach(depth -> System.out.printf("-----------"));
        System.out.printf("\n");

    }

    private ReportModel collectStatics(GameResult result, Session session) {
        ReportModel reportRowModel = new ReportModel();

        reportRowModel.pgnGame = new PGNEncoder().encode(result.getPgnGame());
        reportRowModel.moveDetails = new ArrayList<>();

        session.getMoveResultList().forEach(searchMoveResult -> {
            Move bestMove = searchMoveResult.getBestMove();

            ReportRowMoveDetail moveDetail = new ReportRowMoveDetail();
            moveDetail.move = String.format("%s%s", bestMove.getFrom().getSquare(), bestMove.getTo().getSquare());


            int[] expectedNodesCounters = searchMoveResult.getExpectedNodesCounters();
            int[] visitedNodesCounters = searchMoveResult.getVisitedNodesCounters();
            int[] cuttoffPercentages = new int[30];

            for (int i = 0; i < 30; i++) {
                if (expectedNodesCounters[i] > 0) {
                    cuttoffPercentages[i] = (int) (100 - (100 * visitedNodesCounters[i] / expectedNodesCounters[i]));

                    if(reportRowModel.maxSearchLevel < i) {
                        reportRowModel.maxSearchLevel = i;
                    }
                }

                if (expectedNodesCounters[i] == 0 && visitedNodesCounters[i] > 0) {
                    throw new RuntimeException("expectedNodesCounters[i]  == 0");
                }
            }

            moveDetail.expectedNodesCounters = expectedNodesCounters;
            moveDetail.visitedNodesCounters = visitedNodesCounters;
            moveDetail.cuttoffPercentages = cuttoffPercentages;

            reportRowModel.moveDetails.add(moveDetail);
        });


        return reportRowModel;
    }

    private static class ReportModel {
        String pgnGame;

        int maxSearchLevel;

        List<ReportRowMoveDetail> moveDetails;
    }

    private static class ReportRowMoveDetail {
        String move;
        int[] expectedNodesCounters;
        int[] visitedNodesCounters;
        int[] cuttoffPercentages;

    }
}
