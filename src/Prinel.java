package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Prinel {

	static int n, k;
	static int[] target;
	static int[] p;

	public static void readInput() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("prinel.in"));

		String[] line = br.readLine().split(" ");
		n = Integer.parseInt(line[0]);
		k = Integer.parseInt(line[1]);

		target = new int[n];
		p = new int[n];

		String[] targetStr = br.readLine().split(" ");
		for (int i = 0; i < n; i++) {
			target[i] = Integer.parseInt(targetStr[i]);
		}

		String[] pStr = br.readLine().split(" ");
		for (int i = 0; i < n; i++) {
			p[i] = Integer.parseInt(pStr[i]);
		}

		br.close();
	}

	public static void writeOutput(int sol) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("prinel.out"));
		bw.write(sol + "\n");
		bw.close();
	}

	/* intoarce maximul din vectorul dat */
	public static int maxInArray(int[] v) {
		int max = 0;
		for (int x : v) {
			if (x > max) {
				max = x;
			}
		}

		return max;
	}

	/* intoarce toti divizorii numarului dat, sub forma unui Set
	(pentru a trece prin fiecare o singura data) */
	public static Set<Integer> genDivisors(int a) {

		/* TreeSet pentru a fi adaugati in ordine, o singura data */
		Set<Integer> divs = new TreeSet<>();

		/* se adauga din ambele capete */
		for (int d = 1; d <= Math.sqrt(a); d++) {
			if (a % d == 0) {
				divs.add(d);
				divs.add(a / d);
			}
		}

		return divs;
	}

	public static int[] genNrOp() {

		/* valoarea maxima din vectorul ce trebuie obtinut */
		int maxVal = maxInArray(target);

		int[] dp = new int[maxVal + 1]; // dp[i] = nr maxim de operatii de la 1 la i
		/* se doreste nr minim de operatii => */
		Arrays.fill(dp, maxVal); // se umple vectorul cu nr maxim operatii + 1

		/* pt valoarea maxima nr maxim de operatii este maxVal - 1 (doar adunari de 1) */
		dp[1] = 0; // caz de baza; de la 1 la 1 => nicio operatie
		dp[2] = 1; // caz de baza; de la 1 la 2 => o operatie

		// caz general
		for (int a = 2; a <= maxVal; a++) {
			for (Integer x : genDivisors(a)) {
				if ((a + x) > maxVal) {
					break;
				}
				dp[a + x] = Math.min(dp[a + x], dp[a] + 1);
			}
		}

		int[] nrOp = new int[n];

		for (int i = 0; i < n; i++) {
			nrOp[i] = dp[target[i]];
		}

		return nrOp;
	}

	public static int genPoints(int[] nrOp) {

		int[] dp = new int[k + 1];
		/* se doreste nr maxim de operatii => */
		Arrays.fill(dp, 0); // se umple vectorul cu 0

		// caz general
		for (int i = 1; i <= n; i++) {
			for (int op = k; op >= 0; op--) {

				if (op - nrOp[i - 1] >= 0) {
					dp[op] = Math.max(dp[op], dp[op - nrOp[i - 1]] + p[i - 1]);
				}

			}
		}
		return dp[k];
	}

	public static int prinel() {
		return genPoints(genNrOp());
	}

	public static void main(String[] args) throws IOException {
		readInput();
		writeOutput(prinel());
	}

}