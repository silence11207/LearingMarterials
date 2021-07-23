package class02;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Code02_PrintAllSubsequences {
	public static List<String> subs(String s) {
		char[] str = s.toCharArray();
		String path = "";
		List<String> ans = new ArrayList<>();
		process1(str, 0, ans, path);
		return ans;
	}
	//��ӡһ���ַ�����ȫ��������
	// str�̶�������
	// index��ʱ������λ�ã�Ҫ or ��Ҫ
	// ���index������str�е���ֹλ�ã�����;·�����γɵĴ𰸣�����ans��
	// ֮ǰ������ѡ�񣬾���path
	public static void process1(char[] str, int index, List<String> ans, String path) {
		if (index == str.length) {
			ans.add(path);
			return;
		}
		String no = path;
		process1(str, index + 1, ans, no);
		String yes = path + String.valueOf(str[index]);
		process1(str, index + 1, ans, yes);
	}
	//��ӡһ���ַ�����ȫ�������У�Ҫ��Ҫ�����ظ�����ֵ��������
	public static void process2(char[] str, int index, HashSet<String> set, String path) {
		if (index == str.length) {
			set.add(path);
			return;
		}
		String no = path;
		process2(str, index + 1, set, no);
		String yes = path + String.valueOf(str[index]);
		process2(str, index + 1, set, yes);
	}
	//��ӡһ���ַ�����ȫ������
	
	
	
}
