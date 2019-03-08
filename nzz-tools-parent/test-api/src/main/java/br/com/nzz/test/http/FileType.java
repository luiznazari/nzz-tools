package br.com.nzz.test.http;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FileType {

	TEXT("text/plain"),
	PDF("application/pdf"),
	XLS("application/excel"),
	JSON("application/json"),
	BINARY_FILE("application/octet-stream"),
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

	@Getter
	private String mimeType;

	public static FileType fromFileName(String fileName) {
		int dotIndex;
		if (fileName != null && (dotIndex = fileName.lastIndexOf('.')) != -1) {
			String extension = fileName.substring(dotIndex + 1);
			for (FileType fileExtension : FileType.values()) {
				if (fileExtension.name().equalsIgnoreCase(extension)) {
					return fileExtension;
				}
			}
		}

		log.warn(String.format("Could not identify mime type for file \"%s\". Using generic binary file.", fileName));
		return BINARY_FILE;
	}

}
