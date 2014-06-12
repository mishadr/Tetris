package game_engine.figures;

/**
 * Abstraction of any figure. It can return its body (array of coordinates of
 * all blocks of figure) and center (center of next rotation = -displacement
 * after the turn). This persentation doesn't know about field and position on
 * it.
 * 
 * @author misha
 * 
 */
public abstract class AbstractFigure implements java.io.Serializable {

	private static final long serialVersionUID = -772900941073049175L;
	
	protected final boolean isPenetrating;

	protected AbstractFigure(boolean isPenetrating) {
		this.isPenetrating = isPenetrating;
	}

	boolean isPenetrating() {
		return isPenetrating;
	}

	public abstract AbstractFigure clone();

	abstract AbstractFigure cloneAs(boolean isPenetrating);

	/**
	 * Get current center (displacement after next rotation). Should be default
	 * visible.
	 * XXX what is it for polybody figure?
	 * 
	 * @return
	 */
	abstract int[] getCenter();

	/**
	 * Get current (depending on mood) body array.
	 * 
	 * @return
	 */
	abstract int[] getBody();

	/**
	 * Rotate. Body and center should be updated.
	 */
	abstract void nextMood();

	/**
	 * Inversed rotate. Body and center should be updated.
	 */
	abstract void prevMood();
	
	public abstract boolean equals(Object o);

	abstract void reflectOY();
}
