package br.com.senior.volkswagen.http;

import io.vertx.core.buffer.Buffer;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;


/**
 * Builder for building form data parameters for HTTP requests.
 *
 * @author Luiz.Nazari
 */
public class FormDataBuilder {

	private static final String TOKEN_SEPARATOR = "--";
	private static final String TOKEN_NEWLINE = "\r\n";

	private final Buffer paramsBuffer;
	@Getter
	private final String boundary;

	public FormDataBuilder() {
		this.paramsBuffer = Buffer.buffer();
		this.boundary = "----dLV9Wyq26L24JQxk6f534T153LhOO";
	}

	public FormDataBuilder param(String name, String value) {
		this.parameterHeader(name)
			.appendString(TOKEN_NEWLINE).appendString(TOKEN_NEWLINE)
			.appendString(value)
			.appendString(TOKEN_NEWLINE);
		return this;
	}

	public FormDataBuilder param(String name, File file) {
		try {
			byte[] bytes = FileUtils.readFileToByteArray(file);
			return this.param(name, bytes, file.getName());
		} catch (IOException e) {
			throw new AssertionError(e.getMessage(), e);
		}
	}

	public FormDataBuilder param(String name, byte[] fileBytes, String fileName) {
		Buffer fileBuffer = Buffer.buffer(fileBytes);
		FileType fileType = FileType.fromFileName(fileName);

		this.parameterHeader(name)
			.appendString("; ")
			.appendString("fileName=\"").appendString(fileName).appendString("\"").appendString(TOKEN_NEWLINE)
			.appendString("Content-Type: ").appendString(fileType.getMimeType()).appendString(TOKEN_NEWLINE)
			.appendString("Content-Transfer-Encoding: binary")
			.appendString(TOKEN_NEWLINE).appendString(TOKEN_NEWLINE)
			.appendBuffer(fileBuffer)
			.appendString(TOKEN_NEWLINE);

		return this;
	}

	private Buffer parameterHeader(String name) {
		return this.paramsBuffer
			.appendString(TOKEN_SEPARATOR).appendString(this.boundary).appendString(TOKEN_NEWLINE)
			.appendString("Content-Disposition: form-data; ")
			.appendString("name=\"").appendString(name).appendString("\"");
	}

	public Buffer build() {
		return this.paramsBuffer
			.copy()
			// Footer
			.appendString(TOKEN_SEPARATOR).appendString(this.boundary).appendString(TOKEN_SEPARATOR).appendString(TOKEN_NEWLINE);
	}

}
