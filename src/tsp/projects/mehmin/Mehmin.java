package tsp.projects.mehmin;

import java.util.Random;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.InvalidProjectException;
import tsp.projects.mehmin.genetic.Genetic;
import tsp.projects.mehmin.hillclimbing.HillClimbing;
import tsp.projects.CompetitorProject;

/**
 * @author KARABAY TEKELI
 * Mehmin
 */
public class Mehmin extends CompetitorProject
{
	private TwoOpt twoOpt;
	private HillClimbing hillClimbing;
	private Greedy greedy;
	
	private Chrono chrono;
	private int time = 0; // en secondes
	private int maxTime = 59; // en secondes
	
	Random rand;
	private int length;
	Path bestPath;
	double bestScore=Double.MAX_VALUE;
	
	/**
	 * Méthode d'évaluation de la solution
	 * @param evaluation 
	 * @throws InvalidProjectException
	 */
	public Mehmin (Evaluation evaluation) throws InvalidProjectException
	{
		super (evaluation);
		this.addAuthor ("KARABAY - TEKELI");
		this.setMethodName ("Mehmin");
	}

	@Override
	public void initialization ()
	{
		this.chrono = new Chrono();
		this.time = 0;

		this.length = this.evaluation.getProblem().getLength();
		this.rand = new Random();
		
		this.twoOpt = new TwoOpt(this.evaluation);
		this.hillClimbing = new HillClimbing(this.evaluation);
		this.greedy = new Greedy(this.evaluation);
		
		this.bestPath = this.greedy.greedy();
		this.bestScore = this.evaluation.evaluate(bestPath);
	}
	
	@Override
	public void loop ()
	{	
		if(this.length < 1000)
			smallProblem();
		else {
			if(time < maxTime) {
				this.chrono.start();
				bigProblem();
				this.chrono.stop();
				this.time+= this.chrono.getDureeSec();
			}
		}
	}
	
	/**
	 * Traite les petits problèmes
	 * Crée une solution greedy puis applique l'algo principal dessus
	 * Crée ensuite un certain nombre de fois des voisins aléatoires au meilleur chemin connu
	 * et applique l'algo principal dessus
	 */
	public void smallProblem() {
		smallProblem(this.greedy.greedy());
		
		for(int i = 0; i < 3; i++) {
			Path neighbour = random_neighbour(this.bestPath, length/21);
			smallProblem(neighbour);
		}
	}
	
	/**
	 * Applique l'algo principal pour les petits jeus de données sur le path passé en param
	 * Applique 2-opt, puis hill climbing
	 * @param baseSolution
	 */
	public void smallProblem(Path baseSolution) {
		Path twoOpt = this.twoOpt.twoOpt(baseSolution);
		Path hc = this.hillClimbing.hillClimbing(twoOpt);
		double score = this.evaluation.evaluate(hc);
		if(score < this.bestScore) {
			this.bestPath = hc;
			this.bestScore = score;
		}
	}
	
	/**
	 * Traite les gros jeux de données
	 * On crée un certain nombre de fois des chemins greedy qu'on évalue
	 * puis on applique le hillClimbing sur le meilleur chemin
	 */
	public void bigProblem() {
		Path current = greedy.greedy();
		double score = this.evaluation.evaluate(current);
		boolean trouve = false;
		if(score < this.bestScore) {
			this.bestPath = current;
			this.bestScore = score;
			trouve = true;
		}
		if(trouve)
			this.hillClimbing.hillClimbing(this.bestPath);
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
	
	private void swap(int[] tab, int i, int j) {
		int temp = tab[i];
		tab[i] = tab[j];
		tab[j] = temp;
	}
}
