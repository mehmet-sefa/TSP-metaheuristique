package tsp.projects.mehmin;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import java.util.Random; 

/**
 * @author KARABAY TEKELI
 * Simulated Annealing
 */
public class SimulatedAnnealing
{
	protected Evaluation evaluation;
	protected int length;
	protected Random rand;
	protected Problem problem;
	protected int kmax = 10000;

	public SimulatedAnnealing (Evaluation evaluation)
	{
		this.evaluation = evaluation;
		this.problem = evaluation.getProblem();
		this.length = this.problem.getLength ();
		rand = new Random();
	}
	
	/**
	 * Recuit simulé
	 * @param path
	 * @return
	 */
	public Path simulatedAnnealing(Path path) {
		int k = 0;
		Path s = path;
		double e = this.evaluation.evaluate(path);
		// Tant qu'on a pas atteint le nb d'itérations max
		while(k < kmax) {
			// On créé un chemin voisin au meilleur
			Path sn;
			sn = random_neighbour(s, 1);
			//sn = random_neighbour_with_range(s, 1, this.length/40);
			//sn = random_neighbour_with_zone_swap(s, 1, this.length/40);
			double en = this.evaluation.evaluate(sn);
			// Si ce nouveau chemin est meilleur ou que le critère de température le permet
			if(en < e || rand.nextDouble() < P(Math.abs(en - e), k/kmax)) {
				// Le meilleur chemin devient le voisin
				s = sn;
				e = en;
			}
			k++;
		}
		return new Path(s);
	}
	
	// Fonction de probabilité dépendant de la variation d'énergie et de la température
	private double P(double loss, double temp) {
		return Math.exp(-(loss/temp));
	}
	
	/**
	 * 
	 * @param cities 
	 * @param randomize_n nombre de fois qu'on randomize
	 * @return un voisin aléatoire de cities
	 */
	private Path random_neighbour(Path path, int randomize_n) {
		int[] neighbour = path.getPath().clone();
		for(int i = 0; i < randomize_n; i++) {
			int city1 = rand.nextInt(this.length);
			int city2 = rand.nextInt(this.length);
			swap(neighbour, city1, city2);
		}
		return new Path(neighbour);
	}
	
	/**
	 * swap randomize_n fois des villes dans une certaine range
	 * @param cities 
	 * @param randomize_n nombre de fois qu'on randomize
	 * @return un voisin aléatoire de cities
	 */
	private Path random_neighbour_with_range(Path path, int randomize_n, int range) {
		int[] neighbour = path.getPath().clone();
		for(int i = 0; i < randomize_n; i++) {
			int city1 = rand.nextInt(this.length);
			int city2 = (city1 + rand.nextInt(range) + 1)%this.length;
			swap(neighbour, city1, city2);
		}
		return new Path(neighbour);
	}
	
	/**
	 * Voisin avec swap de zones adjacentes aléatoire
	 * @param cities 
	 * @param randomize_n nombre de fois qu'on randomize
	 * @return un voisin aléatoire de cities
	 */
	private Path random_neighbour_with_zone_swap(Path path, int randomize_n, int range) {
		int[] neighbour = path.getPath().clone();
		for(int i = 0; i < randomize_n; i++) {
			int city1 = rand.nextInt(this.length);
			swap_adjacent_zone(neighbour, city1, range);
		}
		return new Path(neighbour);
	}
	
	private void swap(int[] tab, int i, int j) {
		int temp = tab[i];
		tab[i] = tab[j];
		tab[j] = temp;
	}
	
	/**
	 * swap deux zones adjacentes
	 * @param tab
	 * @param start
	 * @param range
	 */
	private void swap_adjacent_zone(int[] tab, int start, int range) {
		int[] zone1 = new int[range];
		int j = 0;
		int i;
		for(i = start; i < start + range; i++) {
			zone1[j] = tab[i%this.length];
			j++;
		}
		for(i = start; i < start + range; i++)
			tab[i%this.length] = tab[(i+range)%this.length];
		j=0;
		for(i = start; i < start + range; i++) {
			tab[(i+range)%this.length] = zone1[j];
			j++;
		}
	}
}
