package tsp.projects.mehmin;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

/**
 * @author KARABAY TEKELI
 * 2-opt, heuristique qui supprime les arêtes qui se croisent
 */
public class TwoOpt
{
	private int length;
	private Problem problem;

	public TwoOpt (Evaluation evaluation)
	{
		this.problem = evaluation.getProblem();
		this.length = this.problem.getLength ();
	}

	/**
	 * 2-opt
	 * @param path
	 * @return
	 */
	public Path twoOpt(Path path) {
		return twoOpt(path, 300);
	}
	
	/**
	 * 2-opt
	 * @param path
	 * @return
	 */
	protected Path twoOpt(Path path, int max) {
		int[] cities = path.getPath();
		boolean amelioration = true;
		int cpt_max = 0;
		// Tant qu'il y a de l'amélioration et qu'on a pas atteint le nb d'itérations max
		while(amelioration && cpt_max < max) {
			amelioration = false;
			int i = 0;
			int j;
			while(i < this.length-3) {
				j = i+2;
				while(j < this.length-1) {
					// Si le swap vaut le coup, on swap
					if(better_to_swap(cities, i, j)) {
						swap(cities, i+1, j);
						amelioration = true;
					}
					j++;
				}
				i++;
			}
			cpt_max++;
		}
		return new Path(cities);
	}
	
	/**
	 * évalue la distance si après échange de deux villes
	 * @param path
	 * @param city1 ville 1 qu'on souheterait intervertir
	 * @param city2 ville 2 qu'on souheterait intervertir, doit être différent de city1 - 1 et city2 + 1
	 * @return vrai si intervertir les villes city1 + 1 et city2 vaut le coup
	 */
	private boolean better_to_swap(int[] cities, int city1, int city2) {
		double distance1 = this.problem.getCoordinates(cities[city1]).distance(this.problem.getCoordinates(cities[city1+1]));
		double distance2 = this.problem.getCoordinates(cities[city2]).distance(this.problem.getCoordinates(cities[city2+1]));
		double distance3 = this.problem.getCoordinates(cities[city1]).distance(this.problem.getCoordinates(cities[city2]));
		double distance4 = this.problem.getCoordinates(cities[city1+1]).distance(this.problem.getCoordinates(cities[city2+1]));

		return  distance1 + distance2 > distance3 + distance4;
	}
	
	private static void swap(int[] tab, int i, int j) {
		int temp = tab[i];
		tab[i] = tab[j];
		tab[j] = temp;
	}
}
