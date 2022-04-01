/**
 * 
 */
package uci.protocol;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIResponseSingle extends UCIResponse {
	
	UCIResponseType getType();
	
	String encode();

}
