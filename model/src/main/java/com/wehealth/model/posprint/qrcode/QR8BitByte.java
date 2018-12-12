package com.wehealth.model.posprint.qrcode;

import java.io.UnsupportedEncodingException;

/**
 * QR8BitByte
 * @author Kazuhiko Arase 
 */
class QR8BitByte extends QRData {
	
	public QR8BitByte(String data) {
		super(Mode.MODE_8BIT_BYTE, data);
	}
	
	public void write(BitBuffer buffer) {

		try {

			byte[] data = getData().getBytes(QRUtil.getUTF8Encoding());

			for (int i = 0; i < data.length; i++) {
				buffer.put(data[i], 8);
			}
			
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage() );
		}		
	}
	
	public int getLength() {
		try {
			return getData().getBytes(QRUtil.getUTF8Encoding()).length;
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage() );
		}
	}
	
}