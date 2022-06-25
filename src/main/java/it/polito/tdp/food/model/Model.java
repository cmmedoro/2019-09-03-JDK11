package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> porzioni;
	private List<String> vertici;
	//Strutture dati per la ricorsione
	private List<String> best;
	private double pesoBest;
	
	public Model() {
		this.dao = new FoodDao();
		this.porzioni = new ArrayList<>();
	}
	
	public List<String> getDistinctPortions(){
		this.porzioni = this.dao.getDistinctPortion();
		return this.porzioni;
	}
	
	public void creaGrafo(Integer calorie) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungi gli archi
		this.vertici = new ArrayList<>(this.dao.getVertices(calorie));
		Graphs.addAllVertices(this.grafo, this.vertici);
		//aggiungi gli archi
		for(Adiacenza a : this.dao.getArchi(calorie)) {
			if(this.grafo.vertexSet().contains(a.getP1()) && this.grafo.vertexSet().contains(a.getP2())){
				Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
			}
		}
	}
	public boolean isGraphCreated() {
		if(this.grafo == null) {
			return false;
		}
		return true;
	}
	public int nVertices() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<String> getVertices(){
		return this.vertici;
	}
	
	public List<Adiacenza> getConnessa(String porzione){
		List<Adiacenza> ad = new ArrayList<>();
		List<String> vicini = Graphs.neighborListOf(this.grafo, porzione);
		for(String s : vicini) {
			ad.add(new Adiacenza(porzione, s, (int)this.grafo.getEdgeWeight(this.grafo.getEdge(porzione, s))));
		}
		return ad;
	}
	
	public List<String> cercaCammino(int N, String partenza) {
		List<String> parziale = new ArrayList<>();
		parziale.add(partenza);
		this.pesoBest = 0.0;
		this.best = new ArrayList<>();
		ricercaCammino(parziale, N);
		return this.best;
	}

	private void ricercaCammino(List<String> parziale, int n) {
		
		if(parziale.size() == n+1) {
			//devo controllare se è il cammino con peso massimo
			if(getPesoMax(parziale) > this.pesoBest) {
				this.best = new ArrayList<>(parziale);
				this.pesoBest = getPesoMax(parziale);
			}
			return;
		}
		String ultimo = parziale.get(parziale.size()-1);
		for(String s : Graphs.neighborListOf(this.grafo, ultimo)) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				ricercaCammino(parziale, n);
				parziale.remove(s);
			}
			
		}
	}
	
	private Double getPesoMax(List<String> temp) {
		double peso = 0.0;
		for(int i = 1; i < temp.size(); i++) {
			double weight = this.grafo.getEdgeWeight(this.grafo.getEdge(temp.get(i-1), temp.get(i)));
			peso += weight;
		}
		return peso;
	}
	
	public double pesoBest() {
		return this.pesoBest;
	}
}
