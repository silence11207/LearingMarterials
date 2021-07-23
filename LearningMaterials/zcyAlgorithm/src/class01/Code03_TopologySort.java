package class01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Code03_TopologySort {
	// directed graph and no loop
	public List<Node> sortTopology(Graph graph) {
		// key:ĳһ��node
		// value:ʣ������
		HashMap<Node, Integer> inMap = new HashMap<>();
		// ʣ�����Ϊ0�ĵ㣬���ܽ��������
		Queue<Node> zeroInQueue = new LinkedList<>();

		for (Node node : graph.nodes.values()) {
			inMap.put(node, node.in);
			if (node.in == 0) {
				zeroInQueue.add(node);
			}
		}
		// ��������Ľ�������μ���result
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
}
