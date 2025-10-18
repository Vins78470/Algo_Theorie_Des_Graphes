package Modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère la liste des étapes pour l'animation d'un algorithme de graphe.
 */
public class StepManager {

    private final List<Step> steps;  // Liste des étapes

    public StepManager() {
        steps = new ArrayList<>();
    }

    /**
     * Ajoute un Step représentant une action (visite ou arête parcourue).
     * @param step étape à ajouter
     */
    public void addStep(Step step) {
        if (step != null) {
            steps.add(step);
        }
    }

    /**
     * Crée et ajoute un Step représentant la visite d'un nœud.
     * @param node nœud visité
     * @param state état du nœud
     */
    public void markNode(Node node, NodeState state) {
        steps.add(new Step(node, node, state)); // from=to=node pour une action sur le noeud
    }

    /**
     * Crée et ajoute un Step représentant une arête parcourue.
     * @param from nœud de départ
     * @param to nœud d'arrivée
     * @param state état de l'arête
     */
    public void markEdge(Node from, Node to, NodeState state) {
        steps.add(new Step(from, to, state));
    }

    private List<Step> extractPathSteps(StepManager stepManager, List<String> finalNodeNames) {
        List<Step> filtered = new ArrayList<>();

        // On parcourt les Steps et on garde seulement ceux qui correspondent au chemin final
        for (Step step : stepManager.getSteps()) {
            if (step.getFrom() != null && step.getTo() != null) {
                String from = step.getFrom().getName();
                String to = step.getTo().getName();

                // Si l'arête (from → to) correspond à un lien entre deux nœuds consécutifs du chemin
                for (int i = 0; i < finalNodeNames.size() - 1; i++) {
                    if (finalNodeNames.get(i).equals(from) && finalNodeNames.get(i + 1).equals(to)) {
                        filtered.add(step);
                    }
                }
            }
        }
        return filtered;
    }


    /**
     * Réinitialise toutes les étapes
     */
    public void reset() {
        steps.clear();
    }

    /**
     * Retourne toutes les étapes générées
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * Retourne le dernier Step ajouté, ou null si aucun.
     */
    public Step getLastStep() {
        if (steps.isEmpty()) return null;
        return steps.get(steps.size() - 1);
    }

    @Override
    public String toString() {
        if (steps.isEmpty()) return "Aucune étape disponible.";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            sb.append("Step ").append(i + 1).append(": ");
            sb.append("[from=").append(step.getFrom() != null ? step.getFrom().getName() : "null");
            sb.append(", to=").append(step.getTo() != null ? step.getTo().getName() : "null");
            sb.append(", state=").append(step.getState() != null ? step.getState() : "null").append("]\n");
        }
        return sb.toString();
    }

}
