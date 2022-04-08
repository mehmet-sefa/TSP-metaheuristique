package tsp.projects.mehmin.hillclimbing;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.output.OutputWriter;

/**
 * @author KARABAY TEKELI
 * Hill Climbing pour les petits jeux de données
 */
public class SmallHillClimbing extends HillClimbing
{

	public SmallHillClimbing (Evaluation evaluation)
	{
		super(evaluation);
	}
	
	/**
	 * Hill Climbing pour les petits jeux de données
	 * @param path
	 * @return un chemin optimal
	 */
	public Path smallHillClimbing(Path path) {
		Path best_neighbour = new Path(getBestNeighbour(path));
		
		while(evaluation.evaluate(best_neighbour) < evaluation.evaluate(path)) {
			path = best_neighbour;
			best_neighbour = new Path(getBestNeighbour(path));
		}	
		
		return path;
	}
	
	/**
	 * Explore les voisins de path
	 * Garde le meilleur voisin obtenu avec un seul swap
	 * @param path: chemin pour lequel on cherche un voisin
	 * @return le voisin avec le meilleur score trouvé
	 */
	public int[] getBestNeighbour(Path path) {
		double best_score = evaluation.quickEvaluate(path);
		int[] path_clone = path.getPath().clone();
		int[] best_neighbour = path.getPath().clone();

	    for(int i = 0; i < length - 1; i++) {
	    	for(int j = i+1; j < length; j++) {
    			swap(path_clone, i, j);
	    		double neighbour_score = evaluation.quickEvaluate(new Path(path_clone));
	    		if(neighbour_score < best_score) {
	    			best_score = neighbour_score;
	    			swap(best_neighbour, i, j);
	    		}
		    	swap(path_clone, i, j);
	    	}
	    }
		return best_neighbour;
	}
	
	/**
	 * Explore les voisins de path
	 * Garde le meilleur voisin obtenu avec plusieurs swap continus
	 * @param path: chemin pour lequel on cherche un voisin
	 * @return le voisin avec le meilleur score trouvé
	 */
	public int[] getBestNeighbour2(Path path) {
		double best_score = evaluation.quickEvaluate(path);
		int[] best_neighbour = path.getPath().clone();

	    for(int i = 0; i < length - 1; i++) {
	    	for(int j = i+1; j < length; j++) {
    			swap(best_neighbour, i, j);
	    		double neighbour_score = evaluation.quickEvaluate(new Path(best_neighbour));
	    		if(neighbour_score < best_score) {
	    			best_score = neighbour_score;
	    		}else {
		    		swap(best_neighbour, i, j);
	    		}
	    	}
	    }
		return best_neighbour;
	}
}
