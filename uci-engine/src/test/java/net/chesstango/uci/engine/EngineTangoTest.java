/**
 *
 */
package net.chesstango.uci.engine;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.engine.Tango;
import net.chesstango.search.SearchMove;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.stream.UCIOutputStreamToStringAdapter;
import net.chesstango.uci.protocol.stream.strings.StringConsumer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Mauricio Coria
 */
@RunWith(MockitoJUnitRunner.class)
public class EngineTangoTest {
    private EngineTango engine;

    @Mock
    private Tango tango;

    @Before
    public void setUp() {
        engine = new EngineTango() {
            @Override
            protected Tango createTango(SearchMove searchMove) {
                return EngineTangoTest.this.tango;
            }
        };

        engine.open();
    }

    @After
    public void tearDown() {
        engine.close();
    }

    @Test
    public void test1_execute_position_startpos_01() {


        engine.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out))));

        engine.accept(new CmdUci());
        engine.accept(new CmdUciNewGame());
        engine.accept(new CmdPosition(Arrays.asList("e2e4")));

        verify(tango, times(1)).newGame();
        verify(tango, times(1)).setPosition(FENDecoder.INITIAL_FEN, Arrays.asList("e2e4"));
    }

    @Test
    public void test1_execute_position_startpos_02() {
        List<String> moveList = Arrays.asList("e2e4", "g8h6", "e4e5", "e7e6", "f2f3", "b8c6", "b2b4", "h6f5", "b1c3", "c6a5", "a2a3", "g7g5", "d1e2", "a7a6", "c3d1", "c7c6", "g2g3", "g5g4", "f1h3", "f5d4", "e2f1", "d8e7", "d1b2", "a5c4", "h3g4", "e8d8", "a1b1", "c4e3", "f1h3", "e3d5", "g4h5", "d4e2", "e1f2", "d5f4", "h3f1", "e7c5", "b4c5", "f4d5", "c2c4", "d5c7", "f1e1", "a6a5", "h5g4", "d7d6", "h2h4", "b7b6", "e5d6", "c8a6", "b2a4", "e2f4", "f2f1", "h7h5", "b1b4", "c7d5", "d2d3", "a6b5", "e1e4", "f8e7", "e4e2", "h8h6", "e2c2", "d5f6", "a4b2", "h6h8", "c2h2", "h8h6", "b2a4", "b6c5", "g4f5", "f4d3", "f5h3", "e7f8", "f1e2", "f8e7", "h3f5", "f6d5", "c1h6", "a8c8", "h6e3", "b5a4", "b4b5", "e7f6", "c4d5", "e6f5", "e3g5", "d3c1", "e2e1", "c1e2", "d5c6", "c8b8", "c6c7", "d8e8", "g5h6", "e8d7", "b5b8", "d7e6", "b8h8", "a4c6", "c7c8q");
        engine.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out))));
        engine.accept(new CmdUci());
        engine.accept(new CmdUciNewGame());
        engine.accept(new CmdPosition(moveList));

        verify(tango, times(1)).newGame();
        verify(tango, times(1)).setPosition(FENDecoder.INITIAL_FEN, moveList);
    }

    @Test
    public void test1_execute_position_startpos_03() {
        engine.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out))));
        engine.accept(new CmdUci());
        engine.accept(new CmdUciNewGame());

        String moveListStr = "e2e4 e7e6 g1f3 f7f5 e4e5 c7c6 d2d4 d8a5 c1d2 f8b4 c2c3 b4e7 f1e2 a5d5 c3c4 d5e4 e1g1 b7b6 b1c3 e4g4 a2a3 h7h5 h2h3 g4g6 d4d5 c6d5 c4d5 e8f8 d5e6 g6e6 a1c1 c8b7 c3b5 b7f3 e2f3 b8c6 b5c7 e6e5 c7a8 e7d6 g2g3 e5e8 f3c6 d7c6 a8b6 a7b6 d2c3 e8g6 d1f3 d6c5 b2b4 c5e7 c3b2 g6g5 c1c6 g5d2 c6c8 e7d8 f3f5 g8f6 b2f6 g7f6 f5f6 f8g8 c8d8 d2d8 f6d8 g8h7 d8b6 h8g8 a3a4 h5h4 g3g4 h7g7 a4a5 g8f8 a5a6 g7h7 a6a7 f8a8 b6b7 h7g6 b7a8 g6g5 a8b8 g5f6 a7a8q";
        String noveListArray[] = moveListStr.split(" ");
        List<String> moveList = Arrays.asList(noveListArray);

        engine.accept(new CmdPosition(moveList));

        verify(tango, times(1)).newGame();
        verify(tango, times(1)).setPosition(FENDecoder.INITIAL_FEN, moveList);
    }

    @Test
    public void test1_execute_position_fen() {

        engine.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out))));

        engine.accept(new CmdUci());
        engine.accept(new CmdUciNewGame());
        engine.accept(new CmdPosition("rnbqkbrr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", Arrays.asList("e2e4")));

        verify(tango, times(1)).newGame();
        verify(tango, times(1)).setPosition("rnbqkbrr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", Arrays.asList("e2e4"));
    }


    @Test
    public void test_play() throws IOException, InterruptedException {
        PipedOutputStream posOutput = new PipedOutputStream();
        PipedInputStream pisOutput = new PipedInputStream(posOutput);

        engine.setResponseOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(new PrintStream(posOutput, true)))));

        BufferedReader in = new BufferedReader(new InputStreamReader(pisOutput));

        // Initial state
        Assert.assertEquals(WaitCmdUci.class, engine.currentState.getClass());

        // uci command
        engine.accept(new CmdUci());
        Assert.assertEquals("id name Tango", in.readLine());
        Assert.assertEquals("id author Mauricio Coria", in.readLine());
        Assert.assertEquals("uciok", in.readLine());
        Assert.assertEquals(Ready.class, engine.currentState.getClass());

        // isready command
        engine.accept(new CmdIsReady());
        Assert.assertEquals("readyok", in.readLine());
        Assert.assertEquals(Ready.class, engine.currentState.getClass());

        // ucinewgame command
        engine.accept(new CmdUciNewGame());
        Assert.assertEquals(Ready.class, engine.currentState.getClass());

        // isready command
        engine.accept(new CmdIsReady());
        Assert.assertEquals("readyok", in.readLine());
        Assert.assertEquals(Ready.class, engine.currentState.getClass());

        // position command
        engine.accept(new CmdPosition(Arrays.asList("e2e4")));
        Assert.assertEquals
                (WaitCmdGo.class, engine.currentState.getClass());

        // quit command
        engine.accept(new CmdQuit());
    }

    @Test
    public void testAndThen() {
        Consumer<String> consumer1 = string -> System.out.println("From consumer1 : " + string);

        Consumer<String> consumer2 = consumer1.andThen(string2 -> System.out.println("From consumer2 : " + string2));

        consumer1.accept("A");

        System.out.println("------------------");

        consumer2.accept("B");
    }

    private String fenCode(Game board) {
        FENEncoder coder = new FENEncoder();
        board.getChessPosition().constructBoardRepresentation(coder);
        return coder.getChessRepresentation();
    }
}
