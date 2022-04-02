/**
 * 
 */
package uci.protocol.responses;

import uci.protocol.UCIResponseSingle;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractSinlgeResponse implements UCIResponseSingle{
	

	@Override
	public String toString() {
		return encode();
	}
	

}
