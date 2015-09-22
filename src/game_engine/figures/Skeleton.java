package game_engine.figures;


/**
 * Skeleton of any figure, a firm construction. It provides basic
 * transformations: rotation and reflection. It contains its body (array of
 * coordinates of all blocks of skeleton) and center (center of next rotation =
 * -displacement after the turn). This representation doesn't know about field,
 * position, moving properties. Skeleton rotates as a whole object.
 * 
 * @author misha
 *
 */
public class Skeleton implements java.io.Serializable {
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

	protected Skeleton(/*boolean isPenetrating, */int[] body) {
		this(/*isPenetrating, */body, new int[] { 0, 0 });
	}

	protected Skeleton(/*boolean isPenetrating, */int[] body, int[] center) {
//		super(isPenetrating);
		this.center = center;
		this.body = body;
	}

	int[] getCenter() {
		return center;
	}
	
	int[] getBody() {
		return body;
	}

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
		int[] body = ((Skeleton) obj).body;
		if (this.body.length != body.length)
			return false;
		// sets of pairs (x, y) in 2bodies must be compared
		for (int i = 0; i < body.length;) {
			if (!SkeletonsBuilder.containsBlock(this.body, body[i++], body[i++]))
				return false;
		}
		return true;
	}

	@Override
	public Skeleton clone() {
		Skeleton clone = new Skeleton(body.clone(),
				center.clone());
		return clone;
	}

}
