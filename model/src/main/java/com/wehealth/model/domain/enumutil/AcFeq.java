package com.wehealth.model.domain.enumutil;

/**
 * Alternating Current Filter Fequency
 * @author  Hongyu Wang
 */
public enum AcFeq {
	level_50(50),
	level_60(60),
	off(0);
	
	private int _feq;
	
	private AcFeq(int feq)
	{
		_feq = feq;
	}
	
	public int getFeq()
	{
		return _feq;
	}
}
