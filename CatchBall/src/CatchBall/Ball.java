package CatchBall;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;



public class Ball {

	private Screen screen;
	private ScreenWriter writer;
	private int row = 0;
	private int col = 0;
	private int maxUp = 5;
	private String ballString = "#";
	private boolean isCatch = false;
	private int kindBall = 0;

	public Ball(Terminal terminal, Screen screen) {
		this.screen = screen;
		this.writer = new ScreenWriter(this.screen);
		this.row = 10;
		this.col = 10;
	}

	public void changeIsCatch() {
		this.isCatch = true;
	}
	
	public boolean getIsChange(){
		return this.isCatch;
	}

	public int getRow() {
		return this.row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return this.col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void setMaxUp(int maxUp) {
		this.maxUp = maxUp;
	}

	public void setBallString(String ballString) {
		this.ballString = ballString;
	}

	public void move() {
		if (this.maxUp >= 0) {
			this.row--;
			this.maxUp--;
		} else if (row <= 30) {
			this.row++;
		}
	}

	public void print() {
		if (this.row < 25) {
			this.writer.drawString(this.col, this.row, this.ballString,
					ScreenCharacterStyle.Bold);
		}

	}

	public int getKindBall() {
		return kindBall;
	}

	public void setKindBall(int kindBall) {
		this.kindBall = kindBall;
	}

}
