public class Mage extends GameCharacter {

    public Mage(String name, String attack_one_name, String attack_two_name, String special_name) {
        super(name, attack_one_name, attack_two_name, special_name);
    }

    @Override
    public String getType() {
        return "Mage";
    }

    @Override
    public int attackOne() {
        return 60;
    }

    @Override
    public int attackTwo() {
        return 100;
    }

    @Override
    public int specialAbility() {
        return 250;
    }
}