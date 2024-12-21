package net.chesstango.uci.engine.proxy;

import net.chesstango.uci.engine.Service;
import net.chesstango.uci.engine.ServiceVisitor;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.stream.UCIActiveStreamReader;
import net.chesstango.uci.protocol.stream.UCIInputStreamAdapter;
import net.chesstango.uci.protocol.stream.UCIOutputStream;
import net.chesstango.uci.protocol.stream.strings.StringActionSupplier;
import net.chesstango.uci.protocol.stream.strings.StringSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class UciProxy implements Service {
    private static final Logger logger = LoggerFactory.getLogger(UciProxy.class);

    private final UCIActiveStreamReader pipe;

    private UCIOutputStream responseOutputStream;
    private Thread readingPipeThread;
    private UciProcess uciProcess;


    /**
     * Para que Spike pueda leer sus settings, el working directory debe ser el del ejecutable.
     * Los settings generales para todos los engines se controlan desde EngineManagement -> UCI en Arena.
     */
    public UciProxy(ProxyConfig config) {
        this.pipe = new UCIActiveStreamReader();
        this.uciProcess = new UciProcess(config);
    }

    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        serviceVisitor.visit(this);
    }

    @Override
    public void accept(UCIMessage message) {
        if (uciProcess.outputStreamProcess == null) {
            uciProcess.waitProcessStart();
        }

        logger.trace("proxy >> {}", message);

        uciProcess.outputStreamProcess.println(message);
    }

    @Override
    public void open() {
        uciProcess.startProcess();

        Supplier<String> stringSupplier = new StringSupplier(new InputStreamReader(uciProcess.inputStreamProcess));

        stringSupplier = new StringActionSupplier(stringSupplier, line -> logger.trace("proxy << {}", line));

        pipe.setInputStream(new UCIInputStreamAdapter(stringSupplier));
        pipe.setOutputStream(responseOutputStream);

        readingPipeThread = new Thread(this::readFromProcess);
        readingPipeThread.start();
    }

    @Override
    public void close() {
        pipe.stopReading();

        uciProcess.closeProcessIO();

        try {
            readingPipeThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        uciProcess.stopProcess();
    }

    @Override
    public void setResponseOutputStream(UCIOutputStream output) {
        this.responseOutputStream = output;
    }

    private void readFromProcess() {
        logger.debug("readFromPipe(): start reading engine output");
        pipe.run();
        logger.debug("readFromPipe():end reading engine output");
    }
}
