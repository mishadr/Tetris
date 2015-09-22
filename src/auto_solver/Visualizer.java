package auto_solver;

import game_engine.AbstractField;
import game_engine.DrawerPanel;
import game_engine.figures.AbstractFigure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

		drawer.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_SPACE:
					if(timer.isRunning())
						timer.stop();
					else
						timer.start();
					break;
					
				case KeyEvent.VK_ENTER:
					timer.stop();
					visualize();
					break;
					
				case KeyEvent.VK_LEFT:
					timer.stop();
					if(count > 1)
						count -= 2;
					visualize();
					break;
					
				case KeyEvent.VK_RIGHT:
					timer.stop();
					visualize();
					break;
				}
			}
		});
		drawer.setFocusable(true);
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
		if (count >= fieldsArray.size()) {
			timer.stop();
			return;
		}
		AbstractField field = fieldsArray.get(count);
		AbstractFigure figure = figuresSequence.get(count);
		figure.put(field);
		drawer.setFigureToDraw(figure);
		drawer.setFieldToDraw(field);
		drawer.repaint();
		count++;
	}
}
