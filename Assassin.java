public class Assassin extends GameCharacter {

    public Assassin(String name, String attack_one_name, String attack_two_name, String special_name) {
        super(name, attack_one_name, attack_two_name, special_name);
    }

    @Override
    public String getType() {
        return "Assassin";
    }

    @Override
    public int attackOne() {
        return 90;
    }

    @Override
    public int attackTwo() {
        return 140;
    }

    @Override
    public int specialAbility() {
        return 180;
    }
}