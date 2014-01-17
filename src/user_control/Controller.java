package user_control;

import game_engine.Game.Action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

public class Controller {

	private static boolean lock;

	public static void setListeners(Component component,
			final GameManager manager) {
		lock = true;
		component.addKeyListener(new KeyListener() {

			private Action actionX;
			private Action actionY;
			private Action actionZ;
			private Timer timerX = new Timer(80, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					manager.performAction(actionX);
				}
			});
			private Timer timerY = new Timer(50, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					manager.performAction(actionY);
				}
			});
			private Timer timerZ = new Timer(400, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					manager.performAction(actionZ);
				}
			});

			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (lock)
					return;
				defineAction(e.getKeyCode(), false);
				if (actionX == null && timerX.isRunning()) {
					timerX.stop();
				}
				if (actionY == null && timerY.isRunning()) {
					timerY.stop();
				}
				if (actionZ == null && timerZ.isRunning()) {
					timerZ.stop();
				}
			}

			public void keyPressed(KeyEvent e) {
				if (lock)
					return;
				defineAction(e.getKeyCode(), true);
				if (actionX != null && !timerX.isRunning()) {
					manager.performAction(actionX);
					timerX.start();
				}
				if (actionY != null && !timerY.isRunning()) {
					manager.performAction(actionY);
					timerY.start();
				}
				if (actionZ != null && !timerZ.isRunning()) {
					manager.performAction(actionZ);
					timerZ.start();
				}
			}

			private Action defineAction(int keyCode, boolean press) {
				switch (keyCode) {
				case KeyEvent.VK_LEFT:
					return actionX = press ? Action.LEFT
							: (actionX == Action.LEFT ? null : actionX);
				case KeyEvent.VK_RIGHT:
					return actionX = press ? Action.RIGHT
							: (actionX == Action.RIGHT ? null : actionX);
				case KeyEvent.VK_UP:
					return actionZ = press ? Action.ROTATE
							: (actionZ == Action.ROTATE ? null : actionZ);
				case KeyEvent.VK_DOWN:
					return actionY = press ? Action.DOWN
							: (actionY == Action.DOWN ? null : actionY);
				case KeyEvent.VK_T:
					if (press) {
						manager.performAction(Action.VERTICAL_REFLECT);
					}
				case KeyEvent.VK_SPACE:
					if (press) {
						manager.performAction(Action.FULL_DOWN);
					}
				default:
					return null;
				}
			}
		});
		component.setFocusable(true);
	}

	public static void lock() {
		lock = true;
	}

	public static void unlock() {
		lock = false;
	}
}
