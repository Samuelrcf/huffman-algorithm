package com.filescompressorgroup.files_compressor.filecompressor.util;

import java.io.IOException;
import java.util.HashMap;

import com.filescompressorgroup.files_compressor.filecompressor.application.HuffmanCompression;
import com.filescompressorgroup.files_compressor.filecompressor.entities.Node;

public class FileEnconding {

	// método para codificar o arquivo de texto e retornar a raiz da árvore de Huffman
	public static Node encodeFile(String inputFilePath, String outputBinaryFilePath) throws IOException {
		// lê o conteúdo do arquivo para uma string
		String input = ReadingFiles.readFile(inputFilePath);

		// conta a frequência de cada caractere na entrada
		HashMap<Character, Integer> frequencyMap = HuffmanCompression.countFrequency(input);

		// constrói a árvore de Huffman
		Node root = HuffmanCompression.buildHuffmanTree(frequencyMap);

		// constrói a tabela de códigos de Huffman
		HashMap<Character, String> huffmanCodes = HuffmanCompression.buildHuffmanCodesStart(root);

		// codifica a entrada original
		String encodedString = HuffmanCompression.encode(input, huffmanCodes);

		// salva a sequência de bits codificada em um arquivo binário
		SavingFiles.saveToFileBinary(encodedString, outputBinaryFilePath);

		// retorna a raiz da árvore de Huffman
		return root;
	}

	public static void decodeFile(String inputBinaryFilePath, String outputFilePath, Node root) throws IOException {
		// lê o conteúdo do arquivo binário
		String encodedString = ReadingFiles.readFileBinary(inputBinaryFilePath);

		// decodifica a sequência de bits usando o algoritmo de Huffman
		String decodedString = HuffmanCompression.decode(encodedString, root);

		// salva o texto decodificado em um arquivo de texto
		SavingFiles.saveToFile(decodedString, outputFilePath);

		System.out.println("Arquivo decodificado salvo em: " + outputFilePath);
	}
}
