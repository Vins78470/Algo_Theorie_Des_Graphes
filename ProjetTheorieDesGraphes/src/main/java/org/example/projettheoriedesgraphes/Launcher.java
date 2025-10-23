package org.example.projettheoriedesgraphes;

import javafx.application.Application;

/**
 * Classe principale de lancement de l’application JavaFX.
 *
 * Cette classe contient uniquement la méthode {@code main()} qui
 * initialise et démarre l’application graphique via la classe
 * {@link VueApplication}.
 *
 * Elle sert de point d’entrée du programme pour le module JavaFX,
 * permettant ainsi à l’environnement d’exécuter correctement
 * l’interface utilisateur.
 *
 */
public class Launcher {

    /**
     * Point d’entrée principal de l’application.
     *
     * Cette méthode appelle {@link Application#launch(Class, String...)}
     * pour démarrer l’interface graphique JavaFX.
     *
     * @param args arguments de ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
        Application.launch(VueApplication.class, args);
    }
}
