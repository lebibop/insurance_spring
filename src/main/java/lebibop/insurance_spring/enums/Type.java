package lebibop.insurance_spring.enums;

import lombok.Getter;

@Getter
public enum Type {
    TYPE_A("ОСАГО"),
    Type_B("КАСКО"),
    Type_C("type C");

    private final String displayName;

    Type(String displayName) {
        this.displayName = displayName;
    }
}
