package class02;

public class Code02_ConvertToLetterString {
	// str只含有数字字符串0-9
	// 返回多少种转化方案

	// str[0..i-1]转化无需过问
	// str[i....]去转化，返回有多少种转化方法
	public static int process(char[] str, int i) {
		if (i == str.length) {
			return 1;
		}
		// i没到最后，说明有字符
		if (str[i] == '0') {// 之前的决定有问题
			return 0;
		}
		// i没有到终止位置
		// str[i]字符不是‘0’
		if (str[i] == '1') {
			int res = process(str, i + 1); // i自己作为单独的部分，后续有多少种方法
			if (i + 1 < str.length) {
				res += process(str, i + 2);// (i和i+1)作为单独部分，后续有多少种方法
			}
			return res;
		}
		if (str[i] == '2') {
			int res = process(str, i + 1); // i自己作为单独的部分，后续有多少种方法
			if (i + 1 < str.length && (str[i + 1] >= '0' && str[i + 1] < '6')) {
				res += process(str, i + 2);// (i和i+1)作为单独部分并且没有超过26，后续有多少种方法
			}
			return res;
		}
		// str[i] == '3' - '9'
		return process(str, i + 1);
	}

}
