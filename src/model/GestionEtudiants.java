package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Gère la collection des étudiants. */
public class GestionEtudiants {
    private final ArrayList<Etudiant> etudiants = new ArrayList<>();

    public void ajouter(Etudiant etudiant) {
        etudiants.add(etudiant);
    }

    public boolean supprimer(Etudiant etudiant) {
        return etudiants.remove(etudiant);
    }

    public List<Etudiant> rechercher(String nom, String parcours) {
        String nomRecherche = nom == null ? "" : nom.trim().toLowerCase(Locale.ROOT);
        String parcoursRecherche = parcours == null ? "" : parcours.trim().toLowerCase(Locale.ROOT);
        ArrayList<Etudiant> resultat = new ArrayList<>();
        for (Etudiant e : etudiants) {
            boolean nomOk = nomRecherche.isEmpty()
                    || e.getNom().toLowerCase(Locale.ROOT).contains(nomRecherche)
                    || e.getPrenom().toLowerCase(Locale.ROOT).contains(nomRecherche);
            boolean parcoursOk = parcoursRecherche.isEmpty()
                    || e.getParcours().toLowerCase(Locale.ROOT).contains(parcoursRecherche);
            if (nomOk && parcoursOk) resultat.add(e);
        }
        return resultat;
    }

    public List<Etudiant> getEtudiants() {
        return Collections.unmodifiableList(etudiants);
    }
}

