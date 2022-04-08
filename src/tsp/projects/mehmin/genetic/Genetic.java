package tsp.projects.mehmin.genetic;

import java.util.Arrays;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;

/**
 * @author KARABAY TEKELI
 * Genetic
 */
public class Genetic
{
	private int length;
	private Problem problem;
	private Evaluation evaluation;
	
	private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    private int tournamentSize;
	private int maxGenerations;
	
	public Genetic (Evaluation evaluation, int populationSize, double mutationRate,
			double crossoverRate,int elitismCount,int tournamentSize, int maxGenerations){
		this.problem = evaluation.getProblem();
		this.evaluation = evaluation;
		this.length = this.problem.getLength ();
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.tournamentSize = tournamentSize;
		this.maxGenerations = maxGenerations;
	}
	
	/**
	 * Algorithme génétique
	 * @return
	 */
	public Path genetic() {
		// Population initiale
		Population population = this.initPopulation(this.length);
		this.evalPopulation(population);
		Path startPath = new Path(population.getFittest(0).getChromosome());
		System.out.println("Start Distance: " + this.evaluation.evaluate(startPath));
		
		int generation = 1;
		// Tant qu'on a pas atteind le nb max de générations
		while (!this.isTerminationConditionMet(generation, maxGenerations)) {
			// On sélectionne le meilleur de la génération
			Path path = new Path(population.getFittest(0).getChromosome());
			System.out.println("G"+generation+" Best distance: " + this.evaluation.evaluate(path));
			// On applique le crossover et la mutation
			population = this.crossoverPopulation(population);
			population = this.mutatePopulation(population);
			// On évalue cette population
			this.evalPopulation(population);
			generation++;
		}
		
		System.out.println("Stopped after " + maxGenerations + " generations.");
		Path path = new Path(population.getFittest(0).getChromosome());
		System.out.println("Best distance: " + this.evaluation.evaluate(path));
		return path;
	}
	
	/**
	 * Initialise la population
	 * @param chromosomeLength
	 * @return
	 */
	public Population initPopulation(int chromosomeLength){
        Population population = new Population(this.populationSize, chromosomeLength, this.evaluation);
        return population;
    }
	
	/**
	 * Séléctionne un parent après tournoi
	 * @param population
	 * @return
	 */
	public Individual selectParent(Population population) {
		Population tournament = new Population(this.tournamentSize);

		population.randomize();
		for (int i = 0; i < this.tournamentSize; i++) {
			Individual tournamentIndividual = population.getIndividual(i);
			tournament.setIndividual(i, tournamentIndividual);
		}

		return tournament.getFittest(0);
	}
	
	/**
	 * Crossover (croisement) d'une population
	 * @param population
	 * @return
	 */
	public Population crossoverPopulation(Population population){
		// On crée une nouvelle population
        Population newPopulation = new Population(population.size());
        // Pour chaque individu
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            // On sélectionne le parent 1 qui est le plus "adapaté" (fittest)
        	Individual parent1 = population.getFittest(populationIndex);
            
            // On applique le crossover sur les individus avec un certain taux
            if (this.crossoverRate <= Math.random() && populationIndex >= this.elitismCount) {
            	// On filtre le deuxième parent avec un tournoi
                Individual parent2 = this.selectParent(population);
                
                // On crée un fils à partir du parent
                int offspringChromosome[] = new int[parent1.getChromosomeLength()];
                Arrays.fill(offspringChromosome, -1);
                Individual offspring = new Individual(offspringChromosome);

                // On prend des parties des chromosomes du parent
                int substrPos1 = (int) (Math.random() * parent1.getChromosomeLength());
                int substrPos2 = (int) (Math.random() * parent1.getChromosomeLength());
                
                // On calcule les index de début et de fin
                final int startSubstr = Math.min(substrPos1, substrPos2);
                final int endSubstr = Math.max(substrPos1, substrPos2);
                
                // On ajoute la partie du parent à l'enfant
                for (int i = startSubstr; i < endSubstr; i++)
                    offspring.setGene(i, parent1.getGene(i));
                
                // On ajoute la contribution du parent 2
                for (int i = 0; i < parent2.getChromosomeLength(); i++) {
                    int parent2Gene = i + endSubstr;
                    if (parent2Gene >= parent2.getChromosomeLength()) {
                        parent2Gene -= parent2.getChromosomeLength();
                    }

                    if (offspring.containsGene(parent2.getGene(parent2Gene)) == false) {
                        for (int ii = 0; ii < offspring.getChromosomeLength(); ii++) {
                            if (offspring.getGene(ii) == -1) {
                                offspring.setGene(ii, parent2.getGene(parent2Gene));
                                break;
                            }
                        }
                    }
                }
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        
        return newPopulation;
    }
	
	/**
	 * Mutation d'une population
	 * @param population
	 * @return
	 */
	public Population mutatePopulation(Population population){
		// On crée une nouvelle population
        Population newPopulation = new Population(this.populationSize);
        // Pour chaque individu
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            // On sélectionne le parent 1 qui est le plus "adapaté" (fittest)
        	Individual individual = population.getFittest(populationIndex);
            // On ne touche pas aux élites
            if (populationIndex >= this.elitismCount) {   
            	// On fait muter les gènes
                for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {   

                    if (this.mutationRate <= Math.random()) {
                        int newGenePos = (int) (Math.random() * individual.getChromosomeLength());
                        int gene1 = individual.getGene(newGenePos);
                        int gene2 = individual.getGene(geneIndex);
                        individual.setGene(geneIndex, gene1);
                        individual.setGene(newGenePos, gene2);
                    }
                }
            }
            
            newPopulation.setIndividual(populationIndex, individual);
        }
        
        return newPopulation;
    }
	
	
	/**
	 * Evalue une population
	 * @param population
	 */
	public void evalPopulation(Population population){
	    double populationFitness = 0;
	    
	    for (Individual individual : population.getIndividuals())
	        populationFitness += this.calculateFitness(individual);
	    
	    double avgFitness = populationFitness / population.size();
	    population.setFitness(avgFitness);
	}
	 
	/**
	 * Calculte "la qualification" d'un individu
	 * @param individual
	 * @return
	 */
	 public double calculateFitness(Individual individual){
        double fitness = 1 / this.evaluation.evaluate(new Path(individual.getChromosome()));
        individual.setFitness(fitness);
        return fitness;
    }
	
	/**
	 * Condition de fin
	 * @param generationsCount
	 * @param maxGenerations
	 * @return
	 */
	public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
		return (generationsCount > maxGenerations);
	}
}
