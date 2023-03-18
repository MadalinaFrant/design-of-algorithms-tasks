package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;

public class Beamdrone {

	static int N, M; // dimensiuni pista

	static char[][] track; // pista

	static int xi, yi; // coordonate initiale
	static int xf, yf; // coordonate finale

	/* directii posibile */
	static Node toLeft = new Node(0, -1);
	static Node toRight = new Node(0, +1);
	static Node down = new Node(+1, 0);
	static Node up = new Node(-1, 0);

	static Node[] directions = {toLeft, toRight, down, up};

	/* un nod reprezentat de coordonatele x, y */
	public static class Node {
		public int x;
		public int y;

		Node(int _x, int _y) {
			x = _x;
			y = _y;
		}
	}

	/* coada folosita pentru parcurgerea nodurilor va contine elemente de tip QueueNode */
	public static class QueueNode implements Comparable<QueueNode> {
		public Node node; // nodul in sine
		public Node prevDir; // directia pe care se intra in nod
		public int cost; // costul pana la acest nod

		QueueNode(Node _node, Node _prevDir, int _cost) {
			node = _node;
			prevDir = _prevDir;
			cost = _cost;
		}

		/* elementele vor fi adaugate in coada in functie de costul pana la acestea */
		public int compareTo(QueueNode p) {
			return Integer.compare(cost, p.cost);
		}
	}

	public static void readInput() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("beamdrone.in"));

		String[] line = br.readLine().split(" ");
		N = Integer.parseInt(line[0]);
		M = Integer.parseInt(line[1]);

		track = new char[N + 1][M + 1]; // se va indexa de la 1

		String[] coord = br.readLine().split(" ");
		xi = Integer.parseInt(coord[0]) + 1;
		yi = Integer.parseInt(coord[1]) + 1;
		xf = Integer.parseInt(coord[2]) + 1;
		yf = Integer.parseInt(coord[3]) + 1;

		for (int i = 1; i <= N; i++) {
			String chars = br.readLine();
			for (int j = 1; j <= M; j++) {
				track[i][j] = chars.charAt(j - 1);
			}
		}

		br.close();
	}

	public static void writeOutput(int sol) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("beamdrone.out"));
		bw.write(sol + "\n");
		bw.close();
	}

	public static boolean isInTrack(Node n) {
		// verifica daca nodul nu iese din dimensiunile pistei
		if ((1 <= n.x && n.x <= N) && (1 <= n.y && n.y <= M)) {
			return track[n.x][n.y] == '.'; // si daca nu este un perete
		}

		return false;
	}

	/* genereaza costul miscarii in functie de directia precedenta:
	o miscare perpendiculara are costul 1, respectiv una liniara costul 0 */
	public static int genCost(Node prevDir, Node dir) {

		if (prevDir == down || prevDir == up) {
			if (dir == toLeft || dir == toRight) {
				return 1;
			}
		}

		if (prevDir == toLeft || prevDir == toRight) {
			if (dir == down || dir == up) {
				return 1;
			}
		}

		return 0;
	}

	/* transforma o directie (de tip Node) intr-un int pentru a reprezenta un indice in matricea
	de costuri (directia pe care s-a intrat in nod): 0 - orizontala; 1 - verticala */
	public static int dirToInt(Node dir) {

		if (dir == toLeft || dir == toRight) {
			return 0;
		}
		if (dir == down || dir == up) {
			return 1;
		}

		return -1;
	}

	public static int beamdrone() {
		/*
		c[x][y][0] = costul pana la nodul dat de coordonatele x, y, intrand in acesta pe orizontala
		c[x][y][1] = costul pana la nodul dat de coordonatele x, y, intrand in acesta pe verticala
		*/
		int[][][] c = new int[N + 1][M + 1][2];

		/* Se initializeaza costurile la valoarea maxima a unui int
		(se doreste gasirea celui mai mic cost) */
		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= M; j++) {
				c[i][j][0] = Integer.MAX_VALUE;
				c[i][j][1] = Integer.MAX_VALUE;
			}
		}

		PriorityQueue<QueueNode> pq = new PriorityQueue<>();

		/* Se insereaza nodul de plecare in coada, cu costul 0, intrand in acesta pe toate
		cele 4 directii */
		for (Node dir : directions) {
			c[xi][yi][dirToInt(dir)] = 0;
			pq.add(new QueueNode(new Node(xi, yi), dir, 0));
		}

		while (!pq.isEmpty()) {
			QueueNode q = pq.poll(); // se scoate capul cozii
			Node node = q.node;
			Node prevDir = q.prevDir;
			int cost = q.cost;

			if (xf == node.x && yf == node.y) { // s-a ajuns la nodul destinatie
				return cost;
			}

			/* Nu se tine cont de elementele din coada cu costul mai mare decat cel tinut minte in
			matricea de costuri (cel minim), deoarece se doreste costul cat mai mic */
			if (cost > c[node.x][node.y][dirToInt(prevDir)]) {
				continue;
			}

			// pentru toate cele 4 directii posibile
			for (Node dir : directions) {
				// se genereaza noul nod in functie de coordonatele directiei
				Node newNode = new Node(node.x + dir.x, node.y + dir.y);
				if (isInTrack(newNode)) {
					// se calculeaza costul miscarii
					int movCost = genCost(prevDir, dir);
					/* Daca noul cost pana la nod este mai mic decat cel salvat, se
					actualizeaza in matricea de costuri si se adauga nodul in coada */
					int newCost = c[node.x][node.y][dirToInt(prevDir)] + movCost;
					if (newCost < c[newNode.x][newNode.y][dirToInt(dir)]) {
						c[newNode.x][newNode.y][dirToInt(dir)] = newCost;
						pq.add(new QueueNode(newNode, dir, newCost));
					}
				}
			}

		}

		return -1; // nu s-a putut ajunge la destinatie
	}

	public static void main(String[] args) throws IOException {
		readInput();
		writeOutput(beamdrone());
	}

}
