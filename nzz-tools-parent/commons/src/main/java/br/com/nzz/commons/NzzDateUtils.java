package br.com.nzz.commons;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitária auxiliar para objetos que manipulam datas e horas.
 *
 * @author Luiz.Nazari
 */
public final class NzzDateUtils {

	private static final ZoneId BR_ZONE_ID = ZoneId.of("America/Sao_Paulo");

	public static final String BR_DATE = "dd/MM/yyyy";

	public static final String BR_DATE_TIME = BR_DATE + " HH:mm:ss";

	public static final String ISO_DATE = "yyyy-MM-dd";

	public static final String ISO_DATE_TIME = ISO_DATE + "'T'HH:mm:ss[.SSS]['Z']";

	public static final DateTimeFormatter BR_DATE_FORMATTER = DateTimeFormatter
		.ofPattern(BR_DATE);

	public static final DateTimeFormatter BR_DATE_TIME_FORMATTER = DateTimeFormatter
		.ofPattern(BR_DATE_TIME)
		.withZone(BR_ZONE_ID);

	private NzzDateUtils() {
	}

}
