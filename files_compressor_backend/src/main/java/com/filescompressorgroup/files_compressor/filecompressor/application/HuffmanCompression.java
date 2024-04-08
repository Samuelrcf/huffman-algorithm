package com.filescompressorgroup.files_compressor.filecompressor.application;

import java.util.HashMap;

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
	    // Cria um array de Nodes
	    Node[] nodes = new Node[frequencyMap.size()];
	    int index = 0;
	    for (char c : frequencyMap.keySet()) {
	        nodes[index++] = new Node(c, frequencyMap.get(c));
	    }

	    // Ordena o array de Nodes usando Shell Sort
	    shellSort(nodes);

	    // Constrói a árvore de Huffman combinando os Nodes com as menores frequências
	    while (nodes.length > 1) {
	        // Remove os dois Nodes com as menores frequências
	        Node left = nodes[0];
	        Node right = nodes[1];
	        // Cria um novo Node com a soma das frequências
	        Node parent = new Node('\0', left.getFrequency() + right.getFrequency());
	        parent.setLeft(left);
	        parent.setRight(right);

	        // Cria um novo array com tamanho reduzido para armazenar os nós restantes
	        Node[] newNodes = new Node[nodes.length - 1];
	        newNodes[0] = parent; // Adiciona o nó pai na primeira posição
	        for (int i = 1; i < newNodes.length; i++) {
	            newNodes[i] = nodes[i + 1]; // Copia os nós restantes para o novo array
	        }
	        nodes = newNodes; // Atualiza o array de nós para o novo array reduzido

	        // Reordena o array
	        shellSort(nodes);
	    }

	    // Retorna a raiz da árvore de Huffman
	    return nodes[0];
	}


	// Implementação do algoritmo de ordenação Shell Sort
	private static void shellSort(Node[] nodes) {
	    int n = nodes.length;
	    Node[] temp = new Node[n]; // Array temporário para armazenar cópias dos nós

	    for (int i = 0; i < n; i++) {
	        temp[i] = nodes[i]; // Copia os nós para o array temporário
	    }

	    // Algoritmo Shell Sort
	    for (int gap = n / 2; gap > 0; gap /= 2) {
	        for (int i = gap; i < n; i++) {
	            Node tempNode = temp[i];
	            int j;
	            for (j = i; j >= gap && temp[j - gap].getFrequency() > tempNode.getFrequency(); j -= gap) {
	                temp[j] = temp[j - gap];
	            }
	            temp[j] = tempNode;
	        }
	    }

	    // Copia os nós ordenados de volta para o array original
	    for (int i = 0; i < n; i++) {
	        nodes[i] = temp[i];
	    }
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
