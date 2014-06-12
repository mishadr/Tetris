package game_engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import user_control.MainDialog;

public class GameResult {

	static final String path = "results.gr";

	public static void saveResults(Game game, GameParameters params)
			throws IOException {
		double result = game.getScore();
		File file = new File(MainDialog.gameDir, path);
		if (file.createNewFile()) {
			save(file, result);
			return;
		}
		// Map<GameParameters, Gameplay> results;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		double prev = 0;
		try {
			prev = Double.valueOf(reader.readLine());
		} catch (NullPointerException e) {/* empty file	*/	}
		if (result > prev) {
			save(file, result);
		} else {
			System.out.println("best result was: " + prev);
		}

		reader.close();
	}

	private static void save(File file, double result) throws IOException {
		System.out.println("new record: " + result);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file)));
		writer.write("" + result);
		writer.close();
	}

}
