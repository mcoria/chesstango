package chess;

public enum Square {
	a8(0,7),b8(1,7),c8(2,7),d8(3,7),e8(4,7),f8(5,7),g8(6,7),h8(7,7),
	a7(0,6),b7(1,6),c7(2,6),d7(3,6),e7(4,6),f7(5,6),g7(6,6),h7(7,6),
	a6(0,5),b6(1,5),c6(2,5),d6(3,5),e6(4,5),f6(5,5),g6(6,5),h6(7,5),
	a5(0,4),b5(1,4),c5(2,4),d5(3,4),e5(4,4),f5(5,4),g5(6,4),h5(7,4),
	a4(0,3),b4(1,3),c4(2,3),d4(3,3),e4(4,3),f4(5,3),g4(6,3),h4(7,3),
	a3(0,2),b3(1,2),c3(2,2),d3(3,2),e3(4,2),f3(5,2),g3(6,2),h3(7,2),
    a2(0,1),b2(1,1),c2(2,1),d2(3,1),e2(4,1),f2(5,1),g2(6,1),h2(7,1),
    a1(0,0),b1(1,0),c1(2,0),d1(3,0),e1(4,0),f1(5,0),g1(6,0),h1(7,0);
	
	private int file = 0;
	private int rank = 0;
	
	private Square(int file, int rank){
		this.file = file;
		this.rank = rank;
	}
	
	public int getRank() {
		return rank;
	}

	public int getFile() {
		return file;
	}


	public static Square getSquare(int file, int rank){
		Square value = null;
		switch (file) {
		case 0:
			switch (rank) {
			case 0:
				value = a1;
				break;
			case 1:
				value = a2;
				break;
			case 2:
				value = a3;
				break;
			case 3:
				value = a4;
				break;
			case 4:
				value = a5;
				break;
			case 5:
				value = a6;
				break;
			case 6:
				value = a7;
				break;
			case 7:
				value = a8;
				break;
			default:
				break;
			}
			break;
		case 1:
			switch (rank) {
			case 0:
				value = b1;
				break;
			case 1:
				value = b2;
				break;
			case 2:
				value = b3;
				break;
			case 3:
				value = b4;
				break;
			case 4:
				value = b5;
				break;
			case 5:
				value = b6;
				break;
			case 6:
				value = b7;
				break;
			case 7:
				value = b8;
				break;
			default:
				break;
			}
			break;
		case 2:
			switch (rank) {
			case 0:
				value = c1;
				break;
			case 1:
				value = c2;
				break;
			case 2:
				value = c3;
				break;
			case 3:
				value = c4;
				break;
			case 4:
				value = c5;
				break;
			case 5:
				value = c6;
				break;
			case 6:
				value = c7;
				break;
			case 7:
				value = c8;
				break;
			default:
				break;
			}
			break;
		case 3:
			switch (rank) {
			case 0:
				value = d1;
				break;
			case 1:
				value = d2;
				break;
			case 2:
				value = d3;
				break;
			case 3:
				value = d4;
				break;
			case 4:
				value = d5;
				break;
			case 5:
				value = d6;
				break;
			case 6:
				value = d7;
				break;
			case 7:
				value = d8;
				break;
			default:
				break;
			}
			break;
		case 4:
			switch (rank) {
			case 0:
				value = e1;
				break;
			case 1:
				value = e2;
				break;
			case 2:
				value = e3;
				break;
			case 3:
				value = e4;
				break;
			case 4:
				value = e5;
				break;
			case 5:
				value = e6;
				break;
			case 6:
				value = e7;
				break;
			case 7:
				value = e8;
				break;
			default:
				break;
			}
			break;
		case 5:
			switch (rank) {
			case 0:
				value = f1;
				break;
			case 1:
				value = f2;
				break;
			case 2:
				value = f3;
				break;
			case 3:
				value = f4;
				break;
			case 4:
				value = f5;
				break;
			case 5:
				value = f6;
				break;
			case 6:
				value = f7;
				break;
			case 7:
				value = f8;
				break;
			default:
				break;
			}
			break;		
		case 6:
			switch (rank) {
			case 0:
				value = g1;
				break;
			case 1:
				value = g2;
				break;
			case 2:
				value = g3;
				break;
			case 3:
				value = g4;
				break;
			case 4:
				value = g5;
				break;
			case 5:
				value = g6;
				break;
			case 6:
				value = g7;
				break;
			case 7:
				value = g8;
				break;
			default:
				break;
			}
			break;
		case 7:
			switch (rank) {
			case 0:
				value = h1;
				break;
			case 1:
				value = h2;
				break;
			case 2:
				value = h3;
				break;
			case 3:
				value = h4;
				break;
			case 4:
				value = h5;
				break;
			case 5:
				value = h6;
				break;
			case 6:
				value = h7;
				break;
			case 7:
				value = h8;
				break;
			}
			break;			
		default:
			break;
		}
		return value;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
