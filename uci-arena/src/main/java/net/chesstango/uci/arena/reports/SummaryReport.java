package net.chesstango.uci.arena.reports;

import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Este reporte resume el resultado de un conjunto de partidos
 *
 * @author Mauricio Coria
 */
public class SummaryReport {

    /**
     * Cada elemento de enginesCollections es una coleccion de instancias del mismo engine.
     *
     * @param enginesCollections
     * @param matchResult
     */
    public void printReportMultipleEngineInstances(List<List<EngineController>> enginesCollections, List<GameResult> matchResult) {
        List<ReportRowModel> rows = new ArrayList<>();
        enginesCollections.forEach(controllerList -> rows.add(createRowModel(controllerList, matchResult)));
        printReport(rows);
    }

    /**
     * Cada elemento de engines es una instancia de un engine.
     *
     * @param engines
     * @param matchResult
     */
    public void printReportSingleEngineInstance(List<EngineController> engines, List<GameResult> matchResult) {
        List<ReportRowModel> rows = new ArrayList<>();
        engines.forEach(engine -> rows.add(createRowModel(Arrays.asList(engine), matchResult)));
        printReport(rows);
    }

    private ReportRowModel createRowModel(List<EngineController> engineInstances, List<GameResult> matchResult) {
        ReportRowModel row = new ReportRowModel();

        Set<String> engineNames = engineInstances.stream().map(EngineController::getEngineName).collect(Collectors.toSet());
        if (engineNames.size() != 1) {
            throw new RuntimeException("Expected single name, actual " + engineNames.size());
        }

        row.engineName = engineNames.stream().findAny().get();

        row.wonAsWhite = matchResult.stream().filter(result -> engineInstances.contains(result.getEngineWhite()) && result.getWinner() == result.getEngineWhite()).count();
        row.lostAsWhite = matchResult.stream().filter(result -> engineInstances.contains(result.getEngineWhite()) && result.getWinner() == result.getEngineBlack()).count();
        row.drawsAsWhite = matchResult.stream().filter(result -> engineInstances.contains(result.getEngineWhite()) && result.getWinner() == null).count();
        row.puntosAsWhite = row.wonAsWhite + 0.5 * row.drawsAsWhite;

        row.wonAsBlack = matchResult.stream().filter(result -> engineInstances.contains(result.getEngineBlack()) && result.getWinner() == result.getEngineBlack()).count();
        row.lostAsBlack = matchResult.stream().filter(result -> engineInstances.contains(result.getEngineBlack()) && result.getWinner() == result.getEngineWhite()).count();
        row.drawsAsBlack = matchResult.stream().filter(result -> engineInstances.contains(result.getEngineBlack()) && result.getWinner() == null).count();
        row.puntosAsBlack = row.wonAsBlack + 0.5 * row.drawsAsBlack;

        row.puntosTotal = row.puntosAsWhite + row.puntosAsBlack;
        row.playedGames = matchResult.stream().filter(result -> engineInstances.contains(result.getEngineWhite()) || engineInstances.contains(result.getEngineBlack())).count();

        row.winPercentage = (row.puntosTotal / row.playedGames) * 100;

        return row;
    }


    private void printReport(List<ReportRowModel> reportRows) {
        System.out.printf(" ___________________________________________________________________________________________________________________________________________________\n");
        System.out.printf("|ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %%|\n");
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%8d |%8d |%9d |%9d |%9d |%9d |%11.1f |%11.1f |%6.1f /%3d | %6.1f |\n", row.engineName, row.wonAsWhite, row.wonAsBlack, row.lostAsWhite, row.lostAsBlack, row.drawsAsWhite, row.drawsAsBlack, row.puntosAsWhite, row.puntosAsBlack, row.puntosTotal, row.playedGames, row.winPercentage);
        });
        System.out.printf(" ---------------------------------------------------------------------------------------------------------------------------------------------------\n");
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

