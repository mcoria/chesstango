package net.chesstango.uci.engine;

import net.chesstango.uci.protocol.requests.CmdQuit;
import net.chesstango.uci.protocol.stream.UCIActivePipe;
import net.chesstango.uci.protocol.stream.UCIInputStreamAdapter;
import net.chesstango.uci.protocol.stream.UCIOutputStreamAdapter;
import net.chesstango.uci.protocol.stream.UCIOutputStreamSwitch;
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
public class EngineMain {
    private final Engine engine;

    private final UCIActivePipe pipe;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        EngineMain engineMain = new EngineMain(new EngineTango(), System.in, System.out);
        //EngineMain engineMain = new EngineMain(new EngineProxy(), System.in, System.out);

        engineMain.open();

        engineMain.waitTermination();
    }

    public EngineMain(Engine engine, InputStream in, PrintStream out) {
        this.engine = engine;
        this.engine.setResponseOutputStream(new UCIOutputStreamAdapter(new StringConsumer(new OutputStreamWriter(out))));


        this.pipe = new UCIActivePipe();
        this.pipe.setInputStream(new UCIInputStreamAdapter(new StringSupplier(new InputStreamReader(in))));
        this.pipe.setOutputStream(new UCIOutputStreamSwitch(uciMessage -> uciMessage instanceof CmdQuit, executorService::shutdown)
                .setOutputStream(this.engine));
    }


    public void open() {
        engine.open();

        executorService.execute(pipe);
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

        engine.close();
    }

}
