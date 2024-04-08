package com.filescompressorgroup.files_compressor.filecompressor.application;

import java.util.HashMap;
import java.util.PriorityQueue;

import com.filescompressorgroup.files_compressor.filecompressor.entities.Node;

public class HuffmanCompression {

	public static HashMap<Character, Integer> countFrequency(String input) {
		HashMap<Character, Integer> frequencyMap = new HashMap<>();

		// Conta a frequência de cada caractere na entrada
		for (char c : input.toCharArray()) {
			frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1); // obtém o valor associado à chave c ou 0 se não
																	// estiver presente
		}

		return frequencyMap;
	}

	public static Node buildHuffmanTree(HashMap<Character, Integer> frequencyMap) {
		PriorityQueue<Node> pq = new PriorityQueue<>();

		// Cria um nó para cada caractere e adiciona à fila de prioridade
		for (char c : frequencyMap.keySet()) {
			pq.offer(new Node(c, frequencyMap.get(c))); // passa o caracter e a frequencia, pega o valor associado à
														// chave c
		} // nesse ponto, cada nó tem seus nós filhos (right/left) nulos

		// Constrói a árvore de Huffman combinando os nós com as menores frequências
		while (pq.size() > 1) {
			Node left = pq.poll();
			Node right = pq.poll();

			Node parent = new Node('\0', left.getFrequency() + right.getFrequency()); // cria um nó com a frequência da
																						// soma de seus filhos
			parent.setLeft(left);
			parent.setRight(right);

			pq.offer(parent); // coloca o nó na lista de prioridade novamente
		}

		return pq.poll(); // Retorna a raiz da árvore de Huffman quando só tiver 1 nó na fila
	}

	public static HashMap<Character, String> buildHuffmanCodes(Node raiz) {
		HashMap<Character, String> huffmanCodes = new HashMap<>();
		buildHuffmanCodesHelper(raiz, "", huffmanCodes);
		return huffmanCodes;
	}

	private static void buildHuffmanCodesHelper(Node node, String code, HashMap<Character, String> huffmanCodes) {
		if (node == null) {
			return;
		}

		// Se o nó for uma folha, adiciona o caractere e seu código ao mapa
		if (node.getLeft() == null && node.getRight() == null) { // verifica se é terminal
			huffmanCodes.put(node.getCharacter(), code); // quando achar um nó terminal, adiciona
		}

		// Recursivamente constroi os códigos para a subárvore esquerda e direita
		buildHuffmanCodesHelper(node.getLeft(), code + "0", huffmanCodes);
		buildHuffmanCodesHelper(node.getRight(), code + "1", huffmanCodes);
	}

	public static String encode(String input, HashMap<Character, String> huffmanCodes) {
		StringBuilder encodedString = new StringBuilder();

		// Codifica cada caractere na entrada usando a tabela de códigos de Huffman
		for (char c : input.toCharArray()) {
			encodedString.append(huffmanCodes.get(c)); // cocatena os codigos de cada caractere de acordo com a chave c
		}

		return encodedString.toString();
	}

	public static String decode(String encodedString, Node raiz) {
		StringBuilder decodedString = new StringBuilder();
		Node current = raiz;

		// Decodifica a string codificada de acordo com a árvore de Huffman
		for (char bit : encodedString.toCharArray()) {
			if (bit == '0') {
				current = current.getLeft();
			} else {
				current = current.getRight();
			}

			// Se o nó atual for uma folha, adiciona o caractere decodificado à string
			// resultante
			if (current.getLeft() == null && current.getRight() == null) {
				decodedString.append(current.getCharacter()); // obtém o caractere associado ao nó
				current = raiz; // retorna para a raiz
			}
		}

		return decodedString.toString();
	}

}
