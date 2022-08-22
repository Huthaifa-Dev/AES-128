package aes128;

import java.util.Scanner;



public class AES128 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		System.out.println("please input keys:");
//		String keyString = scanner.nextLine();
		String keyString = "000102030405060708090a0b0c0d0e0f";
		System.out.println("please input plaintext:");
//		String plaintextString = scanner.nextLine();
		String plaintextString = "00112233445566778899aabbccddeeff";
		Cipher cipher = new Cipher();
//		@SuppressWarnings("deprecation")
//		byte[][] state = cipher.encryptparsestring(plaintextString, keyString);
//		Decipher decipher = new Decipher(cipher.getKeyExpansionresult());
//		decipher.decryptstate(state);
//		OFB ofb = new OFB();
//		String plaintext = "AES became effective as a federal government standard after approval by the Secretary of Commerce. It is the first publicly accessible cipher approved by the National Security Agency for top secret information.";
//		System.out.println("plaintext:\n" + plaintext);
//		String inputkey = "000102030405060708090a0b0c0d0e0f";
//		String IV = 	  "08070605040302010099aabbccddeeff";
//		ofb.testOFB(plaintext, inputkey, IV);
	}

}
