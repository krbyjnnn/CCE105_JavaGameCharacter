public enum StatusEffect {
    NONE("None"),
    STUN("Stun"),
    MUTE("Mute");

    private final String label;

    StatusEffect(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}