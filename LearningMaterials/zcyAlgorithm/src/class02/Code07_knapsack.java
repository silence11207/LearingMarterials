package class02;

public class Code07_knapsack {

	public static int getMaxValue(int[] w, int[] v, int bag) {
		return process(w, v, 0, 0, bag);
	}

	// ���MAX�㷨
	// ���䣺 w[] v[] bag
	// index.. ����ֵ
	// 0..index-1�����˻����ѡ��ʹ�����Ѿ��ﵽ�������Ƕ���alreadyW
	// �������-1����Ϊû�з���
	// ���������-1����Ϊ���ص�ֵ����ʵ��ֵ
	public static int process(int[] w, int[] v, int index, int alreadyW, int bag) {
		if (alreadyW > bag) {
			return -1;
		}
		// ����û��
		if (index == w.length) {
			return 0;
		}
		int p1 = process(w, v, index + 1, alreadyW, bag);
		int p2next = process(w, v, index + 1, alreadyW + w[index], bag);
		int p2 = -1;
		if (p2next != -1) {
			p2 = v[index] + p2next;
		}
		return Math.max(p1, p2);
	}

	public static int maxValue(int[] w, int[] v, int bag) {
		return process1(w, v, 0, bag);
	}

	// �����㷨
	// ֻʣ��rest�ռ���
	// index..��������ѡ�񣬵���ʣ��ռ䲻ҪС��0
	// ����index..�����ܹ���õ�����ֵ
	public static int process1(int[] w, int[] v, int index, int rest) {
		if (rest < 0) { // base case 1
			return 0;
		}
		// rest >=0
		if (index == w.length) { // base case 2
			return 0;
		}
		// �л�Ҳ�пռ�
		int p1 = process1(w, v, index + 1, rest);
		int p2 = -1;
		int p2Next = process1(w, v, index + 1, rest - w[index]);
		if (p2Next != -1) {
			p2 = v[index] + p2Next;
		}
		return Math.max(p1, p2);
	}

	public static int dpWay(int[] w, int[] v, int bag) {
		int N = w.length;
		int[][] dp = new int[N + 1][bag + 1];
		//dp[N][...] = 0;
		for(int index = N - 1; index >= 0; index--) {
			for(int rest = 0; rest <=bag; rest++) {
				 
				int p1 = process1(w, v, index + 1, rest);
				int p2 = -1;
				int p2Next = process1(w, v, index + 1, rest - w[index]);
				if (p2Next != -1) {
					p2 = v[index] + p2Next;
				}
				return Math.max(p1, p2);
			}
		}
		return;
	}
}
