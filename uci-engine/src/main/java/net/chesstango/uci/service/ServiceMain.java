package net.chesstango.uci.service;

import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.protocol.requests.CmdQuit;
import net.chesstango.uci.protocol.stream.UCIActiveStreamReader;
import net.chesstango.uci.protocol.stream.UCIInputStreamAdapter;
import net.chesstango.uci.protocol.stream.UCIOutputStreamSwitch;
import net.chesstango.uci.protocol.stream.UCIOutputStreamToStringAdapter;
import net.chesstango.uci.protocol.stream.strings.StringConsumer;
import net.chesstango.uci.protocol.stream.strings.StringSupplier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class ServiceMain {
    private final Service service;

    private final UCIActiveStreamReader pipe;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        ServiceMain serviceMain = new ServiceMain(new EngineTango(), System.in, System.out);
        //EngineMain engineMain = new EngineMain(new EngineProxy(), System.in, System.out);

        serviceMain.open();

        serviceMain.waitTermination();
    }

    public ServiceMain(Service service, InputStream in, PrintStream out) {
        this.service = service;
        this.service.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(out))));


        this.pipe = new UCIActiveStreamReader();
        this.pipe.setInputStream(new UCIInputStreamAdapter(new StringSupplier(new InputStreamReader(in))));
        this.pipe.setOutputStream(new UCIOutputStreamSwitch(uciMessage -> uciMessage instanceof CmdQuit, executorService::shutdown)
                .setOutputStream(this.service));
    }


    public void open() {
        service.open();

        executorService.execute(pipe::read);
        //TODO: no podemos esperar que los threads terminen, de lo contrario impedimos la ejecucion de test unitarios
    }


    public void waitTermination() {
        //TODO: no podemos llamar a shutdown() aca, de lo contrario impedimos que Go se ejecute en Zonda
        //      de momento terminamos en EngineZonda.do_quit()
        try {
            while (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                //System.out.println("Engine still executing");
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        service.close();
    }

}
