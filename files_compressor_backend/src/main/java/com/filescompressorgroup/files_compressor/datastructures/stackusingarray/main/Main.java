package com.filescompressorgroup.files_compressor.datastructures.stackusingarray.main;

import com.filescompressorgroup.files_compressor.datastructures.stackusingarray.MyStackUsingArray;

public class Main {
    public static void main(String[] args) {
        // Demonstração do uso da Stack.
        System.out.println("\nDemonstração do uso da Stack: ");
        MyStackUsingArray<Integer> numeros = new MyStackUsingArray<>(5);

        System.out.println("\nA pilha está vazia? " + numeros.isEmpty());

        numeros.push(2);
        numeros.push(5);
        numeros.push(9);
        numeros.push(10);

        System.out.println("\nMostrando os 4 elementos");
        numeros.show();

        System.out.println("\nO elemento do topo é: " + numeros.peek() + "\n");

        System.out.println("\nA pilha está cheia? " + numeros.isFull() + "\n");

        numeros.push(18);

        System.out.println("\nA pilha está cheia? " + numeros.isFull() + "\n");

        numeros.pop();

        System.out.println("\nA pilha está cheia? " + numeros.isFull() + "\n");

        System.out.println("\nMostrando todos os elementos (4 elementos), depois de ter removido o do topo");
        numeros.show();

        System.out.println("\nDemonstração do uso da Queue: \n");
    }
}
