package util.console;

import java.util.Locale;

import util.Strings;

public class ANSI {
	
	public static boolean ENABLE_COLORS = true;
	
	// Reset
	public static final String RESET = "\033[0m"; // Text Reset

	// Regular Colors
	public static final String BLACK = "\033[0;30m"; // BLACK
	public static final String RED = "\033[0;31m"; // RED
	public static final String GREEN = "\033[0;32m"; // GREEN
	public static final String YELLOW = "\033[0;33m"; // YELLOW
	public static final String BLUE = "\033[0;34m"; // BLUE
	public static final String PURPLE = "\033[0;35m"; // PURPLE
	public static final String CYAN = "\033[0;36m"; // CYAN
	public static final String WHITE = "\033[0;37m"; // WHITE

	public static final String[] REGULAR_COLORS = new String[] { BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN,
			WHITE };

	// High Intensity
	public static final String BLACK_BRIGHT = "\033[0;90m"; // BLACK
	public static final String RED_BRIGHT = "\033[0;91m"; // RED
	public static final String GREEN_BRIGHT = "\033[0;92m"; // GREEN
	public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
	public static final String BLUE_BRIGHT = "\033[0;94m"; // BLUE
	public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
	public static final String CYAN_BRIGHT = "\033[0;96m"; // CYAN
	public static final String WHITE_BRIGHT = "\033[0;97m"; // WHITE

	public static final String[] HIGH_INTENSITY_COLORS = new String[] { BLACK_BRIGHT, RED_BRIGHT, GREEN_BRIGHT,
			YELLOW_BRIGHT, BLUE_BRIGHT, PURPLE_BRIGHT, CYAN_BRIGHT, WHITE_BRIGHT };

	// Bold
	public static final String BLACK_BOLD = "\033[1;30m"; // BLACK
	public static final String RED_BOLD = "\033[1;31m"; // RED
	public static final String GREEN_BOLD = "\033[1;32m"; // GREEN
	public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
	public static final String BLUE_BOLD = "\033[1;34m"; // BLUE
	public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
	public static final String CYAN_BOLD = "\033[1;36m"; // CYAN
	public static final String WHITE_BOLD = "\033[1;37m"; // WHITE

	public static final String[] BOLD_COLORS = new String[] { BLACK_BOLD, RED_BOLD, GREEN_BOLD, YELLOW_BOLD,
			BLUE_BOLD, PURPLE_BOLD, CYAN_BOLD, WHITE_BOLD };

	// Underline
	public static final String BLACK_UNDERLINED = "\033[4;30m"; // BLACK
	public static final String RED_UNDERLINED = "\033[4;31m"; // RED
	public static final String GREEN_UNDERLINED = "\033[4;32m"; // GREEN
	public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
	public static final String BLUE_UNDERLINED = "\033[4;34m"; // BLUE
	public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
	public static final String CYAN_UNDERLINED = "\033[4;36m"; // CYAN
	public static final String WHITE_UNDERLINED = "\033[4;37m"; // WHITE

	public static final String[] UNDERLINED_COLORS = new String[] { BLACK_UNDERLINED, RED_UNDERLINED,
			GREEN_UNDERLINED, YELLOW_UNDERLINED, BLUE_UNDERLINED, PURPLE_UNDERLINED, CYAN_UNDERLINED,
			WHITE_UNDERLINED };

	// Background
	public static final String BLACK_BACKGROUND = "\033[40m"; // BLACK
	public static final String RED_BACKGROUND = "\033[41m"; // RED
	public static final String GREEN_BACKGROUND = "\033[42m"; // GREEN
	public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
	public static final String BLUE_BACKGROUND = "\033[44m"; // BLUE
	public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
	public static final String CYAN_BACKGROUND = "\033[46m"; // CYAN
	public static final String WHITE_BACKGROUND = "\033[47m"; // WHITE

	public static final String[] BACKGROUND_COLORS = new String[] { BLACK_BACKGROUND, RED_BACKGROUND,
			GREEN_BACKGROUND, YELLOW_BACKGROUND, BLUE_BACKGROUND, PURPLE_BACKGROUND, CYAN_BACKGROUND,
			WHITE_BACKGROUND };

	// Bold High Intensity
	public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
	public static final String RED_BOLD_BRIGHT = "\033[1;91m"; // RED
	public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
	public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
	public static final String BLUE_BOLD_BRIGHT = "\033[1;94m"; // BLUE
	public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
	public static final String CYAN_BOLD_BRIGHT = "\033[1;96m"; // CYAN
	public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

	public static final String[] BOLD_BRIGHT_COLORS = new String[] { BLACK_BOLD_BRIGHT, RED_BOLD_BRIGHT,
			GREEN_BOLD_BRIGHT, YELLOW_BOLD_BRIGHT, BLUE_BOLD_BRIGHT, PURPLE_BOLD_BRIGHT, CYAN_BOLD_BRIGHT,
			WHITE_BOLD_BRIGHT };

	// High Intensity backgrounds
	public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
	public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
	public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
	public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
	public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
	public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
	public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m"; // CYAN
	public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m"; // WHITE

	public static final String[] BACKGROUND_BRIGHT_COLORS = new String[] { BLACK_BACKGROUND_BRIGHT,
			RED_BACKGROUND_BRIGHT, GREEN_BACKGROUND_BRIGHT, YELLOW_BACKGROUND_BRIGHT, BLUE_BACKGROUND_BRIGHT,
			PURPLE_BACKGROUND_BRIGHT, CYAN_BACKGROUND_BRIGHT, WHITE_BACKGROUND_BRIGHT };

	public static final String DEFAULT_COLOR = WHITE;

	public static String CURRENT_COLOR = DEFAULT_COLOR;
	
	public static void setColor(String color) {
		if (shouldColor())
			System.out.println(color);
		CURRENT_COLOR = color;
	}

	public static void resetColor() {
		if (shouldColor())
			System.out.println(RESET);
		CURRENT_COLOR = DEFAULT_COLOR;
	}
	
	public static String coloredText(Object value, String color) {
		//if (PROMPT_COLORS)
		
		//return color + value.toString() + RESET;
		
		return color + value.toString() + CURRENT_COLOR;
		
		//else
			//return value.toString();
	}

	public static String removeAnsiChars(String msg) {
		msg = Strings.remove(msg, RESET);
		msg = Strings.remove(msg, REGULAR_COLORS);
		msg = Strings.remove(msg, HIGH_INTENSITY_COLORS);
		msg = Strings.remove(msg, BOLD_COLORS);
		msg = Strings.remove(msg, UNDERLINED_COLORS);
		msg = Strings.remove(msg, BACKGROUND_COLORS);
		msg = Strings.remove(msg, BOLD_BRIGHT_COLORS);
		msg = Strings.remove(msg, BACKGROUND_BRIGHT_COLORS);
		return msg;
	}
	
	public static void println(Object value, String color) {
		if (shouldColor())
			value = coloredText(value, color);
		System.out.println(value);
	}

	private static boolean shouldColor() {
		return ENABLE_COLORS;// && consoleSupports(); 
	}

//	public static void println(Object value) {
//		println(value, CURRENT_COLOR);
//	}

	public static void println(Object value) {
		System.out.println(value);
	}

	public static void println() {
		System.out.println();
	}
	
	static {
		ENABLE_COLORS = consoleSupports();
		Locale.setDefault(Locale.US);
	}

	@Deprecated
	public static boolean consoleSupportsANSI_Old() {
		return System.console() != null && System.getenv().get("TERM") != null;
	}

	public static boolean consoleSupports() {
        // 1. Se não houver console interativo (caso clássico do Console do Eclipse), retorna falso
        if (System.console() == null) {
            // Checagem extra: O Eclipse costuma rodar processos em background.
            // Mas se rodarmos via External Tools (CMD), System.console() NÃO será null.
            return false;
        }

        // 2. Verifica variáveis de ambiente comuns de terminais coloridos (Linux/macOS/GitBash)
        String term = System.getenv("TERM");
        if (term != null && (term.contains("color") || term.contains("xterm") || term.contains("ansi"))) {
            return true;
        }

        String colorTerm = System.getenv("COLORTERM");
        if (colorTerm != null && (colorTerm.contains("truecolor") || colorTerm.contains("24bit"))) {
            return true;
        }

        // 3. Detecção específica para o Windows (CMD moderno / Windows Terminal)
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // A partir do Windows 10 (Builds pós-2016), o suporte a ANSI existe.
            // Se passou pelo teste do System.console() ativo e é Windows moderno,
            // podemos assumir que é um CMD real ou Windows Terminal.
            return true;
        }
        
        return false;
    }

	
	
}
