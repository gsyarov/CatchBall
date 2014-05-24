package CatchBall;

import java.awt.Toolkit;
import java.util.ArrayList;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

public class Start {

	private static int startLives = 5;
	private static int lives = 5;
	private static int score = 0;
	private static int sizeBasketDefault = 5;
	private static int ballSpeedDefault = 110;
	private static int gunSpeed = 30;
	private static int ballStartDefault = 1200;
	private static int addLevelSpeed = 0;
	private static int levelBallStart = 0;
	private static int levelNumber = 0;
	private static int nextLevel = 0;
	private static String message = "";

	public static void main(String[] args) throws InterruptedException {

		Terminal terminal = TerminalFacade.createTerminal();
		Screen screen = new Screen(terminal);
		terminal.setCursorVisible(false);

		screen.startScreen();
		ScreenWriter writer = new ScreenWriter(screen);

		Basket basket = new Basket(terminal, screen);
		Gun gun = new Gun(terminal, screen);

		GameTimer timer = new GameTimer(2);

		boolean isRunGame = true;

		ArrayList<Ball> balls = new ArrayList<>();

		int addSizeBasket = 0;
		int addBallSpeed = 0;

		// Start while Loop
		while (isRunGame) {
			printInfo(screen, terminal, lives, score);
			Key key = terminal.readInput();

			int sizeBasket = sizeBasketDefault + addSizeBasket;
			int ballSpeed = ballSpeedDefault + addBallSpeed + addLevelSpeed;
			int ballStart = ballStartDefault + levelBallStart;

			addLevelSpeed(score);
			printLevel(terminal, screen, writer);

			if (key != null) {

				// Exit game
				if (key.getKind() == Key.Kind.Escape) {
					isRunGame = false;
				}

				// Pause game
				if (key.getCharacter() == 'p' || key.getCharacter() == 'P') {
					pauseGame(terminal, screen, writer);
				}

				// Info game
				if (key.getCharacter() == 'i' || key.getCharacter() == 'I') {
					customGameInfo(terminal, screen, writer);
				}

				// New game
				if (key.getKind() == Kind.Home) {
					score = 0;
					lives = startLives;
					levelNumber = 0;
					message = "";
					balls.clear();
					printNewGame(terminal, screen, writer);
				}

				basket.move(key);
			}
			// Ball Speed
			if (timer.isTime(ballSpeed)) {
				for (Ball ball : balls) {
					ball.move();
				}
			}
			// Ball Interval
			if (timer.isTime(ballStart)) {
				balls.add(gun.genereBall());
			}
			// Gun Speed
			if (timer.isTime(gunSpeed)) {
				gun.move();
			}
			// Return default ball speed
			if (timer.isTime(5000)) {
				addBallSpeed = 0;
				message = "";
			}
			// Return default size at basket
			if (timer.isTime(10000)) {
				message = "";
				addSizeBasket = 0;
			}

			for (Ball ball : balls) {
				// A ball in the basket
				if (basket.getCol() <= ball.getCol()
						&& basket.getCol() + basket.getSize() >= ball.getCol()
						&& basket.getRow() == ball.getRow()) {
					if (!ball.getIsChange()) {

						ball.changeIsCatch();

						final Runnable runnable = (Runnable) Toolkit
								.getDefaultToolkit().getDesktopProperty(
										"win.sound.default");
						if (runnable != null)
							runnable.run();

						switch (ball.getKindBall()) {
						// Up lives
						case 1:
							lives++;
							message = "Give live";
							break;
						// Take live
						case 2:
						case 3:
						case 4:
							lives--;
							message = "Take live";
							break;
						// Get big size of basket
						case 5:
							addSizeBasket = 3;
							score += 5;
							message = "Big size basket";
							break;
						// Get small size of basket
						case 6:
							addSizeBasket = -2;
							score += 5;
							message = "Small size basket";
							break;
						// Get faster balls
						case 7:
							addBallSpeed = -15;
							message = "Faster balls";
							score += 5;
							break;
						// Get slower balls
						case 8:
							addBallSpeed = 50;
							message = "Slower balls";
							score += 5;
							break;
						// Get bonus score
						case 9:
							message = "Bonus +10";
							score += 10;
							break;
						// Get score
						default:
							score += 2;
							break;
						}
					}
				}

				// Take a live
				if (ball.getRow() > basket.getRow() && ball.getKindBall() > 6) {
					if (!ball.getIsChange()) {
						final Runnable runnable = (Runnable) Toolkit
								.getDefaultToolkit().getDesktopProperty(
										"win.sound.asterisk");
						if (runnable != null)
							runnable.run();
						lives--;
						ball.changeIsCatch();
					}
				}

				ball.print();
			}

			// Game over
			if (lives < 0) {
				isRunGame = false;
			}

			gun.print();
			basket.setCurrentSize(sizeBasket);
			basket.print();
			screen.refresh();
			screen.clear();
			timer.addLoop();
		}
		// End while Loop

		printGameOver(terminal, screen, writer);
		screen.stopScreen();

	}

	// Pause Game
	public static void pauseGame(Terminal terminal, Screen screen,
			ScreenWriter writer) {

		TerminalSize screenSize = terminal.getTerminalSize();
		boolean isPause = true;
		writer.drawString(screenSize.getColumns() / 2 - 5,
				screenSize.getRows() / 2, "P A U S E");
		screen.refresh();

		while (isPause) {
			Key key = terminal.readInput();
			if (key != null) {
				isPause = false;
			}
		}
	}

	// Info for balls
	public static void customGameInfo(Terminal terminal, Screen screen,
			ScreenWriter writer) {
		boolean isInfo = true;

		TerminalSize screenSize = terminal.getTerminalSize();
		writer.drawString(screenSize.getColumns() / 2 - 5,
				screenSize.getRows() / 4, "I N F O");
		writer.drawString(screenSize.getColumns() / 2 - 9,
				screenSize.getRows() / 4 + 1, "# - add 1 score");
		writer.drawString(screenSize.getColumns() / 2 - 9,
				screenSize.getRows() / 4 + 2, "#+10 - add 10 score");
		writer.drawString(screenSize.getColumns() / 2 - 9,
				screenSize.getRows() / 4 + 3, "$ - add lives");
		writer.drawString(screenSize.getColumns() / 2 - 9,
				screenSize.getRows() / 4 + 4, "\u2620 - take lives");
		writer.drawString(screenSize.getColumns() / 2 - 9,
				screenSize.getRows() / 4 + 5, "@? - unknown bonus. Add 5 score");
		writer.drawString(screenSize.getColumns() / 2 - 9,
				screenSize.getRows() / 4 + 6, "- make bigger basket");
		writer.drawString(screenSize.getColumns() / 2 - 9,
				screenSize.getRows() / 4 + 7, "- make less basket");
		writer.drawString(screenSize.getColumns() / 2 - 9,
				screenSize.getRows() / 4 + 8, "- make balls faster");
		writer.drawString(screenSize.getColumns() / 2 - 9,
				screenSize.getRows() / 4 + 9, "- make balls slower");
		screen.refresh();

		while (isInfo) {
			Key key = terminal.readInput();
			if (key != null) {
				isInfo = false;
			}
		}
	}

	// Print info line
	public static void printInfo(Screen screen, Terminal terminal, int lives,
			int score) {
		TerminalSize screenSize = terminal.getTerminalSize();
		ScreenWriter writer = new ScreenWriter(screen);

		writer.drawString(0, screenSize.getRows() - 4, new String(
				new char[screenSize.getColumns()]).replace((char) 0, '_'));

		writer.drawString(5, screenSize.getRows() - 3, "Lives: "
				+ new String(new char[lives]).replace((char) 0, '*'));

		writer.drawString(5, screenSize.getRows() - 2, "Score: " + score);

		writer.drawString(5, screenSize.getRows() - 1, "Level: " + levelNumber);

		writer.drawString(screenSize.getColumns() - 65,
				screenSize.getRows() - 3, "Bonus: " + message);

		writer.drawString(screenSize.getColumns() - 30,
				screenSize.getRows() - 3, "I - info");

		writer.drawString(screenSize.getColumns() - 17,
				screenSize.getRows() - 3, "Esc - exit");

		writer.drawString(screenSize.getColumns() - 17,
				screenSize.getRows() - 2, "P - pause");

		writer.drawString(screenSize.getColumns() - 17,
				screenSize.getRows() - 1, "Home - new game");

	}

	// Print game over and info for your result
	public static void printGameOver(Terminal terminal, Screen screen,
			ScreenWriter writer) {

		TerminalSize screenSize = terminal.getTerminalSize();

		boolean isPressKey = false;

		writer.drawString(screenSize.getColumns() / 2 - 9,
				screenSize.getRows() / 4, "G A M E    O V E R");

		writer.drawString(screenSize.getColumns() / 2 - 7,
				screenSize.getRows() / 4 + 2, "Your score: " + score);
		writer.drawString(screenSize.getColumns() / 2 - 7,
				screenSize.getRows() / 4 + 3, "Your Level: " + levelNumber);

		writer.drawString(screenSize.getColumns() / 2 - 11,
				screenSize.getRows() / 4 + 5, "Press Enter for close",
				ScreenCharacterStyle.Blinking);
		screen.refresh();

		while (!isPressKey) {
			Key key = terminal.readInput();
			if (key != null && key.getKind() == Kind.Enter) {
				isPressKey = true;
			}
		}
	}

	// Print new game
	public static void printNewGame(Terminal terminal, Screen screen,
			ScreenWriter writer) throws InterruptedException {

		TerminalSize screenSize = terminal.getTerminalSize();

		screen.clear();
		writer.drawString(screenSize.getColumns() / 2 - 10,
				screenSize.getRows() / 2, "N E W    G A M E",
				ScreenCharacterStyle.Bold);
		screen.refresh();

		Thread.sleep(2000);
	}

	// Print when level up
	public static void printLevel(Terminal terminal, Screen screen,
			ScreenWriter writer) throws InterruptedException {

		TerminalSize screenSize = terminal.getTerminalSize();
		// Win Game
		if (score > 360) {
			screen.clear();
			writer.drawString(screenSize.getColumns() / 2 - 6,
					screenSize.getRows() / 2, "Y O U   W I N");
			
			screen.refresh();

		}
		// Level up
		if (levelNumber == nextLevel) {

			screen.clear();

			nextLevel++;

			writer.drawString(screenSize.getColumns() / 2 - 6,
					screenSize.getRows() / 2, "L E V E L    " + levelNumber);
			if (levelNumber != 0) {
				writer.drawString(screenSize.getColumns() / 2 - 12,
						screenSize.getRows() / 2 + 3,
						"You have a bonus ONE live!");
				lives++;
			}
			screen.refresh();

			Thread.sleep(2000);
		}
	}

	// Levels
	public static void addLevelSpeed(int score) {

		// Level 1
		if (score >= 30 && score < 60) {
			addLevelSpeed = -3;
			levelBallStart = -50;
			levelNumber = 1;
		}

		// Level 2
		if (score >= 60 && score < 90) {
			addLevelSpeed = -6;
			levelBallStart = -100;
			levelNumber = 2;
		}

		// Level 3
		if (score >= 90 && score < 120) {
			addLevelSpeed = -9;
			levelBallStart = -150;
			levelNumber = 3;
		}

		// Level 4
		if (score >= 120 && score < 150) {
			addLevelSpeed = -12;
			levelBallStart = -200;
			levelNumber = 4;
		}

		// Level 5
		if (score >= 150 && score < 180) {
			addLevelSpeed = -15;
			levelBallStart = -250;
			levelNumber = 5;
		}

		// Level 6
		if (score >= 180 && score < 210) {
			addLevelSpeed = -18;
			levelBallStart = -300;
			levelNumber = 6;
		}

		// Level 7
		if (score >= 210 && score < 240) {
			addLevelSpeed = -21;
			levelBallStart = -350;
			levelNumber = 7;
		}

		// Level 8
		if (score >= 240 && score < 270) {
			addLevelSpeed = -24;
			levelBallStart = -400;
			levelNumber = 8;
		}

		// Level 9
		if (score >= 300 && score < 330) {
			addLevelSpeed = -27;
			levelBallStart = -450;
			levelNumber = 9;
		}

		// Level 10
		if (score >= 330 && score < 360) {

			addLevelSpeed = -30;
			levelBallStart = -500;
			levelNumber = 10;
		}
	}

}
