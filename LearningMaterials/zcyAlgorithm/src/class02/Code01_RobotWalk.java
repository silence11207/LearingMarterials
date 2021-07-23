package class02;

public class Code01_RobotWalk {

	public static int ways(int N, int M, int K, int P) {
		// ������Чֱ�ӷ���0
		if (N < 2 || K < 1 || M < 1 || M > N || P < 1 || P > N) {
			return 0;
		}
		// �ܹ�N��λ�ã���M���������ʣK�������������ܴﵽP�ķ�����
		return walk(N, M, K, P);
	}

	// N���ܹ���N��λ��
	// cur:��ǰ���������ڵ�
	// rest:��ʣres��û���ߣ��ɱ����
	// P:����Ŀ��λ����P���̶�����
	// �ú����ĺ��壺ֻ����1~N��Щλ�����ƶ�����ǰ��curλ�ã�����rest��֮��ͣ��Pλ���ϵķ���
	public static int walk(int N, int cur, int rest, int P) {
		// ���û��ʣ�ಽ���ˣ���ǰ��curλ�þ�������λ��
		// �������λ��ͣ��P�ϣ���ô֮ǰ�����ƶ�����Ч��
		// �������λ��û��P�ϣ���ô֮ǰ�����ƶ�����Ч��
		if (rest == 0) {
			return cur == P ? 1 : 0;
		}
		// �������rest��Ҫ�ߣ�����ǰ��curλ����1λ���ϣ���ô��ǰ�ⲽֻ�ܴ�1����2
		// �����Ĺ��̾��ǣ�����2λ���ϣ���ʣrest-1��Ҫ��
		if (cur == 1) {
			return walk(N, 2, rest - 1, P);
		}
		// �������rest��Ҫ�ߣ�����ǰ��curλ����Nλ���ϣ���ô��ǰ�ⲽֻ�ܴ�N����N-1
		// �����Ĺ��̾��ǣ�����N-1λ���ϣ���ʣrest-1��Ҫ��
		if (cur == N) {
			return walk(N, N - 1, rest - 1, P);

		}
		// �������rest��Ҫ�ߣ�����ǰ��curλ�����м�λ���ϣ���ô��ǰ�ⲽ����������Ҳ����������
		// ������֮�󣬺����Ĺ��̾��ǣ�����cur-1λ���ϣ���ʣrest-1��Ҫ��
		// ������֮�󣬺����Ĺ��̾��ǣ�����cur+1λ���ϣ���ʣrest-1Ҫ��
		// �������������ǽ�Ȼ��ͬ�ķ����������ܷ�����Ҫ������
		return walk(N, cur + 1, rest - 1, P) + walk(N, cur - 1, rest - 1, P);
	}

	public static int waysCache(int N, int M, int K, int P) {
		// ������Чֱ�ӷ���0
		if (N < 2 || K < 1 || M < 1 || M > N || P < 1 || P > N) {
			return 0;
		}
		int[][] dp = new int[N + 1][K + 1];
		for (int row = 0; row <= N; row++) {
			for (int col = 0; col <= K; col++) {
				dp[row][col] = -1;
			}
		}
		return walkCache(N, M, K, P, dp);
	}

	// ������cur��rest����ϣ����صĽ�������뵽������
	public static int walkCache(int N, int cur, int rest, int P, int[][] dp) {
		if (dp[cur][rest] != -1) {
			return dp[cur][rest];
		}
		if (rest == 0) {
			dp[cur][rest] = cur == P ? 1 : 0;
			return dp[cur][rest];
		}
		if (cur == 1) {
			dp[cur][rest] = walkCache(N, 2, rest - 1, P, dp);
			return dp[cur][rest];
		}
		if (cur == N) {
			dp[cur][rest] = walkCache(N, N - 1, rest - 1, P, dp);
			return dp[cur][rest];
		}
		dp[cur][rest] = walkCache(N, cur + 1, rest - 1, P, dp) + walkCache(N, cur - 1, rest - 1, P, dp);
		return dp[cur][rest];
	}
}
