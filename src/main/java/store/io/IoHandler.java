package store.io;

public class IoHandler {
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;
	private final ResourceHandler resourceHandler;

	private IoHandler(InputHandler inputHandler, OutputHandler outputHandler, ResourceHandler resourceHandler) {
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
		this.resourceHandler = resourceHandler;
	}

	private static class Holder {
		private static final IoHandler INSTANCE = new IoHandler(
			InputHandler.getInstance(),
			OutputHandler.getInstance(),
			ResourceHandler.getInstance());
	}

	public static IoHandler getInstance() {
		return Holder.INSTANCE;
	}
}
