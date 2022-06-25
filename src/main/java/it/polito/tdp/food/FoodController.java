/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.Portion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<String> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCammino(ActionEvent event) {
    	txtResult.clear();
    	if(!this.model.isGraphCreated()) {
    		this.txtResult.setText("Prima devi creare il grafico");
    		return;
    	}
    	String portion = this.boxPorzioni.getValue();
    	if(portion == null) {
    		this.txtResult.setText("Devi selezionare una porzione dal menù a tendina");
    		return;
    	}
    	int N;
    	try {
    		N = Integer.parseInt(this.txtPassi.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Devi inserire un valore numerico intero");
    		return;
    	}
    	//se sono qui proseguo con la ricorsione
    	List<String> best = this.model.cercaCammino(N, portion);
    	this.txtResult.appendText("Cammino: \n");
    	for(String s : best) {
    		this.txtResult.appendText(s+"\n");
    	}
    	this.txtResult.appendText("Peso totale del cammino: "+this.model.pesoBest()+"\n");
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	txtResult.clear();
    	if(!this.model.isGraphCreated()) {
    		this.txtResult.setText("Prima devi creare il grafico");
    		return;
    	}
    	String portion = this.boxPorzioni.getValue();
    	if(portion == null) {
    		this.txtResult.setText("Devi selezionare una porzione dal menù a tendina");
    		return;
    	}
    	//Se sono qui posso proseguire
    	List<Adiacenza> ad = this.model.getConnessa(portion);
    	this.txtResult.setText("Porzioni correlate a "+portion+": "+ad.size()+"\n");
    	for(Adiacenza aa : ad) {
    		this.txtResult.appendText(aa.getP2()+" - "+aa.getPeso()+"\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Integer calories;
    	try {
    		calories = Integer.parseInt(this.txtCalorie.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Devi inserire un valore numerico intero");
    		return;
    	}
    	//Se sono qui posso continuare a creare il grafico
    	this.model.creaGrafo(calories);
    	this.txtResult.setText("Grafo creato\n");
    	this.txtResult.appendText("#VERTICI: "+this.model.nVertices()+"\n");
    	this.txtResult.appendText("#ARCHI: "+this.model.nArchi()+"\n");
    	this.boxPorzioni.getItems().clear();
    	this.boxPorzioni.getItems().addAll(this.model.getVertices());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
