package user_control;

import game_engine.GameManager;
import game_engine.GameParameters;
import game_engine.figures.AbstractFiguresChooser;
import game_engine.figures.FigureType;
import game_engine.figures.UniformByItemFiguresChooser;
import game_engine.figures.UniformByTypeFiguresChooser;
import game_engine.scoring.AbstractScoringStrategy;
import game_engine.scoring.ClassicScoring;
import game_engine.scoring.PackingScoring;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ButtonGroup;

public class ParametersDialog extends JDialog {

	private static final long serialVersionUID = 7227194244996854170L;
	private final JPanel contentPanel = new JPanel();
	private JRadioButton rdbtnSemiwhole2;
	private JRadioButton rdbtnSemiwhole3;
	private JRadioButton rdbtnWhole[] = new JRadioButton[FigureType.WHOLE_MAX_BRICKS];
	private JRadioButton rdbtnPenWhole[] = new JRadioButton[FigureType.WHOLE_MAX_BRICKS];
//	private JRadioButton rdbtnMoreBricks;
//	private JRadioButton rdbtnPenetrating;
	private JSpinner spinnerWidth;
	private JSpinner spinnerHeight;
//	private JSpinner spinnerBrickSize;
	private GameManager manager;
	private JCheckBox chckbxAllowReflections;
	private JCheckBox chckbxAllowPause;
	private JCheckBox chckbxFigureSwap;
	private JCheckBox chckbxAllowShiftDown;
	private JCheckBox chckbxShowNextFigure;
	private JCheckBox chckbxDeleteFullLines;
	private JCheckBox chckbxIncreaseSpeed;
	private JCheckBox chckbxMovingField;
	private final ButtonGroup figureChooserButtonGroup = new ButtonGroup();
	private JRadioButton rdbtnUniformByItem;
	private JRadioButton rdbtnUniformByType;
	private JRadioButton rdbtnPenSemiwhole2;
	private JRadioButton rdbtnPenSemiwhole3;

	public ParametersDialog(Window owner, GameManager manager, GameParameters gameParameters)
	{
		super(owner);
		this.manager = manager;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		openDialog();
//		setUpParams(gameParameters);
		setUpParams(manager.getParams());
		setVisible(true);
	}
	
	private void setUpParams(GameParameters params) {
		spinnerWidth.setValue(params.getWidth());
		spinnerHeight.setValue(params.getHeight());
//		spinnerBrickSize.setValue(manager.DrawerPanel.getBrickSize());
		
		List<FigureType> types = Arrays.asList(params.getIncludedTypes());
		for(int i=0; i<FigureType.WHOLE_MAX_BRICKS; ++i)
		{
			rdbtnWhole[i].setSelected(types.contains(FigureType.valueOf("WHOLE_"+(i+1))));
			rdbtnPenWhole[i].setSelected(types.contains(FigureType.valueOf("PENETRATING_WHOLE_"+(i+1))));
		}
//		rdbtnMoreBricks.setSelected(types.contains(FigureType.WHOLE_MORE));
		rdbtnSemiwhole2.setSelected(types.contains(FigureType.SEMIWHOLE_2));
		rdbtnSemiwhole3.setSelected(types.contains(FigureType.SEMIWHOLE_3));
		rdbtnPenSemiwhole2.setSelected(types.contains(FigureType.PENETRATING_SEMIWHOLE_2));
		rdbtnPenSemiwhole3.setSelected(types.contains(FigureType.PENETRATING_SEMIWHOLE_3));
//		rdbtnPenetrating.setSelected(types.contains(FigureType.PENETRATING));
		chckbxAllowReflections.setSelected(params.isReflectionsAllowed());
		chckbxAllowPause.setSelected(params.isPauseAllowed());
		chckbxFigureSwap.setSelected(params.isReflectionsAllowed());
		chckbxAllowShiftDown.setSelected(params.isDownShiftAllowed());
		chckbxShowNextFigure.setSelected(params.isNextFiguresShowed());
		chckbxDeleteFullLines.setSelected(params.isFullLinesDeleting());
		chckbxIncreaseSpeed.setSelected(params.isIncreaseSpeed());
		chckbxMovingField.setSelected(params.isFieldMoving());
		
		rdbtnUniformByItem.setSelected(params.getFiguresChooser().getClass() == UniformByItemFiguresChooser.class);
		rdbtnUniformByType.setSelected(params.getFiguresChooser().getClass() == UniformByTypeFiguresChooser.class);
		
		// not imlemented yet:
		chckbxAllowPause.setEnabled(false);
		chckbxFigureSwap.setEnabled(false);
		chckbxAllowShiftDown.setEnabled(false);
		chckbxShowNextFigure.setEnabled(false);
	}

	/**
	 * Create the dialog.
	 */
	public void openDialog() {
		setTitle("Settings");
		setBounds(100, 100, 650, 440);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblIncludeTypes = new JLabel("Figure types:");
			lblIncludeTypes.setFont(new Font("Tahoma", Font.PLAIN, 14));
			GridBagConstraints gbc_lblIncludeTypes = new GridBagConstraints();
			gbc_lblIncludeTypes.gridwidth = 4;
			gbc_lblIncludeTypes.insets = new Insets(0, 0, 5, 5);
			gbc_lblIncludeTypes.gridx = 0;
			gbc_lblIncludeTypes.gridy = 0;
			contentPanel.add(lblIncludeTypes, gbc_lblIncludeTypes);
		}
		for(int i=0; i<FigureType.WHOLE_MAX_BRICKS; ++i)
		{
			rdbtnWhole[i] = new JRadioButton((i+1) + " brick");
			GridBagConstraints gbc_rdbtnWhole = new GridBagConstraints();
			gbc_rdbtnWhole.anchor = GridBagConstraints.EAST;
			gbc_rdbtnWhole.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnWhole.gridx = 0;
			gbc_rdbtnWhole.gridy = i+2;
			contentPanel.add(rdbtnWhole[i], gbc_rdbtnWhole);
			
			rdbtnPenWhole[i] = new JRadioButton("");
			GridBagConstraints gbc_rdbtnPenWhole = new GridBagConstraints();
			gbc_rdbtnPenWhole.anchor = GridBagConstraints.WEST;
			gbc_rdbtnPenWhole.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnPenWhole.gridx = 1;
			gbc_rdbtnPenWhole.gridy = i+2;
			contentPanel.add(rdbtnPenWhole[i], gbc_rdbtnPenWhole);
			
		}
//		{
//			rdbtnPenWhole1 = new JRadioButton("");
//			GridBagConstraints gbc_rdbtnPenWhole1 = new GridBagConstraints();
//			gbc_rdbtnPenWhole1.anchor = GridBagConstraints.WEST;
//			gbc_rdbtnPenWhole1.insets = new Insets(0, 0, 5, 5);
//			gbc_rdbtnPenWhole1.gridx = 1;
//			gbc_rdbtnPenWhole1.gridy = 2;
//			contentPanel.add(rdbtnPenWhole1, gbc_rdbtnPenWhole1);
//		}
		{
			JSeparator separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
			GridBagConstraints gbc_separator = new GridBagConstraints();
			gbc_separator.fill = GridBagConstraints.VERTICAL;
			gbc_separator.gridheight = 12;
			gbc_separator.insets = new Insets(0, 0, 0, 5);
			gbc_separator.gridx = 7;
			gbc_separator.gridy = 0;
			contentPanel.add(separator, gbc_separator);
		}
		{
			JLabel lblGameProperties = new JLabel("Game properties:");
			lblGameProperties.setFont(new Font("Tahoma", Font.PLAIN, 14));
			GridBagConstraints gbc_lblGameProperties = new GridBagConstraints();
			gbc_lblGameProperties.gridwidth = 2;
			gbc_lblGameProperties.insets = new Insets(0, 0, 5, 0);
			gbc_lblGameProperties.gridx = 8;
			gbc_lblGameProperties.gridy = 0;
			contentPanel.add(lblGameProperties, gbc_lblGameProperties);
		}
		{
			chckbxAllowReflections = new JCheckBox("allow reflections");
			GridBagConstraints gbc_chckbxAllowReflections = new GridBagConstraints();
			gbc_chckbxAllowReflections.anchor = GridBagConstraints.WEST;
			gbc_chckbxAllowReflections.gridwidth = 2;
			gbc_chckbxAllowReflections.insets = new Insets(0, 0, 5, 0);
			gbc_chckbxAllowReflections.gridx = 8;
			gbc_chckbxAllowReflections.gridy = 1;
			contentPanel.add(chckbxAllowReflections, gbc_chckbxAllowReflections);
		}
//		{
//			rdbtnPenWhole2 = new JRadioButton("");
//			GridBagConstraints gbc_rdbtnPenWhole2 = new GridBagConstraints();
//			gbc_rdbtnPenWhole2.anchor = GridBagConstraints.WEST;
//			gbc_rdbtnPenWhole2.insets = new Insets(0, 0, 5, 5);
//			gbc_rdbtnPenWhole2.gridx = 1;
//			gbc_rdbtnPenWhole2.gridy = 3;
//			contentPanel.add(rdbtnPenWhole2, gbc_rdbtnPenWhole2);
//		}
//		{
//			rdbtnPenWhole3 = new JRadioButton("");
//			GridBagConstraints gbc_rdbtnPenWhole3 = new GridBagConstraints();
//			gbc_rdbtnPenWhole3.anchor = GridBagConstraints.WEST;
//			gbc_rdbtnPenWhole3.insets = new Insets(0, 0, 5, 5);
//			gbc_rdbtnPenWhole3.gridx = 1;
//			gbc_rdbtnPenWhole3.gridy = 4;
//			contentPanel.add(rdbtnPenWhole3, gbc_rdbtnPenWhole3);
//		}
//		{
//			rdbtnPenWhole4 = new JRadioButton("");
//			GridBagConstraints gbc_rdbtnPenWhole4 = new GridBagConstraints();
//			gbc_rdbtnPenWhole4.anchor = GridBagConstraints.WEST;
//			gbc_rdbtnPenWhole4.insets = new Insets(0, 0, 5, 5);
//			gbc_rdbtnPenWhole4.gridx = 1;
//			gbc_rdbtnPenWhole4.gridy = 5;
//			contentPanel.add(rdbtnPenWhole4, gbc_rdbtnPenWhole4);
//		}
		{
			rdbtnSemiwhole2 = new JRadioButton("2 bricks");
			GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
			gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.EAST;
			gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnNewRadioButton.gridx = 2;
			gbc_rdbtnNewRadioButton.gridy = 2;
			contentPanel.add(rdbtnSemiwhole2, gbc_rdbtnNewRadioButton);
		}
		{
			rdbtnSemiwhole3 = new JRadioButton("3 bricks");
			GridBagConstraints gbc_rdbtnBricks = new GridBagConstraints();
			gbc_rdbtnBricks.anchor = GridBagConstraints.EAST;
			gbc_rdbtnBricks.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnBricks.gridx = 2;
			gbc_rdbtnBricks.gridy = 3;
			contentPanel.add(rdbtnSemiwhole3, gbc_rdbtnBricks);
		}
		{
			rdbtnPenSemiwhole2 = new JRadioButton("");
			GridBagConstraints gbc_rdbtnPenSemiwhole2 = new GridBagConstraints();
			gbc_rdbtnPenSemiwhole2.anchor = GridBagConstraints.WEST;
			gbc_rdbtnPenSemiwhole2.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnPenSemiwhole2.gridx = 3;
			gbc_rdbtnPenSemiwhole2.gridy = 2;
			contentPanel.add(rdbtnPenSemiwhole2, gbc_rdbtnPenSemiwhole2);
		}
		{
			rdbtnPenSemiwhole3 = new JRadioButton("");
			GridBagConstraints gbc_rdbtnPenSemiwhole3 = new GridBagConstraints();
			gbc_rdbtnPenSemiwhole3.anchor = GridBagConstraints.WEST;
			gbc_rdbtnPenSemiwhole3.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnPenSemiwhole3.gridx = 3;
			gbc_rdbtnPenSemiwhole3.gridy = 3;
			contentPanel.add(rdbtnPenSemiwhole3, gbc_rdbtnPenSemiwhole3);
		}
		{
			chckbxAllowPause = new JCheckBox("allow pause");
			GridBagConstraints gbc_chckbxAllowPause = new GridBagConstraints();
			gbc_chckbxAllowPause.anchor = GridBagConstraints.WEST;
			gbc_chckbxAllowPause.gridwidth = 2;
			gbc_chckbxAllowPause.insets = new Insets(0, 0, 5, 0);
			gbc_chckbxAllowPause.gridx = 8;
			gbc_chckbxAllowPause.gridy = 2;
			contentPanel.add(chckbxAllowPause, gbc_chckbxAllowPause);
		}
		{
			JSeparator separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
			GridBagConstraints gbc_separator = new GridBagConstraints();
			gbc_separator.fill = GridBagConstraints.VERTICAL;
			gbc_separator.gridheight = 12;
			gbc_separator.insets = new Insets(0, 0, 0, 5);
			gbc_separator.gridx = 4;
			gbc_separator.gridy = 0;
			contentPanel.add(separator, gbc_separator);
		}
		{
			JLabel lblFieldProperties = new JLabel("Field properties:");
			lblFieldProperties.setFont(new Font("Tahoma", Font.PLAIN, 14));
			GridBagConstraints gbc_lblFieldProperties = new GridBagConstraints();
			gbc_lblFieldProperties.gridwidth = 2;
			gbc_lblFieldProperties.insets = new Insets(0, 0, 5, 5);
			gbc_lblFieldProperties.gridx = 5;
			gbc_lblFieldProperties.gridy = 0;
			contentPanel.add(lblFieldProperties, gbc_lblFieldProperties);
		}
		{
			JLabel lblWhole = new JLabel("Whole & penetrating:");
			GridBagConstraints gbc_lblWhole = new GridBagConstraints();
			gbc_lblWhole.gridwidth = 2;
			gbc_lblWhole.insets = new Insets(0, 0, 5, 5);
			gbc_lblWhole.gridx = 0;
			gbc_lblWhole.gridy = 1;
			contentPanel.add(lblWhole, gbc_lblWhole);
		}
		{
			JLabel lblSemiwhole = new JLabel("Semiwhole & penetrating:");
			GridBagConstraints gbc_lblSemiwhole = new GridBagConstraints();
			gbc_lblSemiwhole.gridwidth = 2;
			gbc_lblSemiwhole.insets = new Insets(0, 0, 5, 5);
			gbc_lblSemiwhole.gridx = 2;
			gbc_lblSemiwhole.gridy = 1;
			contentPanel.add(lblSemiwhole, gbc_lblSemiwhole);
		}
		{
			JLabel lblWidth = new JLabel("Width:");
			GridBagConstraints gbc_lblWidth = new GridBagConstraints();
			gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
			gbc_lblWidth.gridx = 5;
			gbc_lblWidth.gridy = 1;
			contentPanel.add(lblWidth, gbc_lblWidth);
		}
		{
			spinnerWidth = new JSpinner();
			spinnerWidth.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int value = (int)spinnerWidth.getValue();
					if(value < GameParameters.WIDTH_MIN)
						spinnerWidth.setValue(GameParameters.WIDTH_MIN);
					else if(value > GameParameters.WIDTH_MAX)
						spinnerWidth.setValue(GameParameters.WIDTH_MAX);
				}
			});
			spinnerWidth.setPreferredSize(new Dimension(70, 25));
			GridBagConstraints gbc_spinner = new GridBagConstraints();
			gbc_spinner.insets = new Insets(0, 0, 5, 5);
			gbc_spinner.gridx = 6;
			gbc_spinner.gridy = 1;
			contentPanel.add(spinnerWidth, gbc_spinner);
		}
		{
			JLabel lblHeight = new JLabel("Height:");
			GridBagConstraints gbc_lblHeight = new GridBagConstraints();
			gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
			gbc_lblHeight.gridx = 5;
			gbc_lblHeight.gridy = 2;
			contentPanel.add(lblHeight, gbc_lblHeight);
		}
		{
			spinnerHeight = new JSpinner();
			spinnerHeight.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int value = (int)spinnerHeight.getValue();
					if(value < GameParameters.HEIGHT_MIN)
						spinnerHeight.setValue(GameParameters.HEIGHT_MIN);
					else if(value > GameParameters.HEIGHT_MAX)
						spinnerHeight.setValue(GameParameters.HEIGHT_MAX);
				}
			});
			spinnerHeight.setPreferredSize(new Dimension(70, 25));
			GridBagConstraints gbc_spinner = new GridBagConstraints();
			gbc_spinner.insets = new Insets(0, 0, 5, 5);
			gbc_spinner.gridx = 6;
			gbc_spinner.gridy = 2;
			contentPanel.add(spinnerHeight, gbc_spinner);
		}
		{
			chckbxFigureSwap = new JCheckBox("allow figure swap");
			GridBagConstraints gbc_chckbxFigureSwap = new GridBagConstraints();
			gbc_chckbxFigureSwap.anchor = GridBagConstraints.WEST;
			gbc_chckbxFigureSwap.gridwidth = 2;
			gbc_chckbxFigureSwap.insets = new Insets(0, 0, 5, 0);
			gbc_chckbxFigureSwap.gridx = 8;
			gbc_chckbxFigureSwap.gridy = 3;
			contentPanel.add(chckbxFigureSwap, gbc_chckbxFigureSwap);
		}
		{
			chckbxAllowShiftDown = new JCheckBox("allow shift down");
			GridBagConstraints gbc_chckbxAllowShiftDown = new GridBagConstraints();
			gbc_chckbxAllowShiftDown.anchor = GridBagConstraints.WEST;
			gbc_chckbxAllowShiftDown.gridwidth = 2;
			gbc_chckbxAllowShiftDown.insets = new Insets(0, 0, 5, 5);
			gbc_chckbxAllowShiftDown.gridx = 5;
			gbc_chckbxAllowShiftDown.gridy = 4;
			contentPanel.add(chckbxAllowShiftDown, gbc_chckbxAllowShiftDown);
		}
		{
			chckbxShowNextFigure = new JCheckBox("show next figure");
			GridBagConstraints gbc_chckbxShowNextFigure = new GridBagConstraints();
			gbc_chckbxShowNextFigure.anchor = GridBagConstraints.WEST;
			gbc_chckbxShowNextFigure.gridwidth = 2;
			gbc_chckbxShowNextFigure.insets = new Insets(0, 0, 5, 0);
			gbc_chckbxShowNextFigure.gridx = 8;
			gbc_chckbxShowNextFigure.gridy = 4;
			contentPanel.add(chckbxShowNextFigure, gbc_chckbxShowNextFigure);
		}
		{
			chckbxDeleteFullLines = new JCheckBox("delete full lines (box packing)");
			GridBagConstraints gbc_chckbxDeleteFullLines = new GridBagConstraints();
			gbc_chckbxDeleteFullLines.anchor = GridBagConstraints.WEST;
			gbc_chckbxDeleteFullLines.gridwidth = 2;
			gbc_chckbxDeleteFullLines.insets = new Insets(0, 0, 5, 5);
			gbc_chckbxDeleteFullLines.gridx = 5;
			gbc_chckbxDeleteFullLines.gridy = 5;
			contentPanel.add(chckbxDeleteFullLines, gbc_chckbxDeleteFullLines);
		}
		{
			chckbxIncreaseSpeed = new JCheckBox("increase speed");
			GridBagConstraints gbc_chckbxIncreaseSpeed = new GridBagConstraints();
			gbc_chckbxIncreaseSpeed.anchor = GridBagConstraints.WEST;
			gbc_chckbxIncreaseSpeed.gridwidth = 2;
			gbc_chckbxIncreaseSpeed.insets = new Insets(0, 0, 5, 0);
			gbc_chckbxIncreaseSpeed.gridx = 8;
			gbc_chckbxIncreaseSpeed.gridy = 5;
			contentPanel.add(chckbxIncreaseSpeed, gbc_chckbxIncreaseSpeed);
		}
		{
			chckbxMovingField = new JCheckBox("moving field");
			GridBagConstraints gbc_chckbxMovingField = new GridBagConstraints();
			gbc_chckbxMovingField.anchor = GridBagConstraints.WEST;
			gbc_chckbxMovingField.gridwidth = 2;
			gbc_chckbxMovingField.insets = new Insets(0, 0, 5, 5);
			gbc_chckbxMovingField.gridx = 5;
			gbc_chckbxMovingField.gridy = 6;
			contentPanel.add(chckbxMovingField, gbc_chckbxMovingField);
		}
		{
			JLabel lblFigureChooser = new JLabel("Figure chooser:");
			GridBagConstraints gbc_lblFigureChooser = new GridBagConstraints();
			gbc_lblFigureChooser.gridwidth = 2;
			gbc_lblFigureChooser.insets = new Insets(0, 0, 5, 0);
			gbc_lblFigureChooser.gridx = 8;
			gbc_lblFigureChooser.gridy = 7;
			contentPanel.add(lblFigureChooser, gbc_lblFigureChooser);
		}
		{
			rdbtnUniformByItem = new JRadioButton("uniform by item");
			figureChooserButtonGroup.add(rdbtnUniformByItem);
			GridBagConstraints gbc_rdbtnUniformByItem = new GridBagConstraints();
			gbc_rdbtnUniformByItem.anchor = GridBagConstraints.WEST;
			gbc_rdbtnUniformByItem.gridwidth = 2;
			gbc_rdbtnUniformByItem.insets = new Insets(0, 0, 5, 0);
			gbc_rdbtnUniformByItem.gridx = 8;
			gbc_rdbtnUniformByItem.gridy = 8;
			contentPanel.add(rdbtnUniformByItem, gbc_rdbtnUniformByItem);
		}
		{
			rdbtnUniformByType = new JRadioButton("uniform by type");
			figureChooserButtonGroup.add(rdbtnUniformByType);
			GridBagConstraints gbc_rdbtnUniformByType = new GridBagConstraints();
			gbc_rdbtnUniformByType.anchor = GridBagConstraints.WEST;
			gbc_rdbtnUniformByType.gridwidth = 2;
			gbc_rdbtnUniformByType.insets = new Insets(0, 0, 5, 0);
			gbc_rdbtnUniformByType.gridx = 8;
			gbc_rdbtnUniformByType.gridy = 9;
			contentPanel.add(rdbtnUniformByType, gbc_rdbtnUniformByType);
		}
//		{
//			spinnerBrickSize = new JSpinner();
//			spinnerBrickSize.addChangeListener(new ChangeListener() {
//				public void stateChanged(ChangeEvent e) {
//					int value = (int)spinnerBrickSize.getValue();
//					if(value < GameParameters.HEIGHT_MIN)
//						spinnerBrickSize.setValue(GameParameters.HEIGHT_MIN);
//					else if(value > GameParameters.HEIGHT_MAX)
//						spinnerBrickSize.setValue(GameParameters.HEIGHT_MAX);
//				}
//			});
//			spinnerBrickSize.setPreferredSize(new Dimension(70, 25));
//			GridBagConstraints gbc_spinner = new GridBagConstraints();
//			gbc_spinner.insets = new Insets(0, 0, 5, 0);
//			gbc_spinner.gridx = 4;
//			gbc_spinner.gridy = 3;
//			contentPanel.add(spinnerBrickSize, gbc_spinner);
//		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Accept");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						manager.beginNewGame(accept());
					}
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	protected GameParameters accept() {
		List<FigureType> types = new LinkedList<>();
		for(int i=1; i<=FigureType.WHOLE_MAX_BRICKS; ++i)
		{
			if (rdbtnWhole[i-1].isSelected())
				types.add(FigureType.valueOf("WHOLE_"+i));
			if (rdbtnPenWhole[i-1].isSelected())
				types.add(FigureType.valueOf("PENETRATING_WHOLE_"+i));
		}
		
		if (rdbtnSemiwhole2.isSelected())
			types.add(FigureType.SEMIWHOLE_2);
		if (rdbtnSemiwhole3.isSelected())
			types.add(FigureType.SEMIWHOLE_3);
		
		if (rdbtnPenSemiwhole2.isSelected())
			types.add(FigureType.PENETRATING_SEMIWHOLE_2);
		if (rdbtnPenSemiwhole3.isSelected())
			types.add(FigureType.PENETRATING_SEMIWHOLE_3);
//		if (rdbtnMoreBricks.isSelected())
//			types.add(FigureType.WHOLE_MORE);
//		if (rdbtnPenetrating.isSelected())
//			types.add(FigureType.PENETRATING);
		FigureType[] figureTypes = types.toArray(new FigureType[types.size()]);
		AbstractFiguresChooser figuresChooser = null;
		if(rdbtnUniformByItem.isSelected()) {
			figuresChooser = new UniformByItemFiguresChooser(figureTypes);
		}
		else if(rdbtnUniformByType.isSelected()) {
			figuresChooser = new UniformByTypeFiguresChooser(figureTypes);
		}
		// XXX should be optional
		AbstractScoringStrategy scoringStrategy;
		if (!chckbxDeleteFullLines.isSelected()) {
			scoringStrategy = new PackingScoring();
		} else {
			scoringStrategy = new ClassicScoring();
		}
		
		dispose();
		return new GameParameters((int) spinnerWidth.getValue(),
				(int) spinnerHeight.getValue(), figureTypes, scoringStrategy,
				chckbxAllowReflections.isSelected(),
				chckbxAllowPause.isSelected(),
				chckbxAllowShiftDown.isSelected(),
				chckbxFigureSwap.isSelected(),
				chckbxShowNextFigure.isSelected(),
				chckbxDeleteFullLines.isSelected(),
				chckbxMovingField.isSelected(),
				chckbxIncreaseSpeed.isSelected(), figuresChooser);
	}
}
