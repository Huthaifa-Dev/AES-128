package aes128;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RanYunlong
 * @author Kingteeloki
 * ZJU 2019.6.17
 */

public class OFB {
	Cipher cipher ;
	
	
	public OFB() {
		cipher = new Cipher();
	}
	
	/**
	 * easy way to check whether the method is correct.
	 * @param plaintext your text need to be encrypted.
	 * @param inputkey your key which used in encryption and it will be regard as hexadecimal characters.
	 * @param IV the value as long as a state which change by time
	 */
	public void testOFB(String plaintext, String inputkey, String IV) {
		String encryption = encryptstringtobytes(plaintext, inputkey, IV);
		System.out.println("after encryption:");
		System.out.println(encryption);
		String decryption = decryptparseString(encryption, inputkey, IV);
		System.out.println("after decryption:");
		System.out.println(decryption);
	}
	
	/**
	 * this method will transfer your input text to byte stream and then
	 * encrypt them in OFB pattern.
	 * @param plaintext your text need to be encrypted.
	 * @param inputkey your key which used in encryption and it will be regard as hexadecimal characters.
	 * @param IV the value as long as a state which change by time
	 * @return return the encrypted string
	 */
	public String encryptstringtobytes(String plaintext, String inputkey, String IV) {
		StringBuilder stringBuilder = new StringBuilder();
		String sb;
		int start = 0;
		int end = 2;
		byte[] key = new byte[cipher.Nk*4];
		if(inputkey.length() != 32) {
			System.out.println("please input correct key!");
			return "";
		}
		//iterate over key two chars a time, add their hex value to matrix
		for (int i = 0; i < key.length; i++) {
				sb = inputkey.substring(start, end);
				key[i] = (byte)(Integer.parseInt(sb.toString(), 16));
				start += 2;
				end += 2;
		}
		
		cipher.Keyexpansion(key);
		
		if(IV.length() != 32) {
			System.out.println("please input correct IV!");
			return "";
		}
		start = 0;
		end = 2;
		byte[][] ivbytes = new byte[cipher.Nb][cipher.Nb];
		//iterate over line two chars at a time, add their hex value to matrix
		for (int i = 0; i < ivbytes.length; i++) {
			for (int j = 0; j < ivbytes[0].length; j++) {
				sb = IV.substring(start, end);
				ivbytes[j][i] = (byte)(Integer.parseInt(sb.toString(), 16));
				start += 2;
				end += 2;
			}
		}
		
		List<byte[][]> states = new ArrayList<byte[][]>();
		List<byte[][]> outputs = new ArrayList<byte[][]>();
		
		byte[] plaintextbytes = plaintext.getBytes();
//		printbytes(plaintextbytes);
		byte[][] state = new byte[cipher.Nb][cipher.Nb];
		
		for (int i = 0; i < plaintextbytes.length; i++) {
			if(i%16 == 0 && i != 0) {
				states.add(state);
				state = new byte[cipher.Nb][cipher.Nb];
				initializestate(state);
			}
			state[i%4][(i/4)%4] = plaintextbytes[i];	
		}
		
		byte[][] Input = ivbytes;
		
		
		for (int i = 0 ; i < states.size(); i++) {
			byte[][] ptext = states.get(i);
			byte[][] afterencryption = cipher.encryptstatenoprint(Input);
			outputs.add(xorbytes(afterencryption, ptext));
			Input = afterencryption;
		}
		
		for (int i = 0; i < states.size(); i++) {
			stringBuilder.append(statetostring(outputs.get(i)));
		}
		
		return stringBuilder.toString(); 
	}
	
	/**
	 * this method will transfer your input encrypted text to hexadecimal characters and then
	 * decrypt them in OFB pattern. 
	 * @param encryption the encrypted text
	 * @param inputkey your key which used in encryption and it will be regard as hexadecimal characters.
	 * @param IV the value as long as a state which change by time
	 * @return a string decrypted from encryption
	 */
	public String decryptparseString(String encryption, String inputkey, String IV) {
		StringBuilder stringBuilder = new StringBuilder();
		String sb;
		int start = 0;
		int end = 2;
		byte[] key = new byte[cipher.Nk*4];
		if(inputkey.length() != 32) {
			System.out.println("please input correct key!");
			return "";
		}
		//iterate over key two chars a time, add their hex value to matrix
		for (int i = 0; i < key.length; i++) {
				sb = inputkey.substring(start, end);
				key[i] = (byte)(Integer.parseInt(sb.toString(), 16));
				start += 2;
				end += 2;
		}
		
		cipher.Keyexpansion(key);
		
		
		if(IV.length() != 32) {
			System.out.println("please input correct IV!");
			return "";
		}
		start = 0;
		end = 2;
		byte[][] ivbytes = new byte[cipher.Nb][cipher.Nb];
		//iterate over line two chars at a time, add their hex value to matrix
		for (int i = 0; i < ivbytes.length; i++) {
			for (int j = 0; j < ivbytes[0].length; j++) {
				sb = IV.substring(start, end);
				ivbytes[j][i] = (byte)(Integer.parseInt(sb.toString(), 16));
				start += 2;
				end += 2;
			}
		}
		
		List<byte[][]> states = new ArrayList<byte[][]>();
		List<byte[][]> outputs = new ArrayList<byte[][]>();
		
		byte[][] state = new byte[cipher.Nb][cipher.Nb];
		
		List<String> encryptions = new ArrayList<String>();
		for(int i = 0 ; i  < encryption.length() ; ) {
			encryptions.add(encryption.substring(i,i+32));
			i = i +32;
		}
		for (int i = 0; i < encryptions.size(); i++) {
			start = 0;
			end = 2;
			String oneline = encryptions.get(i);
			for (int h = 0; h < state.length; h++) {
				for (int j = 0; j < state[0].length; j++) {
					sb = oneline.substring(start, end);
					state[j][h] = (byte)(Integer.parseInt(sb.toString(), 16));
					start += 2;
					end += 2;
				}
			}
			states.add(state);
			state = new byte[cipher.Nb][cipher.Nb];
			initializestate(state);
		}
//		StringBuilder stringBuilder2 = new StringBuilder();
//		for (int i = 0; i < states.size(); i++) {
//			stringBuilder2.append(statetostring(states.get(i)));
//		}
//		System.out.println(stringBuilder2.toString());
		
		
		byte[][] Input = ivbytes;
		
		
		for (int i = 0 ; i < states.size(); i++) {
			byte[][] ptext = states.get(i);
			byte[][] afterencryption = cipher.encryptstatenoprint(Input);
			outputs.add(xorbytes(afterencryption, ptext));
			Input = afterencryption;
		}
		
		byte[] stringbytes = statetobyte(outputs.get(0));
		for (int i = 1; i < outputs.size(); i++) {
			stringbytes = combinebytegroup(stringbytes, statetobyte(outputs.get(i)));
		}
		
//		printbytes(stringbytes);
		
		return new String(stringbytes); 
	}
	
	//initialize state to prevent unexpected stituation
	private void initializestate(byte[][] state) {
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[0].length; j++) {
				state[i][j] = (byte)(0x32);
			}
		}
	}
	
	//implement operation xor on two byte matrix
	private byte[][] xorbytes(byte[][] A, byte[][] B) {
		byte[][] result = new byte[A.length][A[0].length];
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				result[i][j] = (byte)(A[i][j]^B[i][j]);
			}
		}
		return result;
	}
	
	//transfer byte matrix to a string
	private String statetostring(byte[][] state) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				stringBuilder.append(String.format("%02X", state[j][i]));
			}
		}
		String string = stringBuilder.toString();
		return string;
	}
	
	//transfer a byte matrix to byte array 
	private byte[] statetobyte(byte[][] state){
		byte[] result = new byte[state.length*state[0].length];
		for (int i = 0; i < result.length; i++) {
			result[i] = state[i%4][(i/4)%4];
		}
		return result;
	}
	
	//combine two byte arrays into one
	private byte[] combinebytegroup(byte[] A, byte[] B) {
		byte[] result = new byte[A.length+B.length];
		for (int i = 0; i < A.length; i++) {
			result[i] = A[i];
		}
		//A:5 B:4  result: 9  a0 a1 a2 a3 a4
		for (int i = A.length; i < A.length+B.length; i++) {
			result[i] = B[i - A.length];
		}
		return result;
	}
	
	//print bytes in byte array in hexadecimal
	private void printbytes(byte[] A) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < A.length; i++) {
			
				stringBuilder.append(String.format("%02X", A[i]));
			
		}
		String string = stringBuilder.toString();
		System.out.println(string);
	}
	
}
