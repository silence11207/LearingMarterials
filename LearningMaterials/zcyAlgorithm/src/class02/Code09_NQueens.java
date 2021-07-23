package class02;

public class Code09_NQueens {

	public static int num1(int n) {
		if (n < 1) {
			return 0;
		}
		int[] record = new int[n];
		return process1(0, record, n);
	}

	// 潜台词：record[0..i-1]的皇后，任何两个皇后一定都不共行、不共列，不共斜线
	// 目前来到了第i行
	// record[0..i-1]表示之前的行，放了皇后位置
	// n代表整体一共有多少行 0~n-1行
	// 返回值是，摆完所有的皇后，合理的摆法有多少种
	public static int process1(int i, int[] record, int n) {
		if (i == n) { // 终止行
			return 1;
		}
		int res = 0;
		for (int j = 0; j < n; n++) {// 当前行在i行，尝试i行所有的列 -> j
			// 当前i行的皇后放在j列，会不会和之前（0..i-1)的皇后，不共行或者共斜线。
			// 如果是，认为有效
			// 如果不是，认为无效
			if (isValid(record, i, j)) {
				record[i] = j;
				res += process1(i + 1, record, n);
			}

		}
		return res;
	}

	public static boolean isValid(int[] record, int i, int j) {
		for (int k = 0; k < i; k++) { // 之前的某个k行的皇后
			if (record[k] == j || Math.abs(record[k] - j) == Math.abs(i - k)) {
				return false;
			}
		}
		return true;
	}

}
