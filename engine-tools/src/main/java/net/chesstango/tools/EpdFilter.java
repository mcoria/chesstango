package net.chesstango.tools;

import lombok.Setter;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.epd.EPDDecoder;
import net.chesstango.tools.epdfilters.PlayerFilter;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class EpdFilter {

    @Setter
    private Predicate<EPD> filter;

    public static void main(String[] args) {
        EpdFilter epdFilter = new EpdFilter();

        CommandLine parsedArgs = parseArguments(args);

        if (parsedArgs.hasOption('p')) {
            epdFilter.setFilter(new PlayerFilter(parsedArgs.getOptionValue('p')));
        }

        try (InputStream inputStream = parsedArgs.hasOption('i')
                ? new FileInputStream(parsedArgs.getOptionValue('i'))
                : System.in) {
            epdFilter.process(inputStream, System.out, System.err);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process(InputStream in, PrintStream out, PrintStream err) throws IOException {
        EPDDecoder epdDecoder = new EPDDecoder();
        Stream<EPD> epdStream = epdDecoder.readEdpInputStream(in);
        epdStream
                .filter(filter)
                .forEach(out::println);
    }


    private static CommandLine parseArguments(String[] args) {
        final Options options = new Options();
        Option inputOpt = Option.builder("i")
                .argName("input")
                .hasArg()
                .desc("input file")
                .build();
        options.addOption(inputOpt);

        Option player = Option.builder("p")
                .argName("player")
                .hasArg()
                .desc("Player name filter")
                .build();
        options.addOption(player);


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
