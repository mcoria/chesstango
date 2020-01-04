package chess;

public enum Square {
	a8(56),b8(57),c8(58),d8(59),e8(60),f8(61),g8(62),h8(63),
	a7(48),b7(49),c7(50),d7(51),e7(52),f7(53),g7(54),h7(55),
	a6(40),b6(41),c6(42),d6(43),e6(44),f6(45),g6(46),h6(47),
	a5(32),b5(33),c5(34),d5(35),e5(36),f5(37),g5(38),h5(39),
	a4(24),b4(25),c4(26),d4(27),e4(28),f4(29),g4(30),h4(31),
	a3(16),b3(17),c3(18),d3(19),e3(20),f3(21),g3(22),h3(23),
    a2(8) ,b2(9) ,c2(10),d2(11),e2(12),f2(13),g2(14),h2(17),
    a1(0) ,b1(1) ,c1(2) ,d1(3) ,e1(4) ,f1(5) ,g1(6) ,h1(7);
	
	private int idx = 0;
	private Square(int idx){
		this.idx = idx;
	}
	
	public int getIdx() {
		return idx;
	}
	
	public static Square getSquare(int idx){
		Square value = null;
		switch (idx) {
		case 0:
			value = a1;
			break;
		case 1:
			value = a1;
			break;
		case 2:
			value = a1;
			break;
		case 3:
			value = a1;
			break;
		case 4:
			value = a1;
			break;
		case 5:
			value = a1;
			break;		
		case 6:
			value = a1;
			break;
		case 7:
			value = a1;
			break;
		case 8:
			value = a1;
			break;
		case 9:
			value = a1;
			break;
		case 10:
			value = a1;
			break;
		case 11:
			value = a1;
			break;
		case 12:
			value = a1;
			break;
		case 13:
			value = a1;
			break;
		case 14:
			value = a1;
			break;
		case 15:
			value = a1;
			break;
		case 16:
			value = a1;
			break;
		case 17:
			value = a1;
			break;
		case 18:
			value = a1;
			break;
		case 19:
			value = a1;
			break;
		case 20:
			value = a1;
			break;
		case 21:
			value = a1;
			break;
		case 22:
			value = a1;
			break;
		case 23:
			value = a1;
			break;			
		default:
			break;
		}
		return value;
	}
}
