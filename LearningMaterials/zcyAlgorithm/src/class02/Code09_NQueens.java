package class02;

public class Code09_NQueens {

	public static int num1(int n) {
		if (n < 1) {
			return 0;
		}
		int[] record = new int[n];
		return process1(0, record, n);
	}

	// Ǳ̨�ʣ�record[0..i-1]�Ļʺ��κ������ʺ�һ���������С������У�����б��
	// Ŀǰ�����˵�i��
	// record[0..i-1]��ʾ֮ǰ���У����˻ʺ�λ��
	// n��������һ���ж����� 0~n-1��
	// ����ֵ�ǣ��������еĻʺ󣬺���İڷ��ж�����
	public static int process1(int i, int[] record, int n) {
		if (i == n) { // ��ֹ��
			return 1;
		}
		int res = 0;
		for (int j = 0; j < n; n++) {// ��ǰ����i�У�����i�����е��� -> j
			// ��ǰi�еĻʺ����j�У��᲻���֮ǰ��0..i-1)�Ļʺ󣬲����л��߹�б�ߡ�
			// ����ǣ���Ϊ��Ч
			// ������ǣ���Ϊ��Ч
			if (isValid(record, i, j)) {
				record[i] = j;
				res += process1(i + 1, record, n);
			}

		}
		return res;
	}

	public static boolean isValid(int[] record, int i, int j) {
		for (int k = 0; k < i; k++) { // ֮ǰ��ĳ��k�еĻʺ�
			if (record[k] == j || Math.abs(record[k] - j) == Math.abs(i - k)) {
				return false;
			}
		}
		return true;
	}

}
