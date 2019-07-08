package br.com.nzz.commons.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Era;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;

import br.com.nzz.commons.model.persistence.LocalDateBrConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Persistence Wrapper Class for {@link LocalDate} with PT-BR date format (dd/MM/yyyy).
 *
 * @author Luiz.Nazari
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "value")
public final class LocalDateBr implements Temporal, TemporalAdjuster, ChronoLocalDate, Serializable {

	private static final long serialVersionUID = 7241590140588438117L;

	private final LocalDate value;

	public LocalDateBr(String dateBrFormat) {
		this(new LocalDateBrConverter().convertToEntityAttribute(dateBrFormat).getValue());
	}

	private LocalDateBr(LocalDate localDate) {
		this.value = localDate;
	}

	public static LocalDateBr now() {
		return LocalDateBr.from(LocalDate.now());
	}

	public static LocalDateBr from(LocalDate localDate) {
		return new LocalDateBr(localDate);
	}

	public static LocalDateBr parse(String text) {
		return new LocalDateBr(text);
	}

	// TODO BrLocalDateTime
	@Override
	public LocalDateTime atTime(LocalTime localTime) {
		return this.value.atTime(localTime);
	}

	// =-=-=-=-=: Delegate to value :=-=-=-=-=

	@Override
	public IsoChronology getChronology() {
		return this.value.getChronology();
	}

	@Override
	public Era getEra() {
		return this.value.getEra();
	}

	@Override
	public boolean isLeapYear() {
		return this.value.isLeapYear();
	}

	@Override
	public int lengthOfMonth() {
		return this.value.lengthOfMonth();
	}

	@Override
	public int lengthOfYear() {
		return this.value.lengthOfYear();
	}

	@Override
	public boolean isSupported(TemporalField field) {
		return this.value.isSupported(field);
	}

	@Override
	public ValueRange range(TemporalField field) {
		return this.value.range(field);
	}

	@Override
	public int get(TemporalField field) {
		return this.value.get(field);
	}

	@Override
	public boolean isSupported(TemporalUnit unit) {
		return this.value.isSupported(unit);
	}

	@Override
	public LocalDateBr with(TemporalAdjuster adjuster) {
		return LocalDateBr.from(this.value.with(adjuster));
	}

	@Override
	public LocalDateBr with(TemporalField field, long newValue) {
		return LocalDateBr.from(this.value.with(field, newValue));
	}

	@Override
	public LocalDateBr plus(TemporalAmount amount) {
		return LocalDateBr.from(this.value.plus(amount));
	}

	@Override
	public LocalDateBr plus(long amountToAdd, TemporalUnit unit) {
		return LocalDateBr.from(this.value.plus(amountToAdd, unit));
	}

	@Override
	public LocalDateBr minus(TemporalAmount amount) {
		return LocalDateBr.from(this.value.minus(amount));
	}

	@Override
	public LocalDateBr minus(long amountToSubtract, TemporalUnit unit) {
		return LocalDateBr.from(this.value.minus(amountToSubtract, unit));
	}

	@Override
	public <R> R query(TemporalQuery<R> query) {
		return this.value.query(query);
	}

	@Override
	public Temporal adjustInto(Temporal temporal) {
		return this.value.adjustInto(temporal);
	}

	@Override
	public Period until(ChronoLocalDate endDateExclusive) {
		return this.value.until(endDateExclusive);
	}

	@Override
	public String format(DateTimeFormatter formatter) {
		return this.value.format(formatter);
	}

	@Override
	public long toEpochDay() {
		return this.value.toEpochDay();
	}

	@Override
	public int compareTo(ChronoLocalDate other) {
		return this.value.compareTo(other);
	}

	@Override
	public boolean isAfter(ChronoLocalDate other) {
		return this.value.isAfter(other);
	}

	@Override
	public boolean isBefore(ChronoLocalDate other) {
		return this.value.isBefore(other);
	}

	@Override
	public boolean isEqual(ChronoLocalDate other) {
		return this.value.isEqual(other);
	}

	@Override
	public long until(Temporal endExclusive, TemporalUnit unit) {
		return this.value.until(endExclusive, unit);
	}

	@Override
	public long getLong(TemporalField field) {
		return this.value.getLong(field);
	}

}
