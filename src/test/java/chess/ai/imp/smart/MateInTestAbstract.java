/**
 * 
 */
package chess.ai.imp.smart;

/**
 * @author Mauricio Coria
 *
 */
public abstract class MateInTestAbstract {

	protected AbstractSmart bestMoveFinder = null;

	protected abstract int getMaxLevel();

	public void setUp() {
		//bestMoveFinder = new MinMax(getMaxLevel());
		bestMoveFinder = new MinMaxPruning(getMaxLevel());
	}

}