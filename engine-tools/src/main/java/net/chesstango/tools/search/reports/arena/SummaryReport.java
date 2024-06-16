package net.chesstango.tools.search.reports.arena;

import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Este reporte resume el resultado de un conjunto de partidos
 *
 * @author Mauricio Coria
 */
public class SummaryReport {

    private final List<ReportRowModel> reportRowModels = new ArrayList<>();

    private PrintStream out;

    public SummaryReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    /**
     * @param matchResults
     */
    public SummaryReport withMatchResults(List<MatchResult> matchResults) {
        Set<String> engineNames = new HashSet<>();

        matchResults.stream().map(MatchResult::getEngineWhite).map(EngineController::getEngineName).forEach(engineNames::add);
        matchResults.stream().map(MatchResult::getEngineBlack).map(EngineController::getEngineName).forEach(engineNames::add);

        engineNames.stream().map(engineName -> createRowModel(engineName, matchResults)).forEach(reportRowModels::add);

        return this;
    }


    private ReportRowModel createRowModel(String engineName, List<MatchResult> matchResult) {
        ReportRowModel row = new ReportRowModel();

        row.engineName = engineName;

        row.wonAsWhite = matchResult.stream().filter(result -> engineName.equals(result.getEngineWhite().getEngineName()) && result.getWinner() == result.getEngineWhite()).count();
        row.lostAsWhite = matchResult.stream().filter(result -> engineName.equals(result.getEngineWhite().getEngineName()) && result.getWinner() == result.getEngineBlack()).count();
        row.drawsAsWhite = matchResult.stream().filter(result -> engineName.equals(result.getEngineWhite().getEngineName()) && result.getWinner() == null).count();
        row.puntosAsWhite = row.wonAsWhite + 0.5 * row.drawsAsWhite;

        row.wonAsBlack = matchResult.stream().filter(result -> engineName.equals(result.getEngineBlack().getEngineName()) && result.getWinner() == result.getEngineBlack()).count();
        row.lostAsBlack = matchResult.stream().filter(result -> engineName.equals(result.getEngineBlack().getEngineName()) && result.getWinner() == result.getEngineWhite()).count();
        row.drawsAsBlack = matchResult.stream().filter(result -> engineName.equals(result.getEngineBlack().getEngineName()) && result.getWinner() == null).count();
        row.puntosAsBlack = row.wonAsBlack + 0.5 * row.drawsAsBlack;

        row.puntosTotal = row.puntosAsWhite + row.puntosAsBlack;
        row.playedGames = matchResult.stream().filter(result -> engineName.equals(result.getEngineWhite().getEngineName()) || engineName.equals(result.getEngineBlack().getEngineName())).count();

        row.winPercentage = (row.puntosTotal / row.playedGames) * 100;

        return row;
    }


    private void print() {
        out.printf(" ___________________________________________________________________________________________________________________________________________________\n");
        out.printf("|ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %%|\n");
        reportRowModels.forEach(row -> {
            out.printf("|%35s|%8d |%8d |%9d |%9d |%9d |%9d |%11.1f |%11.1f |%6.1f /%3d | %6.1f |\n", row.engineName, row.wonAsWhite, row.wonAsBlack, row.lostAsWhite, row.lostAsBlack, row.drawsAsWhite, row.drawsAsBlack, row.puntosAsWhite, row.puntosAsBlack, row.puntosTotal, row.playedGames, row.winPercentage);
        });
        out.printf(" ---------------------------------------------------------------------------------------------------------------------------------------------------\n");
    }


    private static class ReportRowModel {
        String engineName;
        long wonAsWhite;
        long wonAsBlack;
        long lostAsWhite;
        long lostAsBlack;
        long drawsAsWhite;
        long drawsAsBlack;
        double puntosAsWhite;
        double puntosAsBlack;
        double puntosTotal;
        long playedGames;
        double winPercentage;
    }

}

