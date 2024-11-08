package store.io;

public class ResourceHandler {
	private ResourceHandler() {
	}

	private static class Holder {
		private static final ResourceHandler INSTANCE = new ResourceHandler();
	}

	public static ResourceHandler getInstance() {
		return Holder.INSTANCE;
	}

}
