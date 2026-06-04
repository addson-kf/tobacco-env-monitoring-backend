package org.example.model.downloadtest;

public class WarningData
{
    private String device22;
    private String content22;
    private String time22;

    private String test22;

    // Getter 和 Setter 方法
    public String getDevice22() {
        return device22;
    }

    public void setDevice22(String device) {
        this.device22 = device;
    }

    public String getContent22() {
        return content22;
    }

    public void setContent22(String content) {
        this.content22 = content;
    }

    public String getTime22() {
        return time22;
    }

    public void setTime22(String time) {
        this.time22 = time;
    }

    public String getTest22() {
        return test22;
    }

    public void setTest22(String time) {
        this.test22 = time;
    }

    //Jackson 在反序列化 JSON 时需要一个默认构造函数来实例化对象:即这个无参构造方法
    public WarningData() {
    }

    public WarningData(String device, String content, String time) {
        this.device22 = device;
        this.content22 = content;
        this.time22 = time;
    }

    //输出所有字段用于测试
    @Override
    public String toString() {
        return "Device: " + device22 + ", Content: " + content22 + ", Time: " + time22 + ", 测试: " + test22;
    }
}
