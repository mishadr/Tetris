package user_control;


import game_engine.DrawerPanel;
import game_engine.GameManager;
import game_engine.GameParameters;
import game_engine.GameStyle;
import game_engine.Level;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;



public class MainDialog extends JFrame {

	private static final long serialVersionUID = 2384296700714804348L;
	public static final File gameDir = new File("D:/Games/Tetris");
	public static final String title = "Tetris v.3 : ";
	
	private final DrawerPanel drawerPanel;
	private final GameManager manager;
	private Level level;
	private GameStyle gameStyle;
	private JLabel speedLabel;
	private JLabel linesLabel;
	private JLabel figuresLabel;
	private JLabel scoreLabel;
	private final ButtonGroup levelButtonGroup = new ButtonGroup();
	private final ButtonGroup gameStyleButtonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			gameDir.mkdirs();
			MainDialog dialog = new MainDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MainDialog() {
		setBounds(400, 120, 400, 346);
		setResizable(false);
//		setUndecorated(true);
//		setOpacity(0.8f);
//		setUndecorated(false);
//		setShape(new Ellipse2D.Double(0, 0, 2000, 800));
		try {
			setIconImage(ImageIO.read(new File(gameDir, "icon.jpg")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{100, 80, 0};
		gridBagLayout.rowHeights = new int[]{30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		getContentPane().setLayout(gridBagLayout);
		
		level = Level.DEFAULT;
		gameStyle = GameStyle.CLASSIC;
		setTitle(title + level + " " + gameStyle);

		manager = new GameManager(level, gameStyle);
		drawerPanel = new DrawerPanel(this);
		drawerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagConstraints gbc_drawerPanel = new GridBagConstraints();
		gbc_drawerPanel.gridheight = 11;
		gbc_drawerPanel.insets = new Insets(0, 0, 0, 0);
		gbc_drawerPanel.fill = GridBagConstraints.BOTH;
		gbc_drawerPanel.gridx = 0;
		gbc_drawerPanel.gridy = 0;
		getContentPane().add(drawerPanel, gbc_drawerPanel);
		manager.setUpDrawer(drawerPanel);
		Controller.setListeners(drawerPanel, manager);
		drawerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		{
			JButton pauseButton = new JButton("PAUSE");
			GridBagConstraints gbc_pauseButton = new GridBagConstraints();
			gbc_pauseButton.insets = new Insets(5, 10, 5, 10);
			gbc_pauseButton.gridx = 1;
			gbc_pauseButton.gridy = 0;
			getContentPane().add(pauseButton, gbc_pauseButton);
			pauseButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					manager.pauseGame();
				}
			});
			pauseButton.setFocusable(false);
		}
		{
			JLabel lblLines = new JLabel("Speed:");
			GridBagConstraints gbc_lblLines = new GridBagConstraints();
			gbc_lblLines.insets = new Insets(0, 0, 5, 5);
			gbc_lblLines.gridx = 1;
			gbc_lblLines.gridy = 2;
			getContentPane().add(lblLines, gbc_lblLines);
		}
		{
			speedLabel = new JLabel("0");
			GridBagConstraints gbc_lblXxx = new GridBagConstraints();
			gbc_lblXxx.insets = new Insets(0, 0, 5, 5);
			gbc_lblXxx.gridx = 1;
			gbc_lblXxx.gridy = 3;
			getContentPane().add(speedLabel, gbc_lblXxx);
		}
		{
			JLabel lblLines_1 = new JLabel("Lines:");
			GridBagConstraints gbc_lblLines_1 = new GridBagConstraints();
			gbc_lblLines_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblLines_1.gridx = 1;
			gbc_lblLines_1.gridy = 4;
			getContentPane().add(lblLines_1, gbc_lblLines_1);
		}
		{
			linesLabel = new JLabel("0");
			GridBagConstraints gbc_lblXxx_1 = new GridBagConstraints();
			gbc_lblXxx_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblXxx_1.gridx = 1;
			gbc_lblXxx_1.gridy = 5;
			getContentPane().add(linesLabel, gbc_lblXxx_1);
		}
		{
			JLabel lblFigures = new JLabel("Figures:");
			GridBagConstraints gbc_lblFigures = new GridBagConstraints();
			gbc_lblFigures.insets = new Insets(0, 0, 5, 5);
			gbc_lblFigures.gridx = 1;
			gbc_lblFigures.gridy = 6;
			getContentPane().add(lblFigures, gbc_lblFigures);
		}
		{
			figuresLabel = new JLabel("0");
			GridBagConstraints gbc_lblXxx_2 = new GridBagConstraints();
			gbc_lblXxx_2.insets = new Insets(0, 0, 5, 5);
			gbc_lblXxx_2.gridx = 1;
			gbc_lblXxx_2.gridy = 7;
			getContentPane().add(figuresLabel, gbc_lblXxx_2);
		}
		{
			JLabel lblScore = new JLabel("Score:");
			GridBagConstraints gbc_lblScore = new GridBagConstraints();
			gbc_lblScore.insets = new Insets(0, 0, 5, 5);
			gbc_lblScore.gridx = 1;
			gbc_lblScore.gridy = 8;
			getContentPane().add(lblScore, gbc_lblScore);
		}
		{
			scoreLabel = new JLabel("0");
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.insets = new Insets(0, 0, 0, 5);
			gbc_label.gridx = 1;
			gbc_label.gridy = 9;
			getContentPane().add(scoreLabel, gbc_label);
		}
		{
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			{
				JMenu mnGame = new JMenu("This game");
				menuBar.add(mnGame);
				{
					JMenuItem mntmBeginNew = new JMenuItem("Restart");
					mntmBeginNew.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							manager.restart();
						}
					});
					mnGame.add(mntmBeginNew);
				}
				{
					JMenuItem mntmPause = new JMenuItem("Pause");
					mntmPause.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							manager.pauseGame();
						}
					});
					mnGame.add(mntmPause);
				}
				{
					JMenuItem mntmFinish = new JMenuItem("Finish");
					mntmFinish.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							manager.finishGame();
						}
					});
					mnGame.add(mntmFinish);
				}
				{
					JSeparator separator = new JSeparator();
					mnGame.add(separator);
				}
				{
					JMenuItem mntmExit = new JMenuItem("Exit");
					mntmExit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							manager.finishGame();
							System.exit(0);
						}
					});
					mnGame.add(mntmExit);
				}
			}
			{
				JMenu mnParameters = new JMenu("Level");
				menuBar.add(mnParameters);
				{
					JRadioButton mntmEasy = new JRadioButton("Easy");
					levelButtonGroup.add(mntmEasy);
					mntmEasy.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							level = Level.EASY;
						}
					});
					{
						JRadioButton mntmDeafault = new JRadioButton("Default");
						levelButtonGroup.add(mntmDeafault);
						levelButtonGroup.setSelected(mntmDeafault.getModel(), true);
						mntmDeafault.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								level = Level.DEFAULT;
							}
						});
						mnParameters.add(mntmDeafault);
					}
					mnParameters.add(mntmEasy);
				}
				{
					JRadioButton mntmMedium = new JRadioButton("Medium");
					levelButtonGroup.add(mntmMedium);
					mntmMedium.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							level = Level.MEDIUM;
						}
					});
					mnParameters.add(mntmMedium);
				}
				{
					JRadioButton mntmHard = new JRadioButton("Hard");
					levelButtonGroup.add(mntmHard);
					mntmHard.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							level = Level.HARD;
						}
					});
					mnParameters.add(mntmHard);
				}
				{
					JRadioButton mntmMaster = new JRadioButton("Master");
					levelButtonGroup.add(mntmMaster);
					mntmMaster.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							level = Level.MASTER;
						}
					});
					mnParameters.add(mntmMaster);
				}
				{
					JRadioButton rdbtnFull = new JRadioButton("Full");
					levelButtonGroup.add(rdbtnFull);
					rdbtnFull.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							level = Level.FULL;
						}
					});
					mnParameters.add(rdbtnFull);
				}
			}
			{
				JMenu mnGameStyle = new JMenu("Game style");
				menuBar.add(mnGameStyle);
				{
					JRadioButton mntmClassic = new JRadioButton("Classic");
					gameStyleButtonGroup.add(mntmClassic);
					gameStyleButtonGroup.setSelected(mntmClassic.getModel(), true);
					mntmClassic.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							gameStyle = GameStyle.CLASSIC;
						}
					});
					mnGameStyle.add(mntmClassic);
				}
				{
					JRadioButton mntmCyclicField = new JRadioButton("Cycled field");
					gameStyleButtonGroup.add(mntmCyclicField);
					mntmCyclicField.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							gameStyle = GameStyle.CYCLED_FIELD;
						}
					});
					mnGameStyle.add(mntmCyclicField);
				}
				{
					JRadioButton mntmBoxPacking = new JRadioButton("Box packing");
					gameStyleButtonGroup.add(mntmBoxPacking);
					mntmBoxPacking.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							gameStyle = GameStyle.BOX_PACKING;
						}
					});
					mnGameStyle.add(mntmBoxPacking);
				}
				{
					JRadioButton mntmGarbageDeleting = new JRadioButton("Garbage deleting");
					gameStyleButtonGroup.add(mntmGarbageDeleting);
					mntmGarbageDeleting.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							gameStyle = GameStyle.GARBAGE_DELETING;
						}
					});
					mnGameStyle.add(mntmGarbageDeleting);
					mntmGarbageDeleting.setEnabled(false);// XXX not implemented yet
				}
			}
			{
				JMenu mnNewGame = new JMenu("New game");
				menuBar.add(mnNewGame);
				{
					JMenuItem mntmChosenLevel = new JMenuItem("Chosen level & style");
					mntmChosenLevel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							startNewGame();
						}
					});
					mnNewGame.add(mntmChosenLevel);
				}
				{
					JMenuItem mntmFullyCustomGame = new JMenuItem("Fully custom game...");
					mntmFullyCustomGame.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeParameters();
						}
					});
					mnNewGame.add(mntmFullyCustomGame);
				}
			}
		}
		// TODO manager.beginNewGame(); in new_game.type menu
		
		manager.setupLabels(speedLabel, linesLabel, figuresLabel, scoreLabel);
//		manager.beginNewGame();
	}

    protected void startNewGame() {
		manager.beginNewGame(level, gameStyle);
		setTitle(title + level + " - " + gameStyle);
	}

	protected void changeParameters() {
    	new ParametersDialog(this, manager, new GameParameters(level, gameStyle));
	}

	protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);

        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//        	manager.saveResults();
        	manager.finishGame();
            System.exit(0);
        }
    }
}
