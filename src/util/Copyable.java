package util;

public interface Copyable {

	public default <T extends Copyable> T copy() {
		T r = newDraft();
		r.copyData(this);
		return r;
	}
	
	public <T extends Copyable> T newDraft();
	
	public void copyData(Copyable template);

}
