package br.com.nzz.commons.model.hibernate.types;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;

import java.time.format.DateTimeFormatter;

import br.com.nzz.commons.model.LocalMonthYear;

/**
 * @author Luiz.Nazari
 */
@SuppressWarnings("unchecked")
public class LocalMonthYearTypeJavaDescriptor extends AbstractTypeDescriptor<LocalMonthYear> {

	private static final long serialVersionUID = -3589863102215513006L;

	public static final LocalMonthYearTypeJavaDescriptor INSTANCE = new LocalMonthYearTypeJavaDescriptor();

	public LocalMonthYearTypeJavaDescriptor() {
		super(LocalMonthYear.class, ImmutableMutabilityPlan.INSTANCE);
	}

	@Override
	public LocalMonthYear fromString(String string) {
		return LocalMonthYear.parse(string);
	}

	@Override
	public <X> X unwrap(LocalMonthYear value, Class<X> type, WrapperOptions options) {
		if (value == null) {
			return null;
		}

		if (String.class.isAssignableFrom(type)) {
			return (X) value.format(DateTimeFormatter.ISO_DATE);
		}

		throw unknownWrap(type);
	}

	@Override
	public <X> LocalMonthYear wrap(X value, WrapperOptions options) {
		if (value == null) {
			return null;
		}

		if (CharSequence.class.isAssignableFrom(value.getClass())) {
			return LocalMonthYear.parse((CharSequence) value);
		}

		throw unknownWrap(value.getClass());
	}

}
