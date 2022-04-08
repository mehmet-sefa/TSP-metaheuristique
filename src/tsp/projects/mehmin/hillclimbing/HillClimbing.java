package tsp.projects.mehmin.hillclimbing;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.output.OutputWriter;

/**
 * @author KARABAY TEKELI
 * Hill Climbing
 */
public class HillClimbing
{
	protected int length;
	protected Evaluation evaluation;
	protected Problem problem;

	public HillClimbing (Evaluation evaluation)
	{
		this.evaluation = evaluation;
		this.problem = evaluation.getProblem();
		this.length = this.problem.getLength ();
	}

	/**
	 * Heuristique Hill Climbing
	 * @param path
	 * @return un chemin optimal
	 */
	public Path hillClimbing(Path path) {
		if(length < 1000)
			return new SmallHillClimbing(evaluation).smallHillClimbing(path);
		else
			return new BigHillClimbing(evaluation).bigHillClimbing(path, 1000);
	}
	
	protected void swap(int[] tab, int i, int j) {
		int temp = tab[i];
		tab[i] = tab[j];
		tab[j] = temp;
	}
}
