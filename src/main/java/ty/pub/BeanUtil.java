package ty.pub;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class BeanUtil {

//	static Kryo kryo = new Kryo();
//
//	/**
//	 * 对象转数组
//	 * 
//	 * @param obj
//	 * @return
//	 */
//	public synchronized static byte[] toByteArray(Object obj) {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		Output output = new Output(bos, 40960);
//		kryo.writeObject(output, obj);
//		byte[] bytes = output.toBytes();
//		output.close();
//		return bytes;
//	}
//
//	/**
//	 * 数组转对象
//	 * 
//	 * @param bytes
//	 * @return
//	 */
//	public synchronized static <T> T toObject(byte[] bytes, Class<T> type) {
//		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//		Input input = new Input(bis);
//		T obj = kryo.readObject(input, type);
//		input.close();
//		return obj;
//	}
//
	/**
	 * byte[] 转 十六进制字符串，长度*2
	 * 
	 * @param bArray
	 * @return
	 */
	public static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
//
//	/**
//	 * 十六进制字符串转字符数组，长度/2
//	 * 
//	 * @param hex
//	 * @return
//	 */
//	public static byte[] hexStringToByte(String hex) {
//		int len = (hex.length() / 2);
//		byte[] result = new byte[len];
//		char[] achar = hex.toCharArray();
//		for (int i = 0; i < len; i++) {
//			int pos = i * 2;
//			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
//		}
//		return result;
//	}
//
//	/**
//	 * long to byte[]
//	 * 
//	 * @param values
//	 * @return
//	 */
//	public static byte[] LongToBytes(long data) {
//		byte[] buffer = new byte[8];
//		for (int i = 0; i < 8; i++) {
//			int offset = i*8;
//			buffer[i] = (byte) ((data >> offset) & 0xff);
//		}
//		return buffer;
//	}
//
//	/**
//	 * Byte[] to long
//	 * 
//	 * @param buffer
//	 * @return
//	 */
//	public static long BytesToLong(byte[] bytes) {
//		long values = 0;
//		int j = 0;
//		for (int i = 0; i < 8; i++) {
//			values <<= 8;
//			values |= ((long) (bytes[bytes.length - i - 1] & 0xffL));
//		}
//		return values;
//	}
//
//	public static byte[] getBytesForRaw(RawDataPacket packet) {
//		try {
//			byte[] ids = packet.getRawDataId().getBytes("UTF-8");
//			byte[] topic = packet.getTopic().getBytes("UTF-8");
//			byte[] sIp = packet.getSourceIp().getBytes("UTF-8");
//			byte[] sPort = packet.getSourcePort().getBytes("UTF-8");
//			byte[] ip = packet.getServerIp().getBytes("UTF-8");
//			byte[] port = packet.getServerPort().getBytes("UTF-8");
//			byte[] imei = packet.getIMEI().getBytes("UTF-8");
//			byte[] time = LongToBytes(packet.getTimestamp());
//			byte[] data = (byte[]) packet.getPacketData();
//			byte[] split = new byte[]{13, 10};
//			return concatAll(ids, split,
//					topic, split,
//					sIp, split,
//					sPort, split,
//					ip, split,
//					port, split,
//					imei, split,
//					data, time
//					);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static RawDataPacket split(byte[] bytes) {
//		int length = bytes.length;
//		List<Integer> splits = new ArrayList<>();
//
//		splits.add(-2);
//		for (int j = 0; j < length - 1; j++) {
//			if (bytes[j] == 13 && bytes[j + 1] == 10) {
//				splits.add(j);
//			}
//		}
//		splits.add(length - 8);
//		List<byte[]> containers = new ArrayList<>();
//		for (int i = 0; i < splits.size() - 1; i++) {
//			containers.add(Arrays.copyOfRange(bytes, splits.get(i) + 2, splits.get(i + 1)));
//		}
//		long timestamp = BytesToLong(Arrays.copyOfRange(bytes, length - 8, length));
//		try {
//			return new RawDataPacket(new String(containers.get(0), "UTF-8"), new String(containers.get(1), "UTF-8"),
//					new String(containers.get(2), "UTF-8"), new String(containers.get(3), "UTF-8"),
//					new String(containers.get(4), "UTF-8"), new String(containers.get(5), "UTF-8"),
//					new String(containers.get(6), "UTF-8"), containers.get(7), timestamp);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * char to Byte
//	 * 
//	 * @param c
//	 * @return
//	 */
//	private static byte toByte(char c) {
//		byte b = (byte) "0123456789ABCDEF".indexOf(c);
//		return b;
//	}
//	
//	private static byte[] concatAll(byte[] first, byte[]... rest) {
//		int totalLength = first.length;
//		for (byte[] array : rest) {
//			totalLength += array.length;
//		}
//		byte[] result = Arrays.copyOf(first, totalLength);
//		int offset = first.length;
//		for (byte[] array : rest) {
//			System.arraycopy(array, 0, result, offset, array.length);
//			offset += array.length;
//		}
//		return result;
//	}
//
//	//Test
//	public static void main(String args[]) {
//		long value =  new Date().getTime();
//		RawDataPacket packet = new RawDataPacket("111", "topic", "10.1.1.1", "9000", "155.2.3.4", "9323", "111", hexStringToByte("ABCD1234567"),value);
//		System.out.println(bytesToHexString(getBytesForRaw(packet)));
//		System.out.println(ObjectToString(split(getBytesForRaw(packet))));
//		System.out.println(bytesToHexString((byte[])split(getBytesForRaw(packet)).getPacketData()));
//		System.out.println(value + " ," + BytesToLong(LongToBytes(value)));
//	}

	public static String ObjectToString(Object obj) {
		StringBuffer result = new StringBuffer();
		Class cls = obj.getClass();
		result.append(cls.getName() + ":{");
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			try {
				String out = String.format("%s : %s", f.getName(), f.get(obj));
				if (i < fields.length - 1)
					out += ", ";
				result.append(out);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		result.append("}");
		return result.toString();
	}

}
