package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Statistics {

	static int n;
	static String[] words;

	public static void readInput() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("statistics.in"));

		n = Integer.parseInt(br.readLine());
		words = new String[n];

		for (int i = 0; i < n; i++) {
			words[i] = br.readLine();
		}

		br.close();
	}

	public static void writeOutput(int sol) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("statistics.out"));
		bw.write(sol + "\n");
		bw.close();
	}

	/* calculeaza numarul de ocurente al caracterului dat in cuvantul dat */
	public static int nrOcc(char c, String word) {
		int count = 0;

		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == c) {
				count++;
			}
		}

		return count;
	}

	/* calculeaza dominanta caracterului =
	nr ocurente litera cautata - nr ocurente celelalte litere din cuvant */
	public static int charDominance(char c, String word) {
		int nrOccChar = nrOcc(c, word);
		int nrOccOther = word.length() - nrOccChar;
		return nrOccChar - nrOccOther;
	}

	/* un caracter este dominant daca numarul sau de ocurente este mai mare
	decat jumatate din lungimea cuvantului */
	public static boolean isDominant(char c, String word) {
		return nrOcc(c, word) > (word.length() / 2);
	}

	/* intoarce toate caracterele din lista de cuvinte sub forma unui Set
	(pentru a trece prin fiecare o singura data) */
	public static Set<Character> getChars() {

		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(word);
		}

		Set<Character> characters = new HashSet<>();
		for (char c : sb.toString().toCharArray()) {
			characters.add(c);
		}

		return characters;
	}

	public static int statistics() {
		/* in maxCount se va retine numarul maxim de cuvinte ce pot fi concatenate,
		dupa trecerea prin fiecare litera din subsiruri */
		int maxCount = -1;

		/* pentru fiecare caracter */
		for (char c : getChars()) {
			/* sorteaza cuvintele descrescator in functie de dominanta caracterului curent */
			Arrays.sort(words, (w1, w2) ->
					Integer.compare(charDominance(c, w2), charDominance(c, w1)));

			int count = 0;
			StringBuilder res = new StringBuilder();

			/* se concateneaza cuvintele pana cand caracterul nu mai este dominant
			in sirul format */
			for (String w : words) {
				res.append(w);

				if (isDominant(c, res.toString())) {
					count++;
				} else {
					break;
				}
			}
			if (count > maxCount) {
				maxCount = count;
			}
		}
		return maxCount;
	}

	public static void main(String[] args) throws IOException {
		readInput();
		writeOutput(statistics());
	}

}