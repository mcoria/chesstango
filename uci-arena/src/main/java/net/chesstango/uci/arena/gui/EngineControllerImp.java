package net.chesstango.uci.arena.gui;

import net.chesstango.uci.protocol.UCIGui;
import net.chesstango.uci.protocol.UCIRequest;
import net.chesstango.uci.protocol.UCIResponse;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.*;
import net.chesstango.uci.protocol.stream.UCIOutputStreamGuiExecutor;
import net.chesstango.uci.Service;
import net.chesstango.uci.ServiceVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mauricio Coria
 */
public class EngineControllerImp implements EngineController {
    private static final Logger logger = LoggerFactory.getLogger(EngineControllerImp.class);
    private final Service service;
    private volatile UCIGui currentState;
    private volatile UCIResponse response;
    private String engineName;
    private String engineAuthor;
    private CmdGo cmdGo;

    public EngineControllerImp(Service service) {
        UCIGui messageExecutor = new UCIGui() {
            @Override
            public void do_uciOk(RspUciOk rspUciOk) {
                currentState.do_uciOk(rspUciOk);
            }

            @Override
            public void do_readyOk(RspReadyOk rspReadyOk) {
                currentState.do_readyOk(rspReadyOk);
            }

            @Override
            public void do_bestMove(RspBestMove rspBestMove) {
                currentState.do_bestMove(rspBestMove);
            }

            @Override
            public void do_info(RspInfo rspInfo) {
                currentState.do_info(rspInfo);
            }

            @Override
            public void do_id(RspId rspId) {
                currentState.do_id(rspId);
            }
        };

        this.service = service;
        this.service.setResponseOutputStream(new UCIOutputStreamGuiExecutor(messageExecutor));
        this.currentState = new NoWaitRsp();
    }

    @Override
    public void send_CmdUci() {
        service.open();
		currentState = new WaitRspUciOk();
        sendRequest(new CmdUci(), true);
    }

    @Override
    public void send_CmdIsReady() {
        currentState = new WaitRspReadyOk();
        sendRequest(new CmdIsReady(), true);
    }

    @Override
    public void send_CmdUciNewGame() {
        sendRequest(new CmdUciNewGame(), false);
    }

    @Override
    public void send_CmdPosition(CmdPosition cmdPosition) {
        sendRequest(cmdPosition, false);
    }

    @Override
    public RspBestMove send_CmdGo(CmdGo cmdGo) {
        currentState = new WaitRspBestMove();
        return (RspBestMove) sendRequest(this.cmdGo == null ? cmdGo : this.cmdGo, true);
    }

    @Override
    public void send_CmdStop() {
        sendRequest(new CmdStop(), false);
    }

    @Override
    public void send_CmdQuit() {
        sendRequest(new CmdQuit(), false);
        service.close();
    }

    @Override
    public String getEngineName() {
        return engineName;
    }

    @Override
    public String getEngineAuthor() {
        return engineAuthor;
    }

    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        service.accept(serviceVisitor);
    }


    @Override
    public EngineControllerImp overrideEngineName(String name) {
        this.engineName = name;
        return this;
    }

    @Override
    public EngineController overrideCmdGo(CmdGo cmdGo) {
        this.cmdGo = cmdGo;
        return this;
    }

    public synchronized UCIResponse sendRequest(UCIRequest request, boolean waitResponse) {
        this.response = null;
        service.accept(request);
        if (waitResponse) {
            try {
                int waitingCounter = 0;
                while (response == null && waitingCounter < 20 ) {
                    wait(1000);
                    waitingCounter++;
                }
                if (response == null) {
                    logger.error("Engine {} has not provided any response after sending: {}", engineName, request);
                    throw new RuntimeException("Perhaps engine has closed its output");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }

    protected synchronized void responseReceived(UCIResponse response) {
        this.response = response;
        notifyAll();
    }


    private class NoWaitRsp implements UCIGui {
        @Override
        public void do_uciOk(RspUciOk rspUciOk) {
        }

        @Override
        public void do_readyOk(RspReadyOk rspReadyOk) {
        }

        @Override
        public void do_bestMove(RspBestMove rspBestMove) {
        }

        @Override
        public void do_info(RspInfo rspInfo) {

        }

        @Override
        public void do_id(RspId rspId) {
        }
    }

    private class WaitRspUciOk implements UCIGui {
        @Override
        public void do_uciOk(RspUciOk rspUciOk) {
            responseReceived(rspUciOk);
            currentState = new NoWaitRsp();
        }

        @Override
        public void do_readyOk(RspReadyOk rspReadyOk) {
        }

        @Override
        public void do_bestMove(RspBestMove rspBestMove) {
        }

        @Override
        public void do_info(RspInfo rspInfo) {

        }

        @Override
        public void do_id(RspId rspId) {
            if (RspId.RspIdType.NAME.equals(rspId.getIdType()) && engineName == null) {
                engineName = rspId.getText();
            }
            if (RspId.RspIdType.AUTHOR.equals(rspId.getIdType())) {
                engineAuthor = rspId.getText();
            }
        }

    }

    private class WaitRspReadyOk implements UCIGui {
        @Override
        public void do_uciOk(RspUciOk rspUciOk) {
        }

        @Override
        public void do_readyOk(RspReadyOk rspReadyOk) {
            responseReceived(rspReadyOk);
            currentState = new NoWaitRsp();
        }

        @Override
        public void do_bestMove(RspBestMove rspBestMove) {
        }

        @Override
        public void do_info(RspInfo rspInfo) {

        }

        @Override
        public void do_id(RspId rspId) {
        }
    }


    private class WaitRspBestMove implements UCIGui {
        @Override
        public void do_uciOk(RspUciOk rspUciOk) {
        }

        @Override
        public void do_readyOk(RspReadyOk rspReadyOk) {
        }

        @Override
        public void do_bestMove(RspBestMove rspBestMove) {
            responseReceived(rspBestMove);
            currentState = new NoWaitRsp();
        }

        @Override
        public void do_info(RspInfo rspInfo) {
        }

        @Override
        public void do_id(RspId rspId) {
        }
    }
}
