package com.wehealth.model.domain.enumutil;

/**
 * Electromyography Filter Fequency
 */
public enum EmgFeq {
	level_25(25),
	level_35(35),
	level_45(45),
	off(0);
	
	private int _feq;
	
	private EmgFeq(int feq)
	{
		_feq = feq;
	}
	
	public int getFeq()
	{
		return _feq;
	}
}
