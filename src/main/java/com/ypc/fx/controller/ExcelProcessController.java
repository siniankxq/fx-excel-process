package com.ypc.fx.controller;

import com.ypc.fx.handler.DemoDataListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;


@Slf4j
public class ExcelProcessController {
	@FXML
	private Label welcomeText;

	private File sourceFile;

	@FXML
	private Label sourceLabel;

	private File targetFile;
	/**
	 * 导入的目标行号
	 */
	@FXML
	private TextField targetBeginRow;
	@FXML
	private Label targetLabel;

	@FXML
	private Button sourceButton;

	@FXML
	private Button targetButton;

	@FXML
	protected void onHelloButtonClick() {
		welcomeText.setText("Welcome to JavaFX Application!");
	}

	@FXML
	protected void onSourceFileButtonClick() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("选择源Excel文件");
		Stage selectFile = new Stage();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Excel", "*.xls", "*.xlsx"), new FileChooser.ExtensionFilter("XLS", "*.xls"), new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
		File file = fileChooser.showOpenDialog(selectFile);
		if (file != null) {
			log.info("选择的源文件地址：{}", file.getPath());
			sourceFile = file;
			sourceLabel.setText("源文件：" + file.getPath());
			sourceButton.setText(file.getPath());


		}
	}

	@FXML
	protected void onTargetFileButtonClick() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("选择目标Excel文件");
		Stage selectFile = new Stage();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Excel", "*.xls", "*.xlsx"), new FileChooser.ExtensionFilter("XLS", "*.xls"), new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
		File file = fileChooser.showOpenDialog(selectFile);
		if (file != null) {
			log.info("选择的目标文件地址：{}", file.getPath());
			targetFile = file;
			targetLabel.setText("目标文件地址：" + file.getPath());
			targetButton.setText(file.getPath());
			//				bom.initBOM(ExcelUtil.importExcel(Util.getWorkbok(new FileInputStream(file), file)));
//				session.commit();
//				session.close();

		}
	}

	@FXML
	protected void onFileConfirmButtonClick() {
		if (sourceFile == null || targetFile == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setContentText("源文件或目标文件未选择");
			alert.show();
		} else {
			try {
				int beginRow = Integer.parseInt(targetBeginRow.getText());
				log.info("源文件: " + sourceFile.getPath() + " 目标文件: " + targetFile.getPath());
				try {
					String newTargetFileName = targetFile.getParent() + File.separator + "备份" + File.separator + targetFile.getName();
					File newTargetFile = new File(newTargetFileName);
					FileUtils.copyFile(targetFile, newTargetFile);

					DemoDataListener.writeInExcel(sourceFile, newTargetFile, DemoDataListener.MAPPING, DemoDataListener.DEFAULT_MAP, beginRow - 1, 1);
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setContentText("处理成功，新文件的地址为：" + newTargetFileName);
					alert.show();
				} catch (Exception e) {
					log.error("文件处理异常", e);
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setContentText("文件处理异常，请重试");
					alert.show();
				}
			} catch (NumberFormatException e) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setContentText("行号必须为数字");
				alert.show();
			}

		}

	}
}
