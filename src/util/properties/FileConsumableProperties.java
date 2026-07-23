package util.properties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import util.logical.Assert;

public class FileConsumableProperties extends AbstractConsumableProperties {

	private int currentRow;

	public FileConsumableProperties(String file) {
		super();
		clear();
		loadContent(file);
		loadUnconsumedKeys(keys);
	}

	private void clear() {
		keys.clear();
		values.clear();
	}

	@Override
	protected String readProperty(String key) {
		assertNotMultipleKey(key);
		int i = keys.indexOf(key);
		Assert.that(i >= 0, "Chave não encontrada: " + key);
		String r = values.get(i);
		return r;
	}

	@Override
	protected String readProperty(String key, String defaultValue) {
		assertNotMultipleKey(key);
		int i = keys.indexOf(key);
		if (i == -1)
			return defaultValue;
		String r = values.get(i);
		return r;
	}

	private void assertNotMultipleKey(String key) {
		String[] vs = readMultipleKeyProperties(key);
		Assert.that(vs.length <= 1, "Duplicidade de propriedade " + key);
	}

	@Override
	protected String[] readMultipleKeyProperties(String key) {
		Assert.notNull(key);
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < keys.size(); i++) {
			String k = keys.get(i);
			if (key.equals(k)) {
				String v = values.get(i);
				al.add(v);
			}
		}
		String[] r = new String[al.size()];
		r = al.toArray(r);
		return r;
	}

	@Override
	protected boolean containsKey(String key) {
		return keys.contains(key);
	}

	public void loadContent(String file) {
		try {
			safeLoadContent(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void safeLoadContent(String file) throws IOException {
		FileReader fr = new FileReader(file);
		BufferedReader r = new BufferedReader(fr);
		currentRow = 1;
		try {
			while (r.ready()) {
				String row = r.readLine();
				parseRowContent(row);
				currentRow++;
			}
			if (commentBlock)
				throw new RuntimeException("Bloco de comentário não fechado ao fim do arquivo");
		} finally {
			r.close();
		}
	}

	ArrayList<String> keys = new ArrayList<>();

	ArrayList<String> values = new ArrayList<>();

	private boolean commentBlock = false;

	private void parseRowContent(String row) {
		if (row == null)
			return;
		row = row.trim();
		if (row.equals(""))
			return;
		if (row.startsWith("#"))
			return;
		if (row.startsWith("//"))
			return;

		if (row.contains("/*")) {
			String[] ss = row.split("/\\*", 2);
			parseRowContent(ss[0]);
			setCommentBlock(true);
			parseRowContent(ss[1]);
			return;
		}

		if (row.contains("*/")) {
			String[] ss = row.split("\\*/", 2);
			parseRowContent(ss[0]);
			setCommentBlock(false);
			parseRowContent(ss[1]);
			return;
		}
		
		if (commentBlock)
			return;

		String[] ss = row.split("=", 2);
		String key = ss[0].trim();
		String v = null;
		if (ss.length == 2) {
			v = ss[1];
			if (v != null)
				v = v.trim();
		}
		addEntry(key, v);
	}

	private void setCommentBlock(boolean commentBlock) {
		if (this.commentBlock && commentBlock) {
			throw new RuntimeException("Bloco de comentário já aberto - linha " + currentRow);
		}
		this.commentBlock = commentBlock;
	}

	private void addEntry(String key, String value) {
		keys.add(key);
		values.add(value);
	}

	@Override
	public String getContext() {
		return "";
	}

}
