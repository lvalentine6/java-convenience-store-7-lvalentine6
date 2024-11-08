package store.io;

public class OutputHandler {
	private OutputHandler() {
	}

	private static class Holder {
		private static final OutputHandler INSTANCE = new OutputHandler();
	}

	public static OutputHandler getInstance() {
		return Holder.INSTANCE;
	}

	public void showWelcomeMessage() {
	}
}
