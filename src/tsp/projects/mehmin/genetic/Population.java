package tsp.projects.mehmin.genetic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import tsp.evaluation.Evaluation;

/**
 * @author KARABAY TEKELI
 * Population
 */
public class Population
{
	private Individual population[];
	private double fitness = -1;


	public Population(int populationSize) {
		this.population = new Individual[populationSize];
	}

	public Population(int populationSize, int chromosomeLength, Evaluation evaluation) {
		this.population = new Individual[populationSize];

		for (int individualCount = 0; individualCount < populationSize; individualCount++) {
			Individual individual = new Individual(chromosomeLength, evaluation);
			this.population[individualCount] = individual;
		}
	}

	public Individual[] getIndividuals() {
		return this.population;
	}

	public Individual getFittest(int offset) {
		Arrays.sort(this.population, new Comparator<Individual>() {
			@Override
			public int compare(Individual i1, Individual i2) {
				if (i1.getFitness() > i2.getFitness())
					return -1;
				else if (i1.getFitness() < i2.getFitness())
					return 1;
				return 0;
			}
		});

		return this.population[offset];
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public double getFitness() {
		return this.fitness;
	}

	public int size() {
		return this.population.length;
	}

	public Individual getIndividual(int offset) {
		return population[offset];
	}
	
	public Individual setIndividual(int offset, Individual individual) {
		return population[offset] = individual;
	}

	public void randomize() {
		Random rnd = new Random();
		for (int i = population.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Individual a = population[index];
			population[index] = population[i];
			population[i] = a;
		}
	}
}
