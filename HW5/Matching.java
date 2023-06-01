import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

// TODO : last substring is less than 6. Maybe I need to insert the shorter ones too? Then I need diff method to check correctness for last batch.
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

        public List<T1> toList() {
            List<T1> list = new ArrayList<>();
            LinkedListNode<T1> current = head;
            while (current != null) {
                list.add(current.data);
                current = current.next;
            }
            return list;
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
            
            return node;
        }
    
        public int delete(String key) {
            root = delete(root, key);
            int delNodeCnt = root.locationList.size;
            root.locationList = new LinkedList<>();
            return delNodeCnt;
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

        public void insert_ends(String key, int line) {
            for (int substringLength = 1; substringLength <= 5; substringLength++) {
                for (int startIndex = 0; startIndex + substringLength <= key.length(); startIndex++) {
                    String substring = key.substring(startIndex, startIndex + substringLength);
                    int slotIndex = hash(substring);
                    slots[slotIndex].insert(substring, new Tuple<Integer, Integer>(line, startIndex + 1));
                }
            }
        } 

        public List<String> search(int slot) {
            return slots[slot].preorderTraversal();
        }

        public LinkedList<Tuple<Integer,Integer>> searchSubstr(String substr) {
            return slots[hash(substr)].preorderTraversalSearch(substr);
        }

        public int deleteSubStr(String substr){
            int delNodeCnt = slots[hash(substr)].delete(substr);
            return delNodeCnt;
        }

        public List<Tuple<Integer, Integer>> findString(String pattern, HashTable hashTable_ends) {
            List<String> substrList = new ArrayList<>();
            List<LinkedList<Tuple<Integer,Integer>>> searchResult = new ArrayList<>();
            List<Tuple<Integer, Integer>> result = new ArrayList<>();
            int startIndex = 0;

            // 1. split pattern with 6 char (or less for last)
            while (startIndex < pattern.length()){
               substrList.add(pattern.substring(startIndex, Math.min(startIndex + 6, pattern.length())));
               startIndex += 6;
            }

            // 2. search location of every pattern
            for (int index = 0; index < substrList.size(); index++){
                if (index == substrList.size() - 1 && substrList.get(index).length() != 6){
                    searchResult.add(hashTable_ends.searchSubstr(substrList.get(index))); // dangling pattern searched separately.
                } 
                else searchResult.add(hashTable.searchSubstr(substrList.get(index)));

            }
            
            // 3. if connects, we can fetch first search result.
            for (Tuple<Integer, Integer> tupleIter : searchResult.get(0)) {
                boolean fail = false;
                int prevEndIndex = tupleIter.item2 + 6;
        
                for (int index = 1; index < searchResult.size(); index++) {
                    boolean succ = false;
                    for (Tuple<Integer, Integer> innerTupleIter : searchResult.get(index)) {
                        if (tupleIter.item1.equals(innerTupleIter.item1) && prevEndIndex == innerTupleIter.item2) {
                            succ = true;
                            prevEndIndex = innerTupleIter.item2 + 6;
                            break;
                        }
                    }
                    if (!succ) {
                        fail = true;
                        break;
                    }
                }
        
                if (!fail) {
                    result.add(tupleIter);
                }
            }

            return result;
        }
    }

	static HashTable hashTable;
    static HashTable hashTable_ends;
	static int lineNumber;
    static List<String> inputText;
    
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
            hashTable_ends = new HashTable();
            inputText = new ArrayList<>();
			lineNumber = 1;

			// 2. read lines
			try (BufferedReader reader = new BufferedReader(new FileReader(arg))) {
				String line;
				while ((line = reader.readLine()) != null) {
					hashTable.insert(line, lineNumber);
                    hashTable_ends.insert_ends(line, lineNumber);
                    inputText.add(line);
                    lineNumber++;
				}
			} catch (IOException e) {
				System.err.println("Error reading the file: " + e.getMessage());
			}
		}

		else if (command == '@'){
			List<String> stringList = hashTable.search(Integer.parseInt(arg));
            if (stringList.size() == 0) {
                System.out.println("EMPTY");
            } else {
                StringBuilder resultString = new StringBuilder();
                for (String iter : stringList) {
                    resultString.append(iter).append(' ');
                }
                resultString.deleteCharAt(resultString.length() - 1); // Remove the last space
                System.out.println(resultString);
            }            
		}

		else if (command == '?'){
            // find search result
			List<Tuple<Integer,Integer>> searchResult = hashTable.findString(arg, hashTable_ends);

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
            
            if (searchResult.isEmpty()) {
                System.out.print("(0, 0)");
            } 
            else {
                StringBuilder resultString = new StringBuilder();
                for (Tuple<Integer, Integer> tup : searchResult) {
                    resultString.append(tup.toString()).append(' ');
                }
                resultString.deleteCharAt(resultString.length() - 1); // Remove the last space
                System.out.print(resultString);
            }
            System.out.println();
		}

		else if (command == '/'){
            // get list of locations
            LinkedList<Tuple<Integer, Integer>> locationLinkedList = hashTable.searchSubstr(arg);
            List<Tuple<Integer, Integer>> locationList = locationLinkedList.toList();

            // Create a custom comparator to compare the tuples
            Comparator<Tuple<Integer, Integer>> reverseTupleComparator = new Comparator<Tuple<Integer, Integer>>() {
                @Override
                public int compare(Tuple<Integer, Integer> tuple1, Tuple<Integer, Integer> tuple2) {
                    int item1Comparison = -1 * tuple1.getItem1().compareTo(tuple2.getItem1());
                    if (item1Comparison != 0) {
                        return item1Comparison; // Compare item1 first
                    }
                    return -1 * tuple1.getItem2().compareTo(tuple2.getItem2()); // Compare item2 if item1 is identical
                }
            };

            // reverse sort location list -> string deletion should be done from right to left.
            locationList.sort(reverseTupleComparator);

            // modify input string
            for (Tuple<Integer, Integer> location : locationList) {
                System.out.println(location.toString());
                int lineNumber = location.getItem1();
                int startIndex = location.getItem2();
    
                // Check if the lineNumber is within the inputText range
                String line = inputText.get(lineNumber - 1);
                String updatedLine = line.substring(0, startIndex-1) + line.substring(Math.min(startIndex + 5, line.length()));
                System.out.println(updatedLine);
                inputText.set(lineNumber - 1, updatedLine);   
            }

            for (String str : inputText) System.out.println(str);
            // rehash new substring -> new hash table!
            hashTable = new HashTable();
            hashTable_ends = new HashTable();
            for (int i = 0; i < inputText.size(); i++){
                if (inputText.get(i).length() < 6) continue;
                else{
                    hashTable_ends.insert_ends(inputText.get(i), i+1); 
                    hashTable.insert(inputText.get(i), i+1); 
                }                
            }

            // print del cnt
            System.out.println(locationList.size());
        }

		else if (command == '+'){
            inputText.add(arg);
			hashTable.insert(arg, lineNumber);
			System.out.println(lineNumber);
            lineNumber++;
		}
		else return;
	}
}
