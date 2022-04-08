package tsp.projects.mehmin;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

/**
 * @author KARABAY TEKELI
 * Greedy
 */
public class Greedy
{
	private int length;
	private Problem problem;

	public Greedy (Evaluation evaluation)
	{
		this.problem = evaluation.getProblem();
		this.length = this.problem.getLength ();
	}

	/**
	 * Commence avec une ville aléatoire et sélectionne itérativement la ville plus proche
	 * @return Path
	 */
	public Path greedy() {
		int[] cities = new Path(length).getPath();
		for(int i = 0; i < length - 2; i++) {
			int min_indice = i + 1;
			double min = problem.getCoordinates(cities[i]).distance(problem.getCoordinates(cities[i+1]));
			for(int j = i + 2; j < length; j++) {
				double distance = problem.getCoordinates(cities[i]).distance(problem.getCoordinates(cities[j]));
				if(distance < min) {
					min = distance;
					min_indice = j;
				}
			}
			swap(cities, i + 1, min_indice);
		}

		return new Path(cities);
	}
	
	/**
	 * Commence avec la première ville du path passé en param et sélectionne itérativement la ville plus proche
	 * @param path
	 * @return Path
	 */
	public Path greedy(Path path) {
		int[] cities = path.getPath();
		for(int i = 0; i < length - 2; i++) {
			int min_indice = i + 1;
			double min = problem.getCoordinates(cities[i]).distance(problem.getCoordinates(cities[i+1]));
			for(int j = i + 2; j < length; j++) {
				double distance = problem.getCoordinates(cities[i]).distance(problem.getCoordinates(cities[j]));
				if(distance < min) {
					min = distance;
					min_indice = j;
				}
			}
			swap(cities, i + 1, min_indice);
		}

		return new Path(cities);
	}
	
	/**
	 * Commence avec la ville passée en param et sélectionne itérativement la ville plus proche
	 * @param start
	 * @return Path
	 */
	public Path greedy(int start) {
		int[] cities = new int[length];
		for(int i = start; i < length + start; i++)
			cities[i-start] = i%length;

		return greedy(new Path(cities));
	}
	
	private static void swap(int[] tab, int i, int j) {
		int temp = tab[i];
		tab[i] = tab[j];
		tab[j] = temp;
	}
	

}
