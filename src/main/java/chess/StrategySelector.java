package chess;

import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;

@FunctionalInterface
public interface StrategySelector {
	MoveGenerator getMoveGenerator(MoveGeneratorStrategy strategy);
}
