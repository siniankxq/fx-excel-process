package com.pc.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HelloController {
	@FXML
	private Label welcomeText;

	private File sourceFile;


	@FXML
	protected void onHelloButtonClick() {
		welcomeText.setText("Welcome to JavaFX Application!");
	}

	@FXML
	protected void onFileButtonClick() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("选择Excel文件");
		Stage selectFile = new Stage();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Excel", "*.xlsx"),
				new FileChooser.ExtensionFilter("XLS", "*.xls"), new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
		File file = fileChooser.showOpenDialog(selectFile);
		if (file != null) {
			System.out.println(file.getPath());
			sourceFile = file;
			//				bom.initBOM(ExcelUtil.importExcel(Util.getWorkbok(new FileInputStream(file), file)));
//				session.commit();
//				session.close();

		}
	}

	@FXML
	protected void onFileConfirmButtonClick() {
		if (sourceFile == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setContentText("警告提示！");
			alert.show();
		} else {
			System.out.println(sourceFile.getPath());

		}

	}
}