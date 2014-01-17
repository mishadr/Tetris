package user_control;

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

import game_engine.figures.FigureType;

public class ParametersDialog extends JDialog {

	private static final long serialVersionUID = 7227194244996854170L;
	private final JPanel contentPanel = new JPanel();
	private JRadioButton rdbtnSemiwhole2;
	private JRadioButton rdbtnSemiwhole3;
	private JRadioButton rdbtnWhole1;
	private JRadioButton rdbtnWhole2;
	private JRadioButton rdbtnWhole3;
	private JRadioButton rdbtnWhole4;
	private JRadioButton rdbtnWhole5;
	private JRadioButton rdbtnWhole6;
//	private JRadioButton rdbtnMoreBricks;
//	private JRadioButton rdbtnPenetrating;
	private JSpinner spinnerWidth;
	private JSpinner spinnerHeight;
//	private JSpinner spinnerBrickSize;
	private GameManager manager;

	public ParametersDialog(Window owner, GameManager manager)
	{
		super(owner);
		this.manager = manager;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		openDialog();
		setUpParams(manager.getParams());
		setVisible(true);
	}
	
	private void setUpParams(GameParameters params) {
		spinnerWidth.setValue(params.getWidth());
		spinnerHeight.setValue(params.getHeight());
//		spinnerBrickSize.setValue(manager.DrawerPanel.getBrickSize());
		
		List<FigureType> types = Arrays.asList(params.getIncludedTypes());
		rdbtnWhole1.setSelected(types.contains(FigureType.WHOLE_1));
		rdbtnWhole2.setSelected(types.contains(FigureType.WHOLE_2));
		rdbtnWhole3.setSelected(types.contains(FigureType.WHOLE_3));
		rdbtnWhole4.setSelected(types.contains(FigureType.WHOLE_4));
		rdbtnWhole5.setSelected(types.contains(FigureType.WHOLE_5));
		rdbtnWhole6.setSelected(types.contains(FigureType.WHOLE_6));
//		rdbtnMoreBricks.setSelected(types.contains(FigureType.WHOLE_MORE));
		rdbtnSemiwhole2.setSelected(types.contains(FigureType.SEMIWHOLE_2));
		rdbtnSemiwhole3.setSelected(types.contains(FigureType.SEMIWHOLE_3));
//		rdbtnPenetrating.setSelected(types.contains(FigureType.PENETRATING));
	}

	/**
	 * Create the dialog.
	 */
	public void openDialog() {
		setTitle("Settings");
		setBounds(100, 100, 480, 400);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblIncludeTypes = new JLabel("Include types:");
			lblIncludeTypes.setFont(new Font("Tahoma", Font.PLAIN, 14));
			GridBagConstraints gbc_lblIncludeTypes = new GridBagConstraints();
			gbc_lblIncludeTypes.gridwidth = 2;
			gbc_lblIncludeTypes.insets = new Insets(0, 0, 5, 5);
			gbc_lblIncludeTypes.gridx = 0;
			gbc_lblIncludeTypes.gridy = 0;
			contentPanel.add(lblIncludeTypes, gbc_lblIncludeTypes);
		}
		{
			JSeparator separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
			GridBagConstraints gbc_separator = new GridBagConstraints();
			gbc_separator.fill = GridBagConstraints.VERTICAL;
			gbc_separator.gridheight = 10;
			gbc_separator.insets = new Insets(0, 0, 5, 5);
			gbc_separator.gridx = 2;
			gbc_separator.gridy = 0;
			contentPanel.add(separator, gbc_separator);
		}
		{
			JLabel lblFieldProperties = new JLabel("Field properties:");
			lblFieldProperties.setFont(new Font("Tahoma", Font.PLAIN, 14));
			GridBagConstraints gbc_lblFieldProperties = new GridBagConstraints();
			gbc_lblFieldProperties.gridwidth = 2;
			gbc_lblFieldProperties.insets = new Insets(0, 0, 5, 0);
			gbc_lblFieldProperties.gridx = 3;
			gbc_lblFieldProperties.gridy = 0;
			contentPanel.add(lblFieldProperties, gbc_lblFieldProperties);
		}
		{
			JLabel lblWhole = new JLabel("Whole :");
			GridBagConstraints gbc_lblWhole = new GridBagConstraints();
			gbc_lblWhole.insets = new Insets(0, 0, 5, 5);
			gbc_lblWhole.gridx = 0;
			gbc_lblWhole.gridy = 1;
			contentPanel.add(lblWhole, gbc_lblWhole);
		}
		{
			JLabel lblSemiwhole = new JLabel("Semiwhole:");
			GridBagConstraints gbc_lblSemiwhole = new GridBagConstraints();
			gbc_lblSemiwhole.insets = new Insets(0, 0, 5, 5);
			gbc_lblSemiwhole.gridx = 1;
			gbc_lblSemiwhole.gridy = 1;
			contentPanel.add(lblSemiwhole, gbc_lblSemiwhole);
		}
		{
			JLabel lblWidth = new JLabel("Width:");
			GridBagConstraints gbc_lblWidth = new GridBagConstraints();
			gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
			gbc_lblWidth.gridx = 3;
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
			gbc_spinner.insets = new Insets(0, 0, 5, 0);
			gbc_spinner.gridx = 4;
			gbc_spinner.gridy = 1;
			contentPanel.add(spinnerWidth, gbc_spinner);
		}
		{
			rdbtnWhole1 = new JRadioButton("1 brick");
			GridBagConstraints gbc_rdbtnWhole = new GridBagConstraints();
			gbc_rdbtnWhole.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnWhole.gridx = 0;
			gbc_rdbtnWhole.gridy = 2;
			contentPanel.add(rdbtnWhole1, gbc_rdbtnWhole);
		}
		{
			rdbtnSemiwhole2 = new JRadioButton("2 bricks");
			GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
			gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnNewRadioButton.gridx = 1;
			gbc_rdbtnNewRadioButton.gridy = 2;
			contentPanel.add(rdbtnSemiwhole2, gbc_rdbtnNewRadioButton);
		}
		{
			JLabel lblHeight = new JLabel("Height:");
			GridBagConstraints gbc_lblHeight = new GridBagConstraints();
			gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
			gbc_lblHeight.gridx = 3;
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
			gbc_spinner.insets = new Insets(0, 0, 5, 0);
			gbc_spinner.gridx = 4;
			gbc_spinner.gridy = 2;
			contentPanel.add(spinnerHeight, gbc_spinner);
		}
		{
			rdbtnWhole2 = new JRadioButton("2 bricks");
			GridBagConstraints gbc_rdbtnWhole_1 = new GridBagConstraints();
			gbc_rdbtnWhole_1.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnWhole_1.gridx = 0;
			gbc_rdbtnWhole_1.gridy = 3;
			contentPanel.add(rdbtnWhole2, gbc_rdbtnWhole_1);
		}
		{
			rdbtnSemiwhole3 = new JRadioButton("3 bricks");
			GridBagConstraints gbc_rdbtnBricks = new GridBagConstraints();
			gbc_rdbtnBricks.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnBricks.gridx = 1;
			gbc_rdbtnBricks.gridy = 3;
			contentPanel.add(rdbtnSemiwhole3, gbc_rdbtnBricks);
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
			rdbtnWhole3 = new JRadioButton("3 bricks");
			GridBagConstraints gbc_rdbtnWhole_2 = new GridBagConstraints();
			gbc_rdbtnWhole_2.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnWhole_2.gridx = 0;
			gbc_rdbtnWhole_2.gridy = 4;
			contentPanel.add(rdbtnWhole3, gbc_rdbtnWhole_2);
		}
		{
			JLabel lblSeparate = new JLabel("Separate:");
			GridBagConstraints gbc_lblSeparate = new GridBagConstraints();
			gbc_lblSeparate.insets = new Insets(0, 0, 5, 5);
			gbc_lblSeparate.gridx = 1;
			gbc_lblSeparate.gridy = 4;
			contentPanel.add(lblSeparate, gbc_lblSeparate);
		}
		{
			rdbtnWhole4 = new JRadioButton("4 bricks");
			GridBagConstraints gbc_rdbtnWhole_3 = new GridBagConstraints();
			gbc_rdbtnWhole_3.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnWhole_3.gridx = 0;
			gbc_rdbtnWhole_3.gridy = 5;
			contentPanel.add(rdbtnWhole4, gbc_rdbtnWhole_3);
		}
		{
			rdbtnWhole5 = new JRadioButton("5 bricks");
			GridBagConstraints gbc_rdbtnWhole_4 = new GridBagConstraints();
			gbc_rdbtnWhole_4.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnWhole_4.gridx = 0;
			gbc_rdbtnWhole_4.gridy = 6;
			contentPanel.add(rdbtnWhole5, gbc_rdbtnWhole_4);
		}
		{
			rdbtnWhole6 = new JRadioButton("6 bricks");
			GridBagConstraints gbc_rdbtnWhole_5 = new GridBagConstraints();
			gbc_rdbtnWhole_5.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnWhole_5.gridx = 0;
			gbc_rdbtnWhole_5.gridy = 7;
			contentPanel.add(rdbtnWhole6, gbc_rdbtnWhole_5);
		}
//		{
//			rdbtnMoreBricks = new JRadioButton("more bricks");
//			GridBagConstraints gbc_rdbtnMoreBricks = new GridBagConstraints();
//			gbc_rdbtnMoreBricks.insets = new Insets(0, 0, 5, 5);
//			gbc_rdbtnMoreBricks.gridx = 0;
//			gbc_rdbtnMoreBricks.gridy = 8;
//			contentPanel.add(rdbtnMoreBricks, gbc_rdbtnMoreBricks);
//		}
//		{
//			rdbtnPenetrating = new JRadioButton("penetrating");
//			GridBagConstraints gbc_rdbtnPenetrating = new GridBagConstraints();
//			gbc_rdbtnPenetrating.insets = new Insets(0, 0, 0, 5);
//			gbc_rdbtnPenetrating.gridx = 0;
//			gbc_rdbtnPenetrating.gridy = 9;
//			contentPanel.add(rdbtnPenetrating, gbc_rdbtnPenetrating);
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
						accept();
						manager.beginNewGame();
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

	protected void accept() {
		List<FigureType> types = new LinkedList<>();
		if (rdbtnWhole1.isSelected())
			types.add(FigureType.WHOLE_1);
		if (rdbtnWhole2.isSelected())
			types.add(FigureType.WHOLE_2);
		if (rdbtnWhole3.isSelected())
			types.add(FigureType.WHOLE_3);
		if (rdbtnWhole4.isSelected())
			types.add(FigureType.WHOLE_4);
		if (rdbtnWhole5.isSelected())
			types.add(FigureType.WHOLE_5);
		if (rdbtnWhole6.isSelected())
			types.add(FigureType.WHOLE_6);
		if (rdbtnSemiwhole2.isSelected())
			types.add(FigureType.SEMIWHOLE_2);
		if (rdbtnSemiwhole3.isSelected())
			types.add(FigureType.SEMIWHOLE_3);
//		if (rdbtnMoreBricks.isSelected())
//			types.add(FigureType.WHOLE_MORE);
//		if (rdbtnPenetrating.isSelected())
//			types.add(FigureType.PENETRATING);

		manager.setParams(new GameParameters(
				(int) spinnerWidth.getValue(), (int) spinnerHeight.getValue(),
				types.toArray(new FigureType[types.size()])));
		dispose();
	}
}
