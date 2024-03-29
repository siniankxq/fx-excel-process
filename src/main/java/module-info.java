module com.ypc.fx {
	requires javafx.controls;
	requires javafx.fxml;
	requires poi;
	requires poi.ooxml;
	requires cn.hutool.poi;
	requires org.apache.commons.lang3;
	requires java.management;
	requires org.apache.commons.io;
	requires poi.ooxml.schemas;
	requires org.slf4j;
	requires cn.hutool.core;
	requires static lombok;


	opens com.ypc.fx to javafx.fxml;
	exports com.ypc.fx;
	exports com.ypc.fx.controller;
	opens com.ypc.fx.controller to javafx.fxml;
}
