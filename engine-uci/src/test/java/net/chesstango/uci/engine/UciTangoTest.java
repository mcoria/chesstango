package net.chesstango.uci.engine;

import net.chesstango.engine.Config;
import net.chesstango.engine.Session;
import net.chesstango.engine.Tango;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.goyeneche.requests.UCIRequest;
import net.chesstango.goyeneche.stream.UCIOutputStreamToStringAdapter;
import net.chesstango.goyeneche.stream.strings.StringConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class UciTangoTest {
    private UciTango engine;

    @Mock
    private Tango tango;

    @Mock
    private Session session;

    @BeforeEach
    public void setUp() {
        engine = new UciTango(new Config(), config -> tango);

        when(tango.newSession(any(FEN.class))).thenReturn(session);

        engine.open();
    }

    @AfterEach
    public void tearDown() {
        engine.close();
    }

    @Test
    public void test1_execute_position_startpos_01() {
        engine.setOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out))));

        engine.accept(UCIRequest.uci());
        engine.accept(UCIRequest.ucinewgame());
        engine.accept(UCIRequest.position(List.of("e2e4")));

        verify(tango, times(1)).newSession(FEN.of(FENParser.INITIAL_FEN));
        verify(session, times(1)).setMoves(List.of("e2e4"));
    }

    @Test
    public void test1_execute_position_startpos_02() {
        List<String> moveList = List.of("e2e4", "g8h6", "e4e5", "e7e6", "f2f3", "b8c6", "b2b4", "h6f5", "b1c3", "c6a5", "a2a3", "g7g5", "d1e2", "a7a6", "c3d1", "c7c6", "g2g3", "g5g4", "f1h3", "f5d4", "e2f1", "d8e7", "d1b2", "a5c4", "h3g4", "e8d8", "a1b1", "c4e3", "f1h3", "e3d5", "g4h5", "d4e2", "e1f2", "d5f4", "h3f1", "e7c5", "b4c5", "f4d5", "c2c4", "d5c7", "f1e1", "a6a5", "h5g4", "d7d6", "h2h4", "b7b6", "e5d6", "c8a6", "b2a4", "e2f4", "f2f1", "h7h5", "b1b4", "c7d5", "d2d3", "a6b5", "e1e4", "f8e7", "e4e2", "h8h6", "e2c2", "d5f6", "a4b2", "h6h8", "c2h2", "h8h6", "b2a4", "b6c5", "g4f5", "f4d3", "f5h3", "e7f8", "f1e2", "f8e7", "h3f5", "f6d5", "c1h6", "a8c8", "h6e3", "b5a4", "b4b5", "e7f6", "c4d5", "e6f5", "e3g5", "d3c1", "e2e1", "c1e2", "d5c6", "c8b8", "c6c7", "d8e8", "g5h6", "e8d7", "b5b8", "d7e6", "b8h8", "a4c6", "c7c8q");
        engine.setOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out))));
        engine.accept(UCIRequest.uci());
        engine.accept(UCIRequest.ucinewgame());
        engine.accept(UCIRequest.position(moveList));

        verify(tango, times(1)).newSession(FEN.of(FENParser.INITIAL_FEN));
        verify(session, times(1)).setMoves(moveList);
    }

    @Test
    public void test1_execute_position_startpos_03() {
        engine.setOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out))));
        engine.accept(UCIRequest.uci());
        engine.accept(UCIRequest.ucinewgame());

        String moveListStr = "e2e4 e7e6 g1f3 f7f5 e4e5 c7c6 d2d4 d8a5 c1d2 f8b4 c2c3 b4e7 f1e2 a5d5 c3c4 d5e4 e1g1 b7b6 b1c3 e4g4 a2a3 h7h5 h2h3 g4g6 d4d5 c6d5 c4d5 e8f8 d5e6 g6e6 a1c1 c8b7 c3b5 b7f3 e2f3 b8c6 b5c7 e6e5 c7a8 e7d6 g2g3 e5e8 f3c6 d7c6 a8b6 a7b6 d2c3 e8g6 d1f3 d6c5 b2b4 c5e7 c3b2 g6g5 c1c6 g5d2 c6c8 e7d8 f3f5 g8f6 b2f6 g7f6 f5f6 f8g8 c8d8 d2d8 f6d8 g8h7 d8b6 h8g8 a3a4 h5h4 g3g4 h7g7 a4a5 g8f8 a5a6 g7h7 a6a7 f8a8 b6b7 h7g6 b7a8 g6g5 a8b8 g5f6 a7a8q";
        String[] noveListArray = moveListStr.split(" ");
        List<String> moveList = List.of(noveListArray);

        engine.accept(UCIRequest.position(moveList));

        verify(tango, times(1)).newSession(FEN.of(FENParser.INITIAL_FEN));
        verify(session, times(1)).setMoves(moveList);
    }

    @Test
    public void test1_execute_position_fen() {

        engine.setOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out))));

        engine.accept(UCIRequest.uci());
        engine.accept(UCIRequest.ucinewgame());
        engine.accept(UCIRequest.position("rnbqkbrr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", List.of("e2e4")));

        verify(tango, times(1)).newSession(FEN.of("rnbqkbrr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        verify(session, times(1)).setMoves(List.of("e2e4"));
    }


    @Test
    public void test_play() throws IOException, InterruptedException {
        PipedOutputStream posOutput = new PipedOutputStream();
        PipedInputStream pisOutput = new PipedInputStream(posOutput);

        engine.setOutputStream(new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(new PrintStream(posOutput, true)))));

        BufferedReader in = new BufferedReader(new InputStreamReader(pisOutput));

        // Initial state
        assertEquals(WaitCmdUciState.class, engine.getCurrentState().getClass());

        // uci command
        engine.accept(UCIRequest.uci());
        assertTrue(in.readLine().startsWith("id name Tango"));
        assertEquals("id author Mauricio Coria", in.readLine());
        assertEquals("option name PolyglotFile type string default <empty>", in.readLine());
        assertEquals("option name SyzygyDirectory type string default <empty>", in.readLine());
        assertEquals("uciok", in.readLine());
        assertEquals(ReadyState.class, engine.getCurrentState().getClass());

        // isready command
        engine.accept(UCIRequest.isready());
        assertEquals("readyok", in.readLine());
        assertEquals(ReadyState.class, engine.getCurrentState().getClass());

        // ucinewgame command
        engine.accept(UCIRequest.ucinewgame());
        assertEquals(ReadyState.class, engine.getCurrentState().getClass());

        // isready command
        engine.accept(UCIRequest.isready());
        assertEquals("readyok", in.readLine());
        assertEquals(ReadyState.class, engine.getCurrentState().getClass());

        // position command
        engine.accept(UCIRequest.position((List.of("e2e4"))));
        assertEquals
                (WaitCmdGoState.class, engine.getCurrentState().getClass());

        // quit command
        engine.accept(UCIRequest.quit());
    }
}
