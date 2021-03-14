package uni.ruse.welearn.welearn.enums;

/**
 * @author Ivelin Dimitrov
 */
public enum Role {
    ADMINISTRATOR("ADMINISTRATOR"),
    TAUGHT("TAUGHT"),
    TEACHING("TEACHING");

    private final String role;

    Role(final String name) {
        this.role = name;
    }

    public String getRole() {
        return role;
    }
}
