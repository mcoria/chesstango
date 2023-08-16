package net.chesstango.uci.service;

import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.protocol.stream.UCIActiveStreamReader;
import net.chesstango.uci.protocol.stream.UCIInputStreamAdapter;
import net.chesstango.uci.protocol.stream.UCIOutputStreamToStringAdapter;
import net.chesstango.uci.protocol.stream.strings.StringConsumer;
import net.chesstango.uci.protocol.stream.strings.StringSupplier;

import java.io.*;

/**
 * @author Mauricio Coria
 */
public class UciMain implements Runnable{
    private final Service service;
    private final InputStream in;

    private final PrintStream out;

    private final UCIActiveStreamReader pipe;
    private volatile boolean isRunning;

    public static void main(String[] args) {
        UciMain uciMain = new UciMain(new UciTango(), System.in, System.out);
        //ServiceMain serviceMain = new ServiceMain(new EngineProxy(ProxyConfig.loadEngineConfig("Spike")), System.in, System.out);

        uciMain.run();
    }

    public UciMain(Service service, InputStream in, PrintStream out) {
        this.service = service;
        this.in = in;
        this.out = out;
        this.pipe = new UCIActiveStreamReader();

    }

    @Override
    public void run() {
        this.service.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(out))));
        this.pipe.setInputStream(new UCIInputStreamAdapter(new StringSupplier(new InputStreamReader(in))));
        this.pipe.setOutputStream(this.service);

        try {
            service.open();

            isRunning = true;

            pipe.run();

            isRunning = false;

            service.close();
        } catch (RuntimeException e) {
            e.printStackTrace(System.err);
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            out.close();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}