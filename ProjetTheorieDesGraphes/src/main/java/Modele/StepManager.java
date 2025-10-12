package Modele;


import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class StepManager {

    private final List<Step> steps;  // Liste des étapes
    private Step currentStep;         // Step courant sur lequel on travaille

    public StepManager() {
        steps = new ArrayList<>();
        currentStep = new Step();
    }

    /**
     * Crée une nouvelle étape en copiant l'état courant
     */
    public void addStep() {
        steps.add(currentStep.copy());
    }

    /**
     * Marque un noeud comme visité et ajoute une étape
     */
    public void markNode(int nodeIndex, Color color) {
        currentStep.markNodeVisited(nodeIndex, color);
        addStep();
    }

    /**
     * Marque une arête et ajoute une étape
     */
    public void markEdge(int i, int j, Color color) {
        currentStep.markEdge(i, j, color);
        addStep();
    }

    /**
     * Réinitialise l'état courant (utile avant un nouvel algo)
     */
    public void reset() {
        currentStep = new Step();
        steps.clear();
    }

    /**
     * Retourne toutes les étapes générées
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * Retourne le dernier état courant sans le copier
     */
    public Step getCurrentStep() {
        return currentStep;
    }

    /**
     * Fusionne une étape personnalisée (utile si tu veux créer un Step complet à la main)
     */
    public void addCustomStep(Step step) {
        if (step != null) steps.add(step.copy());
    }
}
