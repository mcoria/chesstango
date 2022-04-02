/**
 * 
 */
package uci.protocol.responses;

import uci.protocol.UCIResponseSingle;

/**
 * @author Mauricio Coria
 *
 */
public abstract class RspAbstractSinlgeResponse implements UCIResponseSingle {

	@Override
	public String toString() {
		return encode();
	}

}
