package store.validator.file;

import java.util.List;

public interface FileValidatingParser<T> {
	void validateFirstLine(String firstLine);

	List<T> processLine(List<String> allLine);
}
