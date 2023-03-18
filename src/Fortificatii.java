package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Fortificatii {

	static int N, M, K;

	static int B; // nr. localitati barbare
	static ArrayList<Integer> barbars; // lista localitati barbare

	/* adj[node] = lista de adiacenta a nodului node; contine elemente de tip
	Pair(destination, cost) cu semnificatia: nodul node are muchia de cost cost
	catre nodul destination */
	static ArrayList<Pair>[] adj;

	public static class Pair implements Comparable<Pair> {
		public int destination;
		public int cost;

		Pair(int _destination, int _cost) {
			destination = _destination;
			cost = _cost;
		}

		/* elementele vor fi adaugate in coada in functie de costul pana la acestea */
		public int compareTo(Pair rhs) {
			return Integer.compare(cost, rhs.cost);
		}
	}

	/* structura folosita pentru a stoca distantele, cat si vectorul de parinti
	folosind algoritmul Dijkstra */
	public static class DijkstraResult {
		ArrayList<Integer> d;
		ArrayList<Integer> p;

		DijkstraResult(ArrayList<Integer> _d, ArrayList<Integer> _p) {
			d = _d;
			p = _p;
		}
	}

	public static void readInput() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("fortificatii.in"));

		String[] line = br.readLine().split(" ");
		N = Integer.parseInt(line[0]);
		M = Integer.parseInt(line[1]);
		K = Integer.parseInt(line[2]);

		String[] locBarbars = br.readLine().split(" ");
		B = Integer.parseInt(locBarbars[0]);
		barbars = new ArrayList<>();
		for (int i = 1; i <= B; i++) {
			barbars.add(Integer.parseInt(locBarbars[i]));
		}

		adj = new ArrayList[N + 1];
		for (int i = 1; i <= N; i++) {
			adj[i] = new ArrayList<>();
		}

		for (int i = 1; i <= M; i++) {
			String[] route = br.readLine().split(" ");
			int x = Integer.parseInt(route[0]);
			int y = Integer.parseInt(route[1]);
			int t = Integer.parseInt(route[2]);
			// graf neorientat => se adauga muchia in ambele directii
			adj[x].add(new Pair(y, t));
			adj[y].add(new Pair(x, t));
		}

		br.close();
	}

	public static void writeOutput(int sol) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("fortificatii.out"));
		bw.write(sol + "\n");
		bw.close();
	}

	/* calculeaza distantele minime de la nodul capitala (1) la toate celelalte noduri */
	public static DijkstraResult dijkstra() {
		// d[node] = distanta minima a unui drum de la 1 la nodul node
		ArrayList<Integer> d = new ArrayList<>(); // vector de distante
		// p[node] = parintele nodului node
		ArrayList<Integer> p = new ArrayList<>(); // vector de parinti

		/* Se initializeaza distantele la valoarea maxima a unui int
		(se doreste gasirea celui mai mic cost) si parintii la 0 */
		for (int i = 0; i <= N; i++) {
			d.add(Integer.MAX_VALUE);
			p.add(0);
		}

		PriorityQueue<Pair> pq = new PriorityQueue<>();

		// Se insereaza nodul de plecare (capitala 1) in coada si i se seteaza distanta la 0
		d.set(1, 0);
		pq.add(new Pair(1, 0));

		while (!pq.isEmpty()) {
			Pair q = pq.poll(); // se scoate capul cozii
			int node = q.destination;
			int cost = q.cost;

			/* Nu se tine cont de elementele din coada cu distanta mai mare decat cea tinuta
			minte in vectorul de distante (cea minima), deoarece se doreste distanta cat mai
			mica */
			if (cost > d.get(node)) {
				continue;
			}

			// Se parcurg toti vecinii nodului
			for (Pair e : adj[node]) {
				int neigh = e.destination;
				int w = e.cost;

				/* Daca noua distanta pana la nod este mai mica decat cea salvata, se
				actualizeaza in vectorul de distante si in cel de parinti si se adauga
				nodul in coada */
				if (d.get(node) + w < d.get(neigh)) {
					d.set(neigh, d.get(node) + w);
					p.set(neigh, node);
					pq.add(new Pair(neigh, d.get(neigh)));
				}
			}

		}

		return new DijkstraResult(d, p);
	}

	/* cauta nodul cu destinatia dest in lista de adiacenta a nodului node
	(in nodurile vecine acestuia) */
	public static Pair getNodeWithDest(int dest, int node) {
		for (Pair p : adj[node]) {
			if (p.destination == dest) {
				return p;
			}
		}

		return null;
	}

	/* intoarce nodul barbar cu costul minim pana la capitala (nodul 1) */
	public static int getBarbarsMinCostNode(ArrayList<Integer> d) {
		int minCost = Integer.MAX_VALUE;
		int minCostNode = 0;

		for (int node = 1; node <= N; node++) {
			if (barbars.contains(node)) {
				if (minCost > d.get(node)) {
					minCost = d.get(node);
					minCostNode = node;
				}
			}
		}

		return minCostNode;
	}

	/* adauga o fortificatie pe muchia cu cel mai mic cost de la un nod
	barbar la capitala (nodul 1) */
	public static void addFortification(DijkstraResult dij) {
		int node = getBarbarsMinCostNode(dij.d);

		/* graf neorientat => se adauga pe ambele directii */
		Pair edgeFromNode = getNodeWithDest(dij.p.get(node), node);
		if (edgeFromNode != null) {
			edgeFromNode.cost++;
		}
		Pair edgeToNode = getNodeWithDest(node, dij.p.get(node));
		if (edgeToNode != null) {
			edgeToNode.cost++;
		}

	}

	public static int fortificatii() {
		for (int i = 1; i <= K; i++) {
			addFortification(dijkstra());
		}

		DijkstraResult dij = dijkstra();
		int minCost = Integer.MAX_VALUE;
		for (int node = 1; node <= N; node++) {
			if (barbars.contains(node)) {
				minCost = Math.min(minCost, dij.d.get(node));
			}
		}

		return minCost;
	}

	public static void main(String[] args) throws IOException {
		readInput();
		writeOutput(fortificatii());
	}

}
