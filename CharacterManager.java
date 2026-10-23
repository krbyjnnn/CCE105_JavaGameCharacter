import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CharacterManager {
    public static final String[] ROLES = {"Warrior", "Mage", "Archer", "Assassin"};
    private static final Path FILE = Paths.get("characters.txt");

    private final ArrayList<GameCharacter> characters = new ArrayList<>();

    public CharacterManager() {
        load();
    }

    public static GameCharacter create(String role, String name, String attackOne, String attackTwo,
                                       String special, StatusEffect effect) {
        GameCharacter character;
        switch (role) {
            case "Warrior":
                character = new Warrior(name, attackOne, attackTwo, special);
                break;
            case "Mage":
                character = new Mage(name, attackOne, attackTwo, special);
                break;
            case "Archer":
                character = new Archer(name, attackOne, attackTwo, special);
                break;
            case "Assassin":
                character = new Assassin(name, attackOne, attackTwo, special);
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
        character.setSpecialEffect(effect);
        return character;
    }

    public void add(GameCharacter character) {
        if (findByName(character.getName()) != null) {
            throw new IllegalArgumentException("A character named " + character.getName() + " already exists.");
        }
        characters.add(character);
        save();
    }

    public void update(GameCharacter character, String name, String attackOne, String attackTwo,
                       String special, StatusEffect effect) {
        GameCharacter same = findByName(name);
        if (same != null && same != character) {
            throw new IllegalArgumentException("A character named " + name.trim() + " already exists.");
        }
        character.update(name, attackOne, attackTwo, special, effect);
        save();
    }

    public void remove(GameCharacter character) {
        if (characters.remove(character)) {
            save();
        }
    }

    public GameCharacter findByName(String name) {
        for (GameCharacter character : characters) {
            if (character.getName().equalsIgnoreCase(name.trim())) {
                return character;
            }
        }
        return null;
    }

    public List<GameCharacter> search(String keyword) {
        List<GameCharacter> result = new ArrayList<>();
        String key = keyword.trim().toLowerCase();
        for (GameCharacter character : characters) {
            if (character.getName().toLowerCase().contains(key)
                    || character.getType().toLowerCase().contains(key)) {
                result.add(character);
            }
        }
        return result;
    }

    public List<GameCharacter> getAll() {
        return new ArrayList<>(characters);
    }

    public void save() {
        List<String> lines = new ArrayList<>();
        for (GameCharacter c : characters) {
            lines.add(String.join("\t", c.getType(), clean(c.getName()), clean(c.getAttackOneName()),
                    clean(c.getAttackTwoName()), clean(c.getSpecialName()), c.getSpecialEffect().name()));
        }
        try {
            Files.write(FILE, lines, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.err.println("Could not save characters: " + ex.getMessage());
        }
    }

    private String clean(String text) {
        return text.replaceAll("[\t\r\n]", " ");
    }

    private void load() {
        if (!Files.exists(FILE)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(FILE, StandardCharsets.UTF_8)) {
                String[] parts = line.split("\t", -1);
                if (parts.length != 6) {
                    continue;
                }
                try {
                    GameCharacter character = create(parts[0], parts[1], parts[2], parts[3], parts[4],
                            StatusEffect.valueOf(parts[5]));
                    if (findByName(character.getName()) == null) {
                        characters.add(character);
                    }
                } catch (IllegalArgumentException ex) {
                    System.err.println("Skipped invalid line: " + line);
                }
            }
        } catch (IOException ex) {
            System.err.println("Could not load characters: " + ex.getMessage());
        }
    }
}