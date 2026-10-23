public class Archer extends GameCharacter {

    public Archer(String name, String attack_one_name, String attack_two_name, String special_name) {
        super(name, attack_one_name, attack_two_name, special_name);
    }

    @Override
    public String getType() {
        return "Archer";
    }

    @Override
    public int attackOne() {
        return 70;
    }

    @Override
    public int attackTwo() {
        return 110;
    }

    @Override
    public int specialAbility() {
        return 220;
    }
}