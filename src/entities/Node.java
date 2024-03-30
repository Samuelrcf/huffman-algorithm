package entities;

public class Node implements Comparable<Node> {
	private char character;
	private int frequency;
	private Node left, right;

	public Node(char character, int frequency) {
		this.character = character;
		this.frequency = frequency;
		left = null;
		right = null;
	}

	@Override
	public int compareTo(Node node) {
		return this.frequency - node.frequency;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

}
