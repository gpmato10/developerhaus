package bowling;

public class Game {

	private int currentRoll;
	private int[] roll = new int[21];

	public void roll(int pins) {
		this.roll[this.currentRoll++] = pins;
	}

	public int score() {
		int score = 0;
		int i = 0;
		for (int frame = 0; frame < 10; frame++) {
			if (isStrike(i)) {
				score += 10 + strikeBonus(i);
				i++;
			} else if (isSpare(i)) {
				score += 10 + spareBonus(i);
				i += 2;
			} else {
				score += sumOfPinsInFrame(i);
				i += 2;
			}
		}
		return score;
	}

	private int strikeBonus(int i) {
		return this.roll[i + 1] + this.roll[i + 2];
	}

	private boolean isSpare(int i) {
		return sumOfPinsInFrame(i) == 10;
	}

	private boolean isStrike(int i) {
		return this.roll[i] == 10;
	}

	private int spareBonus(int i) {
		return this.roll[i + 2];
	}

	private int sumOfPinsInFrame(int i) {
		return this.roll[i] + this.roll[i + 1];
	}
}
