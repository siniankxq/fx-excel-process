package com.ypc.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * package 命令
 * 安装vs2022
 * 设置java环境变量为 graalvm目录
 * 1. 使用cmd 执行 call "C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat"
 * 打包命令: mvn clean gluonfx:build
 */
@Slf4j
public class ExcelProcessApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(ExcelProcessApplication.class.getResource("excel-process.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 420, 340);
		stage.setTitle("excel处理工具");
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(event -> {
			log.info("程序关闭");
			System.exit(0);
		});
		System.out.println("程序启动");
		log.info("程序启动");

	}

	public static void main(String[] args) {
		launch();
	}
}
