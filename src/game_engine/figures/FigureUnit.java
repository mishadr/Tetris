package game_engine.figures;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum FigureUnit {

	// figure with random cells

	PARALLEL_2(FigureType.SEMIWHOLE_2, new int[] { 0, 0, 2, 0}, new int[] { 1, 0 }),
	s_2(FigureType.SEMIWHOLE_2, new int[] { 0, 1, 2, 0}, new int[] { 1, 0 }),
	z_2(FigureType.SEMIWHOLE_2, new int[] { 0, 0, 2, 1}, new int[] { 1, 0 }),
	DIAGONAL_2(FigureType.SEMIWHOLE_2, new int[] { 0, 0, 1, 1}),
	
	v_3(FigureType.SEMIWHOLE_3, new int[] { 0, 0, 1, 1, 2, 0}),
	r_3(FigureType.SEMIWHOLE_3, new int[] { 0, 0, 1, 1, 2, 1}),
	sep12_3(FigureType.SEMIWHOLE_3, new int[] { 0, 0, 2, 0, 2, 1}),
	sep21_3(FigureType.SEMIWHOLE_3, new int[] { 0, 0, 0, 1, 2, 0}),
	V_3(FigureType.SEMIWHOLE_3, new int[] { 0, 0, 1, 2, 2, 0}),
	sepCORNER_3(FigureType.SEMIWHOLE_3, new int[] { 0, 0, 2, 0, 2, 2}),
	l_3(FigureType.SEMIWHOLE_3, new int[] { 0, 1, 1, 0, 2, 0}),
	sep21LEFT_3(FigureType.SEMIWHOLE_3, new int[] { 0, 0, 2, 1, 2, 2}),
	sep21RIGHT_3(FigureType.SEMIWHOLE_3, new int[] { 0, 0, 1, 0, 2, 2}),
	sep111DIAG_3(FigureType.SEMIWHOLE_3, new int[] { 0, 0, 1, 2, 2, 1}),
	DIAGONAL_3(FigureType.SEMIWHOLE_3, new int[] { 0, 0, 1, 1, 2, 2}),
	
	BOX_1(FigureType.WHOLE_1, new int[] { 0, 0}),
	
	STICK_2(FigureType.WHOLE_2, new int[] { 0, 0, 0, 1}),
	
	CORNER_3(FigureType.WHOLE_3, new int[] { 0, 0, 0, 1, 1, 0 }), 
	STICK_3(FigureType.WHOLE_3, new int[] { 0, 0, 0, 1, 0, 2 }, new int[] { 0, 1 }),
	
	CUBE_4(FigureType.WHOLE_4, new int[] { 0, 0, 0, 1, 1, 0, 1, 1 }),
	STICK_4(FigureType.WHOLE_4, new int[] { 0, 0, 0, 1, 0, 2, 0, 3 }, new int[] { 0, 1 }),
	Z_4(FigureType.WHOLE_4, new int[] { 0, 0, 1, 0, 1, 1, 2, 1 }, new int[] { 0, 0 }),
	S_4(FigureType.WHOLE_4, new int[] { 0, 1, 1, 0, 1, 1, 2, 0 }, new int[] { 0, 0 }),
	T_4(FigureType.WHOLE_4, new int[] { 0, 0, 1, 0, 1, 1, 2, 0 }, new int[] { 0, 0 }),
	L_4(FigureType.WHOLE_4, new int[] { 0, 0, 0, 1, 1, 0, 2, 0 }, new int[] { 0, 0 }),
	r_4(FigureType.WHOLE_4, new int[] { 0, 0, 1, 0, 2, 0, 2, 1 }, new int[] { 0, 0 }),
	
	STICK_5(FigureType.WHOLE_5, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 4, 0 }, new int[] { 2, 0 }),
	r_5(FigureType.WHOLE_5, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 3, 1}),
	L_5(FigureType.WHOLE_5, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 0, 1}),
	oT_5(FigureType.WHOLE_5, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 2, 1}, new int[]{1, 0}),
	To_5(FigureType.WHOLE_5, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 1, 1}, new int[]{1, 0}),
	T_5(FigureType.WHOLE_5, new int[] { 0, 0, 0, 1, 1, 1, 2, 1, 0, 2}),
	U_5(FigureType.WHOLE_5, new int[] { 0, 0, 1, 0, 2, 0, 0, 1, 2, 1}),
	z_5(FigureType.WHOLE_5, new int[] { 0, 0, 0, 1, 1, 1, 2, 1, 2, 2}),
	s_5(FigureType.WHOLE_5, new int[] { 2, 0, 0, 1, 1, 1, 2, 1, 0, 2}),
	so_5(FigureType.WHOLE_5, new int[] { 1, 0, 2, 0, 3, 0, 0, 1, 1, 1}, new int[]{1, 0}),
	oz_5(FigureType.WHOLE_5, new int[] { 0, 0, 1, 0, 1, 1, 2, 1, 3, 1}, new int[]{1, 0}),
	t_5(FigureType.WHOLE_5, new int[] { 1, 0, 0, 1, 1, 1, 2, 1, 0, 2}),
	f_5(FigureType.WHOLE_5, new int[] { 1, 0, 0, 1, 1, 1, 2, 1, 2, 2}),
	b_5(FigureType.WHOLE_5, new int[] { 0, 0, 1, 0, 2, 0, 0, 1, 1, 1}),
	d_5(FigureType.WHOLE_5, new int[] { 0, 0, 1, 0, 2, 0, 1, 1, 2, 1}),
	CORNER_5(FigureType.WHOLE_5, new int[] { 0, 0, 1, 0, 2, 0, 0, 1, 0, 2}),
	CROSS_5(FigureType.WHOLE_5, new int[] { 1, 0, 0, 1, 1, 1, 2, 1, 1, 2}),
	W_5(FigureType.WHOLE_5, new int[] { 1, 0, 2, 0, 0, 1, 1, 1, 0, 2}),
	
	STICK_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0}, new int[]{2, 0}),
	r_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 4, 0, 4, 1}, new int[]{2, 0}),
	L_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 4, 0, 0, 1}, new int[]{2, 0}),
	ooT_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 4, 0, 3, 1}, new int[]{2, 0}),
	Too_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 4, 0, 1, 1}, new int[]{2, 0}),
	HIGHt_6(FigureType.WHOLE_6, new int[] { 1, 0, 0, 1, 1, 1, 2, 1, 3, 1, 0, 2}),
	HIGHf_6(FigureType.WHOLE_6, new int[] { 0, 0, 0, 1, 1, 1, 2, 1, 3, 1, 1, 2}),
	revF_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 0, 1, 2, 1}, new int[]{1, 0}),
	F_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 1, 1, 3, 1}, new int[]{1, 0}),
	bigZ_6(FigureType.WHOLE_6, new int[] { 0, 0, 0, 1, 1, 1, 2, 1, 3, 1, 3, 2}),
	bigS_6(FigureType.WHOLE_6, new int[] { 3, 0, 0, 1, 1, 1, 2, 1, 3, 1, 0, 2}),
	LOWt_6(FigureType.WHOLE_6, new int[] { 0, 0, 0, 1, 1, 1, 2, 1, 3, 1, 2, 2}, new int[]{1, 0}),
	LOWf_6(FigureType.WHOLE_6, new int[] { 3, 0, 0, 1, 1, 1, 2, 1, 3, 1, 1, 2}, new int[]{1, 0}),
	X_6(FigureType.WHOLE_6, new int[] { 1, 0, 0, 1, 1, 1, 2, 1, 3, 1, 2, 2}),
	revX_6(FigureType.WHOLE_6, new int[] { 2, 0, 0, 1, 1, 1, 2, 1, 3, 1, 1, 2}),
	WIDEh_6(FigureType.WHOLE_6, new int[] { 0, 0, 0, 1, 1, 1, 2, 1, 0, 2, 2, 2}),
	revWIDEh_6(FigureType.WHOLE_6, new int[] { 0, 0, 2, 0, 0, 1, 1, 1, 2, 1, 0, 2}),
	soo_6(FigureType.WHOLE_6, new int[] { 1, 0, 2, 0, 3, 0, 4, 0, 0, 1, 1, 1}, new int[]{2, 0}),
	ooz_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 1, 1, 2, 1, 3, 1, 4, 1}, new int[]{2, 0}),
	WIDEs_6(FigureType.WHOLE_6, new int[] { 2, 0, 3, 0, 4, 0, 0, 1, 1, 1, 2, 1}, new int[]{2, 0}),
	WIDEz_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 2, 1, 3, 1, 4, 1}, new int[]{2, 0}),
	ss_6(FigureType.WHOLE_6, new int[] { 1, 0, 2, 0, 3, 0, 0, 1, 1, 1, 2, 1}, new int[]{1, 0}),
	zz_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 1, 1, 2, 1, 3, 1}, new int[]{1, 0}),
	Uo_6(FigureType.WHOLE_6, new int[] { 1, 0, 2, 0, 3, 0, 0, 1, 1, 1, 3, 1}, new int[]{1, 0}),
	oU_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 3, 0, 1, 1, 2, 1, 3, 1}, new int[]{1, 0}),
	leftW_6(FigureType.WHOLE_6, new int[] { 3, 0, 1, 1, 2, 1, 3, 1, 0, 2, 1, 2}, new int[]{1, 0}),
	rightW_6(FigureType.WHOLE_6, new int[] { 0, 0, 0, 1, 1, 1, 2, 1, 2, 2, 3, 2}, new int[]{1, 0}),
	t_6(FigureType.WHOLE_6, new int[] { 1, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 2}),
	f_6(FigureType.WHOLE_6, new int[] { 1, 0, 2, 0, 3, 0, 0, 1, 1, 1, 1, 2}),
	WIDEt_6(FigureType.WHOLE_6, new int[] { 2, 0, 0, 1, 1, 1, 2, 1, 2, 2, 3, 2}),
	WIDEf_6(FigureType.WHOLE_6, new int[] { 2, 0, 3, 0, 0, 1, 1, 1, 2, 1, 2, 2}),
	b_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 0, 1, 1, 1}, new int[]{1, 0}),
	d_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 2, 1, 3, 1}, new int[]{1, 0}),
	leftHAND_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 0, 1, 1, 1, 1, 2}),
	rightHAND_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 1, 1, 2, 1, 1, 2}),
	bigL_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 0, 1, 0, 2}),
	bigJ_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 0, 1, 0, 2, 0, 3}),
	revSIGMA_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 0, 1, 2, 1, 0, 2}),
	SIGMA_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 0, 1, 0, 2, 1, 2}),
	To_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 1, 1, 1, 2}),
	oT_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 2, 1, 2, 2}),
	oZ_6(FigureType.WHOLE_6, new int[] { 1, 0, 2, 0, 3, 0, 1, 1, 0, 2, 1, 2}),
	Zo_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 2, 1, 2, 2, 3, 2}),
	W1_6(FigureType.WHOLE_6, new int[] { 1, 0, 2, 0, 3, 0, 0, 1, 1, 1, 0, 2}),
	W2_6(FigureType.WHOLE_6, new int[] { 1, 0, 2, 0, 0, 1, 1, 1, 0, 2, 0, 3}),
	BOXo_6(FigureType.WHOLE_6, new int[] { 1, 0, 2, 0, 0, 1, 1, 1, 0, 2, 1, 2}),
	oBOX_6(FigureType.WHOLE_6, new int[] { 1, 0, 2, 0, 0, 1, 1, 1, 2, 1, 0, 2}),
	oY_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 1, 1, 2, 1, 3, 1, 2, 2}),
	Yo_6(FigureType.WHOLE_6, new int[] { 2, 0, 3, 0, 0, 1, 1, 1, 2, 1, 1, 2}),
	Zzig_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 1, 1, 2, 1, 2, 2, 3, 2}),
	Szig_6(FigureType.WHOLE_6, new int[] { 2, 0, 3, 0, 1, 1, 2, 1, 0, 2, 1, 2}),
	BOXx_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 0, 1, 1, 1, 2, 1, 1, 2}),
	longCROSS_6(FigureType.WHOLE_6, new int[] { 1, 0, 0, 1, 1, 1, 2, 1, 3, 1, 1, 2}),
	bigT_6(FigureType.WHOLE_6, new int[] { 0, 0, 0, 1, 1, 1, 2, 1, 3, 1, 0, 2}),
	wideT_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 1, 1, 2, 1}),
	oTo_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 4, 0, 2, 1}, new int[]{2, 0}),
	bigBOX_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 0, 1, 1, 1, 2, 1}),
	wCORNER_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 0, 1, 1, 1, 0, 2}),
	wideU_6(FigureType.WHOLE_6, new int[] { 0, 0, 1, 0, 2, 0, 3, 0, 0, 1, 3, 1}, new int[]{1, 0}),
	TANK_6(FigureType.WHOLE_6, new int[] { 1, 0, 0, 1, 1, 1, 2, 1, 0, 2, 2, 2}),
	
	
	;
	private final FigureType type;

	/**
	 * 4 pairs (x, y) of displacements of center in 4 moods.
	 */
	private final int center[][];

	/**
	 * 4 arrays of pairs (x, y) of body coordinates (each >= 0) in 4 moods.
	 */
	private final int body[][];

	private FigureUnit(FigureType type, int[] body) {
		this(type, body, new int[] { 0, 0 });
	}

	private FigureUnit(FigureType type, int size, int... is) {
		this(type, Arrays.copyOfRange(is, 0, 2 * size),
				is.length > 2 * size ? Arrays.copyOfRange(is, 2 * size,
						is.length - 2 * size) : new int[] { 0, 0 });
	}

	private FigureUnit(FigureType type, int[] body, int[] center) {
		this.type = type;
		this.center = new int[4][2];
		this.body = new int[4][body.length];
		if (center.length == 8) {
			buildMoods(
					body,
					new int[][] { Arrays.copyOfRange(center, 0, 2),
							Arrays.copyOfRange(center, 2, 4),
							Arrays.copyOfRange(center, 4, 6),
							Arrays.copyOfRange(center, 6, 8) });
		} else {
			int[] centerReversed = new int[]{center[1], center[0]};
			buildMoods(body, new int[][] { center, centerReversed, center, centerReversed });
		}
	}

	private void buildMoods(int[] body, int[][] centers) {
		for (int i = 0; i < 4; ++i) {
			this.center[i] = centers[i];
			this.body[i] = body;
			body = FiguresBuilder.rotate(body);
		}
	}

	public int[] getCenter(int mood) {
		return center[mood];
	}

	public int[] getBody(int mood) {
		return body[mood];
	}

	public static FigureUnit[] values(FigureType[] types) {
		List<FigureUnit> list = new LinkedList<>();
		for (FigureUnit unit : values()) {
			if (Arrays.asList(types).contains(unit.type)) {
				list.add(unit);
			}
		}
		return list.toArray(new FigureUnit[list.size()]);
	}

}
