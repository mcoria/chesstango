/**
 * 
 */
package uci.engine;

import uci.protocol.UCIRequest;
import uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 *
 */
public class Engine {

	private boolean keepProcessing = true;

	/**
	 * @param uciRequest
	 * @return
	 */
	public UCIResponse processRequest(UCIRequest uciRequest) {
		return uciRequest.execute(this);
	}

	/**
	 * @return
	 */
	public UCIResponse quit() {
		keepProcessing = false;
		return null;
	}

	public boolean keepProcessing() {
		return keepProcessing;
	}

}
