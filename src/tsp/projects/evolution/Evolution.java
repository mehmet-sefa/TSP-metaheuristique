package tsp.projects.evolution;

import java.util.ArrayList;
import java.util.Random;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.InvalidProjectException;
import tsp.projects.Project;

public class Evolution extends Project{
	
	private int length;
	private ArrayList<Path> population = new ArrayList<>();
	private static final int TAILLE_POPULATION = 50;
	private static final int NBR_ELITE = 1;
	private static final int NBR_CROSSOVER = 12;
	private static final int NBR_MUTATION = 25;
	Random random = new Random();
	

	public Evolution(Evaluation evaluation) throws InvalidProjectException {
		super(evaluation);
		this.addAuthor("Matthieu Pombet");
		this.setMethodName("Algorithme Evolutionnaire");
	}
	
	@Override
	public void initialization() {
		this.length = this.problem.getLength ();
		for(int i = 0; i<TAILLE_POPULATION; i++) {
			this.population.add(new Path (this.length));
		}
	}
	
	@Override
	public void loop() {
		ArrayList<Path> oldGeneration = (ArrayList<Path>) this.population.clone();
		this.population.clear();
		
		for(int k = 0; k<NBR_ELITE; k++) {
			this.population.add(tournoi(3, oldGeneration));
		}
		
		for(int k=0; k<NBR_CROSSOVER; k++) {
			Path parent1 = tournoi(2, oldGeneration);
			Path parent2 = tournoi(2, oldGeneration);
			croisement(parent1, parent2);
		}

		for(int k=0; k<NBR_MUTATION; k++) {
			mutation(oldGeneration.get(k));
		}

		Path best = bestPath();
		this.evaluation.evaluate(best);
	}
	
	public void croisement(Path parent1, Path parent2) {
		int[] enfant1 = new int[parent1.getPath().length];
		int[] enfant2 = new int[parent1.getPath().length];
		for(int i=0; i<parent1.getPath().length; i++) {
			if(random.nextDouble()>=0.5) {
				enfant1[i] = parent1.getPath()[i];
				enfant2[i] = parent2.getPath()[i];
			}
			else {
				enfant1[i] = parent2.getPath()[i];
				enfant2[i] = parent1.getPath()[i];
			}
		}
		this.population.add(new Path(enfant1));
		this.population.add(new Path(enfant2));
	}
	
	public void mutation(Path parent) {
		int[] tabParent = parent.getPath();
		int mutation1 = random.nextInt(this.length);
		int mutation2 = random.nextInt(this.length);
		int changement = tabParent[mutation1];
		tabParent[mutation1] = tabParent[mutation2];
		tabParent[mutation2] = changement;
		this.population.add(new Path(tabParent));
	}
	
	public Path tournoi(int nbrParticipants, ArrayList<Path> population) {
		ArrayList<Path> participants = new ArrayList<>();
		for(int i=0; i<nbrParticipants; i++) {
			int test = random.nextInt(TAILLE_POPULATION);
			Path p = population.get(test);
			System.out.println(this.evaluation.isValid(p));
			if (!this.evaluation.isValid(p))
				System.exit(0);
			participants.add(p);
		}
		
		double bestFitness = Double.MAX_VALUE;
		double fitness = Double.MAX_VALUE;
		Path best = null;
		for(int i=0; i<participants.size(); i++) {
			fitness = fitness(participants.get(i));
			if (fitness <= bestFitness) {
				bestFitness = fitness;
				best = participants.get(i);
			}
		}
		return best;
	}
	
	public double fitness(Path path) {
		double fitness = 0;
		for(int it=0; it<path.getPath().length-1; it++) {
			fitness += problem.getCoordinates(path.getPath()[it]).distance(problem.getCoordinates(path.getPath()[it+1])); 
		}
		fitness += problem.getCoordinates(path.getPath()[path.getPath().length-1]).distance(problem.getCoordinates(path.getPath()[0]));
		return fitness;
	}
	
	public Path bestPath() {
		double bestFitness = Double.MAX_VALUE;
		Path bestPath = null;
		double fitness = 0 ;
		for(int j=0; j<this.population.size(); j++) {
			fitness = fitness(this.population.get(j));
			if (fitness <= bestFitness) {
				bestFitness = fitness;
				bestPath = this.population.get(j);
			}
		}
		return bestPath;
	}
}
