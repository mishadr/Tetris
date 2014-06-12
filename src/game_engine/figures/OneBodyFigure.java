package game_engine.figures;


/**
 * Figure with the only body (which rotates as a whole object).
 * 
 * @author misha
 *
 */
public class OneBodyFigure extends AbstractFigure {
	private static final long serialVersionUID = 6454266355326437162L;

	/**
	 * coordinates (x, y) of displacements of center in default mood.
	 */
	protected final int center[];

	/**
	 * array of pairs (x, y) of body coordinates in default mood. They are
	 * always >= 0.
	 */
	protected final int body[];

	protected OneBodyFigure(boolean isPenetrating, int[] body) {
		this(isPenetrating, body, new int[] { 0, 0 });
	}

//	protected AbstractFigure(int size, int... body) {
//		this(Arrays.copyOfRange(body, 0, 2 * size),
//				body.length > 2 * size ? Arrays.copyOfRange(body, 2 * size,
//						body.length - 2 * size) : new int[] { 0, 0 });
//	}

	protected OneBodyFigure(boolean isPenetrating, int[] body, int[] center) {
		super(isPenetrating);
		this.center = center;
		this.body = body;
//		this.center = new int[4][2];
//		this.body = new int[4][body.length];
//		if (center.length == 8) {
//			buildMoods(
//					body,
//					new int[][] { Arrays.copyOfRange(center, 0, 2),
//							Arrays.copyOfRange(center, 2, 4),
//							Arrays.copyOfRange(center, 4, 6),
//							Arrays.copyOfRange(center, 6, 8) });
//		} else {
//			int[] centerReversed = new int[]{center[1], center[0]};
//			buildMoods(body, new int[][] { center, centerReversed, center, centerReversed });
//		}
	}

//	private void buildMoods(int[] body, int[][] centers) {
//		for (int i = 0; i < 4; ++i) {
//			this.center[i] = centers[i];
//			this.body[i] = body;
//			body = FiguresBuilder.rotate(body);
//		}
//	}

	int[] getCenter() {
		return center;
	}

//	public int[] getPosition() {
//		return new int[]{position[0] - unit.getCenter()[0], 
//				position[1] - unit.getCenter()[1]};
//	}
	
	@Override
	int[] getBody() {
		return body;
	}

	@Override
	void nextMood() {
		int length = body.length;
		int[] temp = new int[length];
		int ymax = 0;
		for (int i = 1; i < length; i += 2) {
			if (body[i] > ymax) {
				ymax = body[i];
			}
		}
		for (int i = 0; i < length;) {
			int x = body[i];
			temp[i] = ymax - body[++i];
			temp[i++] = x;
		}
		System.arraycopy(temp, 0, body, 0, length);
		int t = center[1];
		center[1] = center[0];
		center[0] = t;
	}
	
	@Override
	void prevMood() {
		int length = body.length;
		int[] temp = new int[length];
		int xmax = 0;
		for (int i = 0; i < length; i += 2) {
			if (body[i] > xmax) {
				xmax = body[i];
			}
		}
		for (int i = 0; i < length;) {
			int x = body[i];
			temp[i] = body[++i];
			temp[i++] = xmax - x;
		}
		System.arraycopy(temp, 0, body, 0, length);
		int t = center[1];
		center[1] = center[0];
		center[0] = t;
	}
	
	@Override
	void reflectOY() {
		int length = body.length;
		int xmax = 0;
		for (int i = 0; i < length; i += 2) {
			if (body[i] > xmax) {
				xmax = body[i];
			}
		}
		for (int i = 0; i < length; i+=2) {
			body[i] = xmax - body[i];
		}
//		center[0] = xmax+1 - center[0];
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		int[] body = ((OneBodyFigure) obj).body;
		if (this.body.length != body.length)
			return false;
		// sets of pairs (x, y) in 2bodies must be compared
		for (int i = 0; i < body.length;) {
			if (!FiguresBuilder.containsBlock(this.body, body[i++], body[i++]))
				return false;
		}
		return true;
	}

	@Override
	public OneBodyFigure clone() {
		return cloneAs(isPenetrating);
	}

	@Override
	OneBodyFigure cloneAs(boolean isPenetrating) {
		OneBodyFigure clone = new OneBodyFigure(isPenetrating, body.clone(),
				center.clone());
		return clone;
	}

//	public static int[] rotate(int[] body) {
//		int[] res = new int[body.length];
//		int ymax = 0;
//		for (int i = 1; i < body.length; i += 2) {
//			if (body[i] > ymax) {
//				ymax = body[i];
//			}
//		}
//		for (int i = 0; i < body.length;) {
//			int x = body[i];
//			res[i] = ymax - body[++i];
//			res[i++] = x;
//		}
//		return res;
//	}
	
	

}
