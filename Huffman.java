package huffman;


import java.io.*;
import java.util.*;

//Huffman Tree
class HaffmanTree {
	 public static final int MAXVALUE = 1000;// maximum weight
	 public int nodeNum; // number of leaf nodes

	public HaffmanTree(int n) {
		this.nodeNum = n;
	}

	/**
	  * Construct Huffman Tree Algorithm
	 * 
	  * @param weight weight
	  * @param nodes leaf node
	 */
	public void haffman(char[] names, int[] weight, HaffNode[] nodes) {
		int n = this.nodeNum;
		 // m1,m2, which means the smallest two weights, x1, x2, which means the number corresponding to the minimum two weights, m1 means the smallest, and m2 means the second smallest
		int m1, m2, x1, x2;

		 // Initialize all nodes, corresponding to a Huffman tree with n leaf nodes, with 2n-1 nodes
		for (int i = 0; i < 2 * n - 1; i++) {
			HaffNode temp = new HaffNode();
			 // Initialize n leaf nodes, which are the input nodes. 0, 1, 2, 3 are leaf nodes and input nodes
			if (i < n) {
				temp.name = names[i];
				temp.weight = weight[i];
			} else {
				temp.name = ' ';
				temp.weight = 0;
			}
			temp.parent = 0;
			temp.flag = 0;
			temp.leftChild = -1;
			temp.rightChild = -1;
			nodes[i] = temp;
		}
		
		 // Initialize n-1 non-leaf nodes, n-1 indicates n-1 numbers to be looped n-1 times
		for (int i = 0; i < n - 1; i++) {
			m1 = m2 = MAXVALUE;
			x1 = x2 = 0;
			 // When the number of n-1 is obtained, each time from 0 to n + i-1, and flag = 0, flag = 1 has been added to the binary tree.
			 // The following 2 steps are to find the 2 least weights
			for (int j = 0; j < n + i; j++) {
				if (nodes[j].weight < m1 && nodes[j].flag == 0) {
					 // m1, the initial value of x1 is the first element. If it is smaller than m1, m1 points to a smaller one. The original m1 points to the current m2.
					 // If the latter is smaller than m1 and smaller than m2, then m2 points to this which is smaller than m1 and smaller than m2.
					 // That is to say m1 points to the smallest and m2 points to the second smallest.
					m2 = m1;
					x2 = x1;
					m1 = nodes[j].weight;
					x1 = j;
				} else if (nodes[j].weight < m2 && nodes[j].flag == 0) {
					m2 = nodes[j].weight;
					x2 = j;
				}
			}
			 // Combine the two with the smallest weight into a 2-tree
			nodes[x1].parent = n + i;
			nodes[x2].parent = n + i;
			nodes[x1].flag = 1;
			nodes[x2].flag = 1;
			nodes[n + i].weight = nodes[x1].weight + nodes[x2].weight;
			nodes[n + i].leftChild = x1;
			nodes[n + i].rightChild = x2;
		}
	}

	/**
	  * Haverman coding algorithm
	 * @param nodes
	 * @param haffCode
	 */
	public void haffmanCode(HaffNode[] nodes, Code[] haffCode) {
		int n = this.nodeNum;
		Code code = new Code(n);
		int child, parent;

		 // Encode the first n input nodes
		for (int i = 0; i < n; i++) {
			code.start = n - 1;
			code.weight = nodes[i].weight;
			code.name = nodes[i].name;
			child = i;
			parent = nodes[child].parent;
			 // Go up from the leaf node to generate the code.
			while (parent != 0) {
				if (nodes[parent].leftChild == child) {
					code.bit[code.start] = 0;
				} else {
					code.bit[code.start] = 1;
				}

				code.start--;
				child = parent;
				parent = nodes[child].parent;
			}

			Code temp = new Code(n);
			for (int j = code.start + 1; j < n; j++) {
				temp.bit[j] = code.bit[j];
			}
			temp.weight = code.weight;
			temp.name = code.name;
			temp.start = code.start;
			haffCode[i] = temp;
		}
	}
	
	public void jiema(String res, HaffNode[] nodes){
		 // Start from the root node
		int index = 2*this.nodeNum-2;
		for(int k = 0; k < res.length(); k++){
			 // Read Huffman coding in sequence 
			 // If you encounter 0, it will traverse the left child of the current node.
			if(res.charAt(k)=='1'){
				 // Encount 1 to traverse the right child of the current node
				index = nodes[index].rightChild;
				 // If the right child of the current node is -1, it is proved that it outputs characters directly for the leaf node (since the Huffman tree only has nodes with 0 or 2 degrees out, only the right child can be judged)
				if(nodes[index].rightChild==-1){
					System.out.print(nodes[index].name);
					 // Re-start from the root node
					index = 2*this.nodeNum-2;
				}
			}else{
				 // Encount 1 to traverse the right child of the current node
				index = nodes[index].leftChild;
				 // If the right child of the current node is -1, it is proved that it outputs characters directly for the leaf node (since the Huffman tree only has nodes with 0 or 2 degrees out, only the right child can be judged)
				if(nodes[index].rightChild==-1){
					System.out.print(nodes[index].name);
					 // Re-start from the root node
					index = 2*this.nodeNum-2;
				}
			}
		}
	}
}

 // Huffman tree node class
class HaffNode {
	 public char name; // character name
	 public int weight; // weight
	 public int parent; // his parents
	 public int flag; // flag, whether it is a leaf node
	 public int leftChild; // his left child
	 public int rightChild; // his right child

	public HaffNode() {}
}

 // Huffman coding class
class Code {
	public int[] bit; // encoded array
	public int start; // the starting subscript of the encoding
	 public int weight; // weight
	 public char name; // character name

	public Code(int n) {
		bit = new int[n];
		start = n - 1;
	}
}
public class Huffman {
	public char[] names;
	public int[] weights;

	public static void main(String[] args) throws Exception {
		Huffman test = new Huffman();
		while(true){
			 // read a line of text in the text
			String s = test.readfile();
	
			 // Count the frequency of occurrence of different characters in the text
			Map<Character, Integer> map = test.getCharMaps(s);
	
			 // Create an array to store characters and frequency of occurrence
			test.names = new char[map.size()];
			test.weights = new int[map.size()];
			int i = 0;
	
			 // convert the map to a set and put the statistic characters and their corresponding frequencies into the array
			Set set = map.keySet();
	
			for (Iterator iter = set.iterator(); iter.hasNext();) {
				char key = (char) iter.next();
				test.names[i] = key;
				test.weights[i] = map.get(key);
				i++;
			}
			 System.out.println("**************** Different characters in the text and their corresponding frequency of occurrence ****************** **");
			 // print characters
			for (int j = 0; j < test.names.length; j++) {
				System.out.print(test.names[j] + " ");
			}
			System.out.println();
			 // print frequency
			for (int j = 0; j < test.weights.length; j++) {
				System.out.print(test.weights[j] + " ");
			}
			System.out.println();
	
			 // Create a Huffman tree
			HaffmanTree haffTree = new HaffmanTree(map.size());
			HaffNode[] nodes = new HaffNode[2 * map.size() - 1];
			Code[] codes = new Code[map.size()];
			 // Construct a Huffman tree
			haffTree.haffman(test.names, test.weights, nodes);
			 // Generate Huffman coding
			haffTree.haffmanCode(nodes, codes);
			
			 // print Huffman coding
			 System.out.println("************************Huffman Code Table ************** ************");
			for (int k = 0; k < map.size(); k++) {
				System.out.print("Name=" + codes[k].name + " Weight=" + codes[k].weight + " Code=");
				for (int j = codes[k].start + 1; j < map.size(); j++) {
					System.out.print(codes[k].bit[j]);
				}
				System.out.println();
			}
			 System.out.println("************************** raw data*************** ************");
			System.out.println(s);
			 System.out.println("**********************Huffman encoded data ************** **********");
			String res = s;
			String bit = "";
			 // Replace the corresponding characters according to the Huffman coding table
			for(int k = 0; k < test.names.length; k++){
				for (int j = codes[k].start + 1; j < map.size(); j++) {
					bit += codes[k].bit[j];
				}
				res = res.replace(String.valueOf(test.names[k]), bit);
				bit = "";
			}
			System.out.println(res);
			
			 System.out.println("************************** Decoded data ************** ************");
			//haffTree.jiema(res, nodes);
			System.out.println();
		}
	}

	/**
	  * Read text file
	 * 
	  * @return a line of string in the text
	 * @throws Exception
	 */
	public String readfile() throws Exception {
		/*
		  * Select test file serial number
		 */
		System.out.println("Total six sets of test data (1~6), please enter the data number:");
		Scanner sc = new Scanner(System.in);
		int num = sc.nextInt();

		/*
		  * Read data
		 */
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\dell\\Documents\\Java\\huffman\\src/input.txt"));
		return (br.readLine());
	}

	/**
	  * The frequency of occurrence of different characters in the statistical text
	 * 
	  * @param s string to be detected
	  * @return different characters map corresponding to the frequency at which it appears
	 */
	public Map<Character, Integer> getCharMaps(String s) {
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for (int i = 0; i < s.length(); i++) {
			Character c = s.charAt(i);
			Integer count = map.get(c);
			map.put(c, count == null ? 1 : count + 1);
		}
		return map;
	}
}

