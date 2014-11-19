package auto_solver;

import game_engine.AbstractField;
import game_engine.DrawerPanel;
import game_engine.figures.AbstractFigure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 * Visualizer of game playing by solver.
 * 
 * @author misha
 * 
 */
public class Visualizer {
	private Timer timer;
	private DrawerPanel drawer;
	private int count;
	private List<AbstractField> fieldsArray;
	private List<AbstractFigure> figuresSequence;

	public Visualizer(int timerDelay) {
		JFrame frame = new JFrame("Solver playing");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		drawer = new DrawerPanel(frame);
		frame.getContentPane().add(drawer);

		timer = new Timer(timerDelay, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				visualize();
			}
		});

	}

	public void show(List<AbstractField> fieldsList, List<AbstractFigure> figuresList) {
		this.fieldsArray = fieldsList;
		this.figuresSequence = figuresList;
		timer.start();
		count = 0;
	}

	/**
	 * Visualizes one step of solver: current figure in a field before figure is
	 * embedded.
	 * 
	 */
	protected void visualize() {
		if (count == fieldsArray.size()) {
			timer.stop();
			return;
		}
		AbstractField field = fieldsArray.get(count);
		AbstractFigure figure = figuresSequence.get(count);
		figure.put(field);
		drawer.figureToDraw(figure);
		drawer.fieldToDraw(field);
		drawer.repaint();
		count++;
	}
}
