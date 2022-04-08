package tsp.projects.mehmin.hillclimbing;

import java.util.Random;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.output.OutputWriter;

/**
 * @author KARABAY TEKELI
 * Hill Climbing
 */
public class StochasticHillClimbing extends HillClimbing
{
	Random rand;
	public StochasticHillClimbing (Evaluation evaluation)
	{
		super(evaluation);
		rand = new Random();
	}
	
	/**
	 * Hill Climbing qui sélectionne un meilleur voisin à chaque itération
	 * @param path
	 * @return un chemin optimal
	 */
	public Path stochasticHillClimbing(Path path) {
		
		Path best_neighbour = new Path(getRandomBestNeighbour(path));

		while(evaluation.evaluate(best_neighbour) < evaluation.evaluate(path)) {
			path = best_neighbour;
			best_neighbour = new Path(getRandomBestNeighbour(path));
		}	
		
		return path;
	}
	
	/**
	 * Hill Climbing qui sélectionne un meilleur voisin à chaque itération
	 * @param path
	 * @return un chemin optimal
	 */
	public Path stochasticSteepestHillClimbing(Path path) {
		
		Path best_neighbour = new Path(getRandomSteepestBestNeighbour(path));

		while(evaluation.evaluate(best_neighbour) < evaluation.evaluate(path)) {
			path = best_neighbour;
			best_neighbour = new Path(getRandomSteepestBestNeighbour(path));
		}	
		
		return path;
	}
	
	/**
	 * Explore les voisins de path
	 * Garde un des meilleurs voisins obtenus au hasard
	 * @param path: chemin pour lequel on cherche un voisin
	 * @return un voisin au hasard avec un meilleur score
	 */
	public int[] getRandomBestNeighbour(Path path) {
		double path_score = evaluation.quickEvaluate(path);
		int[] path_clone = path.getPath().clone();
		int[][] better_neighbours = new int[length*length][2];
		int k = 0;
		
		// On cherche toutes les pentes qui montent
	    for(int i = 0; i < this.length - 1; i++) {
	    	for(int j = i+1; j < this.length; j++) {
    			swap(path_clone, i, j);
	    		double neighbour_score = evaluation.quickEvaluate(new Path(path_clone));
		    	swap(path_clone, i, j);
	    		if(neighbour_score < path_score) {
	    			better_neighbours[k][0] = i;
	    			better_neighbours[k][1] = j;
	    			k++;
	    		}
	    	}
	    }
	    
	    // Si on en a trouvé au moins un, on en tire un au hasard
	    if(k!=0) {
	    	int r = rand.nextInt(k);
	    	swap(path_clone, better_neighbours[r][0], better_neighbours[r][1]);
	    }
	    
		return path_clone;
	}
	
	/**
	 * Explore les voisins de path
	 * Garde un des meilleurs voisins obtenus au hasard
	 * @param path: chemin pour lequel on cherche un voisin
	 * @return un voisin au hasard avec un meilleur score avec plus
	 * de probabilité de choisir la pente la plus aiguë
	 */
	public int[] getRandomSteepestBestNeighbour(Path path) {
		double path_score = evaluation.quickEvaluate(path);
		int[] path_clone = path.getPath().clone();
		int[][] better_neighbours = new int[length*length][2];
		double[] cost = new double[length*length];
		int sum = 0;
		int k = 0;
		
		// On cherche toutes les pentes qui montent
	    for(int i = 0; i < length - 1; i++) {
	    	for(int j = i+1; j < length; j++) {
    			swap(path_clone, i, j);
	    		double neighbour_score = evaluation.quickEvaluate(new Path(path_clone));
	    		if(neighbour_score < path_score) {
	    			better_neighbours[k][0] = i;
	    			better_neighbours[k][1] = j;
	    			cost[k] = 1/neighbour_score;
	    			sum+= cost[k];
	    			k++;
	    		}
		    	swap(path_clone, i, j);
	    	}
	    }
	    // On calcule le tableau de probabilités de sélection
	    for(int i = 0; i < k; i++)
    		cost[i] = cost[i] / sum;
	    
	    // On en choisit un au hasard 
	    if(k!=0) {
	    	int r = pickRandomWithProbability(cost);
	    	swap(path_clone, better_neighbours[r][0], better_neighbours[r][1]);
	    }
	    
		return path_clone;
	}
	
	/**
	 * tirage avec probabilités
	 * @param probabilities
	 * @return
	 */
	int pickRandomWithProbability(double[] probabilities) {
		double tirage = rand.nextDouble();

		int i = 0;
		while(i < probabilities.length) {
			tirage -= probabilities[i];
			if(tirage < 0)
				return i;
			i++;
		}
		return probabilities.length - 1;
	}
}
