package aes128;

import java.util.Scanner;



public class AES128 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		System.out.println("please input plaintext:");
//		String plaintextString = scanner.nextLine();
		String plaintextString = "00112233445566778899aabbccddeeff";
		Cipher cipher = new Cipher();
		@SuppressWarnings("deprecation")
		byte[][] state = cipher.encryptparsestring(plaintextString);
//		
	}

}
