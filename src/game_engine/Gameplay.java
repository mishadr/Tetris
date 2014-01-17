package game_engine;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Storage for current game information. It should update associated
 * {@link JLabel}s and {@link Timer}.
 * 
 * @author misha
 * 
 */
public class Gameplay {
	private static final int SCORE_PER_LEVEL = 100;
	private static final int SCORE_PER_LINE = 10;
	private static final double SCORE_PER_FIGURE = 1;
	private static final double SCORE_PER_DOWNSTEP = 0;
	
	private static final int MAX_LEVEL = 9;
	private static final int[] FIGURES_PER_SPEED = new int[] {
		30, 50, 100, 150, 200, 250, 300, 500, 1000
	};
	private static final int[] INTERVALS_PER_SPEED = new int[] {
		1500, 1200, 1000, 900, 800, 700, 600, 500, 400
	};
	private int speed;
	private JLabel speedLabel;

	private int linesCount;
	private JLabel linesLabel;

	private int figuresCount;
	private JLabel figuresLabel;

	private double score;
	private JLabel scoreLabel;
	private final Timer timer;

	public Gameplay(Timer timer) {
		this.timer = timer;
	}

	public void setLabels(JLabel speedLabel, JLabel linesLabel,
			JLabel figuresLabel, JLabel scoreLabel) {
		this.speedLabel = speedLabel;
		this.linesLabel = linesLabel;
		this.figuresLabel = figuresLabel;
		this.scoreLabel = scoreLabel;
	}

	public double getScore() {
		return score;
	}

	/**
	 * Drop game achivements and timer delay
	 */
	public void clear() {
		speed = 0;
		linesCount = 0;
		figuresCount = 0;
		score = 0;
		timer.setDelay(INTERVALS_PER_SPEED[speed]);
		updateLabels();
	}

	private void updateLabels() {
		speedLabel.setText("" + speed);
		linesLabel.setText("" + linesCount);
		figuresLabel.setText("" + figuresCount);
		scoreLabel.setText("" + score);
	}

	void movedDown() {
		score += SCORE_PER_DOWNSTEP;
		updateLabels();
	}

	void finishedFigure() {
		figuresCount++;
		score += SCORE_PER_FIGURE;
		checkForNewSpeed();
		updateLabels();
	}

	private void checkForNewSpeed() {
		if(figuresCount >= FIGURES_PER_SPEED[speed])
		{
			speed++;
			if(speed == MAX_LEVEL)
			{
				// finish game!
				speed = 0;
				score *= 2;
			}
			timer.setDelay(INTERVALS_PER_SPEED[speed]);
			score += speed * SCORE_PER_LEVEL;
			updateLabels();
		}
	}

	/**
	 * Score plus for n=1,2.. lines: {2, 6, 12, 20, 30, 42}
	 * 
	 * @param lines
	 */
	void deletedLines(int lines) {
		linesCount += lines;
		score += lines * (lines + 1) * SCORE_PER_LINE;
		updateLabels();
	}
}
