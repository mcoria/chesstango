/**
 * 
 */
package uci.protocol;

import uci.engine.Engine;

/**
 * @author Mauricio Coria
 *
 */
public class QUIT implements UCIRequest {


	@Override
	public UCIRequestType getType() {
		return UCIRequestType.QUIT;
	}


	@Override
	public UCIResponse execute(Engine engine) {
		return engine.quit();
	}

}
