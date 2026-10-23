public abstract class GameCharacter {
    public static final int MAX_HEALTH = 1000;
    private static final int MAX_LENGTH = 20;

    private String name;
    private String attackOneName;
    private String attackTwoName;
    private String specialName;
    private StatusEffect specialEffect = StatusEffect.NONE;
    private int health = MAX_HEALTH;

    protected GameCharacter(String name, String attackOneName, String attackTwoName, String specialName) {
        update(name, attackOneName, attackTwoName, specialName, StatusEffect.NONE);
    }

    private static String check(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " cannot be empty.");
        }
        if (value.trim().length() > MAX_LENGTH) {
            throw new IllegalArgumentException(label + " must be " + MAX_LENGTH + " characters or fewer.");
        }
        return value.trim();
    }

    public final void update(String name, String attackOneName, String attackTwoName,
                             String specialName, StatusEffect effect) {
        String checkedName = check(name, "Name");
        String checkedOne = check(attackOneName, "Attack 1 name");
        String checkedTwo = check(attackTwoName, "Attack 2 name");
        String checkedSpecial = check(specialName, "Special ability name");
        this.name = checkedName;
        this.attackOneName = checkedOne;
        this.attackTwoName = checkedTwo;
        this.specialName = checkedSpecial;
        setSpecialEffect(effect);
    }

    public String getName() {
        return name;
    }

    public String getAttackOneName() {
        return attackOneName;
    }

    public String getAttackTwoName() {
        return attackTwoName;
    }

    public String getSpecialName() {
        return specialName;
    }

    public StatusEffect getSpecialEffect() {
        return specialEffect;
    }

    public void setSpecialEffect(StatusEffect specialEffect) {
        this.specialEffect = specialEffect == null ? StatusEffect.NONE : specialEffect;
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        health = Math.max(0, health - damage);
    }

    public void resetHealth() {
        health = MAX_HEALTH;
    }

    public String skillName(int index) {
        return index == 0 ? attackOneName : index == 1 ? attackTwoName : specialName;
    }

    public int skillDamage(int index) {
        return index == 0 ? attackOne() : index == 1 ? attackTwo() : specialAbility();
    }

    public abstract String getType();

    public abstract int attackOne();

    public abstract int attackTwo();

    public abstract int specialAbility();
}