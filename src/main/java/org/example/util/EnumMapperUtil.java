package org.example.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EnumMapperUtil
{
    private static volatile EnumMapperUtil instance;
    private final Map<String, Map<String, Enum<?>>> enumMappings = new HashMap<>();

    private EnumMapperUtil() {
    }

    public static EnumMapperUtil getInstance() {
        if (instance == null) {
            synchronized (EnumMapperUtil.class) {
                if (instance == null) {
                    instance = new EnumMapperUtil();
                }
            }
        }
        return instance;
    }

    // 加载枚举映射（从 XML 读取）
    public void loadMappings(InputStream inputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList enumNodes = doc.getElementsByTagName("enum");
            for (int i = 0; i < enumNodes.getLength(); i++) {
                Node enumNode = enumNodes.item(i);
                if (enumNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element enumElement = (Element) enumNode;
                    String enumClassName = enumElement.getAttribute("class");
                    Class<Enum> enumClass = (Class<Enum>) Class.forName(enumClassName);

                    Map<String, Enum<?>> mapping = new HashMap<>();
                    NodeList entryNodes = enumElement.getElementsByTagName("entry");
                    for (int j = 0; j < entryNodes.getLength(); j++) {
                        Element entryElement = (Element) entryNodes.item(j);
                        String key = entryElement.getAttribute("key");
                        String value = entryElement.getAttribute("value");
                        Enum<?> enumValue = Enum.valueOf(enumClass, value);
                        mapping.put(key, enumValue);
                    }
                    enumMappings.put(enumClassName, mapping);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据枚举类名和键获取枚举值
    public Enum<?> mapValue(String enumClassName, String key) {
        Map<String, Enum<?>> mapping = enumMappings.get(enumClassName);
        return mapping != null ? mapping.get(key) : null;
    }
}
