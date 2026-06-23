package net.chesstango.uci.engine;

import net.chesstango.engine.Config;
import net.chesstango.engine.Session;
import net.chesstango.engine.Tango;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.goyeneche.requests.UCIRequest;
import net.chesstango.goyeneche.stream.UCIOutputStreamToStringAdapter;
import net.chesstango.goyeneche.stream.strings.StringConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static net.chesstango.uci.engine.UciOption.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class UciTangoTest {

    @Mock
    private Tango tango;

    @Mock
    private Session session;


    @Test
    public void shouldSetPolyglotFileWhenOptionProvided() {
        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out)));

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            engine.setUCIOutputStream(outputStream);
            engine.accept(UCIRequest.uci());
            engine.accept(UCIRequest.setOption(POLYGLOT_FILE.getId(), "/mnt/PolyglotFile.bin"));
        }

        verify(tango, times(1)).setPolyglotFile(Path.of("/mnt/PolyglotFile.bin"));
    }

    @Test
    public void shouldNotSetPolyglotFileWhenNoOptionProvided() {
        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out)));

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            engine.setUCIOutputStream(outputStream);
            engine.accept(UCIRequest.uci());
            engine.accept(UCIRequest.setOption(POLYGLOT_FILE.getId(), ""));
        }

        verify(tango, never()).setPolyglotFile(any(Path.class));
    }


    @Test
    public void shouldSetSyzygyPathWhenOptionProvided() {
        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out)));

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            engine.setUCIOutputStream(outputStream);
            engine.accept(UCIRequest.uci());
            engine.accept(UCIRequest.setOption(SYZYGY_PATH.getId(), "/mnt/Syzygy"));
        }

        verify(tango, times(1)).setSyzygyPath(Path.of("/mnt/Syzygy"));
    }


    @Test
    public void shouldNotSyzygyPathWhenNoOptionProvided() {
        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out)));

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            engine.setUCIOutputStream(outputStream);
            engine.accept(UCIRequest.uci());
            engine.accept(UCIRequest.setOption(SYZYGY_PATH.getId(), ""));
        }

        verify(tango, never()).setSyzygyPath(any(Path.class));
    }


    @Test
    public void shouldSetHashSizeWhenOptionProvided() {
        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out)));

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            engine.setUCIOutputStream(outputStream);
            engine.accept(UCIRequest.uci());
            engine.accept(UCIRequest.setOption(HASH_SIZE.getId(), "32"));
        }

        verify(tango, times(1)).setHashSize(32);
    }

    @Test
    public void shouldExecutePositionWithSingleMove() {
        when(tango.newSession(any())).thenReturn(session);

        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out)));

        List<String> moveList = List.of("e2e4");

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            engine.setUCIOutputStream(outputStream);
            engine.accept(UCIRequest.uci());
            engine.accept(UCIRequest.ucinewgame());
            engine.accept(UCIRequest.position(moveList));
            engine.accept(UCIRequest.goInfinite());
        }

        verify(tango, times(1)).newSession(any());
        verify(session, times(1)).setMoves(moveList);
        verify(session, times(1)).goInfinite();
    }

    @Test
    public void shouldExecutePositionWithLongMoveSequence() {
        when(tango.newSession(any())).thenReturn(session);

        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out)));

        List<String> moveList = List.of("e2e4", "g8h6", "e4e5", "e7e6", "f2f3", "b8c6", "b2b4", "h6f5", "b1c3", "c6a5", "a2a3", "g7g5", "d1e2", "a7a6", "c3d1", "c7c6", "g2g3", "g5g4", "f1h3", "f5d4", "e2f1", "d8e7", "d1b2", "a5c4", "h3g4", "e8d8", "a1b1", "c4e3", "f1h3", "e3d5", "g4h5", "d4e2", "e1f2", "d5f4", "h3f1", "e7c5", "b4c5", "f4d5", "c2c4", "d5c7", "f1e1", "a6a5", "h5g4", "d7d6", "h2h4", "b7b6", "e5d6", "c8a6", "b2a4", "e2f4", "f2f1", "h7h5", "b1b4", "c7d5", "d2d3", "a6b5", "e1e4", "f8e7", "e4e2", "h8h6", "e2c2", "d5f6", "a4b2", "h6h8", "c2h2", "h8h6", "b2a4", "b6c5", "g4f5", "f4d3", "f5h3", "e7f8", "f1e2", "f8e7", "h3f5", "f6d5", "c1h6", "a8c8", "h6e3", "b5a4", "b4b5", "e7f6", "c4d5", "e6f5", "e3g5", "d3c1", "e2e1", "c1e2", "d5c6", "c8b8", "c6c7", "d8e8", "g5h6", "e8d7", "b5b8", "d7e6", "b8h8", "a4c6", "c7c8q");

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            engine.setUCIOutputStream(outputStream);
            engine.accept(UCIRequest.uci());
            engine.accept(UCIRequest.ucinewgame());
            engine.accept(UCIRequest.position(moveList));
            engine.accept(UCIRequest.goDepth(5));
        }

        verify(tango, times(1)).newSession(any());
        verify(session, times(1)).setMoves(moveList);
        verify(session, times(1)).goDepth(5);
    }

    @Test
    public void shouldExecutePositionWithComplexGameMoves() {
        when(tango.newSession(any())).thenReturn(session);

        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out)));

        List<String> moveList = List.of("e2e4", "e7e6", "g1f3", "f7f5", "e4e5", "c7c6", "d2d4", "d8a5", "c1d2", "f8b4", "c2c3", "b4e7", "f1e2", "a5d5", "c3c4", "d5e4", "e1g1", "b7b6", "b1c3", "e4g4", "a2a3", "h7h5", "h2h3", "g4g6", "d4d5", "c6d5", "c4d5", "e8f8", "d5e6", "g6e6", "a1c1", "c8b7", "c3b5", "b7f3", "e2f3", "b8c6", "b5c7", "e6e5", "c7a8", "e7d6", "g2g3", "e5e8", "f3c6", "d7c6", "a8b6", "a7b6", "d2c3", "e8g6", "d1f3", "d6c5", "b2b4", "c5e7", "c3b2", "g6g5", "c1c6", "g5d2", "c6c8", "e7d8", "f3f5", "g8f6", "b2f6", "g7f6", "f5f6", "f8g8", "c8d8", "d2d8", "f6d8", "g8h7", "d8b6", "h8g8", "a3a4", "h5h4", "g3g4", "h7g7", "a4a5", "g8f8", "a5a6", "g7h7", "a6a7", "f8a8", "b6b7", "h7g6", "b7a8", "g6g5", "a8b8", "g5f6", "a7a8q");

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            engine.setUCIOutputStream(outputStream);
            engine.accept(UCIRequest.uci());
            engine.accept(UCIRequest.ucinewgame());
            engine.accept(UCIRequest.position(moveList));
            engine.accept(UCIRequest.goFast(1000, 10, 2000, 20));
        }

        verify(tango, times(1)).newSession(any());
        verify(session, times(1)).setMoves(moveList);
        verify(session, times(1)).goFast(1000, 10, 2000, 20);
    }

    @Test
    public void shouldExecutePositionWithCustomFen() {
        when(tango.newSession(any())).thenReturn(session);

        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out)));

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            engine.setUCIOutputStream(outputStream);
            engine.accept(UCIRequest.uci());
            engine.accept(UCIRequest.ucinewgame());
            engine.accept(UCIRequest.position("rnbqkbrr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", List.of("e2e4")));
        }

        verify(tango, times(1)).newSession(any());
        verify(session, times(1)).setMoves(List.of("e2e4"));
    }

    @Test
    public void shouldExecutePositionWithMultiplePositionCommands() {
        FEN fen = FEN.from("8/p5pp/1pk5/5p2/P1nn4/2NN3P/5PPK/8 w - - 0 1");

        when(tango.newSession(fen)).thenReturn(session);
        when(session.getFen()).thenReturn(fen);

        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(System.out)));

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            engine.setUCIOutputStream(outputStream);
            engine.accept(UCIRequest.uci());
            engine.accept(UCIRequest.ucinewgame());
            engine.accept(UCIRequest.position(fen.toString(), Collections.emptyList()));
            engine.accept(UCIRequest.position(fen.toString(), List.of("g2g3")));
        }

        verify(tango, times(1)).newSession(fen);
        verify(session).setMoves(Collections.emptyList());
        verify(session).setMoves(List.of("g2g3"));
    }

    @Test
    public void shouldTransitionThroughStatesCorrectly() throws IOException {
        when(tango.newSession(any())).thenReturn(session);

        PipedOutputStream posOutput = new PipedOutputStream();
        PipedInputStream pisOutput = new PipedInputStream(posOutput);

        UCIOutputStreamToStringAdapter outputStream = new UCIOutputStreamToStringAdapter(new StringConsumer(new OutputStreamWriter(new PrintStream(posOutput, true))));

        BufferedReader in = new BufferedReader(new InputStreamReader(pisOutput));

        try (UciTango engine = new UciTango(Config.create(), _ -> tango)) {
            // Set the consumer to the engine
            engine.setUCIOutputStream(outputStream);

            // Initial state
            assertEquals(WaitCmdUciState.class, engine.getCurrentState().getClass());

            // uci command
            engine.accept(UCIRequest.uci());
            assertTrue(in.readLine().startsWith("id name Tango"));
            assertEquals("id author Mauricio Coria", in.readLine());
            assertEquals("option name PolyglotFile type string default <empty>", in.readLine());
            assertEquals("option name SyzygyPath type string default <empty>", in.readLine());
            assertEquals("option name Hash type spin default 32 min 1 max 1024", in.readLine());
            assertEquals("uciok", in.readLine());
            assertEquals(WaitCmdPositionState.class, engine.getCurrentState().getClass());

            // isready command
            engine.accept(UCIRequest.isready());
            assertEquals("readyok", in.readLine());
            assertEquals(WaitCmdPositionState.class, engine.getCurrentState().getClass());

            // ucinewgame command
            engine.accept(UCIRequest.ucinewgame());
            assertEquals(WaitCmdPositionState.class, engine.getCurrentState().getClass());

            // isready command
            engine.accept(UCIRequest.isready());
            assertEquals("readyok", in.readLine());
            assertEquals(WaitCmdPositionState.class, engine.getCurrentState().getClass());

            // position command
            engine.accept(UCIRequest.position((List.of("e2e4"))));
            assertEquals(WaitCmdGoState.class, engine.getCurrentState().getClass());

            // quit command
            engine.accept(UCIRequest.quit());
        }

        verify(tango, times(1)).newSession(any());
        verify(session, times(1)).setMoves(List.of("e2e4"));
    }
}
