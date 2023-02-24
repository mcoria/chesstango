package net.chesstango.uci.service;

import net.chesstango.uci.protocol.stream.UCIOutputStream;

/**
 * @author Mauricio Coria
 *
 * El servicio recibe mensajes UCI por ser UCIOutputStream y
 * y escribe toda respuesta en el OutputStream que recibe como referencia.
 *
 * La interfaz podria servir para implementar engine como gui.
 *
 */
public interface UCIService extends UCIOutputStream {
    void open();
    void close();
    void setResponseOutputStream(UCIOutputStream output);
}
