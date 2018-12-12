package com.wehealth.model.domain.model;

import java.io.Serializable;
import java.util.Date;

public class PatientHealthHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String patientId;
	
	private boolean hasAllergy;//过敏史
	private String allergyThing;//过敏源：食物药品等
	private String mainStatement;//主诉：近期不适之类
	
	//即往病史：{@MedicalHistoryEnum}
	//name1,name2,name3 ...
	private String history;
	private String chronicName;//其他慢性疾病的名称
	
	//做过的手术：{@SurgeryHistoryEnum}
	//name1, name2, name3
	private String surgery;
	private String otherSurgery;//其他手术
	
	//家族:{@FamilyMedicalHistoryEnum}
	//name1, name2, name3
	private String familyHistory;
	//家族史其他的名称
	private String otherFH;
	
	//blood pressure high level
	private int bph;
	//blood pressure low level
	private int bpl;
	//空腹血糖，存储string类型，不存double了
	private String bs;
	
	private Date createTime;
	private Date updateTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public boolean isHasAllergy() {
		return hasAllergy;
	}
	public void setHasAllergy(boolean hasAllergy) {
		this.hasAllergy = hasAllergy;
	}
	public String getAllergyThing() {
		return allergyThing;
	}
	public void setAllergyThing(String allergyThing) {
		this.allergyThing = allergyThing;
	}
	public String getMainStatement() {
		return mainStatement;
	}
	public void setMainStatement(String mainStatement) {
		this.mainStatement = mainStatement;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public String getChronicName() {
		return chronicName;
	}
	public void setChronicName(String chronicName) {
		this.chronicName = chronicName;
	}
	public String getSurgery() {
		return surgery;
	}
	public void setSurgery(String surgery) {
		this.surgery = surgery;
	}
	public String getOtherSurgery() {
		return otherSurgery;
	}
	public void setOtherSurgery(String otherSurgery) {
		this.otherSurgery = otherSurgery;
	}
	public String getFamilyHistory() {
		return familyHistory;
	}
	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}
	public String getOtherFH() {
		return otherFH;
	}
	public void setOtherFH(String otherFH) {
		this.otherFH = otherFH;
	}
	public int getBph() {
		return bph;
	}
	public void setBph(int bph) {
		this.bph = bph;
	}
	public int getBpl() {
		return bpl;
	}
	public void setBpl(int bpl) {
		this.bpl = bpl;
	}
	public String getBs() {
		return bs;
	}
	public void setBs(String bs) {
		this.bs = bs;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
