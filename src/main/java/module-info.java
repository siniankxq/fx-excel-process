module com.pc.fx {
	requires javafx.controls;
	requires javafx.fxml;


	opens com.pc.fx to javafx.fxml;
	exports com.pc.fx;
}