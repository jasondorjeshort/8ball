package com.dorjesoft.eightball;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;

/**
 * Eight Ball class. Very simply, call getEightBall() to get an 8ball string. It
 * has a 1/3 chance of being yes, no, or neutral, with a randomly chosen text
 * selected. Thread-safe.
 * 
 * @author Dorje
 * 
 */
public class EightBall {

	private static final Random RAND = new Random();

	public static synchronized String getEightBall(Context c) {
		Resources res = c.getResources();
		String[][] choices = { res.getStringArray(R.array.eightball_yes),
				res.getStringArray(R.array.eightball_maybe),
				res.getStringArray(R.array.eightball_no) };

		int i = Math.abs(RAND.nextInt()) % choices.length;
		int j = Math.abs(RAND.nextInt()) % choices[i].length;

		return choices[i][j];
	}
}
