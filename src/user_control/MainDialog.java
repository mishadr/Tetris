package user_control;


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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;


public class MainDialog extends JDialog {

	private static final long serialVersionUID = 2384296700714804348L;
	public static final File gameDir = new File("D:/Games/Tetris/");
	private final DrawerPanel drawerPanel;
	private final GameManager manager;
	private JLabel speedLabel;
	private JLabel linesLabel;
	private JLabel figuresLabel;
	private JLabel scoreLabel;

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
		setTitle("Tetris v.1");
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
		
		manager = new GameManager();
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
				JMenu mnGame = new JMenu("Game");
				menuBar.add(mnGame);
				{
					JMenuItem mntmBeginNew = new JMenuItem("Begin new");
					mntmBeginNew.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							manager.beginNewGame();
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
				JMenu mnParameters = new JMenu("New game");
				menuBar.add(mnParameters);
				{
					JMenuItem mntmChange = new JMenuItem("Custom parameters...");
					mntmChange.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeParameters();
//							manager.beginNewGame();
						}
					});
					{
						JMenuItem mntmSimpleSet = new JMenuItem("Simple set");
						mntmSimpleSet.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								manager.setParams(GameParameters.simple());
								manager.beginNewGame();
							}
						});
						{
							JMenuItem mntmDeafaultSet = new JMenuItem("Default set");
							mntmDeafaultSet.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									manager.setParams(GameParameters.defaultParameters());
									manager.beginNewGame();
								}
							});
							mnParameters.add(mntmDeafaultSet);
						}
						mnParameters.add(mntmSimpleSet);
					}
					{
						JMenuItem mntmMediumSet = new JMenuItem("Medium set");
						mntmMediumSet.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								manager.setParams(GameParameters.medium());
								manager.beginNewGame();
							}
						});
						mnParameters.add(mntmMediumSet);
					}
					{
						JMenuItem mntmLargeSet = new JMenuItem("Large set");
						mntmLargeSet.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								manager.setParams(GameParameters.large());
								manager.beginNewGame();
							}
						});
						mnParameters.add(mntmLargeSet);
					}
					{
						JMenuItem mntmFullSet = new JMenuItem("Full set");
						mntmFullSet.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								manager.setParams(GameParameters.full());
								manager.beginNewGame();
							}
						});
						mnParameters.add(mntmFullSet);
					}
					{
						JSeparator separator = new JSeparator();
						mnParameters.add(separator);
					}
					mnParameters.add(mntmChange);
				}
			}
		}
		manager.setUpLabels(speedLabel, linesLabel, figuresLabel, scoreLabel);
//		manager.beginNewGame();
	}

    protected void changeParameters() {
    	new ParametersDialog(this, manager);
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
