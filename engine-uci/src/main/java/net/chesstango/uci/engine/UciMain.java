package net.chesstango.uci.engine;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.engine.Tango;
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
public class UciMain implements Runnable, AutoCloseable {

    private final UciTango uciTango;

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

        try (UciMain uciMain = new UciMain(System.in, System.out)) {

            uciMain.run();

            System.exit(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UciMain(InputStream in, PrintStream out) {
        this.pipe = new UCIActiveStreamReader();
        this.uciTango = new UciTango();
        this.uciTango.setConsumer(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(out))));
        this.pipe.setInputStream(new UCIInputStreamFromStringAdapter(new StringSupplier(new InputStreamReader(in))));
        this.pipe.setOutputStream(uciTango::accept);
    }

    @Override
    public void run() {
        try {
            log.info("{} {} by {}", Tango.ENGINE_NAME, Tango.ENGINE_AUTHOR, Tango.ENGINE_VERSION);

            isRunning = true;

            pipe.run();

        } catch (RuntimeException e) {
            log.error("Error:", e);
            throw e;
        } finally {
            isRunning = false;
        }
    }

    @Override
    public void close() throws Exception {
        uciTango.close();
    }
}
