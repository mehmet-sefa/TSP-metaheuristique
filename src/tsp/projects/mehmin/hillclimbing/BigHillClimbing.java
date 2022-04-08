package tsp.projects.mehmin.hillclimbing;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.output.OutputWriter;

/**
 * @author KARABAY TEKELI
 * Hill Climbing pour les gros jeux de données
 */
public class BigHillClimbing extends HillClimbing
{

	public BigHillClimbing (Evaluation evaluation){
		super(evaluation);
	}

	/**
	 * Hill Climbing pour les gros jeux de données
	 * @param path
	 * @param max_evaluations
	 * @return un chemin satisfaisant
	 */
	public Path bigHillClimbing(Path path, int max_evaluations) {
		Path best_neighbour = new Path(getApproxBestNeighbour(path, (int)length / 1000));

		int n_evaluations = 0;
		while(evaluation.evaluate(best_neighbour) < evaluation.evaluate(path) && n_evaluations < max_evaluations) {
			path = best_neighbour;
			best_neighbour = new Path(getApproxBestNeighbour(path, (int)length / 1000));
			n_evaluations++;
		}

		return path;
	}
	
	/**
	 * Explore les voisins de path avec un pas défini
	 * Garde le meilleur voisin obtenu avec plusieurs swap continus
	 * @param path: chemin pour lequel on cherche un voisin
	 * @param pas: pas d'exploration, explore tous les voisins quand pas == 1
	 * @return le voisin avec le meilleur score trouvé
	 */
	public int[] getApproxBestNeighbour(Path path, int pas) {
		double best_score = evaluation.quickEvaluate(path);
		int[] best_neighbour = path.getPath().clone();
		int[] path_clone = path.getPath().clone();

	    for(int i = 0; i < length - pas; i+=pas) {
	    	for(int j = i+1; j < i + pas; j++) {
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
}
