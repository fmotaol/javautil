package util;

import java.awt.Color;

public class Colors {


	public static Color randomColor() {
		return new Color(Maths.random(255), Maths.random(255), Maths.random(255));
	}
}
