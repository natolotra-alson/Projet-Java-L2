package gui;

import model.Etudiant;
import model.GestionEtudiants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Interface graphique principale du gestionnaire d'étudiants. */
public class FenetreEtudiant extends JFrame {
    private final GestionEtudiants gestion = new GestionEtudiants();
    private int prochainNumero = 1;
    private final JTextField matriculeField = new JTextField();
    private final JTextField nomField = new JTextField();
    private final JTextField prenomField = new JTextField();
    private final JTextField dateNaissanceField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField telephoneField = new JTextField();
    private final JComboBox<String> parcoursBox = new JComboBox<>(new String[]{"Sélectionner", "Informatique", "Sciences sociales", "Paramédical", "Chimie appliquée"});
    private final JComboBox<String> anneeBox = new JComboBox<>(new String[]{"2024 - 2025", "2025 - 2026", "2026 - 2027"});
    private final JComboBox<String> statutBox = new JComboBox<>(new String[]{"Actif", "Inactif"});
    private final JTextField rechercheField = new JTextField();
    private final JComboBox<String> critereBox = new JComboBox<>(new String[]{"Nom", "Filière"});
    private final DefaultTableModel tableModel;
    private final JTable table;

    public FenetreEtudiant() {
        setTitle("Gestion des étudiants");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1150, 680));
        setLocationRelativeTo(null);
        matriculeField.setEditable(false);
        matriculeField.setBackground(new Color(238, 242, 247));
        matriculeField.setToolTipText("Généré automatiquement");
        tableModel = new DefaultTableModel(new String[]{"Matricule", "Nom", "Prénom", "Date naissance", "Email", "Téléphone", "Parcours", "Année", "Statut"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) chargerEtudiantSelectionne();
        });
        construireInterface();
        mettreAJourMatricule();
    }

    private void construireInterface() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.setBackground(new Color(247, 249, 252));
        root.add(creerBarreRecherche(), BorderLayout.NORTH);
        root.add(creerZoneCentrale(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel creerBarreRecherche() {
        JPanel panel = new JPanel(new BorderLayout(8, 5));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(160, 190, 210)), "ZONE RECHERCHE"));
        panel.setBackground(Color.WHITE);
        JPanel champs = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 3));
        champs.setOpaque(false);
        champs.add(new JLabel("Rechercher :"));
        champs.add(critereBox);
        rechercheField.setPreferredSize(new Dimension(230, 26));
        champs.add(rechercheField);
        panel.add(champs, BorderLayout.CENTER);
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 3));
        boutons.setOpaque(false);
        JButton rechercher = new JButton("Rechercher");
        JButton reinitialiser = new JButton("Réinitialiser");
        rechercher.addActionListener(e -> rechercher());
        reinitialiser.addActionListener(e -> { rechercheField.setText(""); afficherTous(); });
        rechercheField.addActionListener(e -> rechercher());
        boutons.add(rechercher);
        boutons.add(reinitialiser);
        panel.add(boutons, BorderLayout.EAST);
        return panel;
    }

    private JSplitPane creerZoneCentrale() {
        JPanel formulaire = creerFormulaire();
        JPanel tableauPanel = new JPanel(new BorderLayout(5, 5));
        tableauPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(160, 190, 210)), "ZONE TABLE / TABLEAU DES ÉTUDIANTS"));
        tableauPanel.setBackground(Color.WHITE);
        tableauPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        JLabel aide = new JLabel("Sélectionnez une ligne puis cliquez sur Supprimer.");
        aide.setBorder(new EmptyBorder(5, 5, 5, 5));
        tableauPanel.add(aide, BorderLayout.SOUTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formulaire, tableauPanel);
        split.setResizeWeight(0.28);
        split.setDividerLocation(330);
        split.setBorder(null);
        return split;
    }

    private JPanel creerFormulaire() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(160, 190, 210)), "ZONE FORMULAIRE ÉTUDIANT"));
        panel.setBackground(Color.WHITE);
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;
        String[] labels = {"Matricule", "Nom *", "Prénom *", "Date naissance *", "Email *", "Téléphone *", "Parcours *", "Année universitaire *", "Statut *"};
        JComponent[] inputs = {matriculeField, nomField, prenomField, dateNaissanceField, emailField, telephoneField, parcoursBox, anneeBox, statutBox};
        for (int i = 0; i < labels.length; i++) {
            c.gridx = 0; c.gridy = i; c.weightx = 0;
            fields.add(new JLabel(labels[i]), c);
            c.gridx = 1; c.weightx = 1;
            fields.add(inputs[i], c);
        }
        panel.add(fields, BorderLayout.NORTH);
        JPanel actions = new JPanel(new GridLayout(1, 3, 5, 0));
        actions.setOpaque(false);
        JButton ajouter = new JButton("Ajouter");
        JButton vider = new JButton("Vider");
        JButton supprimer = new JButton("Supprimer");
        JButton modifier = new JButton("Modifier");
        ajouter.addActionListener(e -> ajouterEtudiant());
        vider.addActionListener(e -> viderFormulaire());
        supprimer.addActionListener(e -> supprimerEtudiant());
        modifier.addActionListener(e -> modifierEtudiant());
        actions.setLayout(new GridLayout(2, 2, 5, 5));
        actions.add(ajouter); actions.add(vider); actions.add(modifier); actions.add(supprimer);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private void ajouterEtudiant() {
        String[] valeurs = {nomField.getText().trim(), prenomField.getText().trim(), dateNaissanceField.getText().trim(), emailField.getText().trim(), telephoneField.getText().trim()};
        for (String valeur : valeurs) {
            if (valeur.isEmpty()) { JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.", "Saisie incomplète", JOptionPane.WARNING_MESSAGE); return; }
        }
        if (parcoursBox.getSelectedIndex() == 0) { JOptionPane.showMessageDialog(this, "Veuillez sélectionner un parcours.", "Saisie incomplète", JOptionPane.WARNING_MESSAGE); return; }
        String matricule = matriculeField.getText();
        Etudiant e = new Etudiant(matricule, valeurs[0], valeurs[1], valeurs[2], valeurs[3], valeurs[4], (String) parcoursBox.getSelectedItem(), (String) anneeBox.getSelectedItem(), (String) statutBox.getSelectedItem());
        gestion.ajouter(e);
        prochainNumero++;
        afficherTous();
        viderFormulaire();
    }

    private void supprimerEtudiant() {
        int ligneVue = table.getSelectedRow();
        if (ligneVue < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un étudiant à supprimer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE); return; }
        int ligne = table.convertRowIndexToModel(ligneVue);
        String matricule = (String) tableModel.getValueAt(ligne, 0);
        gestion.getEtudiants().stream().filter(e -> e.getMatricule().equals(matricule)).findFirst().ifPresent(gestion::supprimer);
        afficherTous();
    }

    private void chargerEtudiantSelectionne() {
        int ligneVue = table.getSelectedRow();
        if (ligneVue < 0) return;
        int ligne = table.convertRowIndexToModel(ligneVue);
        matriculeField.setText((String) tableModel.getValueAt(ligne, 0));
        nomField.setText((String) tableModel.getValueAt(ligne, 1));
        prenomField.setText((String) tableModel.getValueAt(ligne, 2));
        dateNaissanceField.setText((String) tableModel.getValueAt(ligne, 3));
        emailField.setText((String) tableModel.getValueAt(ligne, 4));
        telephoneField.setText((String) tableModel.getValueAt(ligne, 5));
        parcoursBox.setSelectedItem(tableModel.getValueAt(ligne, 6));
        anneeBox.setSelectedItem(tableModel.getValueAt(ligne, 7));
        statutBox.setSelectedItem(tableModel.getValueAt(ligne, 8));
    }

    private void modifierEtudiant() {
        int ligneVue = table.getSelectedRow();
        if (ligneVue < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un étudiant à modifier.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] valeurs = {nomField.getText().trim(), prenomField.getText().trim(), dateNaissanceField.getText().trim(), emailField.getText().trim(), telephoneField.getText().trim()};
        for (String valeur : valeurs) {
            if (valeur.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.", "Saisie incomplète", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        if (parcoursBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un parcours.", "Saisie incomplète", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ligne = table.convertRowIndexToModel(ligneVue);
        String matricule = (String) tableModel.getValueAt(ligne, 0);
        gestion.getEtudiants().stream()
                .filter(e -> e.getMatricule().equals(matricule))
                .findFirst()
                .ifPresent(e -> {
                    e.setNom(valeurs[0]);
                    e.setPrenom(valeurs[1]);
                    e.setDateNaissance(valeurs[2]);
                    e.setEmail(valeurs[3]);
                    e.setTelephone(valeurs[4]);
                    e.setParcours((String) parcoursBox.getSelectedItem());
                    e.setAnneeUniversitaire((String) anneeBox.getSelectedItem());
                    e.setStatut((String) statutBox.getSelectedItem());
                });
        afficherTous();
        viderFormulaire();
        JOptionPane.showMessageDialog(this, "Étudiant modifié avec succès.", "Modification", JOptionPane.INFORMATION_MESSAGE);
    }

    private void rechercher() {
        String texte = rechercheField.getText().trim();
        String nom = critereBox.getSelectedItem().equals("Nom") ? texte : "";
        String parcours = critereBox.getSelectedItem().equals("Filière") ? texte : "";
        afficher(gestion.rechercher(nom, parcours));
    }

    private void afficherTous() { afficher(gestion.getEtudiants()); }

    private void afficher(List<Etudiant> etudiants) {
        tableModel.setRowCount(0);
        for (Etudiant e : etudiants) tableModel.addRow(new Object[]{e.getMatricule(), e.getNom(), e.getPrenom(), e.getDateNaissance(), e.getEmail(), e.getTelephone(), e.getParcours(), e.getAnneeUniversitaire(), e.getStatut()});
    }

    private void mettreAJourMatricule() { matriculeField.setText(String.format("ETU%04d", prochainNumero)); }

    private void viderFormulaire() {
        nomField.setText(""); prenomField.setText(""); dateNaissanceField.setText(""); emailField.setText(""); telephoneField.setText(""); parcoursBox.setSelectedIndex(0); anneeBox.setSelectedIndex(0); statutBox.setSelectedIndex(0); mettreAJourMatricule();
    }
}
