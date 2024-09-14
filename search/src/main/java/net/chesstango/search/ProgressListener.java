package net.chesstango.search;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface ProgressListener extends Consumer<SearchResultByDepth> {
}
