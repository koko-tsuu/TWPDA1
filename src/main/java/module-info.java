module com.example.twpda1 {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.twpda1 to javafx.fxml;
    exports com.example.twpda1;
}