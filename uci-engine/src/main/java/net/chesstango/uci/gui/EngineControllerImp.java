package net.chesstango.uci.gui;

import net.chesstango.uci.protocol.UCIGui;
import net.chesstango.uci.protocol.UCIRequest;
import net.chesstango.uci.protocol.UCIResponse;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;
import net.chesstango.uci.protocol.stream.UCIOutputStreamGuiExecutor;
import net.chesstango.uci.service.UCIService;

/**
 * @author Mauricio Coria
 */
public class EngineControllerImp implements EngineController {
    private final UCIService UCIService;
    private EngineClientState currentState;
    private String engineName;
    private String engineAuthor;

    public EngineControllerImp(UCIService UCIService) {
        UCIGui messageExecutor = new UCIGui() {
            @Override
            public void received_uciOk(RspUciOk rspUciOk) {
                currentState.received_uciOk(rspUciOk);
            }

            @Override
            public void received_readyOk(RspReadyOk rspReadyOk) {
                currentState.received_readyOk(rspReadyOk);
            }

            @Override
            public void received_bestMove(RspBestMove rspBestMove) {
                currentState.received_bestMove(rspBestMove);
            }

            @Override
            public void received_id(RspId rspId) {
                currentState.received_id(rspId);
            }
        };

        this.UCIService = UCIService;
        this.UCIService.setResponseOutputStream( new UCIOutputStreamGuiExecutor(messageExecutor) );
    }

    @Override
    public void send_CmdUci() {
        UCIService.open();
        currentState = new WaitRspUciOk();
        currentState.sendRequest(new CmdUci(), true);
    }

    @Override
    public void send_CmdIsReady() {
        currentState = new WaitRspReadyOk();
        currentState.sendRequest(new CmdIsReady(), true);
    }

    @Override
    public void send_CmdUciNewGame() {
        currentState = new NoWaitRsp();
        currentState.sendRequest(new CmdUciNewGame(), false);
    }

    @Override
    public void send_CmdPosition(CmdPosition cmdPosition) {
        currentState = new NoWaitRsp();
        currentState.sendRequest(cmdPosition, false);
    }

    @Override
    public RspBestMove send_CmdGo(CmdGo cmdGo) {
        currentState = new WaitRspBestMove();
        currentState.sendRequest(cmdGo, true);
        return (RspBestMove) currentState.getResponse();
    }

    @Override
    public void send_CmdStop() {
        currentState = new NoWaitRsp();
        currentState.sendRequest(new CmdStop(), false);
    }

    @Override
    public void send_CmdQuit() {
        currentState = new NoWaitRsp();
        currentState.sendRequest(new CmdQuit(), false);
        UCIService.close();
    }

    @Override
    public String getEngineName() {
        return engineName;
    }

    @Override
    public String getEngineAuthor() {
        return engineAuthor;
    }


    private interface EngineClientState {
        void received_uciOk(RspUciOk rspUciOk);

        void received_readyOk(RspReadyOk rspReadyOk);

        void received_bestMove(RspBestMove rspBestMove);

        void received_id(RspId rspId);

        void sendRequest(UCIRequest request, boolean waitResponse);

        UCIResponse getResponse();
    }

    private abstract class RspAbstract implements EngineClientState {

        private UCIResponse response;

        @Override
        public synchronized void sendRequest(UCIRequest request, boolean waitResponse) {
            UCIService.accept(request);
            if (waitResponse) {
                try {
                    if (response == null) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        protected synchronized void responseReceived(UCIResponse response) {
            this.response = response;
            notifyAll();
        }

        @Override
        public UCIResponse getResponse() {
            return response;
        }
    }

    private class WaitRspUciOk extends RspAbstract {
        @Override
        public void received_uciOk(RspUciOk rspUciOk) {
            responseReceived(rspUciOk);
        }

        @Override
        public void received_readyOk(RspReadyOk rspReadyOk) {
        }

        @Override
        public void received_bestMove(RspBestMove rspBestMove) {
        }

        @Override
        public void received_id(RspId rspId) {
            if (RspId.RspIdType.NAME.equals(rspId.getIdType()) && engineName == null) {
                engineName = rspId.getText();
            }
            if (RspId.RspIdType.AUTHOR.equals(rspId.getIdType())) {
                engineAuthor = rspId.getText();
            }
        }

    }

    private class WaitRspReadyOk extends RspAbstract {
        @Override
        public void received_uciOk(RspUciOk rspUciOk) {
        }

        @Override
        public void received_readyOk(RspReadyOk rspReadyOk) {
            responseReceived(rspReadyOk);
        }

        @Override
        public void received_bestMove(RspBestMove rspBestMove) {
        }

        @Override
        public void received_id(RspId rspId) {
        }
    }

    private class NoWaitRsp extends RspAbstract {
        @Override
        public void received_uciOk(RspUciOk rspUciOk) {
        }

        @Override
        public void received_readyOk(RspReadyOk rspReadyOk) {
        }

        @Override
        public void received_bestMove(RspBestMove rspBestMove) {
        }

        @Override
        public void received_id(RspId rspId) {
        }
    }


    private class WaitRspBestMove extends RspAbstract {
        @Override
        public void received_uciOk(RspUciOk rspUciOk) {
        }

        @Override
        public void received_readyOk(RspReadyOk rspReadyOk) {
        }

        @Override
        public void received_bestMove(RspBestMove rspBestMove) {
            responseReceived(rspBestMove);
        }

        @Override
        public void received_id(RspId rspId) {
        }
    }

    public EngineControllerImp overrideEngineName(String name){
        engineName = name;
        return this;
    }
}
