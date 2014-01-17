package user_control;

import game_engine.Game;
import game_engine.Game.Action;
import game_engine.GameResult;
import game_engine.Gameplay;
import game_engine.figures.Figure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.Timer;

import auto_solver.Solver;

public class GameManager {

	private final Gameplay gameplay;
	private Game currentGame;
	private GameParameters params;
	private DrawerPanel drawer;
	private Timer timer;

	public GameManager() {
		currentGame = null;
		params = GameParameters.defaultParameters();
		initTimer();
		gameplay = new Gameplay(timer);
	}

	List<Figure> figures = Solver.loadFiguresFromFile("large_figures1000.fg");
	int i = 0;
	private void initTimer() {
		int delay = 900;
		timer = new Timer(delay, new ActionListener() {

			
			public void actionPerformed(ActionEvent e) {
				if (!currentGame.performStep()) {
					currentGame.finishFigure();
					if (!currentGame.prepareNewFigure(/*figures.get(i++)*/)) {
						finishGame();
					}
				}
				repaint();
			}
		});
	}

	/**
	 * Should be called next to the constructor
	 * 
	 * @param drawer
	 */
	void setUpDrawer(DrawerPanel drawer) {
		this.drawer = drawer;
	}

	void setUpLabels(JLabel speedLabel, JLabel linesLabel, JLabel figuresLabel, JLabel scoreLabel) {
		gameplay.setLabels(speedLabel, linesLabel, figuresLabel, scoreLabel);
	}

	private void repaint() {
		drawer.repaint();
	}

	GameParameters getParams() {
		return params;
	}

	void setParams(GameParameters gameParameters) {
		this.params = gameParameters;
	}

	void beginNewGame() {
		timer.stop();
		gameplay.clear();
		currentGame = new Game(params, gameplay);
		if (!currentGame.prepareNewFigure(/*figures.get(i++)*/)) {
			System.err.println("error at start");
		}
		drawer.gameToDraw(currentGame);
		repaint();
		Controller.unlock();
		timer.start();
	}

	void finishGame() {
		if (currentGame == null) {
			return;
		}
		timer.stop();
		Controller.lock();
		currentGame = null;
		saveResults();
	}

	void saveResults() {
		try {
			GameResult.saveResults(gameplay, params);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void pauseGame() {
		if (currentGame == null) {
			return;
		}
		if (timer.isRunning())
			timer.stop();
		else
			timer.start();
	}

	void performAction(Action action) {
		/* boolean success = */currentGame.performAction(action);
		repaint();
	}
}
