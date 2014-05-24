package CatchBall;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

public class Basket {

	Terminal terminal;
	Screen screen;
	ScreenWriter writer;
	int size = 3;
	int row = 0;
	int col = 0;

	public Basket(Terminal terminal, Screen screen) {
		this.terminal = terminal;
		this.screen = screen;
		this.writer = new ScreenWriter(this.screen);
		TerminalSize screenSize = terminal.getTerminalSize();
		this.row = screenSize.getRows() - 5;
		this.col = screenSize.getColumns() / 2 - this.size/2;
	}

	public void print() {
		this.writer.drawString(this.col, this.row, this.getBasketString(),
				ScreenCharacterStyle.Bold);
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	private String getBasketString() {
		String basketString = "\\"
				+ new String(new char[this.size]).replace((char) 0, '_') + "/";
		return basketString;
	}
	
	public int getRow(){
		return this.row;
	}

	public int getCol(){
		return this.col;
	}
	
	public int getSize() {
		return this.size+1;
	}
	
	public void setCurrentSize(int size) {
		this.size = size;
	}

	public void move(Key key){
		TerminalSize screenSize = terminal.getTerminalSize();
		this.row = screenSize.getRows() - 5;
		if (key != null) {
			if (key.getKind() == Key.Kind.ArrowLeft) {
				if (this.col > 0) {
					this.col--;
				}
			}

			if (key.getKind() == Key.Kind.ArrowRight) {
				if (this.col < (this.terminal.getTerminalSize().getColumns()- this.size-2)) {
					this.col++;
				}
				
			}
		}
	}
	
}
