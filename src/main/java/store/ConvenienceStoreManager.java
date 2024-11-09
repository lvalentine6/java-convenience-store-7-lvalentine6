package store;

import java.util.List;

import store.domain.product.Product;
import store.domain.product.Stock;
import store.domain.promotion.Promotions;
import store.io.IoHandler;

public class ConvenienceStoreManager {
	private static final String PRODUCT_FILE_PATH_NAME = "/products.md";
	private static final String PROMOTION_FILE_PATH_NAME = "/promotions.md";

	private final IoHandler ioHandler;
	private final Promotions promotions;
	private final Stock stock;

	private ConvenienceStoreManager(IoHandler ioHandler, Promotions promotions, Stock stock) {
		this.ioHandler = ioHandler;
		this.promotions = promotions;
		this.stock = stock;
	}

	public static ConvenienceStoreManager from(IoHandler ioHandler) {
		Promotions promotions = Promotions.from(ioHandler.getPrimitivePromotionInfos(PROMOTION_FILE_PATH_NAME));
		List<Product> products = ioHandler.getPrimitiveProductInfos(PRODUCT_FILE_PATH_NAME).stream()
			.map(info -> Product.of(info, promotions.findByName(info.promotion())))
			.toList();

		Stock stock = Stock.from(products);
		return new ConvenienceStoreManager(ioHandler, promotions, stock);
	}

	public void run() {

	}
}
