package br.com.nzz.commons.model;

import br.com.nzz.commons.model.persistence.BrLocalDateConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.chrono.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;

/**
 * Persistence Wrapper Class for {@link LocalDate} with PT-BR date format (dd/MM/yyyy).
 *
 * @author Luiz.Nazari
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = "value")
public final class BrLocalDate implements Temporal, TemporalAdjuster, ChronoLocalDate, Serializable {

	private static final long serialVersionUID = 7241590140588438117L;

	private final LocalDate value;

	public BrLocalDate(String dateBrFormat) {
		this(new BrLocalDateConverter().convertToEntityAttribute(dateBrFormat).getValue());
	}

	private BrLocalDate(LocalDate localDate) {
		this.value = localDate;
	}

	public static BrLocalDate now() {
		return BrLocalDate.from(LocalDate.now());
	}

	public static BrLocalDate from(LocalDate localDate) {
		return new BrLocalDate(localDate);
	}

	public static BrLocalDate parse(String text) {
		return new BrLocalDate(text);
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
	public BrLocalDate with(TemporalAdjuster adjuster) {
		return BrLocalDate.from(this.value.with(adjuster));
	}

	@Override
	public BrLocalDate with(TemporalField field, long newValue) {
		return BrLocalDate.from(this.value.with(field, newValue));
	}

	@Override
	public BrLocalDate plus(TemporalAmount amount) {
		return BrLocalDate.from(this.value.plus(amount));
	}

	@Override
	public BrLocalDate plus(long amountToAdd, TemporalUnit unit) {
		return BrLocalDate.from(this.value.plus(amountToAdd, unit));
	}

	@Override
	public BrLocalDate minus(TemporalAmount amount) {
		return BrLocalDate.from(this.value.minus(amount));
	}

	@Override
	public BrLocalDate minus(long amountToSubtract, TemporalUnit unit) {
		return BrLocalDate.from(this.value.minus(amountToSubtract, unit));
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
