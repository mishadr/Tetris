package game_engine.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import user_control.MainDialog;

/**
 * Simple utility for generating skeletons of figures.
 * 
 * @author misha
 *
 */
public class SkeletonsBuilder extends JPanel {
	private static final long serialVersionUID = 3627390607519465519L;
	private final int fieldSize = 10;
	private final int cellSize = 30;
	// private String path = "";
	private boolean field[][];
	
	public static final String pathToFiguresFile = MainDialog.gameDir + "/" + "data/allowed_figures.fg";

	public SkeletonsBuilder() {
		JFrame frame = new JFrame("Builder");
		frame.setSize(fieldSize * cellSize + 20, fieldSize * cellSize + 40);
		frame.setLocation(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(this);
		frame.setVisible(true);

		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent ev) {
			}

			public void mousePressed(MouseEvent ev) {
				if (ev.getModifiersEx() == 1024)// left, 5120 - both
				{
					int i = Math.min(ev.getX() / cellSize, fieldSize - 1);
					int j = Math.min(ev.getY() / cellSize, fieldSize - 1);
					field[i][j] = !field[i][j];
				}
				repaint();
			}

			public void mouseReleased(MouseEvent ev) {
			}

			public void mouseEntered(MouseEvent ev) {
			}

			public void mouseExited(MouseEvent ev) {
			}
		});

		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent ev) {
				int key = ev.getKeyCode();
				if (key == KeyEvent.VK_S) {
					for (int i = 0; i < fieldSize; ++i)
						for (int j = 0; j < fieldSize; ++j)
							if (field[i][j])
								System.out.print(i + ", " + j + ", ");
					System.out.println();
				}
				if (key == KeyEvent.VK_C) {
					for (int i = 0; i < fieldSize; ++i)
						for (int j = 0; j < fieldSize; ++j)
							field[i][j] = false;
				}
				repaint();
			}

			public void keyReleased(KeyEvent ev) {
			}

			public void keyTyped(KeyEvent ev) {
			}
		});
		setFocusable(true);

		field = new boolean[fieldSize][fieldSize];
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;

		g.setColor(Color.darkGray);
		for (int i = 0; i <= fieldSize; i++)
			g.drawLine(cellSize * i, 0, cellSize * i, cellSize * fieldSize);
		for (int j = 0; j <= fieldSize; j++)
			g.drawLine(0, cellSize * j, cellSize * fieldSize, cellSize * j);
		for (int i = 0; i < fieldSize; ++i)
			for (int j = 0; j < fieldSize; ++j) {
				g.setColor(field[i][j] ? Color.red : Color.darkGray.darker());
				g.fillRect(i * cellSize + 1, j * cellSize + 1, cellSize - 2,
						cellSize - 2);
			}
	}

	public static void main(String[] args) {

		// new SkeletonsBuilder();
		generateSkeletons();
	}

	/**
	 * Load all possible skeletons from file or generate and save them.
	 * 
	 * @return
	 */
	public static Map<FigureType, List<Skeleton>> generateSkeletons() {
		File file = new File(pathToFiguresFile);
		if (file.exists()) {
			System.out.println("File with figures exists.");
			// load from file and add penetrating figures
			try (ObjectInputStream is = new ObjectInputStream(
					new FileInputStream(file))) {
				@SuppressWarnings("unchecked")
				Map<FigureType, List<Skeleton>> type2list = (Map<FigureType, List<Skeleton>>) is
						.readObject();
				
				// TODO add TBF
				return type2list;
			} catch (IOException e) {
				System.out.println("Couldn't load figures! Regenerating.");
				// e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else 
		{
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		Map<FigureType, List<Skeleton>> type2list = generateFigures();
	
		// save generated figures
		try (ObjectOutputStream os = new ObjectOutputStream(
				new FileOutputStream(file))) {
			os.writeObject(type2list);
			System.out.println("figures saved to " + file.getAbsolutePath());
			
		} catch (IOException e) {
			e.printStackTrace();
			file.delete();
		}
		
		return type2list;
	}
	
	/**
	 * Get all possible figures.
	 * 
	 * @return
	 */
	public static Map<FigureType, List<AbstractFigure>> getAllFigures() {
		
		Map<FigureType, List<Skeleton>> type2list = generateSkeletons();
		
		// complete set with figures built on Skeletons
		Map<FigureType, List<AbstractFigure>> res = new HashMap<>();
		
		res.putAll(buildSeparateFigures(type2list));
		res.putAll(buildFiguresFromSkeletons(type2list));
		
		return res;
	}


	private static Map<FigureType, List<AbstractFigure>> buildFiguresFromSkeletons(
			Map<FigureType, List<Skeleton>> type2list) {
		Map<FigureType, List<AbstractFigure>> res = new HashMap<>();
		Iterator<Entry<FigureType, List<Skeleton>>> iter = type2list.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Entry<FigureType, List<Skeleton>> entry = iter.next();
			List<AbstractFigure> figures = new ArrayList<>(entry.getValue().size());
			List<AbstractFigure> penFigures = new ArrayList<>(entry.getValue().size());
			for (Skeleton s : entry.getValue()) {
				figures.add(new OneBodyFigure(s, false));
				penFigures.add(new OneBodyFigure(s, true));
			}
			FigureType type = entry.getKey();
			res.put(type, figures);
			// TODO separate figures with 1 penetrating part
			// add penetrating version
			FigureType penetratingType = FigureType.valueOf("PENETRATING_" + type.toString());
			res.put(penetratingType, penFigures);
		}
		return res;
	}

//	private static Map<FigureType, List<OneBodyFigure>> buildPenetratingFigures(
//			Map<FigureType, List<Skeleton>> type2list) {
//		Map<FigureType, List<OneBodyFigure>> res = new HashMap<>();
//		// whole
//		for (int i = 1; i <= FigureType.WHOLE_MAX_BRICKS; ++i) {
//			List<Skeleton> whole = type2list.get(FigureType
//					.valueOf("WHOLE_" + i));
//			List<Skeleton> penetrating = new ArrayList<>(whole.size());
//			for (Skeleton af : whole) {
//				penetrating.add(af.cloneAs(true));
//			}
//			res.put(new FigureFigureType.valueOf("PENETRATING_WHOLE_" + i),
//					penetrating);
//		}
//	
//		// semiwhole 2-3
//		List<Skeleton> semiwhole2 = type2list.get(FigureType.SEMIWHOLE_2);
//		List<Skeleton> penetratingSemi2 = new ArrayList<>(semiwhole2.size());
//		for(Skeleton af: semiwhole2) {
//			penetratingSemi2.add(af.cloneAs(true));
//		}
//		type2list.put(FigureType.PENETRATING_SEMIWHOLE_2, penetratingSemi2);
//	
//		List<Skeleton> semiwhole3 = type2list.get(FigureType.SEMIWHOLE_3);
//		List<Skeleton> penetratingSemi3 = new ArrayList<>(semiwhole3.size());
//		for(Skeleton af: semiwhole3) {
//			penetratingSemi3.add(af.cloneAs(true));
//		}
//		type2list.put(FigureType.PENETRATING_SEMIWHOLE_3, penetratingSemi3);
//	
//	}
	
	// not penetrating yet!
	private static Map<FigureType, List<AbstractFigure>> buildSeparateFigures(
			Map<FigureType, List<Skeleton>> type2list) {
		Map<FigureType, List<AbstractFigure>> res = new HashMap<>();
		int n = 5;
		for(int i=2; i<=n; ++i) {
			for(int j=2; j<=n; ++j) {
				res.put(FigureType.valueOf("SEPARATE_"+i+"_"+j), buildSeparateFigures(
						type2list.get(FigureType.valueOf("WHOLE_"+i)), 
						type2list.get(FigureType.valueOf("WHOLE_"+j)), Math.max(i, j)+1, 0));
			}
		}
		return res;
	}
	
	// TODO let all figures share more bodies and FiguresChooser just clone them

	private static List<AbstractFigure> buildSeparateFigures(
			List<Skeleton> list1, List<Skeleton> list2, int dx, int dy) {
		List<TwoBodiesFigure> candidates = new LinkedList<>();
		// bruit force method
		for (Skeleton item1 : list1) {
			for (Skeleton item2 : list2) {
				OneBodyFigure rot = new OneBodyFigure(item2.clone(), false);
				candidates.add(new TwoBodiesFigure(new OneBodyFigure(item1
						.clone(), false), rot.clone(), dx, dy));
				rot.nextMood();
				candidates.add(new TwoBodiesFigure(new OneBodyFigure(item1
						.clone(), false), rot.clone(), dx, dy));
				rot.nextMood();
				candidates.add(new TwoBodiesFigure(new OneBodyFigure(item1
						.clone(), false), rot.clone(), dx, dy));
				rot.nextMood();
				candidates.add(new TwoBodiesFigure(new OneBodyFigure(item1
						.clone(), false), rot.clone(), dx, dy));
			}
		}
		
		
		List<AbstractFigure> result = new LinkedList<>();
		lbl:for(AbstractFigure candidate: candidates)
		{
			// check if rotated current figure coincides with any older one
			for(int mood = 0; ; ++mood)
			{
				// XXX check comparing for Figures
				if (result.contains(candidate)) {
					continue lbl;
				}
				if (mood < 3) {// only 3 rotations are needed
					candidate.nextMood();
				} else {
					break;
				}
			}
			// none of moods was encountered
			result.add(candidate);
		}
		

		return result;
	}

	private static Map<FigureType, List<Skeleton>> generateFigures() {
		Map<FigureType, List<Skeleton>> type2list = new TreeMap<>();
	
		List<Skeleton> semiwhole2 = new LinkedList<>();
		semiwhole2.add(new Skeleton(new int[] { 0, 0, 2, 0}));
		semiwhole2.add(new Skeleton(new int[] { 0, 1, 2, 0}));
		semiwhole2.add(new Skeleton(new int[] { 0, 0, 2, 1}, 
				new int[] { 1, 0 }));
		semiwhole2.add(new Skeleton(new int[] { 0, 0, 1, 1}));
		type2list.put(FigureType.SEMIWHOLE_2, semiwhole2);
		
		List<Skeleton> semiwhole3 = new LinkedList<>();
		semiwhole3.add(new Skeleton(new int[] { 0, 0, 1, 1, 2, 0}));
		semiwhole3.add(new Skeleton(new int[] { 0, 0, 1, 1, 2, 1}));
		semiwhole3.add(new Skeleton(new int[] { 0, 0, 2, 0, 2, 1}));
		semiwhole3.add(new Skeleton(new int[] { 0, 0, 0, 1, 2, 0}));
		semiwhole3.add(new Skeleton(new int[] { 0, 0, 1, 2, 2, 0}));
		semiwhole3.add(new Skeleton(new int[] { 0, 0, 2, 0, 2, 2}));
		semiwhole3.add(new Skeleton(new int[] { 0, 1, 1, 0, 2, 0}));
		semiwhole3.add(new Skeleton(new int[] { 0, 0, 2, 1, 2, 2}));
		semiwhole3.add(new Skeleton(new int[] { 0, 0, 1, 0, 2, 2}));
		semiwhole3.add(new Skeleton(new int[] { 0, 0, 1, 2, 2, 1}));
		semiwhole3.add(new Skeleton(new int[] { 0, 0, 1, 1, 2, 2}));
		type2list.put(FigureType.SEMIWHOLE_3, semiwhole3);
		
		type2list.putAll(buildWholeFigures());
		return type2list;
	}

	/**
	 * Iteratively build whole figures of up to size 9.
	 * 
	 */
	private static Map<FigureType, List<Skeleton>> buildWholeFigures() {
		JFrame frame = new JFrame("Generating figures...");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setType(Window.Type.UTILITY);
		frame.setLocation(500, 300);
		frame.setSize(250, 50);
		frame.setVisible(true);
		
		JProgressBar progressBar = new JProgressBar();
		frame.getContentPane().add(progressBar);
		progressBar.setVisible(true);
		progressBar.setStringPainted(true);
		progressBar.setValue(1);
		
		Map<FigureType, List<Skeleton>> type2list = new TreeMap<>();
		
		Skeleton whole1 = new Skeleton(new int[]{0, 0});
		List<Skeleton> grown = new LinkedList<>();
		grown.add(whole1.clone());
		type2list.put(FigureType.WHOLE_1, grown);
		type2list.put(FigureType.WHOLE_2, grown = growWholeFigures(grown));//1
		type2list.put(FigureType.WHOLE_3, grown = growWholeFigures(grown));//2
		type2list.put(FigureType.WHOLE_4, grown = growWholeFigures(grown));//7
		type2list.put(FigureType.WHOLE_5, grown = growWholeFigures(grown));//18
		type2list.put(FigureType.WHOLE_6, grown = growWholeFigures(grown));//60
		progressBar.setValue(3);
		progressBar.setString("building 7 blocks figures");
		type2list.put(FigureType.WHOLE_7, grown = growWholeFigures(grown));//196
		progressBar.setValue(15);
		progressBar.setString("building 8-blocks figures");
		type2list.put(FigureType.WHOLE_8, grown = growWholeFigures(grown));//704
		progressBar.setValue(30);
		progressBar.setString("building 9-blocks figures");
		type2list.put(FigureType.WHOLE_9, grown = growWholeFigures(grown));//2500
//		progressBar.setValue(20);
//		progressBar.setString("building 10-blocks figures");
//		type2list.put(FigureType.WHOLE_10, grown = growWholeFigures(grown));//9189

		progressBar.setValue(100);
		frame.dispose();

		return type2list;
	}

	/**
	 * Return all possible {@link WholeFigure}s derived by adding 1 block to
	 * given figures
	 * 
	 * @param figuresList
	 * @return
	 */
	private static List<Skeleton> growWholeFigures(
			List<Skeleton> figuresList) {
		List<Skeleton> result = new LinkedList<>();
		for(Skeleton figure: figuresList)
		{
			List<Skeleton> candidates = getGrownWhole(figure);
			lbl:for(Skeleton candidate: candidates)
			{
				// check if rotated current figure coincides with any older one
				for(int mood = 0; ; ++mood)
				{
					if (result.contains(candidate)) {
						continue lbl;
					}
					if (mood < 3) {// only 3 rotations are needed
						candidate.nextMood();
					} else {
						break;
					}
				}
				// none of moods was encountered
				int[] body = candidate.getBody();
				int xc = 0, yc = 0;
				for(int i=0; i<body.length; )
				{
					xc += body[i++];
					yc += body[i++];
				}
				xc = (2*xc + 1) / body.length;
				yc = (2*yc + 1) / body.length;
				result.add(new Skeleton(body.clone(), new int[] { xc, yc }));
			}
		}
		System.out.printf("Grown set of %d whole figures\n", result.size());
		return result;
	}

	/**
	 * Get all possible {@link WholeFigure}s derived by adding 1 block to the
	 * given figure.
	 * 
	 * @param figure source figure
	 * @return
	 */
	private static List<Skeleton> getGrownWhole(Skeleton figure) {
		List<Skeleton> result = new LinkedList<>();
		
		int[] body = figure.getBody();
		int length = body.length;
		List<Integer> neighbors = new LinkedList<>();
		for(int c=0; c<length;)
		{
			int x = body[c++];
			int y = body[c++];
			if(!containsBlock(body, x-1, y))
				addNeighbor(neighbors, x-1, y);
			if(!containsBlock(body, x+1, y))
				addNeighbor(neighbors, x+1, y);
			if(!containsBlock(body, x, y-1))
				addNeighbor(neighbors, x, y-1);
			if(!containsBlock(body, x, y+1))
				addNeighbor(neighbors, x, y+1);
		}
		// create and add new figures
		for (Iterator<Integer> it = neighbors.iterator(); it.hasNext();) {
			int[] newBody = new int[length+2];
			newBody[0] = it.next();
			newBody[1] = it.next();
			System.arraycopy(body, 0, newBody, 2, length);
			// shift to x,y >=0 zone
			int dx = 0;
			int dy = 0;
			if(newBody[0] < 0)
				dx = -newBody[0];
			if(newBody[1] < 0)
				dy = -newBody[1];
			if(dx != 0 || dy != 0)
			{
				for(int i=0; i<newBody.length; )
				{
					newBody[i++] += dx;
					newBody[i++] += dy;
				}
			}
			result.add(new Skeleton(newBody));
		}
		
		return result;
	}

	/**
	 * Add pair (x, y) to list of pairs if it doesn't contain such one yet.
	 * 
	 * @param list
	 * @param x
	 * @param y
	 */
	private static void addNeighbor(List<Integer> list, int x, int y) {
		for (Iterator<Integer> it = list.iterator(); it.hasNext();) {
			int xi = it.next();
			int yi = it.next();
			if (xi == x && yi == y)
				return; // pair (x, y) exists
		}
		list.add(x);
		list.add(y);
	}

	/**
	 * Check if specified array of pairs contains pair (x, y).
	 * 
	 * @param array
	 * @param x
	 * @param y
	 * @return
	 */
	static boolean containsBlock(int[] array, int x, int y) {
		for (int i = 0; i < array.length;) {
			int xi = array[i++];
			int yi = array[i++];
			if (xi == x && yi == y)
				return true;
		}
		return false;
	}

}