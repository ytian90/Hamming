import java.util.Random;

/**
 * Lab3 Part2 CSC 762
 * @author yutian
 * @since Oct 13, 2015
 */
public class Hamming {
	/*
	 * Function to generate Hamming Code with * represents P
	 */
	public static char[] encodeHam(String data) {
		int oriLen = data.length();
		System.out.println(data);
		// generate the hamming code with * in parity bits
		StringBuilder newData = new StringBuilder();
		for (int i = 0, j = 0; j < oriLen; ++i) {
			if (isPowerOfTwo(i + 1)) {
				newData.append('*');
			} else {
				newData.append(data.charAt(j));
				j++;
			}
		}
		System.out.println(newData);
		int newLen = newData.length();
		char[] newArr = newData.toString().toCharArray();

		char[] finalArr = new char[newLen];

		// Function to generate the parity bits
		for (int i = 0, j = 0; i < newLen; ++i, ++j) {
			if (newArr[i] == '*') {
				final int round = i + 1;
				int countOne = 0;

				// inner loop to count ones based on length of round
				for (int start = i + 1, it = start; it < newLen; ) {
					int pos = 0;
					while (pos < round && it < newLen) {
						if (newArr[it++] == '1') {
							countOne++;
						}
						pos++;
					}
					while (pos != 0) {
						++it;
						--pos;
					}
				}

				// replace * with parity bits
				if (countOne % 2 == 0) {
					finalArr[i] = '0';
				} else {
					finalArr[i] = '1';
				}
			// if not the parity bit, move to next bit.
			} else {
				finalArr[i] = newArr[j];
			}
		}
		return finalArr;
	}

	/*
	 * Function to fix the wrong code with the right one
	 */
	public static char[] decodeHam(char[] data, char[] rc, char[] wc) {
		int target = 0;
		for (int i = 0; i < rc.length; ++i) {
			if (rc[i] != wc[i]) {
				target += Math.pow(2, i);
			}
		}
		data[target - 1] = (data[target - 1] == '1') ? '0' : '1';
		return data;
	}

	/*
	 * Function randomly change one bit of the origin data
	 */
	public static char[] messup(char[] a) {
		int len = a.length;
		int rand = randInt(0, len - 1);
//		int rand = 10;
		a[rand] = (a[rand] == '1') ? '0' : '1';
		return a;
	}

	/*
	 * Function to get the parity bits
	 */
	public static char[] getCheck(char[] data) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			if (isPowerOfTwo(i + 1)) {
				str.append(data[i]);
			}
		}
		return str.toString().toCharArray();
	}

	/*
	 * Function to check the parity bits
	 */
	public static char[] verifyCheck(char[] data) {
		int len = data.length;
		int count = 0;
		while (Math.pow(2, count) < len) {
			count++;
		}
//		System.out.println(count);

		char[] result = new char[count];
		for (int i = 0, j = 0; i < len; i++) {
			if (isPowerOfTwo(i + 1)) {
				final int round = i + 1;
				int countOne = 0;

				for (int start = i, it = start; it < len; ) {
					int pos = 0;
					while (pos < round && it < len) {
						if (data[it++] == '1') {
							countOne++;
						}
						pos++;
					}
					while (pos != 0) {
						++it;
						--pos;
					}
				}

				if (data[i] == '1') countOne--;

				if (countOne % 2 == 0) {
					result[j++] = '0';
				} else {
					result[j++] = '1';
				}
			}
		}
		return result;
	}

	/*
	 * Function to randomly generate a number between min and max
	 */
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	/*
	 * Function to check the position is whether power of two
	 */
	public static boolean isPowerOfTwo(int n) {
		return ((n & n - 1) == 0 && n >= 0);
	}

	public static void main(String[] args)
	{
		// Test Program
		String s = "10011010";
		char[] rightString = encodeHam(s);
		System.out.print("Right String: ");
		System.out.println(rightString);

		char[] BeforeMess = getCheck(rightString);
		System.out.print("Right Check: ");
		System.out.println(BeforeMess);

		System.out.println("Mess it up!!");
		char[] wrongString = messup(rightString);
		System.out.print("Wrong String: ");
		System.out.println(wrongString);

		char[] rightCheck = getCheck(wrongString);
		System.out.print("Right Check: ");
		System.out.println(rightCheck);

		char[] wrongCheck = verifyCheck(wrongString);
		System.out.print("Wrong Check: ");
		System.out.println(wrongCheck);

		char[] fixedString = decodeHam(wrongString, rightCheck, wrongCheck);
		System.out.print("Fixed String: ");
		System.out.println(fixedString);
	}
}
