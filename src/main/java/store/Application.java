package store;

import store.io.IoHandler;

public class Application {
	public static void main(String[] args) {
		IoHandler ioHandler = IoHandler.getInstance();
		ConvenienceStoreManager convenienceStoreManager = new ConvenienceStoreManager(ioHandler);
	}
}
