package application;


import java.util.ArrayList;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

public class Rootcontrol {

	@FXML
	private TextField Start_station;
	@FXML
	private TextField End_station;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private ImageView imageView;

	private Station start = null;
	private Station end = null;
	private String input = "";

	static boolean s_check = false;
	static boolean e_check = false;

	public void handle(ScrollEvent event) {
		double Width = imageView.getFitWidth();
		double Height = imageView.getFitHeight();
		if (event.getDeltaY() > 0) {
			Width *= 1.1;
			Height *= 1.1;
		} else if (event.getDeltaY() < 0) {
			Width /= 1.1;
			Height /= 1.1;
		}
		if (400 > Width) {
			Width = 400;
		}
		if (320 > Height) {
			Height = 320;
		}
		if (2500 < Width) {
			Width = 2500;
		}
		if (2000< Height) {
			Height = 2000;
		}
		
		imageView.setFitWidth(Width);
		imageView.setFitHeight(Height);
	}

	public void Input_start(ActionEvent event) {
		input = Start_station.getText();

		s_check = false;
		for (Map.Entry<String, ArrayList<Station>> pair : Main.St.entrySet()) {
			ArrayList tmp = pair.getValue();
			for (int i = 0; i < tmp.size(); i++) {
				Station cur = (Station) tmp.get(i);
				if (input.equals(cur.Station_name)) {
					start = cur;
					s_check = true;
					break;
				}
			}
		}

		if (s_check && e_check) {
			// output(Main.dijkstra(start, end));
			System.out.println(start.Station_name+" "+end.Station_name);
		}
	}

	public void Input_end(ActionEvent event) {
		input = End_station.getText();
		e_check = false;
		for (Map.Entry<String, ArrayList<Station>> pair : Main.St.entrySet()) {
			ArrayList tmp = pair.getValue();
			for (int i = 0; i < tmp.size(); i++) {
				Station cur = (Station) tmp.get(i);
				if (input.equals(cur.Station_name)) {
					end = cur;
					e_check = true;
					break;
				}
			}
		}

		if (s_check && e_check) {
			// output(Main.dijkstra(start, end));
			System.out.println(start.Station_name+" "+end.Station_name);
		}
	}

}
