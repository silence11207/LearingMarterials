package class01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Code01_MergeUser {
	
	public static class User {
		public String a;
		public String b;
		public String c;

		public User(String a, String b, String c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}

		// (1,10,13) (2,10,37) (400,500,37)
		//如果两个user,a字段�?样，或�?�b字段�?样，或�?�c字段�?样，就认为是�?个人
		//请合并users,返回合并之后的用户数�?
		public static int mergeUser(List<User> users) {
			Code05_UnionFind.UnionFind<User> unionFind = new Code05_UnionFind.UnionFind(users);
			HashMap<String, User> mapA = new HashMap<>();
			HashMap<String, User> mapB = new HashMap<>();
			HashMap<String, User> mapC = new HashMap<>();
			
			for(User user : users) {
				//如果遍历到的用户的a字段和mapA中的字段相同，则union两�??
				if(mapA.containsKey(user.a)) {
					unionFind.union(user, mapA.get(user.a));
				}else {
					//否则是新字段
					mapA.put(user.a, user);
				}
				if(mapB.containsKey(user.b)) {
					unionFind.union(user, mapB.get(user.b));
				}else {
					mapB.put(user.b, user);
				}
				if(mapC.containsKey(user.c)) {
					unionFind.union(user, mapC.get(user.c));
				}else {
					mapC.put(user.c, user);
				}
			}
			//向并查集询问，合并之后，还有多少个集合？
			return unionFind.sets();
		}
	}
	
	public static void main(String[] args) {
		User user1 =new User("abc","kst","ef");
		User user2 =new User("ab","kst","efr");
		User user3 =new User("abc","ks","ef");
		User user4 =new User("ee","bc","ca");
		User user5 =new User("cc","bc","zz");
		List users =new ArrayList();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		users.add(user5);
		System.out.println(User.mergeUser(users));
		
	}
}
