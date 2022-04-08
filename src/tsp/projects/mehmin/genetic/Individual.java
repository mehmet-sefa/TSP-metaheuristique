package tsp.projects.mehmin.genetic;

import java.util.Random;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.mehmin.Greedy;
import tsp.projects.mehmin.TwoOpt;

/**
 * @author KARABAY TEKELI
 * Individual
 */
public class Individual
{
	protected int[] chromosome;
	protected double fitness = -1;
	protected Evaluation evaluation;
	protected Greedy greedy;
	protected TwoOpt twoOpt;
	protected Random rand;

	public Individual(int[] chromosome) {
		this.chromosome = chromosome;
	}


	public Individual(int chromosomeLength, Evaluation evaluation) {
		/*this.rand = new Random();
		this.evaluation = evaluation;
		this.greedy = new Greedy(evaluation);
		this.twoOpt = new TwoOpt(evaluation);

		double r = this.rand.nextDouble();
		if(r < 0.001)
			this.chromosome = this.greedy.greedy().getPath();
		else {
			this.chromosome = new Path(chromosomeLength).getPath();
			if( r < 0.002)
				this.chromosome = this.twoOpt.twoOpt(new Path(this.chromosome)).getPath();
		}*/
		this.chromosome = new int[chromosomeLength];
		for (int gene = 0; gene < chromosomeLength; gene++)
			this.chromosome[gene] = gene;
	}
	

	/**
	 * @param gene
	 * @return vrai si cet individu contient gene
	 */
	public boolean containsGene(int gene) {
		for (int i = 0; i < this.chromosome.length; i++)
			if (this.chromosome[i] == gene)
				return true;
		return false;
	}
	
	public int getGene(int offset) {
		return this.chromosome[offset];
	}
	
	public void setGene(int offset, int gene) {
		this.chromosome[offset] = gene;
	}
	
	public int[] getChromosome() {
		return this.chromosome;
	}
	
	public int getChromosomeLength() {
		return this.chromosome.length;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public double getFitness() {
		return this.fitness;
	}
}
