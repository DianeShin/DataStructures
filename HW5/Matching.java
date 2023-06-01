import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Matching
{
    // linked list Node
    static class LinkedListNode<T1> { 
        T1 data; 
        LinkedListNode<T1> next; 
    
        // Constructor 
        LinkedListNode(T1 data) 
        { 
            this.data = data; 
            next = null; 
        } 
    }     
    
    static class LinkedList<T1> implements Iterable<T1>{ 
    
        LinkedListNode<T1> head; // head of list 
        int size = 0;

        // Method to insert a new node 
        public void insert(T1 data) { 
            // Create a new node with given data 
            LinkedListNode<T1> new_node = new LinkedListNode<T1>(data); 
        
            // If the Linked List is empty, 
            // then make the new node as head 
            if (this.head == null) { 
                this.head = new_node; 
            } 
            else { 
                // Else traverse till the last node 
                // and insert the new_node there 
                LinkedListNode<T1> last = this.head; 
                while (last.next != null) { 
                    last = last.next; 
                } 
        
                // Insert the new_node at last node 
                last.next = new_node; 
            }
            this.size++;
        } 

        public void addList(LinkedList<T1> list){        
            // If the Linked List is empty, 
            // then make the new node as head 
            if (this.head == null) { 
                this.head = list.head; 
                this.size = list.size;
            } 
            else { 
                // Else traverse till the last node 
                // and insert the new_node there 
                LinkedListNode<T1> last = this.head; 
                while (last.next != null) { 
                    last = last.next; 
                } 
        
                // Insert the new_node at last node 
                last.next = list.head;
                this.size += list.size; 
            }
        }

        public T1 get(int index){
            LinkedListNode<T1> result = head;
            for (int i = 0; i < index-1; i++){
                result = result.next;
            }
            return result.data;
        }

        public Iterator<T1> iterator(){
            return new LinkedListIterator<T1>(this);
        }
        
        public int size(){
            return size;
        }
    }

    static class LinkedListIterator<T> implements Iterator<T>{
        LinkedListNode<T> current;

        // constructor
        LinkedListIterator(LinkedList<T> list) {
            current = list.head;
        }
        
        // Checks if the next element exists
        public boolean hasNext() {
            return current != null;
        }
        
        // moves the cursor/iterator to next element
        public T next() {
            T data = current.data;
            current = current.next;
            return data;
        }
        
        // Used to remove an element. Implement only if needed
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    static class Tuple<T1, T2>{
        private final T1 item1;
        private final T2 item2;
    
        public Tuple(T1 item1, T2 item2) {
            this.item1 = item1;
            this.item2 = item2;
        }

        public T1 getItem1() {
            return item1;
        }
    
        public T2 getItem2() {
            return item2;
        }
    
        @Override
        public String toString() {
            return "(" + item1 + ", " + item2 + ")";
        }
    }

    static class Node{
        String key;
        int height;
        LinkedList<Tuple<Integer,Integer>> locationList = new LinkedList<>();
        Node left;
        Node right;
    
        Node(String key, Tuple<Integer, Integer> location) {
            this.key = key;
            this.locationList.insert(location);
            height = 1;
        }

    }
    
    static class AVLTree {
        private Node root;
    
        private int height(Node node) {
            if (node == null)
                return 0;
            return node.height;
        }
    
        private int balanceFactor(Node node) {
            if (node == null)
                return 0;
            return height(node.left) - height(node.right);
        }
    
        private Node rotateLeft(Node z) {
            Node y = z.right;
            Node T2 = y.left;
    
            y.left = z;
            z.right = T2;
    
            z.height = Math.max(height(z.left), height(z.right)) + 1;
            y.height = Math.max(height(y.left), height(y.right)) + 1;
    
            return y;
        }
    
        private Node rotateRight(Node z) {
            Node y = z.left;
            Node T2 = y.right;
    
            y.right = z;
            z.left = T2;
    
            z.height = Math.max(height(z.left), height(z.right)) + 1;
            y.height = Math.max(height(y.left), height(y.right)) + 1;
    
            return y;
        }
    
        private Node insert(Node node, String key, Tuple<Integer, Integer> location) {
            if (node == null)
                return new Node(key, location);
    
            if (key.compareTo(node.key) < 0)
                node.left = insert(node.left, key, location);
            else if (key.compareTo(node.key) > 0)
                node.right = insert(node.right, key, location);
            else{
                node.locationList.insert(location);
                return node;
            }
                
    
            node.height = 1 + Math.max(height(node.left), height(node.right));
    
            int balance = balanceFactor(node);
    
            if (balance > 1 && key.compareTo(node.left.key) < 0)
                return rotateRight(node);
    
            if (balance < -1 && key.compareTo(node.right.key) > 0)
                return rotateLeft(node);
    
            if (balance > 1 && key.compareTo(node.left.key) > 0) {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
    
            if (balance < -1 && key.compareTo(node.right.key) < 0) {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
    
            return node;
        }
    
        public void insert(String key, Tuple<Integer, Integer> location) {
            root = insert(root, key, location);
        }

        private Node delete(Node node, String key) {
            if (node == null)
                return null;
    
            if (key.compareTo(node.key) < 0)
                node.left = delete(node.left, key);
            else if (key.compareTo(node.key) > 0)
                node.right = delete(node.right, key);
            else {
                if (node.left == null && node.right == null) {
                    // Case 1: Node to be deleted has no children
                    return null;
                } else if (node.left == null || node.right == null) {
                    // Case 2: Node to be deleted has one child
                    if (node.left != null)
                        return node.left;
                    else
                        return node.right;
                } else {
                    // Case 3: Node to be deleted has two children
                    Node successor = findMinimum(node.right);
                    node.key = successor.key;
                    node.locationList = successor.locationList;
                    node.right = delete(node.right, successor.key);
                }
            }
    
            node.height = 1 + Math.max(height(node.left), height(node.right));
            int balance = balanceFactor(node);
    
            if (balance > 1 && balanceFactor(node.left) >= 0)
                return rotateRight(node);
    
            if (balance > 1 && balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
    
            if (balance < -1 && balanceFactor(node.right) <= 0)
                return rotateLeft(node);
    
            if (balance < -1 && balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
    
            return node;
        }
    
        public void delete(String key) {
            root = delete(root, key);
        }
    
        private Node findMinimum(Node node) {
            if (node == null || node.left == null)
                return node;
            return findMinimum(node.left);
        }

        private void preorderTraversal(Node node, List<String> result) {
            if (node != null) {
                result.add(node.key);
                preorderTraversal(node.left, result);
                preorderTraversal(node.right, result);
            }
        }
        
        public List<String> preorderTraversal() {
            List<String> result = new ArrayList<>();
            preorderTraversal(root, result);
            return result;
        }

        private LinkedList<Tuple<Integer, Integer>> preorderTraversalSearch(Node node, String substr) {
            LinkedList<Tuple<Integer, Integer>> result = new LinkedList<>();
            if (node != null) {
                if (node.key.equals(substr)) {
                    result = node.locationList;
                }
                result.addList(preorderTraversalSearch(node.left, substr));
                result.addList(preorderTraversalSearch(node.right, substr));
            }
            return result;
        }
        
        public LinkedList<Tuple<Integer, Integer>> preorderTraversalSearch(String substr) {
            return preorderTraversalSearch(root, substr);
        }
    }
    
    static class HashTable {
        private AVLTree[] slots;
    
        public HashTable() {
            slots = new AVLTree[100];
            for (int i = 0; i < 100; i++) {
                slots[i] = new AVLTree();
            }
        }
    
        private int hash(String key) {
            int sum = 0;
            for (int i = 0; i < key.length(); i++) {
                sum += (int) key.charAt(i);
            }
            return sum % 100;
        }
    
        public void insert(String key, int line) {
            int startIndex = 0;
            while (startIndex + 5 < key.length()){
                String substring = key.substring(startIndex, startIndex + 6);
                int slotIndex = hash(substring);
                slots[slotIndex].insert(substring, new Tuple<Integer, Integer>(line, startIndex+1));
                startIndex++;                
            }
        }               
    
        public List<String> search(int slot) {
            return slots[slot].preorderTraversal();
        }

        public LinkedList<Tuple<Integer,Integer>> searchSubstr(String substr) {
            return slots[hash(substr)].preorderTraversalSearch(substr);
        }

        public List<Tuple<Integer, Integer>> findString(String pattern) {
            List<String> substrList = new ArrayList<>();
            List<LinkedList<Tuple<Integer,Integer>>> searchResult = new ArrayList<>();
            List<Tuple<Integer, Integer>> result = new ArrayList<>();
            int startIndex = 0;

            // 1. split pattern with 6 char.
            while (startIndex < pattern.length()){
               substrList.add(pattern.substring(startIndex, Math.min(startIndex + 6, pattern.length())));
               startIndex += 6;
            }

            // 2. search location of every pattern
            for (String substr : substrList){
                searchResult.add(hashTable.searchSubstr(substr));
            }
            
            // 3. if connects, we can fetch first search result.
            for(Tuple<Integer,Integer> tupleIter : searchResult.get(0)){ // iterating starting index
                boolean fail = false;

                for (int index = 1; index < searchResult.size(); index++){ // access each block of pattern substr
                    if (fail) break; // search fail -> move to next starter location
                    else{
                        boolean succ = false;
                        for (Tuple<Integer, Integer> innerTupleIter : searchResult.get(index)){
                            if (tupleIter.item1 == innerTupleIter.item1 && tupleIter.item2 + 6 == innerTupleIter.item2){
                                succ = true;
                                break; // search success for now.
                            } 
                            else continue; // mismatch
                        }  
                        if (!succ) fail = true; // search fail        
                    }
                }      
                if (!fail) result.add(tupleIter);   
            }

            return result;
        }
    }

	static HashTable hashTable;
	static int lineNumber;

	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input)
	{
		char command = input.charAt(0);
		String arg = input.substring(2);

		if (command == '<'){
			// 1. init values
			hashTable = new HashTable();
			lineNumber = 1;

			// 2. read lines
			try (BufferedReader reader = new BufferedReader(new FileReader(arg))) {
				String line;
				while ((line = reader.readLine()) != null) {
					hashTable.insert(line, lineNumber);
                    lineNumber++;
				}
			} catch (IOException e) {
				System.err.println("Error reading the file: " + e.getMessage());
			}
		}

		else if (command == '@'){
			List<String> stringList = hashTable.search(Integer.parseInt(arg));
			if (stringList.size() == 0) System.out.println("EMPTY");
			else{
				for (String iter : stringList) System.out.print(iter + " ");
                System.out.println();
			}
		}

		else if (command == '?'){
            // find search result
			List<Tuple<Integer,Integer>> searchResult = hashTable.findString(arg);

            // Create a custom comparator to compare the tuples
            Comparator<Tuple<Integer, Integer>> tupleComparator = new Comparator<Tuple<Integer, Integer>>() {
                @Override
                public int compare(Tuple<Integer, Integer> tuple1, Tuple<Integer, Integer> tuple2) {
                    int item1Comparison = tuple1.getItem1().compareTo(tuple2.getItem1());
                    if (item1Comparison != 0) {
                        return item1Comparison; // Compare item1 first
                    }
                    return tuple1.getItem2().compareTo(tuple2.getItem2()); // Compare item2 if item1 is identical
                }
            };

            // Sort the searchResult list using the custom comparator
            Collections.sort(searchResult, tupleComparator);
            
            if (searchResult.isEmpty()) System.out.print("(0, 0)");
            else{
                for (Tuple<Integer, Integer> tup : searchResult) System.out.print(tup.toString() + ' ');
            }
            System.out.println();
		}

		else if (command == '/'){

        }
		else if (command == '+'){
			hashTable.insert(arg, lineNumber);
			System.out.println(lineNumber);
            lineNumber++;
		}
		else return;
	}
}
