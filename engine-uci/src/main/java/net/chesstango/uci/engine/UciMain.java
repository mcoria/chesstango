package net.chesstango.uci.engine;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.engine.Tango;
import net.chesstango.goyeneche.UCIService;
import net.chesstango.goyeneche.stream.UCIActiveStreamReader;
import net.chesstango.goyeneche.stream.UCIInputStreamFromStringAdapter;
import net.chesstango.goyeneche.stream.UCIOutputStreamToStringAdapter;
import net.chesstango.goyeneche.stream.strings.StringConsumer;
import net.chesstango.goyeneche.stream.strings.StringSupplier;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Mauricio Coria
 */
@Slf4j
public class UciMain implements Runnable {

    private final UCIService service;

    private final InputStream in;

    private final PrintStream out;

    private final UCIActiveStreamReader pipe;

    @Getter
    private volatile boolean isRunning;


    public static void main(String[] args) {
        String julFile = System.getProperty("java.util.logging.config.file");
        if (julFile == null) {
            // Get the root logger
            Logger rootLogger = LogManager.getLogManager().getLogger("");
            // By default set the level to OFF
            rootLogger.setLevel(Level.OFF);
        } else {
            // GralVM hace caso omiso de java.util.logging.config.file y configura LogManager a su antojo (loggea a consola)
            // Aca basicamente forzamos el mecanismo del JDK
            // NO QUITAR ESTA SECCION DE LO CONTRARIO NO CARGA el archivo de configuracion cuando se ejecuta una imagen nativa
            try (InputStream configStream = new FileInputStream(julFile)) {
                LogManager.getLogManager().readConfiguration(configStream);
            } catch (IOException e) {
                log.error("e: ", e);
                System.exit(1);
            }
        }

        UciMain uciMain = new UciMain(new UciTango(), System.in, System.out);

        uciMain.run();
    }

    public UciMain(UCIService service, InputStream in, PrintStream out) {
        this.service = service;
        this.in = in;
        this.out = out;
        this.pipe = new UCIActiveStreamReader();
        this.service.setOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(out))));
        this.pipe.setInputStream(new UCIInputStreamFromStringAdapter(new StringSupplier(new InputStreamReader(in))));
        this.pipe.setOutputStream(service::accept);
    }

    @Override
    public void run() {
        try {
            log.info("{} {} by {}", Tango.ENGINE_NAME, Tango.ENGINE_AUTHOR, Tango.ENGINE_VERSION);

            service.open();

            isRunning = true;

            pipe.run();

            isRunning = false;

            service.close();
        } catch (RuntimeException e) {
            log.error("Error:", e);
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error("Error:", e);
            }
            out.close();
        }
    }

}
