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

	/**
	 * @param uciRequest
	 * @return
	 */
	public UCIResponse processRequest(UCIRequest uciRequest) {
		return uciRequest.execute(this);
	}

}
