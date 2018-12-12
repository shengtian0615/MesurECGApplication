package com.wehealth.mesurecg.ecgbtutil;

import com.wehealth.mesurecg.utils.PreferUtils;

class ParserEcgDataDefine24 {
	public static final int MAXPACKETLEN = 100;
	public static final int MAXBUFERLEN = 1000;
	public static final byte PACKETHEAD = (byte) 0xA5;
	public static final byte PACKETTAIL = 0x5A;
	public static final int PACKDATALEN = 31;
	public static final int BASEDATA = 0x800;
	public static final int ID = 0;
	public static final int WAVE10LEAD = 0x30;
	public static final int MAXWAVEBUF = 8000;
	public static final int BITEINT1 = 0x1;
	public static final int BITEINT2 = (0x1 << 1);
	public static final int BITEINT3 = (0x1 << 2);
	public static final int BITEINT4 = (0x1 << 3);
	public static final int BITEINT5 = (0x1 << 4);
	public static final int BITEINT6 = (0x1 << 5);
	public static final int BITEINT7 = (0x1 << 6);
	public static final int BITEINT8 = (0x1 << 7);
	public static final int CMDMODLE = 0;
	public static final int DATAMODLE = 1;
}

public class EcgDataParser24 {
	
	private byte buffer[];
	private short dataNum; /* 包中数据个数 */
	private boolean packGet; /* 获取包头标志 */
	public static final int LEADNUM = 12; /* 导联通道数 */
	public static final String TAG = "EcgDataParser";
	EcgDataGetListener ecgListener;
	private int dataModle;

//	int parserCMD = -1;
//	int parserData = -1;
//	int parserComeIn = -1;
//	StringBuffer sb = new StringBuffer();
//	StringBuffer sblen = new StringBuffer();
//	StringBuffer sbcl = new StringBuffer();
//	String filePath4 = "/sdcard/len.txt";
//	String filePath5 = "/sdcard/cl.txt";
//	String filePath6 = "/sdcard/v6.txt";

	public EcgDataParser24(EcgDataGetListener ecgListener) {

		buffer = new byte[ParserEcgDataDefine24.MAXPACKETLEN];
		dataNum = 0;
		packGet = false;
		this.ecgListener = ecgListener;
		dataModle = ParserEcgDataDefine24.CMDMODLE;
	}

	public void EcgDataParserInit() {
		dataNum = 0;
		packGet = false;
	}
	
	public static byte[] PackEcgDeivceInfoCmd() {
		byte cmdBuf[];

		cmdBuf = new byte[5];
		cmdBuf[0] = ParserEcgDataDefine24.PACKETHEAD;
		cmdBuf[1] = 0x06;
		cmdBuf[2] = 0x00;
		cmdBuf[3] = 0x06;
		cmdBuf[4] = ParserEcgDataDefine24.PACKETTAIL;

		return cmdBuf;
	}

	public void EcgParserPacket(byte EcgData[], int dLen) {
		int i = 0;
		byte pData[];
//		parserComeIn++;
		pData = EcgData;
		
		for (i = 0; i < dLen; i++) {
//			sblen.append(dLen+"\n");
//			sbcl.append(dataModle+"\n");
			if (dataModle == ParserEcgDataDefine24.CMDMODLE) {
				EcgParsercmd(pData[i]);
//				parserCMD++;
			} else {
//				parserData++;
				EcgParserData(pData[i]);
			}

			if (dataNum >= ParserEcgDataDefine24.MAXPACKETLEN) {
				packGet = false;
				dataNum = 0;
			}
		}
	}

	private boolean EcgParsercmd(byte data) {
		boolean cmdInfos = false;
		if (!packGet) { // 找帧头
			if (ParserEcgDataDefine24.PACKETHEAD == data) {
				dataNum = 0;
				packGet = true; // 包头标志
			}
		} else {
			if (ParserEcgDataDefine24.PACKETTAIL == data) {
				if (EcgXORCheck(buffer, dataNum)) {
					cmdInfos =  EcgPacketCmdData(buffer, dataNum);
				} else {
//					Log.e(TAG, "pack error");
				}
				packGet = false;
			} else {
				buffer[dataNum++] = data;
			}
		}
		return cmdInfos;
	}

	private synchronized boolean EcgPacketCmdData(byte buffer[], int size) {
		if (buffer[0] == 0x06) {
			byte tmp[] = new byte[21];
			for (int i = 2; i < 23; i++) {
				tmp[i-2] = buffer[i];
			}
			String serialNo = new String(tmp);
			PreferUtils.getIntance().setSerialNo(serialNo.replace(" ", ""));
			dataModle = ParserEcgDataDefine24.DATAMODLE;
			dataNum = 0;
			packGet = false;
			return true;
		}
		return false;
	}
	
	public void setModle(){
		dataModle = ParserEcgDataDefine24.DATAMODLE;
		dataNum = 0;
		packGet = false;
	}

	private void EcgParserData(byte data) {
		if (!packGet) {
			// 找帧头
			if (data < 0) {
				dataNum = 0;
				packGet = true; // 包头标志
				buffer[dataNum++] = data;
			}
		} else {
			if (data >= 0) {
				if (dataNum >= (ParserEcgDataDefine24.PACKDATALEN - 1)) {
					buffer[dataNum] = data;
					EcgPacketData(buffer, ParserEcgDataDefine24.PACKDATALEN);
					packGet = false;
					dataNum = 0;
				} else {
					buffer[dataNum++] = data;
				}
				
			} else {
				packGet = false;
				dataNum = 0;
			}
		}
	}

	private synchronized void EcgPacketData(byte buffer[], int size) {
		boolean bitData[];
		byte packetEcgData[];
		byte leadData[];
		int ecgData[];
		int ecgDataReal[];
		boolean leadState[];
		boolean pace;

//		Log.e(TAG, "EcgPacketData   test");
		ecgData = new int[8];
		leadState = new boolean[9];
		packetEcgData = new byte[24];
		leadData = new byte[3];
		bitData = new boolean[ParserEcgDataDefine24.PACKDATALEN - 4];
		ecgDataReal = new int[12];

//		for(int i = 0; i < size; i++)
//			StringUtils.writException2File(filePath4, buffer[i] + " ");
//		
//		StringUtils.writException2File(filePath4, "\n");
		
		GetEcgDataBitValue(buffer, size, bitData);
		GetLeadStateData(buffer, size, bitData, leadData);
		GetLeadEcgData(buffer, size, bitData, packetEcgData);
		pace = GetPace(leadData);
		GetLeadValue(leadData, leadState);
//		Log.e(TAG, "PACE" + String.valueOf(pace));
//		for (int i = 0; i < 9; i++)
//			Log.e(TAG, "lead" + String.valueOf(leadState[i]));
		GetLeadEcgValue(packetEcgData, ecgData);
		
		 for(int i = 0; i < 8; i++){
		        if(ecgData[i] > 0x800000)
		        	ecgData[i] -= 0x1000000;
		}
		 
		ecgDataReal[0] = ecgData[0];
		ecgDataReal[1] = ecgData[1];
		ecgDataReal[2] = (int) (ecgData[1] - ecgData[0]); // III II - I
		ecgDataReal[3] = (int) (-((ecgData[1] + ecgData[0]) >> 1)); // avR
		ecgDataReal[4] = (int) (ecgData[0] - (ecgData[1] >> 1)); // avL
		ecgDataReal[5] = (int) (ecgData[1] - (ecgData[0] >> 1)); // avF
		for (int i = 2; i < 8; i++)
			ecgDataReal[i + 4] = ecgData[i];
		
		ecgListener.GetEcgData(ecgDataReal, LEADNUM, leadState, pace);
	}

	private boolean EcgXORCheck(byte buffer[], int size) {
		byte cksum = 0;
		int i;
		int len = buffer[1];

		if (len > (size - 2))
			return false;

		for (i = 0; i < (size - 1); i++)
			cksum ^= buffer[i];

		return (cksum == buffer[i]);
	}

	private void GetEcgDataBitValue(byte buffer[], int size, boolean bitData[]) {
		int cnt = 0;
		int i, j;
		int bitNum;

		for (j = 0; j < 4; j++) {
			if (j == 0) {
				bitNum = 5;
			} else {
				bitNum = 6;
			}

			for (i = bitNum; i >= 0; i--) {
				if ((buffer[j] & (1 << i)) == 0) {
					bitData[cnt++] = false;
				} else {
					bitData[cnt++] = true;
				}
			}
		}
	}

	private void GetLeadStateData(byte buffer[], int size, boolean bitData[],
			byte leadData[]) {
		int i;

		for (i = 0; i < 3; i++) {
			if (bitData[i]) {
				leadData[i] = (byte) (buffer[i + 4] | 0x80);
			} else {
				leadData[i] = buffer[i + 4];
			}
		}
	}

	private void GetLeadEcgData(byte buffer[], int size, boolean bitData[],
			byte ecgData[]) {
		int i;

		for (i = 0; i < 24; i++) {
			if (bitData[i + 3]) {
				ecgData[i] = (byte) (buffer[i + 7] | 0x80);
			} else {
				ecgData[i] = buffer[i + 7];
			}
		}
	}

	private boolean GetPace(byte leadData[]) {
		boolean state = false;

		if ((leadData[2] & 0x01) == 1)
			state = true;
		return state;
	}

	private void GetLeadValue(byte leadData[], boolean leadState[]) {
		int cnt = 0;

		for (int i = 0; i < 2; i++)
			for (int j = 0; j < (5 - i); j++) {
				if ((leadData[i + 1] & (1 << (i * 4 + j))) == 0) {
					leadState[cnt++] = false;
				} else {
					leadState[cnt++] = true;
				}
			}
	}

	private void GetLeadEcgValue(byte data[], int ecgData[]) {
		int cnt = 0;
		int j = 0;
		byte tmp[];

		tmp = new byte[3];

		for (int i = 0; i < 24; i++) {

			tmp[j++] = data[i];
			if (j >= 3) {
				ecgData[cnt++] = toInt(tmp);
				j = 0;
			}

		}
	}

	private int toInt(byte[] bytes) {
		switch (bytes.length) {
		case 1:
			return 0xff & bytes[0];
		case 2:
			return ((bytes[0] << 8) & 0xff00) + (bytes[1] & 0xff);
		case 3:
			return ((bytes[0] << 16) & 0xff0000) + ((bytes[1] << 8) & 0xff00)
					+ (bytes[2] & 0xff);
		default:
			return ((bytes[0] << 24) & 0xff000000)
					+ ((bytes[1] << 16) & 0xff0000)
					+ ((bytes[2] << 8) & 0xff00) + (bytes[3] & 0xff);
		}
	}

	// 接收心电数据
	public interface EcgDataGetListener { /* 使用接口实现回调 */
		void GetEcgData(int data[], int len, boolean[] leadState, boolean pace);
	}

	public static byte[] PackEcgDeivceStart() {
		byte cmdBuf[];

		cmdBuf = new byte[5];
		cmdBuf[0] = ParserEcgDataDefine24.PACKETHEAD;
		cmdBuf[1] = 0x03;
		cmdBuf[2] = 0x00;
		cmdBuf[3] = 0x03;
		cmdBuf[4] = ParserEcgDataDefine24.PACKETTAIL;

		return cmdBuf;
	}

	public static byte[] PackEcgDeivceStop() {
		byte cmdBuf[];

		cmdBuf = new byte[5];
		cmdBuf[0] = ParserEcgDataDefine24.PACKETHEAD;
		cmdBuf[1] = 0x04;
		cmdBuf[2] = 0x00;
		cmdBuf[3] = 0x04;
		cmdBuf[4] = ParserEcgDataDefine24.PACKETTAIL;

		return cmdBuf;
	}

	public void stopInit() {
		dataModle = ParserEcgDataDefine24.CMDMODLE;
		dataNum = 0;
		packGet = false;
	}
	
	public boolean EcgParserCMDInfo(byte[] cmds){
		boolean cmdInfo = false;
		for (int i = 0; i < cmds.length; i++) {
			cmdInfo = EcgParsercmd(cmds[i]);
			if (cmdInfo) {
				break;
			}
		}
		return cmdInfo;
	}
}
