package com.wehealth.mesurecg.ecgbtutil;//package com.wehealth.ecgequipment.btservice;
//
//import android.util.Log;
//
//class ParserEcgDataDefine {
//	public static final int MAXPACKETLEN = 100;
//	public static final int MAXBUFERLEN = 1000;
//	public static final byte PACKETHEAD = (byte) 0xA5;
//	public static final byte PACKETTAIL = 0x5A;
//	public static final int PACKDATALEN = 22;
//	public static final int BASEDATA = 0x800;
//	public static final int ID = 0;
//	public static final int WAVE10LEAD = 0x30;
//	public static final int MAXWAVEBUF = 8000;
//	public static final int BITEINT1 = 0x1;
//	public static final int BITEINT2 = (0x1 << 1);
//	public static final int BITEINT3 = (0x1 << 2);
//	public static final int BITEINT4 = (0x1 << 3);
//	public static final int BITEINT5 = (0x1 << 4);
//	public static final int BITEINT6 = (0x1 << 5);
//	public static final int BITEINT7 = (0x1 << 6);
//	public static final int BITEINT8 = (0x1 << 7);
//	public static final int CMDMODLE = 0;
//	public static final int DATAMODLE = 1;
//}
//
//public class EcgDataParser {
//	private byte buffer[];
//	private short dataNum; /* �������ݸ��� */
//	private boolean packGet; /* ��ȡ��ͷ��־ */
//	public static final int LEADNUM = 12; /* ����ͨ���� */
//	public static final String TAG = "EcgDataParser";
//	EcgDataGetListener ecgListener;
//	private int dataModle;
//
//	public EcgDataParser(EcgDataGetListener ecgListener) {
//
//		buffer = new byte[ParserEcgDataDefine.MAXPACKETLEN];
//		dataNum = 0;
//		packGet = false;
//		this.ecgListener = ecgListener;
//		dataModle = ParserEcgDataDefine.CMDMODLE;
//	}
//
//	public void EcgDataParserInit() {
//		dataNum = 0;
//		packGet = false;
//	}
//
//	public byte[] PackEcgDeivceInfoCmd() {
//		byte cmdBuf[];
//
//		cmdBuf = new byte[5];
//		cmdBuf[0] = ParserEcgDataDefine.PACKETHEAD;
//		cmdBuf[1] = 0x06;
//		cmdBuf[2] = 0x00;
//		cmdBuf[3] = 0x06;
//		cmdBuf[4] = ParserEcgDataDefine.PACKETTAIL;
//
//		return cmdBuf;
//	}
//
//	public byte[] PackEcgDeivceStart() {
//		byte cmdBuf[];
//
//		cmdBuf = new byte[5];
//		cmdBuf[0] = ParserEcgDataDefine.PACKETHEAD;
//		cmdBuf[1] = 0x03;
//		cmdBuf[2] = 0x00;
//		cmdBuf[3] = 0x03;
//		cmdBuf[4] = ParserEcgDataDefine.PACKETTAIL;
//
//		return cmdBuf;
//	}
//
//	public byte[] PackEcgDeivceStop() {
//		byte cmdBuf[];
//
//		cmdBuf = new byte[5];
//		cmdBuf[0] = ParserEcgDataDefine.PACKETHEAD;
//		cmdBuf[1] = 0x04;
//		cmdBuf[2] = 0x00;
//		cmdBuf[3] = 0x04;
//		cmdBuf[4] = ParserEcgDataDefine.PACKETTAIL;
//
//		return cmdBuf;
//	}
//
//	public void EcgParserPacket(byte EcgData[], int dLen) {
//		int i = 0;
//		byte pData[];
//
//		pData = EcgData;
//		for (i = 0; i < dLen; i++) {
//			if (dataModle == ParserEcgDataDefine.CMDMODLE) {
//				EcgParsercmd(pData[i]);
//			} else {
//				// Log.e(TAG, String.valueOf(pData[i]));
//				EcgParserData(pData[i]);
//			}
//
//			if (dataNum >= ParserEcgDataDefine.MAXPACKETLEN) {
//				packGet = false;
//				dataNum = 0;
//			}
//		}
//	}
//
//	private void EcgParsercmd(byte data) {
//		if (!packGet) { // ��֡ͷ
//			if (ParserEcgDataDefine.PACKETHEAD == data) {
//				dataNum = 0;
//				packGet = true; // ��ͷ��־
//			}
//		} else {
//			if (ParserEcgDataDefine.PACKETTAIL == data) {
//				if (EcgXORCheck(buffer, dataNum)) {
//					EcgPacketCmdData(buffer, dataNum);
//				} else {
//					Log.e(TAG, "pack error");
//				}
//				packGet = false;
//			} else {
//				buffer[dataNum++] = data;
//			}
//		}
//	}
//
//	private synchronized void EcgPacketCmdData(byte buffer[], int size) {
//		if (buffer[0] == 0x06) {
//			dataModle = ParserEcgDataDefine.DATAMODLE;
//			dataNum = 0;
//			packGet = false;
//			StringBuffer sb = new StringBuffer();
//			for (int i = 0; i < size; i++) {
//				sb.append(String.valueOf(buffer[i]));
//			}
//			Log.e(TAG, "Cmd  " + sb.toString());
//		}
//
//	}
//
//	public void stopInit() {
//		dataModle = ParserEcgDataDefine.CMDMODLE;
//		dataNum = 0;
//		packGet = false;
//	}
//
//	private void EcgParserData(byte data) {
//		if (!packGet) {
//			// ��֡ͷ
//			if (data < 0) {
//				dataNum = 0;
//				packGet = true; // ��ͷ��־
//				buffer[dataNum++] = data;
//			}
//		} else {
//			if (data >= 0) {
//				if (dataNum >= (ParserEcgDataDefine.PACKDATALEN - 1)) {
//					buffer[dataNum] = data;
//					EcgPacketData(buffer, ParserEcgDataDefine.PACKDATALEN);
//				} else {
//					buffer[dataNum++] = data;
//				}
//			} else {
//				packGet = false;
//				dataNum = 0;
//			}
//		}
//	}
//
//	private synchronized void EcgPacketData(byte buffer[], int size) {
//		boolean bitData[];
//		byte packetEcgData[];
//		byte leadData[];
//		short ecgData[];
//		short ecgDataReal[];
//		boolean leadState[];
//		boolean pace;
//
//		// Log.e(TAG, "EcgPacketData   test");
//		ecgData = new short[8];
//		leadState = new boolean[9];
//		packetEcgData = new byte[16];
//		leadData = new byte[3];
//		bitData = new boolean[ParserEcgDataDefine.PACKDATALEN - 3];
//		ecgDataReal = new short[12];
//
//		GetEcgDataBitValue(buffer, size, bitData);
//		GetLeadStateData(buffer, size, bitData, leadData);
//		GetLeadEcgData(buffer, size, bitData, packetEcgData);
//		pace = GetPace(leadData);
//		GetLeadValue(leadData, leadState);
////		Log.e(TAG, "PACE" + String.valueOf(pace));
//		for (int i = 0; i < 9; i++)
//			// Log.e(TAG, "lead" + String.valueOf(leadState[i]));
//			GetLeadEcgValue(packetEcgData, ecgData);
//		ecgDataReal[0] = ecgData[0];
//		ecgDataReal[1] = ecgData[1];
//		ecgDataReal[2] = (short) (ecgData[1] - ecgData[0]); // III II - I
//		ecgDataReal[3] = (short) (-((ecgData[1] + ecgData[0]) >> 1)); // avR
//		ecgDataReal[4] = (short) (ecgData[0] - (ecgData[1] >> 1)); // avL
//		ecgDataReal[5] = (short) (ecgData[1] - (ecgData[0] >> 1)); // avF
//		for (int i = 2; i < 8; i++)
//			ecgDataReal[i + 4] = ecgData[i];
//
//		ecgListener.GetEcgData(ecgData, LEADNUM);
//	}
//
//	private boolean EcgXORCheck(byte buffer[], int size) {
//		byte cksum = 0;
//		int i;
//		int len = buffer[1];
//
//		if (len > (size - 2))
//			return false;
//
//		for (i = 0; i < (size - 1); i++)
//			cksum ^= buffer[i];
//
//		return (cksum == buffer[i]);
//	}
//
//	private void GetEcgDataBitValue(byte buffer[], int size, boolean bitData[]) {
//		int cnt = 0;
//		int i, j;
//		int bitNum;
//
//		for (j = 0; j < 3; j++) {
//			if (j == 0) {
//				bitNum = 5;
//			} else {
//				bitNum = 7;
//			}
//
//			for (i = 0; i < bitNum; i++) {
//				if ((buffer[0] & (1 << i)) == 0) {
//					bitData[cnt++] = false;
//				} else {
//					bitData[cnt++] = true;
//				}
//			}
//		}
//	}
//
//	private void GetLeadStateData(byte buffer[], int size, boolean bitData[],
//			byte leadData[]) {
//		int i;
//
//		for (i = 0; i < 3; i++) {
//			if (bitData[i]) {
//				leadData[i] = (byte) (buffer[i + 3] | 0x80);
//			} else {
//				leadData[i] = buffer[i + 3];
//			}
//		}
//	}
//
//	private void GetLeadEcgData(byte buffer[], int size, boolean bitData[],
//			byte ecgData[]) {
//		int i;
//
//		for (i = 0; i < 16; i++) {
//			if (bitData[i + 3]) {
//				ecgData[i] = (byte) (buffer[i + 6] | 0x80);
//			} else {
//				ecgData[i] = buffer[i + 6];
//			}
//		}
//	}
//
//	private boolean GetPace(byte leadData[]) {
//		boolean state = false;
//
//		if ((leadData[2] & 0x01) == 1)
//			state = true;
//		return state;
//	}
//
//	private void GetLeadValue(byte leadData[], boolean leadState[]) {
//		int cnt = 0;
//
//		for (int i = 0; i < 2; i++)
//			for (int j = 0; j < (5 - i); j++) {
//				if ((leadData[i + 1] & (1 << (i * 3 + j))) == 0) {
//					leadState[cnt++] = false;
//				} else {
//					leadState[cnt++] = true;
//				}
//			}
//	}
//
//	private void GetLeadEcgValue(byte data[], short ecgData[]) {
//		int cnt = 0;
//		int j = 0;
//		byte tmp[];
//
//		tmp = new byte[2];
//
//		for (int i = 0; i < 16; i++) {
//
//			tmp[j++] = data[i];
//			if (j >= 2) {
//				ecgData[cnt++] = toShort(tmp);
//				j = 0;
//			}
//
//		}
//	}
//
//	private short toShort(byte[] bytes) {
//		switch (bytes.length) {
//		case 1:
//			return (short) (0xff & bytes[0]);
//		default:
//			return (short) (((bytes[0] << 8) & 0xff00) + (bytes[1] & 0xff));
//		}
//	}
//
//	// �����ĵ�����
//	public interface EcgDataGetListener { /* ʹ�ýӿ�ʵ�ֻص� */
//		void GetEcgData(short data[], int len);
//	}
//}