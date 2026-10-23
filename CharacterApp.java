import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class CharacterApp extends JFrame {
    private static final String HINT = "Ctrl+click two characters, then press Fight.";

    private final CharacterManager manager = new CharacterManager();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Role"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final JTextField search_field = new JTextField();
    private final JLabel message_label = new JLabel(HINT);
    private List<GameCharacter> shown = new ArrayList<>();

    public CharacterApp() {
        super("Game Character Manager");
        search_field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refresh();
            }
        });
        table.setRowHeight(28);

        JPanel search_bar = new JPanel(new BorderLayout(8, 0));
        search_bar.add(new JLabel("Search"), BorderLayout.WEST);
        search_bar.add(search_field, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 4, 8, 0));
        buttons.add(button("Add", e -> addCharacter()));
        buttons.add(button("Edit", e -> editCharacter()));
        buttons.add(button("Remove", e -> removeCharacters()));
        buttons.add(button("Fight", e -> fight()));

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.add(message_label, BorderLayout.NORTH);
        bottom.add(buttons, BorderLayout.CENTER);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.add(search_bar, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        setContentPane(root);

        refresh();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 600);
        setLocationRelativeTo(null);
    }

    private JButton button(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    private void refresh() {
        String key = search_field.getText().trim();
        shown = key.isEmpty() ? manager.getAll() : manager.search(key);
        model.setRowCount(0);
        for (GameCharacter c : shown) {
            model.addRow(new Object[]{c.getName(), c.getType()});
        }
        showMessage(HINT, false);
    }

    private List<GameCharacter> selectedCharacters() {
        List<GameCharacter> selected = new ArrayList<>();
        for (int row : table.getSelectedRows()) {
            selected.add(shown.get(row));
        }
        return selected;
    }

    private void addCharacter() {
        new CharacterDialog(this, manager, null).setVisible(true);
        refresh();
    }

    private void editCharacter() {
        List<GameCharacter> selected = selectedCharacters();
        if (selected.size() != 1) {
            showMessage("Select one character to edit.", true);
            return;
        }
        new CharacterDialog(this, manager, selected.get(0)).setVisible(true);
        refresh();
    }

    private void removeCharacters() {
        List<GameCharacter> selected = selectedCharacters();
        if (selected.isEmpty()) {
            showMessage("Select a character to remove.", true);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Remove " + selected.size() + " character(s)?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            for (GameCharacter c : selected) {
                manager.remove(c);
            }
            refresh();
        }
    }

    private void fight() {
        List<GameCharacter> selected = selectedCharacters();
        if (selected.size() != 2) {
            showMessage("Select exactly 2 characters to fight.", true);
            return;
        }
        new FightDialog(this, selected.get(0), selected.get(1)).setVisible(true);
    }

    private void showMessage(String message, boolean error) {
        message_label.setForeground(error ? Theme.ERROR : Theme.TEXT);
        message_label.setText(message);
    }

    public static void main(String[] args) {
        Theme.install();
        SwingUtilities.invokeLater(() -> new CharacterApp().setVisible(true));
    }
}