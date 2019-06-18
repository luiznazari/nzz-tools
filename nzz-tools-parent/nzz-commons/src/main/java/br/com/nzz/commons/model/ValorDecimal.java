package br.com.nzz.commons.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Classe que encapsula a manipulação de valores decimais do tipo {@link BigDecimal}.
 *
 * @author Luiz.Nazari
 */
@Getter
@ToString(of = "valor")
@EqualsAndHashCode(callSuper = false, of = "valor")
public final class ValorDecimal implements Serializable, Comparable<ValorDecimal> {

	private static final long serialVersionUID = -6846209439240316684L;

	private static final Integer PRECISAO = 2;
	private static final String FORMATO_MONETARIO_TEMPLATE = "%s %s";
	private static final DecimalFormat FORMATADOR = new DecimalFormat("#,##.00");

	private BigDecimal valor;

	public ValorDecimal(String valor) {
		this(NumberUtils.isCreatable(valor) ? new BigDecimal(valor) : BigDecimal.ZERO);
	}

	public ValorDecimal(Integer valor) {
		this(BigDecimal.valueOf(valor));
	}

	public ValorDecimal(Double valor) {
		this(BigDecimal.valueOf(valor));
	}

	public ValorDecimal(BigDecimal valor) {
		this.valor = (valor == null ? BigDecimal.ZERO : valor).setScale(PRECISAO, BigDecimal.ROUND_HALF_UP);
	}

	public static ValorDecimal zero() {
		return new ValorDecimal(BigDecimal.ZERO);
	}

	public static ValorDecimal valorPara(BigDecimal valor) {
		return new ValorDecimal(valor);
	}

	public static ValorDecimal valorPara(String valor) {
		return new ValorDecimal(valor);
	}

	public static ValorDecimal valorPara(Number valor) {
		return new ValorDecimal(BigDecimal.valueOf(valor.doubleValue()));
	}

	public String getValorFormatado() {
		return FORMATADOR.format(this.valor);
	}

	@Override
	public int compareTo(ValorDecimal valorDecimal) {
		return this.valor.compareTo(valorDecimal.valor);
	}

	/**
	 * Avalia o {@link ValorDecimal} existe e possui algum valor.
	 *
	 * @param valorDecimal o valor a ser validado.
	 * @return <code>true</code> caso seja diferente de <code>null</code> e o
	 * valor não seja zerado, <code>false</code> caso seja
	 * <code>null</code> ou o valor seja zerado.
	 */
	public static boolean isVazio(ValorDecimal valorDecimal) {
		return valorDecimal == null || valorDecimal.getValor().compareTo(BigDecimal.ZERO) == 0;
	}

}