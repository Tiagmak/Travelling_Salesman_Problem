
// Exemplo de pesquisa em largura (BFS) num grafo nao dirigido
// (similar ao codigo feito na teorica - inclui calculo de distancias)

import java.io.*;
import java.util.*;

// Classe que representa um no
class Node {
    public LinkedList<Integer> adj; // Lista de adjacencias
    public boolean visited;         // Valor booleano que indica se foi visitado numa pesquisa
    public int distance;            // Distancia ao no origem da pesquisa

    Node() {
	adj = new LinkedList<Integer>();      //uma lista com os seus nos visinhos
    }
}

// Classe que representa um grafo
class Graph {
    int n;           // Numero de nos do grafo
    Node nodes[];    // Array para conter os nos

    Graph(int n) {                              //construtor do grafo
	this.n = n;
	nodes  = new Node[n+1]; // +1 se nos comecam em 1 ao inves de 0
	for (int i=1; i<=n; i++)
	    nodes[i] = new Node();                   //cria o no para todos os nos do grafo
    }

    public void addLink(int a, int b) {          //como este grafo é nao dirigido ele cria logo as duas direçoes 
	nodes[a].adj.add(b);
	nodes[b].adj.add(a);
    }

    // Algoritmo de pesquisa em largura
    public void bfs(int v) {
	LinkedList<Integer> q = new LinkedList<Integer>();                     //cria a lista para guardar os nos que vai visitar
	for (int i=1; i<=n; i++) nodes[i].visited = false;                     //ao meter aqui tudo a falso, permite correr o BFS varias vezes no mesmo grafo, ao fazer isso conseguimos começar uma vez em cada no e guardar as distancias de uns para os outros

	q.add(v);                                                    //adiciona o v à lista para depois no while o tirar e começar
	nodes[v].visited = true;                                     //ja o visitou mete a true
	nodes[v].distance = 0;                                       //como é a raiz a dist é zero

	while (q.size() > 0) {
	    int u = q.removeFirst();                                         //tira o no que esta na cabeça da lista
	    System.out.println(u + " [dist=" + nodes[u].distance + "]");
	    for (int w : nodes[u].adj)                                       //para todos os nos (w) que sao os nos visinhos a u 
		if (!nodes[w].visited) {                                         //ainda nao foi visto
		    q.add(w);                                                    //mete o na lista para depois o ver
		    nodes[w].visited  = true;
		    nodes[w].distance = nodes[u].distance + 1;                   //a distancia de (w) é a distancia do no pai mais 1
		}	    
	}
    }
}

public class BFS {
    public static void main(String args[]) {
	Scanner in = new Scanner(System.in);

	Graph g = new Graph(in.nextInt());
	int   e = in.nextInt();
	for (int i=0; i<e; i++) 
	    g.addLink(in.nextInt(), in.nextInt());

	// Pesquisa em largura a partir do no 1
	g.bfs(1);		
    }
}
