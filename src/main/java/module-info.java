module com.myapp {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.myapp to javafx.fxml;
    exports com.myapp;
}
