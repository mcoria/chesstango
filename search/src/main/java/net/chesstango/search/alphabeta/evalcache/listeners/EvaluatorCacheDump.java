package net.chesstango.search.alphabeta.evalcache.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.evalcache.EvaluatorCacheArray;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author Mauricio Coria
 */
@Setter
@Slf4j
public class EvaluatorCacheDump implements Acceptor {

    @Getter
    @Accessors(chain = true)
    private EvaluatorCacheArray gameEvaluatorCacheArray;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void dumpCache(String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameEvaluatorCacheArray);
        } catch (IOException e) {
            log.error("Error dumping cache!", e);
        }
    }
}
