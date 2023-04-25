package com.pc.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FileController {
	@FXML
	private Label welcomeText;

	@FXML
	protected void onFileButtonClick() {
		welcomeText.setText("Welcome to JavaFX Application!");
	}
}