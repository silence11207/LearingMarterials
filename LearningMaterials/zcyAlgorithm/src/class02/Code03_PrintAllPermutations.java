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

	// str[0..i-1]�Ѿ����þ�����
	// str[i..]���л�������iλ��
	// i��ֹλ�ã�str��ǰ�����ӣ�����һ�ֽ��->ans
	public static void process(char[] str, int i, ArrayList<String> ans) {
		if (i == str.length) {
			ans.add(String.valueOf(str));
		}
		// ���iû����ֹ��i...����������iλ��
		for (int j = i; j < str.length; j++) { // j i������ַ������л���
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
