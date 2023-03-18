package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Curse {

	static int N, M, A;

	static int[][] trainings;

	static ArrayList<Integer>[] adj;

	public static void readInput() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("curse.in"));

		String[] line = br.readLine().split(" ");
		N = Integer.parseInt(line[0]);
		M = Integer.parseInt(line[1]);
		A = Integer.parseInt(line[2]);

		adj = new ArrayList[M + 1];

		for (int node = 1; node <= M; node++) {
			adj[node] = new ArrayList<>();
		}

		trainings = new int[A + 1][N + 1];

		for (int i = 1; i <= A; i++) {
			String[] training = br.readLine().split(" ");
			for (int j = 1; j <= N; j++) {
				trainings[i][j] = Integer.parseInt(training[j - 1]);
			}
		}

		br.close();
	}

	public static void writeOutput(ArrayList<Integer> res) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("curse.out"));
		for (Integer i : res) {
			bw.write(i + " ");
		}
		bw.write("\n");
		bw.close();
	}

	/* se compara antrenamentele 2 cate 2, pe fiecare pista pana la gasirea
	unei inegalitati (gasire relatie de ordine), adaugand muchia in graf */
	public static void genGraph() {
		for (int i = 1; i < A; i++) {
			for (int j = 1; j <= N; j++) {
				if (trainings[i][j] != trainings[i + 1][j]) {
					adj[trainings[i][j]].add(trainings[i + 1][j]);
					break;
				}
			}
		}
	}

	public static ArrayList<Integer> genTopsort() {
		ArrayList<Integer> topsort = new ArrayList<>();

		boolean[] used = new boolean[M + 1];

		// pentru fiecare nod
		for (int node = 1; node <= M; node++) {
			// daca nodul nu a fost vizitat, se porneste o parcurgere DFS
			if (!used[node]) {
				dfs(node, used, topsort);
			}
		}

		// rezultatul a fost obtinut in ordine inversa
		Collections.reverse(topsort);

		return topsort;
	}

	public static void dfs(int node, boolean[] used, ArrayList<Integer> topsort) {
		used[node] = true;

		for (Integer neigh : adj[node]) {
			if (!used[neigh]) {
				dfs(neigh, used, topsort);
			}
		}

		// se adauga nodul in sortarea topologica dupa terminarea vizitarii acestuia
		topsort.add(node);
	}

	public static ArrayList<Integer> curse() {
		genGraph();
		return genTopsort();
	}

	public static void main(String[] args) throws IOException {
		readInput();
		writeOutput(curse());
	}

}
