package tsp.projects.mehmin;

import java.util.Random;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.projects.mehmin.hillclimbing.HillClimbing;

/**
 * @author KARABAY TEKELI
 * GRASP
 */
public class GRASP
{
	private HillClimbing hillClimbing;
	private TwoOpt twoOpt;
	private Problem problem;
	private Random rand;
	private int length;
	
	public GRASP (Evaluation evaluation)
	{
		this.hillClimbing = new HillClimbing(evaluation);
		this.twoOpt = new TwoOpt(evaluation);
		this.problem = evaluation.getProblem();
		this.length = this.problem.getLength();
		this.rand = new Random();
	}
	
	/**
	 * GRASP
	 * @param alpha facteur d'exploration
	 * @return Path
	 */
	public Path grasp(double alpha) {
		int[] cities = new Path(length).getPath();			
		int[] RCL = new int[length];
		for(int i = 0; i < length - 2; i++) {
			double[] cost = new double[length-i-1];
			double cmin = Double.MAX_VALUE;
			double cmax = -Double.MAX_VALUE;
			// Calcul des coûts
			for(int j = i+1; j < length; j++) {
				cost[j-i-1] = problem.getCoordinates(cities[i]).distance(problem.getCoordinates(cities[j]));
				if(cost[j-i-1] < cmin)
					cmin = cost[j-i-1];
				if(cost[j-i-1] > cmax)
					cmax = cost[j-i-1];
			}
			int k = 0;
			// Calcul de la RCL
			for(int j = i+1; j < length; j++) {
				// Critère d'admission
				if(cost[j-i-1] <= (cmin + alpha * (cmax - cmin))) {
					RCL[k] = j;
					k++;
				}
			}
			swap(cities, i + 1, RCL[rand.nextInt(k)]);
		}
		
		// On applique le hill climbing et on renvoie
		return this.hillClimbing.hillClimbing(new Path(cities));
	}
	
	/**
	 * GRASP avec 2-opt en plus
	 * @param alpha facteur d'exploration
	 * @return Path
	 */
	public Path grasp_twoOpt(double alpha) {
		int[] cities = new Path(length).getPath();			
		int[] RCL = new int[length];
		for(int i = 0; i < length - 2; i++) {
			double[] cost = new double[length-i-1];
			double cmin = Double.MAX_VALUE;
			double cmax = -Double.MAX_VALUE;
			// Calcul des coûts
			for(int j = i+1; j < length; j++) {
				cost[j-i-1] = problem.getCoordinates(cities[i]).distance(problem.getCoordinates(cities[j]));
				if(cost[j-i-1] < cmin)
					cmin = cost[j-i-1];
				if(cost[j-i-1] > cmax)
					cmax = cost[j-i-1];
			}
			int k = 0;
			// Calcul de la RCL
			for(int j = i+1; j < length; j++) {
				// Critère d'admission
				if(cost[j-i-1] <= (cmin + alpha * (cmax - cmin))) {
					RCL[k] = j;
					k++;
				}
			}
			
			swap(cities, i + 1, RCL[rand.nextInt(k)]);
		}
		
		// On applique 2-opt puis le hill climbing et on renvoie
		return this.hillClimbing.hillClimbing(this.twoOpt.twoOpt(new Path(cities)));
	}
	
	/**
	 * GRASP avec probabilité de sélection inversement proportionnelle de la distance entre les villes
	 * @param 
	 * @return Path
	 */
	public Path grasp() {
		int[] cities = new Path(length).getPath();			

		for(int i = 0; i < length - 2; i++) {
			float[] probabilities = new float[length-i];
			double cmin = Double.MAX_VALUE;
			double cmax = -Double.MAX_VALUE;
			double sum  = 0;
			// on calcule la somme des distances
			for(int j = i+1; j < length; j++) {
				probabilities[j-i-1] = (float) Math.exp(-problem.getCoordinates(cities[i]).distance(problem.getCoordinates(cities[j])));
				sum += probabilities[j-i-i];
				if(probabilities[j-i-1] < cmin)
					cmin = probabilities[j-i-1];
				if(probabilities[j-i-1] > cmax)
					cmax = probabilities[j-i-1];
			}
			// on calcule les probabilités
			for(int j = 0; j < length - i - 1; j++)
					probabilities[j] /= sum;
			swap(cities, i + 1, pickRandomWithProbability(probabilities) + i + 1);
		}
		
		return this.hillClimbing.hillClimbing(new Path(cities));
	}
	
	/**
	 * tirage avec probabilités
	 * @param probabilities
	 * @return
	 */
	int pickRandomWithProbability(float[] probabilities) {
		int max = 0;
		for(int j = 0; j < probabilities.length;j++)
			max+=probabilities[j];
		float tirage = rand.nextFloat() - 1 + max;
		int i = 0;
		while(i < probabilities.length) {
			tirage -= probabilities[i];
			if(tirage <= 0)
				return i;
			i++;
		}
		return probabilities.length - 1;
	}
	
	private static void swap(int[] tab, int i, int j) {
		int temp = tab[i];
		tab[i] = tab[j];
		tab[j] = temp;
	}
}
