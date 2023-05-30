import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Node {
    String key;
    int height;
    Node left;
    Node right;

    Node(String key) {
        this.key = key;
        height = 1;
    }
}

class AVLTree {
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

    private Node insert(Node node, String key) {
        if (node == null)
            return new Node(key);

        if (key.compareTo(node.key) < 0)
            node.left = insert(node.left, key);
        else if (key.compareTo(node.key) > 0)
            node.right = insert(node.right, key);
        else
            return node;

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

    public void insert(String key) {
        root = insert(root, key);
    }

    private void inorderTraversal(Node node, List<String> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(node.key);
            inorderTraversal(node.right, result);
        }
    }

    public List<String> inorderTraversal() {
        List<String> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }
}

class HashTable {
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

    public void insert(String key) {
        int slotIndex = hash(key);
        slots[slotIndex].insert(key);
    }

    public List<String> search(String key) {
        int slotIndex = hash(key);
        return slots[slotIndex].inorderTraversal();
    }
}

public class Matching
{
	static HashTable hashTable = new HashTable();

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
		String arg = input.split(" ")[1];

		if (command == '<'){
			try (BufferedReader reader = new BufferedReader(new FileReader(arg))) {
				String line;
				while ((line = reader.readLine()) != null) {
					hashTable.insert(line);
				}
			} catch (IOException e) {
				System.err.println("Error reading the file: " + e.getMessage());
			}
		}

		else if (command == '@'){
			List<String> stringList = hashTable.search(arg);
			if (stringList.size() == 0) System.out.println("EMPTY");
			else{
				for (String iter : stringList) System.out.println(iter);
			}
		}
		
		else if (command == '?');
		else if (command == '/');
		else if (command == '+');
		else return;
	}
}
