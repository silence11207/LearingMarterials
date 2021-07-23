package class01;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

//undirected graph only
public class Code06_Prim {

	public static class EdgeComparator implements Comparator<Edge> {

		@Override
		public int compare(Edge o1, Edge o2) {
			return o1.weight - o2.weight;
		}
	}

	public static Set<Edge> primMST(Graph graph) {
		// �����ı߽���С����
		PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(new EdgeComparator());

		// ��Щ�㱻����������
		HashSet<Node> nodeSet = new HashSet<>();

		Set<Edge> result = new HashSet<>(); // ������ѡ�ı���result��

		for (Node node : graph.nodes.values()) { // ������ѡ�ı���result��
			// node�ǿ�ʼ��
			if (!nodeSet.contains(node)) {
				nodeSet.add(node);
				for (Edge edge : node.edges) { // ��һ���㣬�������������ı�
					priorityQueue.add(edge);
				}
				while (!priorityQueue.isEmpty()) {
					Edge edge = priorityQueue.poll(); // ���������ı��У���С�ı�
					Node toNode = edge.to; // ���ܵ�һ���µĵ�
					if (!nodeSet.contains(toNode)) { // �����е�ʱ�򣬾����µĵ�
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
