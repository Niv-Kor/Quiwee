package com.utility.math;

public class Range<T extends Number>
{
	private T min, max;
	
	public Range(T min, T max) {
		this.min = min;
		this.max = max;
	}
	
	public Range(Range<T> other) {
		this.min = other.min;
		this.max = other.max;
	}
	
	public double percentOf(double perc) {
		double min = (double) this.min.floatValue();
		double max = (double) this.max.floatValue();
		
		return (int) Percentage.percentOfNum(perc, max - min) + min;
	}
	
	public double numOf(double num) {
		double min = (double) this.min.floatValue();
		double max = (double) this.max.floatValue();
		
		return (int) Percentage.numOfNum(num, max - min) - min;
	}
	
	public boolean intersects(T num) {
		double min = (double) this.min.floatValue();
		double max = (double) this.max.floatValue();
		
		return num.floatValue() <= max && num.floatValue() >= min;
	}
	
	public boolean intersects(Range<T> other) {
		double min = (double) this.min.floatValue();
		double max = (double) this.max.floatValue();
		
		return other.min.floatValue() <= max && other.max.floatValue() >= min;
	}
	
	public Float getMin() { return min.floatValue(); }
	public Float getMax() { return max.floatValue(); }
	
	public static <T extends Number> double effectOf(double num, Range<? extends Number> range, Range<? extends Number> effectModel){
		return Percentage.limit(effectModel.percentOf(100 - range.numOf(num)), effectModel);
	}
	public double generate() { return RNG.generateDouble(min.floatValue(), max.floatValue()); }
	public String toString() { return "[" + NumeralHandler.round(getMin(), 2) + ", " + NumeralHandler.round(getMax(), 2) + "]"; }
}