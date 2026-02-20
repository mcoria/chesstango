package net.chesstango.reports;

/**
 *
 * @author Mauricio Coria
 */
public interface Model<I> {
    Model<I> collectStatistics(String searchGroupName, I input);
}
