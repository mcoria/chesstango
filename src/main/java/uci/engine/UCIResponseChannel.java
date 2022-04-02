/**
 * 
 */
package uci.engine;

import uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIResponseChannel {

	void send(UCIResponse response);

}
