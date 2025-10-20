module org.example.projettheoriedesgraphes {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires annotations;
    requires gs.core;
    requires junit;
    requires com.brunomnsilva.smartgraph; // Pour JUnit 4 (si tu utilises JUnit 5 ce serait `requires org.junit.jupiter.api;`)

    opens org.example.projettheoriedesgraphes to javafx.fxml;
    opens Controller to javafx.fxml;
    opens Helpers to javafx.fxml;
    //opens Vue to javafx.fxml;

   // exports Vue;  // Ajouter cette ligne
    exports Tests; // ✅ rend le package des tests accessible (par exemple à IntelliJ / Maven / Gradle)
    exports org.example.projettheoriedesgraphes;
}
