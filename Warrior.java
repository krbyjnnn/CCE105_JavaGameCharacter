public class Warrior extends GameCharacter {

    public Warrior(String name, String attack_one_name, String attack_two_name, String special_name) {
        super(name, attack_one_name, attack_two_name, special_name);
    }

    @Override
    public String getType() {
        return "Warrior";
    }

    @Override
    public int attackOne() {
        return 80;
    }

    @Override
    public int attackTwo() {
        return 120;
    }

    @Override
    public int specialAbility() {
        return 200;
    }
}