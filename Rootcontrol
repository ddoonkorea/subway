package application;

import java.util.Observable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

public class Rootcontrol {

	@FXML
	private TextField Start_station;
	@FXML
	private TextField End_station;
	@FXML
	private Label test_label;


	static String start = "";
	static String end = "";

	static boolean s_check = false;
	static boolean e_check = false;

	public void Input_start(ActionEvent event) {
		start = Start_station.getText();
		if(start=="") {
			s_check = false;
		}
		else s_check = true;
		
		if(s_check&&e_check) {
			output();
		}
	}
	
	public void Input_end(ActionEvent event) {
		end = End_station.getText();
		if(end=="") {
			e_check = false;
		}
		else e_check = true;
		
		if(s_check&&e_check) {
			output();
		}
	}
	
	public void output() {
		test_label.setText(start+" "+end);
	}
}
