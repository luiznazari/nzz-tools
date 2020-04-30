package br.com.nzz.test;

import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.annotation.Nonnull;

/**
 * Base class for unit tests with Spring.<br> Provides shorthand methods for easing the writing
 * testes, assertions, instantiate common objects and improves test readability.
 *
 * @author Luiz.Nazari
 */
@RunWith(SpringRunner.class)
public abstract class UnitTestSpring extends UnitTest {

	/**
	 * A shorthand for creating a {@link org.springframework.data.domain.Page}.
	 *
	 * @param objects the page's content objects
	 * @param <T>     type of the objects
	 * @return a {@link org.springframework.data.domain.Page} containing the given objects.
	 */
	protected <T> org.springframework.data.domain.Page<T> pageOf(T... objects) {
		return new org.springframework.data.domain.PageImpl<>(Arrays.asList(objects));
	}

	public static String getResourcePath(@Nonnull String path) throws IOException {
		return new ClassPathResource(path).getURL().getPath();
	}

	public static InputStream getResourceStream(@Nonnull String path) throws IOException {
		return new ClassPathResource(path).getInputStream();
	}

	public static String getResourceContent(@Nonnull String path) {
		Assert.notNull(path, "Path must not be null");
		if (path.startsWith("classpath:")) {
			path = path.replace("classpath:", "");
		}

		try {
			File file = new ClassPathResource(path).getFile();
			try (FileInputStream in = new FileInputStream(file);
			     ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				byte[] bytes = new byte[(int) file.length()];
				int read;
				while ((read = in.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				return out.toString();
			}
		} catch (IOException e) {
			fail(e);
		}
		return "";
	}

}
