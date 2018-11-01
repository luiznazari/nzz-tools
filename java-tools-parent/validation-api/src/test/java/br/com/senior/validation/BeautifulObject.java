package br.com.senior.validation;

import br.com.senior.validation.constraint.BeautifulIdNotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A simple POJO for tests purpose.
 *
 * @author Luiz.Nazari
 */
@Getter
@Setter
@NoArgsConstructor
public class BeautifulObject implements BeautifulId {

	public static final String ERROR_MSG_NAME_EMPTY = "test.beautiful.object.name.empty";
	public static final String ERROR_MSG_WHAT_IM_DOING_EMPTY = "test.beautiful.object.whatImDoing.empty";
	public static final String ERROR_MSG_WHAT_IM_DOING_INVALID_RANGE = "test.beautiful.object.whatImDoing.invalid.range";
	public static final String ERROR_MSG_WMBCF_EMPTY = "test.beautiful.object.whereMyBeautyCameFrom.empty";

	private Long id;

	@NotEmpty(message = ERROR_MSG_NAME_EMPTY)
	private String name;

	@NotEmpty(message = ERROR_MSG_WHAT_IM_DOING_EMPTY)
	@Length(min = 1, max = 100, message = ERROR_MSG_WHAT_IM_DOING_INVALID_RANGE)
	private String whatImDoing;

	@BeautifulIdNotNull(message = ERROR_MSG_WMBCF_EMPTY)
	private BeautifulObject whereMyBeautyCameFrom;

}
