import javax.swing.SwingUtilities;
import gui.FenetreEtudiant;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FenetreEtudiant().setVisible(true));
    }
}

