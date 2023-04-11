var stompClient = null;
var board = Chessboard('myBoard', 'start')
var currentGameId = '';
var movesCounter = 0;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/game_messages', function(message) {
            showNewGame(JSON.parse(message.body));
        });

        stompClient.subscribe('/topic/move_messages', function(message) {
            showMove(JSON.parse(message.body));
        });

        stompClient.subscribe('/topic/current_game', function(message) {
            loadCurrentGame(JSON.parse(message.body));
        });

        stompClient.send("/app/retrieve_game", {}, {});
    });
}

function disconnect() {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function loadCurrentGame(response) {
    console.log('Loading current game');
    var gameDescriptionInitial = response.gameDescriptionInitial;
    var gameDescriptionCurrent = response.gameDescriptionCurrent;

    currentGameId = gameDescriptionInitial.gameId;

    if(gameDescriptionCurrent == null){
        movesCounter = 0;
        board = Chessboard('myBoard', gameDescriptionInitial.initialFEN);
    } else {
        movesCounter = gameDescriptionCurrent.moves.length;
        board = Chessboard('myBoard', gameDescriptionCurrent.currentFEN);
    }
}


function showNewGame(gameNotification) {
    console.log('Loading new game');
    var gameDescriptionInitial = gameNotification.gameDescriptionInitial;

    currentGameId = gameDescriptionInitial.gameId;
    movesCounter = 0;
    board = Chessboard('myBoard', gameDescriptionInitial.initialFEN);
}

function showMove(moveNotification) {
    console.log('Moving');
    var gameDescriptionCurrent = moveNotification.gameDescriptionCurrent;
    if(currentGameId == gameDescriptionCurrent.gameId){
        if(movesCounter + 1 == gameDescriptionCurrent.moves.length) {
            board.move(moveNotification.userData);
            movesCounter = movesCounter + 1;
        } else {
            stompClient.send("/app/retrieve_game", {}, {});
        }
    } else {
        stompClient.send("/app/retrieve_game", {}, {});
    }
}

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
}