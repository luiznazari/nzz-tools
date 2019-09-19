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

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Persistence Wrapper Class for {@link LocalDate} only with month and year, always with the
 * first day of the month.
 *
 * @author Luiz.Nazari
 */
@EqualsAndHashCode(callSuper = false, of = "value")
public final class LocalMonthYear implements Temporal, TemporalAdjuster, ChronoLocalDate, Serializable {

	private static final DateTimeFormatter FORMATTER_MONTH_YEAR_BR = DateTimeFormatter.ofPattern("MM/yyyy");

	private static final long serialVersionUID = 3059121371803480233L;

	@Getter
	private final LocalDate value;

	public LocalMonthYear(CharSequence text) {
		this(LocalDate.parse(text));
	}

	private LocalMonthYear(LocalDate localDate) {
		this.value = localDate.withDayOfMonth(1);
	}

	public static LocalMonthYear now() {
		return LocalMonthYear.from(LocalDate.now());
	}

	public static LocalMonthYear from(LocalDate localDate) {
		return new LocalMonthYear(localDate);
	}


	public static LocalMonthYear of(int year, int month) {
		return from(LocalDate.of(year, month, 1));
	}

	public static LocalMonthYear parse(CharSequence text) {
		return new LocalMonthYear(text);
	}

	public static LocalMonthYear parse(CharSequence text, DateTimeFormatter dateTimeFormatter) {
		return from(LocalDate.parse(text, dateTimeFormatter));
	}

	/**
	 * Format this month and year with pattern "MM/yyyy".
	 *
	 * @return the formatted period.
	 */
	public String formatBr() {
		return FORMATTER_MONTH_YEAR_BR.format(this.value);
	}

	@Override
	public String toString() {
		return this.formatBr();
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
	public LocalMonthYear with(TemporalAdjuster adjuster) {
		return LocalMonthYear.from(this.value.with(adjuster));
	}

	@Override
	public LocalMonthYear with(TemporalField field, long newValue) {
		return LocalMonthYear.from(this.value.with(field, newValue));
	}

	@Override
	public LocalMonthYear plus(TemporalAmount amount) {
		return LocalMonthYear.from(this.value.plus(amount));
	}

	@Override
	public LocalMonthYear plus(long amountToAdd, TemporalUnit unit) {
		return LocalMonthYear.from(this.value.plus(amountToAdd, unit));
	}

	@Override
	public LocalMonthYear minus(TemporalAmount amount) {
		return LocalMonthYear.from(this.value.minus(amount));
	}

	@Override
	public LocalMonthYear minus(long amountToSubtract, TemporalUnit unit) {
		return LocalMonthYear.from(this.value.minus(amountToSubtract, unit));
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

	@Override
	public LocalDateTime atTime(LocalTime localTime) {
		return this.value.atTime(localTime);
	}

}