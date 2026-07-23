package util.console;

public class ColorUtil extends ANSI {


	public static String colorFromValue(Double value, boolean bright) {
		if ((value == null) || (value == 0.0))
			return RESET;

		if (!bright) {
			if (value < 0.0)
				return RED;
			else
				return GREEN;
		} else {
			if (value < 0.0)
				return RED_BRIGHT;
			else
				return GREEN_BRIGHT;
		}
	}

	public static String colorFromValue(Double value, double brightThreshold) {
		if ((value == null) || (value == 0.0))
			return RESET;

		if (Math.abs(value) < brightThreshold) {
			if (value < 0.0)
				return RED;
			else
				return GREEN;
		} else {
			if (value < 0.0)
				return RED_BRIGHT;
			else
				return GREEN_BRIGHT;
		}
	}

	public static String colorFromValue(Double value, double colorThreshold, double brightThreshold) {
		return colorFromValue(value, colorThreshold, brightThreshold, WHITE);
	}

	public static String colorFromValue(Double value, double colorThreshold, double brightThreshold,
			String neutralColor) {
		if (value == null)
			return RESET;
		double a = Math.abs(value);
		if (a < colorThreshold)
			return neutralColor;

		if (a < brightThreshold) {
			if (value < 0.0)
				return RED;
			else
				return GREEN;
		} else {
			if (value < 0.0)
				return RED_BRIGHT;
			else
				return GREEN_BRIGHT;
		}
	}


}
