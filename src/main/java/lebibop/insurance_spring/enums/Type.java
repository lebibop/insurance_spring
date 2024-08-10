package lebibop.insurance_spring.enums;

import lombok.Getter;

@Getter
public enum Type {
    TYPE_A("ОСАГО"),
    Type_B("КАСКО"),
    Type_C("ИПОТЕКА-Ж"),
    TYPE_D("ИПОТЕКА-ИМ"),
    TYPE_E("ИПОТЕКА-К"),
    Type_F("ВЗР"),
    Type_G("ИМУЩЕСТВО"),
    TYPE_H("НС"),
    Type_K("СИН. КАРТА"),
    Type_J("ЗАЩИТА БЕЗ ОСАГО");

    private final String displayName;

    Type(String displayName) {
        this.displayName = displayName;
    }
}
