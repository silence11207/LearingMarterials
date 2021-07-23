package class02;

import java.util.ArrayList;

public class Code03_PrintAllPermutations {
	public static ArrayList<String> permutation(String str) {
		ArrayList<String> res = new ArrayList<>();
		if (str == null || str.length() == 0) {
			return res;
		}
		char[] chs = str.toCharArray();
		process(chs, 0, res);
		return res;
	}

	// str[0..i-1]已经做好决定的
	// str[i..]都有机会来到i位置
	// i终止位置，str当前的样子，就是一种结果->ans
	public static void process(char[] str, int i, ArrayList<String> ans) {
		if (i == str.length) {
			ans.add(String.valueOf(str));
		}
		// 如果i没有终止，i...都可以来到i位置
		for (int j = i; j < str.length; j++) { // j i后面的字符串都有机会
			swap(str, i, j);
			process(str, i + 1, ans);
			swap(str, i, j);
		}
	}

	public static void swap(char[] chs, int i, int j) {
		char tmp = chs[i];
		chs[i] = chs[j];
		chs[j] = tmp;
	}
}
