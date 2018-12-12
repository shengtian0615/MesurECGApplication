package com.wehealth.model.domain.model;

import com.wehealth.model.domain.enumutil.AcFeq;
import com.wehealth.model.domain.enumutil.EmgFeq;
import com.wehealth.model.domain.enumutil.LowFeq;
import com.wehealth.model.domain.enumutil.ShiftFeq;

import java.io.Serializable;

public class ECGFilter implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EmgFeq emgFeq;
	private LowFeq lowFeq;
	private ShiftFeq shiftFeq;
	private AcFeq acFeq;
	
	public EmgFeq getEmgFeq() {
		return emgFeq;
	}
	public void setEmgFeq(EmgFeq emgFeq) {
		this.emgFeq = emgFeq;
	}
	public LowFeq getLowFeq() {
		return lowFeq;
	}
	public void setLowFeq(LowFeq lowFeq) {
		this.lowFeq = lowFeq;
	}
	public ShiftFeq getShiftFeq() {
		return shiftFeq;
	}
	public void setShiftFeq(ShiftFeq shiftFeq) {
		this.shiftFeq = shiftFeq;
	}
	public AcFeq getAcFeq() {
		return acFeq;
	}
	public void setAcFeq(AcFeq acFeq) {
		this.acFeq = acFeq;
	}
}
