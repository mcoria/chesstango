package net.chesstango.uci.engine;

import net.chesstango.goyeneche.requests.UCIRequest;
import net.chesstango.goyeneche.responses.UCIResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class SearchingStateTest {
    private SearchingState searchingState;

    @Mock
    private UciTango engine;

    @BeforeEach
    public void setUp() {
        searchingState = new SearchingState(engine);
    }

    @Test
    public void isReadyTest() {
        searchingState.do_isReady(UCIRequest.isready());

        verify(engine).reply(searchingState, UCIResponse.readyok());
    }

}
