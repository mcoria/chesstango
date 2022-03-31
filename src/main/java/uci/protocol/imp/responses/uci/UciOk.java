/**
 * 
 */
package uci.protocol.imp.responses.uci;

import uci.protocol.UCIResponseSingle;
import uci.protocol.UCIResponseType;

/**
 * @author Mauricio Coria
 *
 */
public class UciOk implements UCIResponseSingle {


	@Override
	public UCIResponseType getType() {
		return UCIResponseType.UCIOK;
	}
	

	@Override
	public String toString() {
		return "uciok";
	}

}
