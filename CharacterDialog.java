import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CharacterDialog extends JDialog {
    private final CharacterManager manager;
    private final GameCharacter existing;
    private final JComboBox<String> role_box = new JComboBox<>(CharacterManager.ROLES);
    private final JTextField name_field = new JTextField(20);
    private final JTextField attack_one_field = new JTextField();
    private final JTextField attack_two_field = new JTextField();
    private final JTextField special_field = new JTextField();
    private final JComboBox<StatusEffect> effect_box = new JComboBox<>(StatusEffect.values());
    private final JLabel error_label = new JLabel(" ");

    public CharacterDialog(JFrame owner, CharacterManager manager, GameCharacter existing) {
        super(owner, existing == null ? "Add Character" : "Edit Character", true);
        this.manager = manager;
        this.existing = existing;
        if (existing != null) {
            role_box.setSelectedItem(existing.getType());
            role_box.setEnabled(false);
            name_field.setText(existing.getName());
            attack_one_field.setText(existing.getAttackOneName());
            attack_two_field.setText(existing.getAttackTwoName());
            special_field.setText(existing.getSpecialName());
            effect_box.setSelectedItem(existing.getSpecialEffect());
        }

        String[] labels = {"Role", "Character name", "Attack 1 name", "Attack 2 name",
                "Special ability name", "Special effect"};
        JComponent[] inputs = {role_box, name_field, attack_one_field, attack_two_field, special_field, effect_box};
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 4));
        form.setBorder(new EmptyBorder(16, 16, 16, 16));
        for (int i = 0; i < labels.length; i++) {
            form.add(new JLabel(labels[i]));
            form.add(inputs[i]);
        }

        error_label.setForeground(Theme.ERROR);
        JButton save_button = new JButton("Save");
        JButton cancel_button = new JButton("Cancel");
        save_button.addActionListener(e -> save());
        cancel_button.addActionListener(e -> dispose());
        JPanel buttons = new JPanel(new GridLayout(1, 2, 8, 0));
        buttons.add(save_button);
        buttons.add(cancel_button);
        form.add(error_label);
        form.add(buttons);

        setContentPane(form);
        getRootPane().setDefaultButton(save_button);
        pack();
        setLocationRelativeTo(owner);
    }

    private void save() {
        try {
            String name = name_field.getText();
            String attack_one = attack_one_field.getText();
            String attack_two = attack_two_field.getText();
            String special = special_field.getText();
            StatusEffect effect = (StatusEffect) effect_box.getSelectedItem();
            if (existing == null) {
                manager.add(CharacterManager.create((String) role_box.getSelectedItem(),
                        name, attack_one, attack_two, special, effect));
            } else {
                manager.update(existing, name, attack_one, attack_two, special, effect);
            }
            dispose();
        } catch (IllegalArgumentException ex) {
            error_label.setText(ex.getMessage());
        }
    }
}