package class02;

public class Code02_ConvertToLetterString {
	// strֻ���������ַ���0-9
	// ���ض�����ת������

	// str[0..i-1]ת���������
	// str[i....]ȥת���������ж�����ת������
	public static int process(char[] str, int i) {
		if (i == str.length) {
			return 1;
		}
		// iû�����˵�����ַ�
		if (str[i] == '0') {// ֮ǰ�ľ���������
			return 0;
		}
		// iû�е���ֹλ��
		// str[i]�ַ����ǡ�0��
		if (str[i] == '1') {
			int res = process(str, i + 1); // i�Լ���Ϊ�����Ĳ��֣������ж����ַ���
			if (i + 1 < str.length) {
				res += process(str, i + 2);// (i��i+1)��Ϊ�������֣������ж����ַ���
			}
			return res;
		}
		if (str[i] == '2') {
			int res = process(str, i + 1); // i�Լ���Ϊ�����Ĳ��֣������ж����ַ���
			if (i + 1 < str.length && (str[i + 1] >= '0' && str[i + 1] < '6')) {
				res += process(str, i + 2);// (i��i+1)��Ϊ�������ֲ���û�г���26�������ж����ַ���
			}
			return res;
		}
		// str[i] == '3' - '9'
		return process(str, i + 1);
	}

}
