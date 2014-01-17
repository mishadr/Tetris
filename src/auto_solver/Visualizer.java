package auto_solver;

import game_engine.Field;
import game_engine.figures.Figure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import user_control.DrawerPanel;

public class Visualizer {
	private Timer timer;
	private DrawerPanel drawer;
	private int count;
	private List<Field> fieldsList;
	private List<Figure> figuresList;

	public Visualizer()
	{
		JFrame frame = new JFrame("Solver training");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		drawer = new DrawerPanel(frame);
		frame.getContentPane().add(drawer);
		
		timer = new Timer(800, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualize();
			}
		});
		
	}

	public void show(List<Field> fieldsList, List<Figure> figuresList) {
		this.fieldsList = fieldsList;
		this.figuresList = figuresList;
		timer.start();
		count = 0;
	}
	
	
	protected void visualize() {
		if(count == fieldsList.size())
		{
			timer.stop();
			return;
		}
		Field field = fieldsList.get(count);
		Figure figure = figuresList.get(count);
		drawer.fieldAndFigureToDraw(field, figure);
		drawer.repaint();
		count++;
	}
}
