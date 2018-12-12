package com.wehealth.model.domain.model;

public class PrintModel {

	private int mAlign;
	private String mContent;
	private int mTextSize;
	private float mX;
	private float mY;
	
	public PrintModel(int align, String content, int textsize, float x, float y){
		this.mContent = content;
		this.mTextSize = textsize;
		this.mX = x;
		this.mY = y;
		this.mAlign = align;
	}
	
	public int getAlign() {
		return mAlign;
	}
	public void setAlign(int align) {
		this.mAlign = align;
	}
	public String getContent() {
		return mContent;
	}
	public void setContent(String content) {
		this.mContent = content;
	}
	public int getTextSize() {
		return mTextSize;
	}
	public void setTextSize(int textSize) {
		this.mTextSize = textSize;
	}
	public float getX() {
		return mX;
	}
	public void setX(float x) {
		this.mX = x;
	}
	public float getY() {
		return mY;
	}
	public void setY(float y) {
		this.mY = y;
	}
}
