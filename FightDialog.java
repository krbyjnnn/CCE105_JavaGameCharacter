import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FightDialog extends JDialog {
    private static final int COOLDOWN_SIZE = 2;
    private static final int SPECIAL = 2;

    private final GameCharacter[] fighters;
    private final JLabel[] names = {new JLabel(), new JLabel()};
    private final JProgressBar[] bars = {
            new JProgressBar(0, GameCharacter.MAX_HEALTH), new JProgressBar(0, GameCharacter.MAX_HEALTH)};
    private final JButton[] skills = {new JButton(), new JButton(), new JButton()};
    private final JTextArea log = new JTextArea();
    private final JLabel turn_label = new JLabel();
    private final List<Queue<Integer>> cooldowns = new ArrayList<>();
    private final boolean[] muted = new boolean[2];
    private int turn;
    private boolean fight_active = true;

    public FightDialog(JFrame owner, GameCharacter one, GameCharacter two) {
        super(owner, one.getName() + " vs " + two.getName(), true);
        fighters = new GameCharacter[]{one, two};
        cooldowns.add(new LinkedList<>());
        cooldowns.add(new LinkedList<>());

        JPanel fighters_row = new JPanel(new GridLayout(1, 2, 16, 0));
        for (int i = 0; i < 2; i++) {
            fighters[i].resetHealth();
            names[i].setText(fighters[i].getName() + " (" + fighters[i].getType() + ")");
            names[i].setFont(new Font("SansSerif", Font.BOLD, 20));
            bars[i].setStringPainted(true);
            JPanel side = new JPanel(new GridLayout(0, 1, 0, 6));
            side.setBackground(Theme.PANEL);
            side.setBorder(new EmptyBorder(12, 12, 12, 12));
            side.add(names[i]);
            side.add(bars[i]);
            fighters_row.add(side);
        }

        JPanel skills_row = new JPanel(new GridLayout(1, 3, 8, 0));
        for (int i = 0; i < 3; i++) {
            int index = i;
            skills[i].addActionListener(e -> useSkill(index));
            skills_row.add(skills[i]);
        }

        turn_label.setFont(new Font("SansSerif", Font.BOLD, 16));
        JPanel arena = new JPanel(new BorderLayout(0, 12));
        arena.add(fighters_row, BorderLayout.NORTH);
        arena.add(turn_label, BorderLayout.CENTER);
        arena.add(skills_row, BorderLayout.SOUTH);

        log.setEditable(false);
        log.setFont(new Font("Monospaced", Font.PLAIN, 13));
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        log.setText("Fight!\n");

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.add(arena, BorderLayout.NORTH);
        root.add(new JScrollPane(log), BorderLayout.CENTER);
        setContentPane(root);

        refresh();
        setSize(900, 520);
        setLocationRelativeTo(owner);
    }

    private boolean canUse(int side, int skill) {
        return !cooldowns.get(side).contains(skill) && !(muted[side] && skill == SPECIAL);
    }

    private void useSkill(int skill) {
        if (!fight_active || !canUse(turn, skill)) {
            return;
        }
        GameCharacter attacker = fighters[turn];
        GameCharacter defender = fighters[1 - turn];
        int damage = attacker.skillDamage(skill);
        defender.takeDamage(damage);
        log.append(attacker.getName() + " used " + attacker.skillName(skill)
                + " on " + defender.getName() + " for " + damage + " damage.\n");

        Queue<Integer> cooldown = cooldowns.get(turn);
        cooldown.add(skill);
        if (cooldown.size() > COOLDOWN_SIZE) {
            cooldown.poll();
        }
        muted[turn] = false;

        if (!defender.isAlive()) {
            fight_active = false;
            log.append(attacker.getName() + " wins!\n");
        } else {
            boolean stunned = false;
            StatusEffect effect = attacker.getSpecialEffect();
            if (skill == SPECIAL && effect == StatusEffect.STUN) {
                stunned = true;
                log.append(defender.getName() + " is stunned and loses the next turn.\n");
            } else if (skill == SPECIAL && effect == StatusEffect.MUTE) {
                muted[1 - turn] = true;
                log.append(defender.getName() + " is muted and cannot use the special next turn.\n");
            }
            if (!stunned) {
                turn = 1 - turn;
            }
            skipIfNoSkill();
        }
        log.setCaretPosition(log.getDocument().getLength());
        refresh();
    }

    private void skipIfNoSkill() {
        for (int i = 0; i < 3; i++) {
            if (canUse(turn, i)) {
                return;
            }
        }
        log.append(fighters[turn].getName() + " has no skill available and loses the turn.\n");
        muted[turn] = false;
        turn = 1 - turn;
    }

    private void refresh() {
        for (int i = 0; i < 2; i++) {
            bars[i].setValue(fighters[i].getHealth());
            bars[i].setString(fighters[i].getHealth() + " / " + GameCharacter.MAX_HEALTH);
        }
        GameCharacter current = fighters[turn];
        for (int i = 0; i < 3; i++) {
            String text = current.skillName(i) + " (" + current.skillDamage(i) + ")";
            if (i == SPECIAL && current.getSpecialEffect() != StatusEffect.NONE) {
                text += " [" + current.getSpecialEffect() + "]";
            }
            if (cooldowns.get(turn).contains(i)) {
                text += " - cooldown";
            } else if (i == SPECIAL && muted[turn]) {
                text += " - muted";
            }
            skills[i].setText(text);
            skills[i].setEnabled(fight_active && canUse(turn, i));
        }
        turn_label.setText(current.getName() + (fight_active ? "'s turn" : " wins!"));
    }
}