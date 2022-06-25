/**
 *
 */
package chess.uci.engine.imp;

import chess.ai.BestMoveFinder;
import chess.ai.imp.smart.IterativeDeeping;
import chess.board.Game;
import chess.board.moves.Move;
import chess.board.representations.fen.FENDecoder;
import chess.uci.engine.Engine;
import chess.uci.protocol.UCIEncoder;
import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.UCIMessageExecutor;
import chess.uci.protocol.requests.*;
import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspId;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.RspUciOk;
import chess.uci.protocol.stream.UCIOutputStream;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class EngineZonda implements Engine {
    private final BestMoveFinder bestMoveFinder;
    private final UCIMessageExecutor messageExecutor;

    private ExecutorService executor;

    private Game game;
    private ZondaState currentState;
    private UCIOutputStream responseOutputStream;

    public EngineZonda() {
        this.bestMoveFinder = new IterativeDeeping();
        this.currentState = new Ready();
        this.messageExecutor = new UCIMessageExecutor(){

            @Override
            public void do_uci(CmdUci cmdUci) {
                responseOutputStream.accept(new RspId(RspId.RspIdType.NAME, "Zonda"));
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

    @Override
    public void open() {
        if(executor == null) {
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

        if(executor != null) {
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
            if(executor != null) {
                executor.execute(findingBestMove::findBestMove);
            }else{
                findingBestMove.findBestMove();
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
            bestMoveFinder.stopSearching();
        }

        public void findBestMove() {
            Move selectedMove = bestMoveFinder.searchBestMove(game);

            responseOutputStream.accept(new RspBestMove(uciEncoder.encode(selectedMove)));

            currentState = new Ready();
        }
    }
}
