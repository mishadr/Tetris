package game_engine;

import game_engine.Game.Action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import user_control.Controller;

/**
 * Manager of game process. It can start, stop, pause, finish, save the current
 * game and run solver on it. Manager should filter {@link Action}s over the
 * game according to allowed ones in {@link GameParameters} and setup
 * {@link Gameplay} depending on {@link GameStyle}. It must regularly update associated
 * {@link JLabel}s.
 * 
 * @author misha
 * 
 */
public class GameManager {

	private GameParameters params;
	private Game currentGame;
	private boolean isPlaying;
	
	private DrawerPanel drawer;
	private Timer gameTimer;
	private Timer drawerTimer;

	private JLabel speedLabel;
	private JLabel linesLabel;
	private JLabel figuresLabel;
	private JLabel scoreLabel;
	
	public GameManager(Level level, GameStyle gameStyle) {
		currentGame = null;
		isPlaying = false;
		initTimers();
		// XXX is it needed?
		params = new GameParameters(level, gameStyle);
		// XXX is it needed?
//		gameplay = new Gameplay(timer, params);
	}

//	List<AbstractFigure> figures = Solver.loadFiguresFromFile("medium_figures1000.fg");
	int i = 0;
	private void initTimers() {
		gameTimer = new Timer(10000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!currentGame.performStep()) {
					currentGame.finishFigure();
					if(params.isIncreaseSpeed()) {
						currentGame.checkForNewSpeed();
					}
					if(params.isFullLinesDeleting()) {
						currentGame.deleteFullLines();
					}
					if(params.isFieldMoving()) {
						currentGame.moveField(1);
					}
					if(params.isFadeWhenFigureFinished()) {
						drawer.timeStep(currentGame.getFieldGrid());
					}
					if (!currentGame.prepareNewFigure(/*figures.get(i++)*/)) {
						finishGame();
					}
					drawer.figureToDraw(currentGame.getFigure());
				}
				repaint();
			}
		});
		
		drawerTimer = new Timer(35, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!params.isFadeWhenFigureFinished()) {
					drawer.timeStep(currentGame.getFieldGrid());
					drawer.repaint();
				}
			}
		});
	}

	private void repaint() {
		speedLabel.setText("" + currentGame.getSpeed());
		linesLabel.setText("" + currentGame.getLinesCount());
		figuresLabel.setText("" + currentGame.getFiguresCount());
		scoreLabel.setText("" + currentGame.getScore());
		drawer.repaint();
	}

	/**
	 * Should be called next to the constructor
	 * 
	 * @param drawer
	 */
	public void setUpDrawer(DrawerPanel drawer) {
		this.drawer = drawer;
	}

	public void setupLabels(JLabel speedLabel, JLabel linesLabel,
			JLabel figuresLabel, JLabel scoreLabel) {
		this.speedLabel = speedLabel;
		this.linesLabel = linesLabel;
		this.figuresLabel = figuresLabel;
		this.scoreLabel = scoreLabel;
	}

	public GameParameters getParams() {
		return params;
	}

	/**
	 * New game with custom {@link GameParameters}
	 */
	public void beginNewGame(GameParameters params) {
		gameTimer.stop();
		drawerTimer.stop();
		this.params = params;
		drawer.setFadingIterations(params.isFadeWhenFigureFinished() ? params
				.getFadingIterationsCount() : 256);
		currentGame = new Game(gameTimer, params);
		isPlaying = true;
		
		if (!currentGame.prepareNewFigure(/* figures.get(i++) */)) {
			System.err.println("error at start");
		}
		drawer.fieldToDraw(currentGame.getFieldGrid());
		drawer.figureToDraw(currentGame.getFigure());
		repaint();
		Controller.unlock();
		gameTimer.start();
		drawerTimer.start();
	}
	
	public void beginNewGame(Level level, GameStyle gameStyle) {
		this.params = new GameParameters(level, gameStyle);
		beginNewGame(params);
	}

	public void restart() {
		beginNewGame(params);
	}

	public void finishGame() {
		gameTimer.stop();
		drawerTimer.stop();
		isPlaying = false;
		Controller.lock();
		saveResults();
	}

	public void saveResults() {
//		try {
//			// TODO refactor
//			GameResult.saveResults(currentGame, params);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void pauseGame() {
		if (!isPlaying) {
			return;
		}
		if (gameTimer.isRunning()) {
			gameTimer.stop();
//			if(!params.isAllTimeFading()) {
//				drawerTimer.stop();
//			}
		}
		else {
			gameTimer.start();
//			if(!params.isAllTimeFading()) {
//				drawerTimer.start();
//			}
		}
	}

	/**
	 * Method should filter only allowed in {@link GameParameters} actions. 
	 * 
	 * @param action
	 */
	public void performAction(Action action) {
		if(action == Action.VERTICAL_REFLECT && !params.isReflectionsAllowed()) {
			return;
		}
		/* boolean success = */currentGame.performAction(action);
		repaint();
	}
}
