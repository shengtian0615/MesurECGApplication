package com.wehealth.model.domain.enumutil;

/**
 * Low Band Filter Frequency
 * @author  Hongyu Wang
 */
public enum LowFeq {
	level_70(70),
	level_100(100),
	level_150(150),
	off(0);
	
	private int _feq;
	
	private LowFeq(int feq)
	{
		_feq = feq;
	}
	
	public int getFeq()
	{
		return _feq;
	}
}
