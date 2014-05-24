package CatchBall;

import java.util.Random;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

public class Gun {
	private Terminal terminal;
	private Screen screen;
	private ScreenWriter writer;
	private int row = 12;
	private int col = 2;
	private boolean isRight = true;
	private Random random = new Random();

	public Gun(Terminal terminal, Screen screen) {
		this.terminal = terminal;
		this.screen = screen;
		this.writer = new ScreenWriter(this.screen);
		this.row = 12;
		this.col = 2;
	}

	public void print() {
		this.writer.drawString(this.col, this.row - 1, " /@\\",
				ScreenCharacterStyle.Bold);
		this.writer.drawString(this.col, this.row, "/___\\",
				ScreenCharacterStyle.Bold);
	}

	public Ball genereBall() {

		Ball ball = new Ball(terminal, screen);
		ball.setCol(this.col);
		ball.setMaxUp(random.nextInt(9) + 3);

		int kindBall = random.nextInt(20);

		switch (kindBall) {
		// Up lives
		case 1:
			ball.setBallString("$");
			ball.setKindBall(kindBall);
			break;
		// Take lives
		case 2:
		case 3:
		case 4:
			ball.setBallString("" + '\u2620' );
			ball.setKindBall(kindBall);
			break;
		case 5:
			ball.setBallString("@?");
			ball.setKindBall(kindBall);
			break;
		case 6:
			ball.setBallString("@?");
			ball.setKindBall(kindBall);
			break;
		case 7:
			ball.setBallString("@?");
			ball.setKindBall(kindBall);
			break;
		case 8:
			ball.setBallString("@?");
			ball.setKindBall(kindBall);
			break;
		case 9: 
			ball.setBallString("#+10");
			ball.setKindBall(kindBall);
		default:
			ball.setKindBall(kindBall);
			break;
		}

		return ball;
	}

	public void move() {
		TerminalSize screenSize = this.terminal.getTerminalSize();
		if (this.col == 0) {
			this.isRight = true;
		} else if (this.col == screenSize.getColumns() - 5) {
			this.isRight = false;
		}
		if (this.isRight) {

			this.col++;
		} else {
			this.col--;
		}
	}
}
