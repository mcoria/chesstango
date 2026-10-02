package net.chesstango.search.visitors;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Game;
import net.chesstango.gardel.pgn.PGN;
import net.chesstango.search.IterativeDeepening;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.NoIterativeDeepening;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.evalcache.listeners.EvaluatorCacheDump;
import net.chesstango.search.alphabeta.transposition.listeners.TTDump;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Esta clase recorre ella misma toda la estructura
 *
 * @author Mauricio Coria
 */
@Slf4j
public class DumperVisitor implements Visitor {

    private final PGN pgn;
    private final String uuid;

    public DumperVisitor(String uuid, Game game) {
        this.pgn = game.toPGN();
        this.uuid = uuid;
    }

    @Override
    public void visit(NoIterativeDeepening noIterativeDeepening) {
        ListenerMediator listenerMediator = noIterativeDeepening.getListenerMediator();
        dumpState(listenerMediator);
    }


    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        ListenerMediator listenerMediator = iterativeDeepening.getListenerMediator();
        dumpState(listenerMediator);
    }

    @Override
    public void visit(TTDump ttDump) {
        ttDump.dumpTable(String.format("%s-TT.ser", uuid));
    }

    @Override
    public void visit(EvaluatorCacheDump evaluatorCacheDump) {
        evaluatorCacheDump.dumpCache(String.format("%s-EVALCACHE.ser", uuid));
    }

    void dumpState(ListenerMediator listenerMediator) {
        listenerMediator.accept(this);
        dumpGame();
    }

    private void dumpGame() {
        String fileName = String.format("%s-GAME.txt", uuid);

        // Pass 'true' as the second argument to FileWriter to enable APPEND mode
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            writer.write(pgn.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
