package game_engine.figures;

/**
 * Figure consisting of 2 {@link OneBodyFigure}s.
 * 
 * @author misha
 *
 */
public class TwoBodiesFigure extends AbstractFigure {
	private static final long serialVersionUID = -2623516228557568638L;

	/**
	 * XXX items should be {@link OneBodyFigure}
	 */
	protected AbstractFigure item1;
	protected AbstractFigure item2;
	protected int dx, dy;

	protected TwoBodiesFigure(boolean isPenetrating, AbstractFigure item1,
			AbstractFigure item2, int dx, int dy) {
		super(isPenetrating);
		this.item1 = item1;
		this.item2 = item2;
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	int[] getCenter() {
		// XXX
		return item1.getCenter();
	}

	/**
	 * Union of both items' bodies.
	 */
	@Override
	int[] getBody() {
		int[] b1 = item1.getBody();
		int[] b2 = item2.getBody();
		int[] res = new int[b1.length + b2.length];
		System.arraycopy(b1, 0, res, 0, b1.length);
		for (int i = 0; i < b2.length; ++i) {
			res[i + b1.length] = b2[i++] + dx;
			res[i + b1.length] = b2[i] + dy;
		}
//		System.arraycopy(b2, 0, res, b1.length, b2.length);
		return res;
	}

	@Override
	void nextMood() {
		item1.nextMood();
		item2.nextMood();
	}

	@Override
	void prevMood() {
		item1.prevMood();
		item2.prevMood();
	}

	@Override
	void reflectOY() {
		// XXX ?
		item1.reflectOY();
		item2.reflectOY();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwoBodiesFigure other = (TwoBodiesFigure) obj;
		if (item1 == null) {
			if (other.item1 != null)
				return false;
		} else if (!item1.equals(other.item1))
			return false;
		if (item2 == null) {
			if (other.item2 != null)
				return false;
		} else if (!item2.equals(other.item2))
			return false;
		return true;
	}

	@Override
	public TwoBodiesFigure clone() {
		return cloneAs(isPenetrating);
	}

	@Override
	TwoBodiesFigure cloneAs(boolean isPenetrating) {
		TwoBodiesFigure clone = new TwoBodiesFigure(isPenetrating,
				item1.clone(), item2.clone(), dx, dy);
		return clone;
	}

}
