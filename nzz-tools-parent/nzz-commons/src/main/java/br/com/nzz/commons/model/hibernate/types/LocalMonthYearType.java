package br.com.nzz.commons.model.hibernate.types;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import br.com.nzz.commons.model.LocalMonthYear;

/**
 * @author Luiz.Nazari
 */
public class LocalMonthYearType extends AbstractSingleColumnStandardBasicType<LocalMonthYear> {

	private static final long serialVersionUID = 5240869865908647211L;

	public static final String TYPE = "br.com.nzz.commons.model.hibernate.types.LocalMonthYearType";
	public static final LocalMonthYearType INSTANCE = new LocalMonthYearType();

	public LocalMonthYearType() {
		super(VarcharTypeDescriptor.INSTANCE, LocalMonthYearTypeJavaDescriptor.INSTANCE);
	}

	@Override
	public String getName() {
		return LocalMonthYearType.class.getSimpleName();
	}

}
