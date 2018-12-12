package com.wehealth.model.domain.enumutil;

/**
 * Shift Filter Fequency
 * @author  Hongyu Wang
 */
public enum ShiftFeq {
	level_005(0.05),
	level_015(0.15),
	level_025(0.25),
	level_050(0.5),
	off(0);
	
	private double _feq;
	
	private ShiftFeq(double feq)
	{
		_feq = feq;
	}
	
	public double getFeq()
	{
		return _feq;
	}
}
