package net.chesstango.search.alphabeta.transposition.listeners;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author Mauricio Coria
 */
@Slf4j
@Setter
public class TTDump implements Acceptor {
    private TTable tTable;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    public void dumpTable(String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tTable);
            oos.flush();
        } catch (IOException e) {
            log.error("Error dumping TT!", e);
        }
    }

}
