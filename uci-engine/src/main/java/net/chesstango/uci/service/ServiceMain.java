package net.chesstango.uci.service;

import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.protocol.stream.UCIActiveStreamReader;
import net.chesstango.uci.protocol.stream.UCIInputStreamAdapter;
import net.chesstango.uci.protocol.stream.UCIOutputStreamToStringAdapter;
import net.chesstango.uci.protocol.stream.strings.StringConsumer;
import net.chesstango.uci.protocol.stream.strings.StringSupplier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class ServiceMain implements Runnable{
    private final Service service;
    private final UCIActiveStreamReader pipe;
    private volatile boolean isRunning;

    public static void main(String[] args) {
        ServiceMain serviceMain = new ServiceMain(new EngineTango(), System.in, System.out);
        //ServiceMain serviceMain = new ServiceMain(new EngineProxy(ProxyConfig.loadEngineConfig("Spike")), System.in, System.out);

        serviceMain.run();
    }

    public ServiceMain(Service service, InputStream in, PrintStream out) {
        this.service = service;
        this.service.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(out))));

        this.pipe = new UCIActiveStreamReader();
        this.pipe.setInputStream(new UCIInputStreamAdapter(new StringSupplier(new InputStreamReader(in))));
        this.pipe.setOutputStream(this.service);
    }

    @Override
    public void run() {
        try {
            service.open();

            isRunning = true;

            pipe.run();

            isRunning = false;

            service.close();
        } catch (RuntimeException e) {
            e.printStackTrace(System.err);
            throw e;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
