package com.filescompressorgroup.files_compressor.filecompressor.util;

import java.io.IOException;
import java.util.HashMap;

import com.filescompressorgroup.files_compressor.filecompressor.application.HuffmanCompression;
import com.filescompressorgroup.files_compressor.filecompressor.entities.Node;

public class FileEnconding {

	// Método para codificar o arquivo de texto e retornar a raiz da árvore de
	// Huffman
	public static Node encodeFile(String inputFilePath, String outputBinaryFilePath) throws IOException {
		// Lê o conteúdo do arquivo para uma string
		String input = ReadingFiles.readFile(inputFilePath);

		// Conta a frequência de cada caractere na entrada
		HashMap<Character, Integer> frequencyMap = HuffmanCompression.countFrequency(input);

		// Constrói a árvore de Huffman
		Node root = HuffmanCompression.buildHuffmanTree(frequencyMap);

		// Constrói a tabela de códigos de Huffman
		HashMap<Character, String> huffmanCodes = HuffmanCompression.buildHuffmanCodes(root);

		// Codifica a entrada original
		String encodedString = HuffmanCompression.encode(input, huffmanCodes);

		// Salva a sequência de bits codificada em um arquivo binário
		SavingFiles.saveToFileBinary(encodedString, outputBinaryFilePath);

		System.out.println("Arquivo binário codificado salvo em: " + outputBinaryFilePath);

		// Retorna a raiz da árvore de Huffman
		return root;
	}

	public static void decodeFile(String inputBinaryFilePath, String outputFilePath, Node root) throws IOException {
		// Lê o conteúdo do arquivo binário
		String encodedString = ReadingFiles.readFileBinary(inputBinaryFilePath);

		// Decodifica a sequência de bits usando o algoritmo de Huffman
		String decodedString = HuffmanCompression.decode(encodedString, root);

		// Salva o texto decodificado em um arquivo de texto
		SavingFiles.saveToFile(decodedString, outputFilePath);

		System.out.println("Arquivo decodificado salvo em: " + outputFilePath);
	}
}
