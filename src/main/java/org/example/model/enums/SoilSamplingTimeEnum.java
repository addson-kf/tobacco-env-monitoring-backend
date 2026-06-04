package org.example.model.enums;

public enum SoilSamplingTimeEnum {
    BEFORE_TOBACCO_PLANTING("Before_tobacco_field_ridging", "种烟前"),
    AFTER_TOBACCO_PLANTING("After_tobacco_leaf_harvesting", "种烟后"),
    AFTER_HARVESTING_RICE("After_rice_harvest", "种稻后");

    private final String code;
    private final String zh;

    SoilSamplingTimeEnum(String code, String zh) {
        this.code = code;
        this.zh = zh;
    }

    public String getCode() {
        return code;
    }

    public String getZh() {
        return zh;
    }

    // 允许输入“英文code/中文label”
    public static SoilSamplingTimeEnum fromAny(String v) {
        if (v == null) return null;
        for (SoilSamplingTimeEnum e : values()) {
            if (e.code.equalsIgnoreCase(v) || e.zh.equals(v)) {
                return e;
            }
        }
        // 不认识就返回 null 或抛异常，看你系统风格
        throw new IllegalArgumentException("Unknown SoilSamplingTime: " + v);
    }

    public static String toZh(String code) {
        if (code == null) return null;
        for (SoilSamplingTimeEnum e : values()) {
            if (e.code.equalsIgnoreCase(code)) return e.zh;
        }
        return code; // 兜底：不认识就原样返回
    }
}
