/**
 * 
 */
package uci.protocol;

import uci.engine.Engine;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIRequest {

	UCIRequestType getType();


	UCIResponse execute(Engine engine);
}
