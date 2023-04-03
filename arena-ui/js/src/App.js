import './App.css';
import { Chessboard } from "chessboardjs";
import * as $ from 'jquery';

function App() {
  var board = Chessboard('myBoard')
	
  return (
    <div className="App">
      <header className="App-header">

		<div id="myBoard" style="width: 400px"></div>

      </header>
    </div>
  );
}

export default App;
