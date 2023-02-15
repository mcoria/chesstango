/**
 *
 */
package net.chesstango.uci.engine;

import net.chesstango.search.SearchMove;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.UCIMessageExecutor;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;
import net.chesstango.uci.protocol.stream.UCIOutputStream;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class EngineTango implements Engine {
    private final SearchMove searchMove;
    private final UCIMessageExecutor messageExecutor;
    private ExecutorService executor;
    private boolean asyncEnabled;
    private Game game;
    private ZondaState currentState;
    private UCIOutputStream responseOutputStream;

    public EngineTango() {
        this(new DefaultSearchMove());
    }


    public EngineTango(SearchMove searchMove) {
        this.searchMove = searchMove;
        this.currentState = new Ready();
        this.asyncEnabled = true;
        this.messageExecutor = new UCIMessageExecutor() {

            @Override
            public void do_uci(CmdUci cmdUci) {
                responseOutputStream.accept(new RspId(RspId.RspIdType.NAME, "Tango"));
                responseOutputStream.accept(new RspId(RspId.RspIdType.AUTHOR, "Mauricio Coria"));
                responseOutputStream.accept(new RspUciOk());
            }

            @Override
            public void do_isReady(CmdIsReady cmdIsReady) {
                responseOutputStream.accept(new RspReadyOk());
            }

            @Override
            public void do_setOption(CmdSetOption cmdSetOption) {
            }

            @Override
            public void do_newGame(CmdUciNewGame cmdUciNewGame) {
            }

            @Override
            public void do_position(CmdPosition cmdPosition) {
                game = CmdPosition.CmdType.STARTPOS == cmdPosition.getType() ? FENDecoder.loadGame(FENDecoder.INITIAL_FEN) : FENDecoder.loadGame(cmdPosition.getFen());
                executeMoves(cmdPosition.getMoves());
                currentState = new WaitCmdGo();
            }

            @Override
            public void do_go(CmdGo cmdGo) {
                currentState.do_go(cmdGo);
            }

            @Override
            public void do_stop(CmdStop cmdStop) {
                currentState.do_stop();
            }

            @Override
            public void do_quit(CmdQuit cmdQuit) {
                currentState.do_stop();
                close();
            }

            @Override
            public void receive_uciOk(RspUciOk rspUciOk) {
            }

            @Override
            public void receive_id(RspId rspId) {
            }

            @Override
            public void receive_readyOk(RspReadyOk rspReadyOk) {
            }

            @Override
            public void receive_bestMove(RspBestMove rspBestMove) {
            }

        };
    }


    @Override
    public void accept(UCIMessage message) {
        message.execute(messageExecutor);
    }

    public EngineTango disableAsync() {
        asyncEnabled = false;
        return this;
    }

    @Override
    public void open() {
        if (asyncEnabled && executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public void close() {
        try {
            responseOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (asyncEnabled && executor != null) {
            try {
                executor.shutdown();
                while (!executor.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                }
                executor = null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Game getGame() {
        return game;
    }

    public void setResponseOutputStream(UCIOutputStream output) {
        this.responseOutputStream = output;
    }


    private void executeMoves(List<String> moves) {
        if (moves != null && !moves.isEmpty()) {
            UCIEncoder uciEncoder = new UCIEncoder();
            for (String moveStr : moves) {
                boolean findMove = false;
                for (Move move : game.getPossibleMoves()) {
                    String encodedMoveStr = uciEncoder.encode(move);
                    if (encodedMoveStr.equals(moveStr)) {
                        game.executeMove(move);
                        findMove = true;
                        break;
                    }
                }
                if (!findMove) {
                    throw new RuntimeException("No move found " + moveStr);
                }
            }
        }
    }

    public ZondaState getCurrentState() {
        return currentState;
    }


    public interface ZondaState {

        void do_go(CmdGo cmdGo);

        void do_stop();
    }


    public class Ready implements ZondaState {
        @Override
        public void do_go(CmdGo cmdGo) {
        }

        @Override
        public void do_stop() {
        }
    }


    public class WaitCmdGo implements ZondaState {
        @Override
        public void do_go(CmdGo cmdGo) {
            FindingBestMove findingBestMove = new FindingBestMove();
            currentState = findingBestMove;
            if (executor != null) {
                executor.execute(() -> findingBestMove.findBestMove(cmdGo));
            } else {
                findingBestMove.findBestMove(cmdGo);
            }
        }

        @Override
        public void do_stop() {
        }
    }

    public class FindingBestMove implements ZondaState {

        private UCIEncoder uciEncoder = new UCIEncoder();


        @Override
        public void do_go(CmdGo cmdGo) {
        }

        @Override
        public void do_stop() {
            searchMove.stopSearching();
        }

        public void findBestMove(CmdGo cmdGo) {

            // TODO: for the moment we are cheating
            Move selectedMove = null;

            if (CmdGo.GoType.INFINITE.equals(cmdGo.getGoType())) {
                selectedMove = searchMove.searchBestMove(game);
            } else if (CmdGo.GoType.DEPTH.equals(cmdGo.getGoType())) {
                selectedMove = searchMove.searchBestMove(game, cmdGo.getDepth() + 2);
            } else {
                throw new RuntimeException("go subtype not implemented yet");
            }

            responseOutputStream.accept(new RspBestMove(uciEncoder.encode(selectedMove)));

            currentState = new Ready();
        }
    }
}
