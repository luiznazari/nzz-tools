package br.com.nzz.commons;

import br.com.nzz.commons.model.ValorDecimal;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ValorDecimalTest {

	@Test
	public void deveConstruirValor() {
		ValorDecimal vd1 = new ValorDecimal(1);
		assertEquals(1.0, vd1.getValor().doubleValue(), 0.0);

		ValorDecimal vd2 = new ValorDecimal(1.1);
		assertEquals(1.1, vd2.getValor().doubleValue(), 0.0);
	}

	@Test
	public void deveInstanciarValorDecimalComSucesso() {
		assertNotNull(ValorDecimal.valorPara((String) null));
		assertNotNull(ValorDecimal.valorPara(1));
		assertNotNull(ValorDecimal.valorPara(1.1));
		assertNotNull(ValorDecimal.valorPara(""));
		assertNotNull(ValorDecimal.valorPara("1"));
		assertNotNull(ValorDecimal.valorPara("1.1"));
		assertNotNull(ValorDecimal.valorPara(new BigDecimal("1.0001")));
		assertEquals(ValorDecimal.zero(), ValorDecimal.valorPara("CAMTWO"));
	}

	@Test
	public void deveValidarValorVazio() {
		ValorDecimal nulo = null;
		ValorDecimal zero = ValorDecimal.zero();
		ValorDecimal comValor = ValorDecimal.valorPara(1.00);
		ValorDecimal naoNuloSemValor = ValorDecimal.valorPara((BigDecimal) null);
		assertTrue(ValorDecimal.isVazio(nulo));
		assertTrue(ValorDecimal.isVazio(zero));
		assertTrue(ValorDecimal.isVazio(naoNuloSemValor));
		assertFalse(ValorDecimal.isVazio(comValor));
	}

}