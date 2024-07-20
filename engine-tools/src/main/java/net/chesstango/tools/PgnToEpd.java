package net.chesstango.tools;

import net.chesstango.board.representations.pgn.PGNStringDecoder;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class PgnToEpd {

    /**
     * Run with -i C:\java\projects\chess\chess-utils\testing\positions\players\Kasparov.pgn
     *
     */
    public static void main(String[] args) {

        PgnToEpd pgnToEpd = new PgnToEpd();

        CommandLine parsedArgs = parseArguments(args);

        try (InputStream inputStream = parsedArgs.hasOption('i')
                ? new FileInputStream(parsedArgs.getOptionValue('i'))
                : System.in) {

            pgnToEpd.process(inputStream, System.out, System.err);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process(InputStream inputStream, PrintStream out, PrintStream err) {
        PGNStringDecoder pgnStringDecoder = new PGNStringDecoder();
        pgnStringDecoder.decodePGNs(inputStream)
                .forEach(pgn -> pgn.toEPD()
                        .forEach(out::println)
                );
    }


    private static CommandLine parseArguments(String[] args) {
        final Options options = new Options();
        Option inputOpt = Option.builder("i")
                .argName("input")
                .hasArg()
                .desc("input file")
                .build();
        options.addOption(inputOpt);
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            return parser.parse(options, args);
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            System.exit(-1);
        }
        return null;
    }
}
