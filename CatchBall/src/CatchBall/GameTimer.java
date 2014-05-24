package CatchBall;

public class GameTimer {
	private long loops = 0;
	private long delay;

	public GameTimer(long delay) {
		this.delay = delay;
	}

	public void addLoop() throws InterruptedException {

		if (this.loops == Long.MAX_VALUE) {
			this.loops = 0;
		}

		this.loops++;
		Thread.sleep(this.delay);
	}

	public boolean isTime(int velocity) {

		boolean isTime = false;

		if (this.loops % velocity == 0) {

			isTime = true;
		}

		return isTime;
	}
}
