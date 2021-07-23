# 左程云算法

## 入门

刷算法题技巧：小技巧多如牛毛，大思路：**动态规划、树形dp，主要解决二叉树问题**。

在数据结构上玩算法。

 

位运算：>>、>>>、<<、|、&、^

\>>:带符号右移

\>>>:不带符号右移

算法和数据结构学习的大脉络

(1) 知道怎么算的算法

(2) 知道怎么试的算法



### 认识对数器

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/asdadczxasda.png)

### 认识二分法

在一个有序数组中找某个数是否存在

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/sadzxccasdadcza.png)

（L+R）/2 -->位运算：L+（R-L）>>1

N*2+1 --> (N<<1) | 1

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/adccasdasdacd.png)

## 技术与工具

### 并查集

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/bcj.jpg)

方法复杂度：O（1）

代码：

```java
	public static class Node<V> {
		V value;

		public Node(V v) {
			value = v;
		}
	}

	public static class UnionFind<V> {
		public HashMap<V, Node<V>> nodes;
		public HashMap<Node<V>, Node<V>> parents;
		public HashMap<Node<V>, Integer> sizeMap;

		public UnionFind(List<V> values) {
			nodes = new HashMap<>();
			parents = new HashMap<>();
			sizeMap = new HashMap<>();
			for (V cur : values) {
				Node<V> node = new Node<>(cur);
				nodes.put(cur, node);
				parents.put(node, node);
				sizeMap.put(node, 1);
			}
		}

		// 给你一个节点，请你往上到不能再往上，把代表返回
		public Node<V> findFather(Node<V> cur) {
			Stack<Node<V>> path = new Stack<>();
			while (cur != parents.get(cur)) {
				path.push(cur);
				cur = parents.get(cur);
			}
			while (!path.isEmpty()) {
				parents.put(path.pop(), cur);
			}
			return cur;
		}

		public boolean isSameSet(V a, V b) {
			return findFather(nodes.get(a)) == findFather(nodes.get(b));
		}

		public void union(V a, V b) {
			Node<V> aHead = findFather(nodes.get(a));
			Node<V> bHead = findFather(nodes.get(b));
			if (aHead != bHead) {
				int aSetSize = sizeMap.get(aHead);
				int bSetSize = sizeMap.get(bHead);
				Node<V> big = aSetSize >= bSetSize ? aHead : bHead;
				Node<V> small = big == aHead ? bHead : aHead;
				parents.put(small, big);
				sizeMap.put(big, aSetSize + bSetSize);
				sizeMap.remove(small);
			}
		}

		public int sets() {
			return sizeMap.size();
		}

	}
```



**一个数字&上自己取反（~）加1的结果就是把最右侧的1提取出来**

```java
result = pos & (~pos + 1);
```



## 图

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/tu.png)

> 表达图的方法

- 邻接表法

- 邻接矩阵法

### **左程云推荐使用的图描述代码**

> 点结构的描述

```java
public class Node {
	public int value;
	public int in;
	public int out;
	public ArrayList<Node> nexts;
	public ArrayList<Edge> edges;
	
	public Node(int value) {
		this.value = value;
		in = 0;
		out = 0;
		nexts = new ArrayList<>();
		edges = new ArrayList<>();
		
	}
}
```

> 边结构的描述

```java
public class Edge {
	public int weight;
	public Node from;
	public Node to;
	
	public Edge(int weight, Node from, Node to) {
		this.weight = weight;
		this.from = from;
		this.to = to;
	}
}
```

> 图结构描述

```java
public class Graph {
	public HashMap<Integer,Node> nodes;
	public HashSet<Edge> edges;
	
	public Graph() {
		nodes = new HashMap<>();
		edges = new HashSet<>();
	}
}
```

> 图生成器

```java
public class GraphGenerator {
	//matrix 所有的边
	//N*3的矩阵
	//[weight, from节点上的值，to节点上的值]
	public static Graph createGraph(Integer[][] matrix) {
		Graph graph = new Graph();
		for(int i = 0; i<matrix.length; i++) {
			//matrix[0][0],matrix[0][1],matrix[0][2]
			Integer weight = matrix[i][0];
			Integer from = matrix[i][1];
			Integer to = matrix[i][2];
			
			if(!graph.nodes.containsKey(from)) {
				graph.nodes.put(from, new Node(from));
			}
			if(!graph.nodes.containsKey(to)) {
				graph.nodes.put(to, new Node(to));
			}
			Node fromNode = graph.nodes.get(from);
			Node toNode = graph.nodes.get(to);
			Edge newEdge = new Edge(weight, fromNode, toNode);
			fromNode.nexts.add(toNode);
			fromNode.out++;
			toNode.in++;
			fromNode.edges.add(newEdge);
			graph.edges.add(newEdge);
			
		}
		return graph;
	}
	
}
```

> 如何将邻接矩阵转为N*3的矩阵

<img src="https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/ljjz.jpg" style="zoom: 50%;" />



遍历邻接矩阵看到“正”就建边



![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/tmst.jpg)



![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/k.jpg)

### 宽度优先遍历

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/bfs.jpg)

```java
	// 从node出发，进行宽度优先遍历
	public static void bfs(Node node) {
		if (node == null) {
			return;
		}
		Queue<Node> queue = new LinkedList<>();
		HashSet<Node> set = new HashSet<>();
		queue.add(node);
		set.add(node);
		while (!queue.isEmpty()) {
			Node cur = queue.poll();
			System.out.println(cur.value);
			for (Node next : cur.nexts) {
				if (!set.contains(next)) {
					set.add(next);
					queue.add(next);
				}
			}
		}
	}
```



### 深度优先遍历

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626402494%281%29.jpg)

```java
	public static void dfs(Node node) {
		if (node == null) {
			return;
		}
		Stack<Node> stack = new Stack<>();
		HashSet<Node> set = new HashSet<>();
		stack.add(node);
		set.add(node);
		System.out.println(node.value);
		while (!stack.isEmpty()) {
			Node cur = stack.pop();
			for (Node next : cur.nexts) {
				if (!set.contains(next)) {
					stack.push(cur);
					stack.push(next);
					set.add(next);
					System.out.println(next.value);
					break;
				}
			}
		}
	}
```

### 图的拓扑排序算法

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626402395%281%29.jpg)

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626403276%281%29.jpg)

```java
	// directed graph and no loop
	public List<Node> sortTopology(Graph graph) {
		// key:某一个node
		// value:剩余的入度
		HashMap<Node, Integer> inMap = new HashMap<>();
		// 剩余入度为0的点，才能进这个队列
		Queue<Node> zeroInQueue = new LinkedList<>();

		for (Node node : graph.nodes.values()) {
			inMap.put(node, node.in);
			if (node.in == 0) {
				zeroInQueue.add(node);
			}
		}
		// 拓扑排序的结果，依次加入result
		List<Node> result = new ArrayList<Node>();

		while (!zeroInQueue.isEmpty()) {
			Node cur = zeroInQueue.poll();
			result.add(cur);
			for (Node next : cur.nexts) {
				inMap.put(next, inMap.get(next) - 1);
				if (inMap.get(next) == 0) {
					zeroInQueue.add(next);
				}
			}
		}
		return result;
	}
```

> 最小生成树

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626413188%281%29.jpg)

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626413840%281%29.jpg)

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626425287%281%29.png)

```java
//undirected graph only
public class Code04_Kruskal {

	// Union-Find Set
	public static class UnionFind {
		// key 某一个节点，value, key节点往上的节点
		private HashMap<Node, Node> fatherMap;
		// key 某一个集合的代表节点，value key所在集合的节点个数
		private HashMap<Node, Integer> sizeMap;

		public UnionFind() {
			fatherMap = new HashMap<Node, Node>();
			sizeMap = new HashMap<Node, Integer>();
		}

		public void makeSets(Collection<Node> nodes) {
			fatherMap.clear();
			sizeMap.clear();
			for (Node node : nodes) {
				fatherMap.put(node, node);
				sizeMap.put(node, 1);
			}
		}

		private Node findFather(Node n) {
			Stack<Node> path = new Stack<>();
			while (n != fatherMap.get(n)) {
				path.add(n);
				n = fatherMap.get(n);
			}
			while (!path.isEmpty()) {
				fatherMap.put(path.pop(), n);
			}
			return n;
		}

		public boolean isSameSet(Node a, Node b) {
			return findFather(a) == findFather(b);
		}

		public void union(Node a, Node b) {
			if (a == null || b == null) {
				return;
			}
			Node aDai = findFather(a);
			Node bDai = findFather(b);
			if (aDai != bDai) {
				int aSetSize = sizeMap.get(aDai);
				int bSetSize = sizeMap.get(bDai);
				if (aSetSize <= bSetSize) {
					fatherMap.put(aDai, bDai);
					sizeMap.put(bDai, aSetSize + bSetSize);
					sizeMap.remove(aDai);
				} else {
					fatherMap.put(bDai, aDai);
					sizeMap.put(aDai, aSetSize + bSetSize);
					sizeMap.remove(bDai);
				}
			}

		}

	}

	public static class EdgeComparator implements Comparator<Edge> {

		@Override
		public int compare(Edge o1, Edge o2) {
			return o1.weight - o2.weight;
		}

	}

	public static Set<Edge> krusalMST(Graph graph) {
		UnionFind unionFind = new UnionFind();
		unionFind.makeSets(graph.nodes.values());
		PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(new EdgeComparator());
		for (Edge edge : graph.edges) { // M条边
			priorityQueue.add(edge); // O(logM)
		}
		Set<Edge> result = new HashSet<>();
		while (!priorityQueue.isEmpty()) { // M条边
			Edge edge = priorityQueue.poll(); // O(logM)
			if (!unionFind.isSameSet(edge.from, edge.to)) { // O(1)
				result.add(edge);
				unionFind.union(edge.from, edge.to);
			}
		}
		return result;
	}
}
```

> Prim算法

```java
//undirected graph only
public class Code06_Prim {

	public static class EdgeComparator implements Comparator<Edge> {

		@Override
		public int compare(Edge o1, Edge o2) {
			return o1.weight - o2.weight;
		}
	}

	public static Set<Edge> primMST(Graph graph) {
		// 解锁的边进入小根堆
		PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(new EdgeComparator());

		// 哪些点被解锁出来了
		HashSet<Node> nodeSet = new HashSet<>();

		Set<Edge> result = new HashSet<>(); // 依次挑选的边在result里

		for (Node node : graph.nodes.values()) { // 依次挑选的边在result里
			// node是开始点
			if (!nodeSet.contains(node)) {
				nodeSet.add(node);
				for (Edge edge : node.edges) { // 由一个点，解锁所有相连的边
					priorityQueue.add(edge);
				}
				while (!priorityQueue.isEmpty()) {
					Edge edge = priorityQueue.poll(); // 弹出解锁的边中，最小的边
					Node toNode = edge.to; // 可能的一个新的点
					if (!nodeSet.contains(toNode)) { // 不含有的时候，就是新的点
						nodeSet.add(toNode);
						result.add(edge);
						for (Edge nextEdge : toNode.edges) {
							priorityQueue.add(nextEdge);
						}
					}
				}
			}
			// break;
		}
		return result;
	}
}
```

## 熟悉尝试

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626672086%281%29.jpg)

> 打印一个字符串的全部子序列

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626672758%281%29.jpg)

```java
	public static List<String> subs(String s) {
		char[] str = s.toCharArray();
		String path = "";
		List<String> ans = new ArrayList<>();
		process1(str, 0, ans, path);
		return ans;
	}
	//打印一个字符串的全部子序列
	// str固定，不变
	// index此时来到的位置，要 or 不要
	// 如果index来到了str中的终止位置，把沿途路径所形成的答案，放入ans中
	// 之前做出的选择，就是path
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
```

>打印一个字符串的全部子序列，要求不要出现重复字面值的子序列

```java
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
```

> 打印一个字符串的全部排列

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626676242%281%29.jpg)

```java
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
```

### 一、从左往右的尝试模型

#### 从左往右的尝试模型1

规定1和A对应、2和B对应、3和C对应...

那么一个数字字符串比如“111”就可以转化为：

“AAA“、”KA“和"AK"

给定一个只有数字字符串组成的字符串str,返回有多少种转化结果

```java
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
```

#### 从左往右的尝试模型2

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626745973%281%29.jpg)

```java
	public static int getMaxValue(int[] w, int[] v, int bag) {
		return process(w, v, 0, 0, bag);
	}
	//理解MAX算法
	// 不变： w[] v[] bag
	// index.. 最大价值
	// 0..index-1上做了货物的选择，使得你已经达到的重量是多少alreadyW
	// 如果返回-1，认为没有方案
	// 如果不返回-1，认为返回的值是真实价值
	public static int process(int[] w, int[] v, int index, int alreadyW, int bag) {
		if (alreadyW > bag) {
			return -1;
		}
		// 重量没超
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
	


	//常用算法
	// 只剩下rest空间了
	// index..货物自由选择，但是剩余空间不要小于0
	// 返回index..货物能够获得的最大价值
	public static int process1(int[] w, int[] v, int index, int rest) {
		if (rest < 0) { // base case 1
			return 0;
		}
		// rest >=0
		if (index == w.length) { // base case 2
			return 0;
		}
		// 有货也有空间
		int p1 = process1(w, v, index + 1, rest);
		int p2 = -1;
		int p2Next = process1(w, v, index + 1, rest - w[index]);
		if (p2Next != -1) {
			p2 = v[index] + p2Next;
		}
		return Math.max(p1, p2);
	}
```

### 二、范围上尝试的模型

#### 一条线纸牌问题

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626751665%281%29.jpg)

```java
int f(arr, L, R)//返回先手拿纸牌后获得的最大分数
int s(arr, L, R)//返回后手拿纸牌后获得的最大分数
```

先手

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626759292%281%29.jpg)

后手

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626759845%281%29.jpg)

例子：

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626762468%281%29.jpg)

```java
	public static int win1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		return Math.max(f(arr, 0, arr.length - 1), s(arr, 0, arr.length - 1));
	}

	public static int f(int[] arr, int L, int R) {
		if (L == R) {
			return arr[L];
		}
		return Math.max(arr[L] + s(arr, L + 1, R), arr[R] + s(arr, L, R - 1));
	}

	public static int s(int[] arr, int i, int j) {
		if (i == j) {
			return 0;
		}
		return Math.min(f(arr, i + 1, j), f(arr, i, j - 1));
	}
```



#### N皇后问题

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626768675%281%29.png)

```java
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
```

> 加速常数时间做法

一个数字&上自己取反（~）加1的结果就是把最右侧的1提取出来

```java
result = pos & (~pos + 1);
```

太难，面试不会考



![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626856042%281%29.png)

## 暴力递归到动态规划

**暴力递归的分析过程抽象出来就是动态规划的转移方程**

**几个可变参数几维表**

### 题目一（阿里面试题）

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626860246%281%29.png)

暴力递归

```java
	public static int ways(int N, int M, int K, int P) {
		// 参数无效直接返回0
		if (N < 2 || K < 1 || M < 1 || M > N || P < 1 || P > N) {
			return 0;
		}
		// 总共N个位置，从M点出发，还剩K步，返回最终能达到P的方法数
		return walk(N, M, K, P);
	}

	// N：总共有N个位置
	// cur:当前机器人所在的
	// rest:还剩res步没有走，可变参数
	// P:最终目标位置是P，固定参数
	// 该函数的含义：只能在1~N这些位置上移动，当前在cur位置，走完rest步之后停在P位置上的方法
	public static int walk(int N, int cur, int rest, int P) {
		// 如果没有剩余步数了，当前的cur位置就是最后的位置
		// 如果最后的位置停在P上，那么之前做的移动是有效的
		// 如果最后的位置没在P上，那么之前做的移动是无效的
		if (rest == 0) {
			return cur == P ? 1 : 0;
		}
		// 如果还有rest步要走，而当前的cur位置在1位置上，那么当前这步只能从1走向2
		// 后续的过程就是，来到2位置上，还剩rest-1步要走
		if (cur == 1) {
			return walk(N, 2, rest - 1, P);
		}
		// 如果还有rest步要走，而当前的cur位置在N位置上，那么当前这步只能从N走向N-1
		// 后续的过程就是，来到N-1位置上，还剩rest-1步要走
		if (cur == N) {
			return walk(N, N - 1, rest - 1, P);

		}
		// 如果还有rest步要走，而当前的cur位置在中间位置上，那么当前这步可以走向左，也可以走向右
		// 走向左之后，后续的过程就是，来到cur-1位置上，还剩rest-1步要走
		// 走向右之后，后续的过程就是，来到cur+1位置上，还剩rest-1要走
		// 走向左、走向右是截然不同的方法，所以总方法数要都算上
		return walk(N, cur + 1, rest - 1, P) + walk(N, cur - 1, rest - 1, P);
	}
```

动态规划

```java
	public static int waysCache(int N, int M, int K, int P) {
		// 参数无效直接返回0
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

	// 把所有cur和rest的组合，返回的结果，加入到缓存里
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
```

### 背包问题

![](https://tanhua11207.oss-cn-shanghai.aliyuncs.com/markdown%E5%9B%BE%E7%89%87/1626937530%281%29.png)

