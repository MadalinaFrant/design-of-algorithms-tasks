package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Walsh {

	/* intoarce negatul numarului: pentru 1 intoarce 0, respectiv pentru 0 intoarce 1 */
	public static int neg(int i) {
		return (i == 1) ? 0 : 1;
	}

	/* verifica daca (x, y) se afla in dreptunghiul [ (tlx, tly), (drx, dry) ] */
	public static boolean in_rectangle(int x, int y, int tlx, int tly, int drx, int dry) {
		return (tlx <= x && x <= drx) && // x intre cele 2 laturi verticale
				(tly <= y && y <= dry); // y intre cele 2 laturi orizontale
	}

	/* cauta recursiv ce valoare se afla la (x, y), impartind matricea in 4 submatrici */
	public static int search_val(int x, int y, int tlx, int tly, int dim, int val) {
		if (dim == 1) {
			return val; // s-a ajuns la o matrice de dimensiune 1 => s-a ajuns la solutie
		}

		/* calculeaza noile "colturi" ale submatricilor */

		int drx = tlx + dim - 1;
		int dry = tly + dim - 1;

		int mx = (tlx + drx) >> 1;
		int my = (tly + dry) >> 1;

		/* submatricea are dimensiune de 2 ori mai mica */
		int new_dim = (dim >> 1); // new_dim = dim / 2

		// (x, y) este in stanga-sus
		if (in_rectangle(x, y, tlx, tly, mx, my)) {
			return search_val(x, y, tlx, tly, new_dim, val);
		}

		// (x, y) este in dreapta-sus
		if (in_rectangle(x, y, tlx, my + 1, mx, dry)) {
			return search_val(x, y, tlx, my + 1, new_dim, val);
		}

		// (x, y) este in stanga-jos
		if (in_rectangle(x, y, mx + 1, tly, drx, my)) {
			return search_val(x, y, mx + 1, tly, new_dim, val);
		}

		// (x, y) este in dreapta-jos; in acest caz valoarea cautata este negata
		if (in_rectangle(x, y, mx + 1, my + 1, drx, dry)) {
			return search_val(x, y, mx + 1, my + 1, new_dim, neg(val));
		}

		return -1; // nu exista solutie
	}

	public static int walsh(int n, int x, int y) {
		/* cautarea pleaca din coltul (1, 1) din stanga sus (de la matricea initiala) */
		return search_val(x, y, 1, 1, n, 0);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("walsh.in"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("walsh.out"));

		String[] line = br.readLine().split(" ");
		int n = Integer.parseInt(line[0]);
		int k = Integer.parseInt(line[1]);

		for (int i = 0; i < k; i++) {

			String[] x_y = br.readLine().split(" ");
			int x = Integer.parseInt(x_y[0]);
			int y = Integer.parseInt(x_y[1]);

			bw.write(walsh(n, x, y) + "\n");
		}

		br.close();
		bw.close();
	}

}