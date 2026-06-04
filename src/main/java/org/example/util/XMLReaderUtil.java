package org.example.util;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class XMLReaderUtil
{
    private static volatile XMLReaderUtil instance; // volatile 保证多线程可见性

    private XMLReaderUtil() {
        // 私有构造函数，防止外部实例化
    }

    // 双重检查锁定，确保线程安全的懒汉式单例
    public static XMLReaderUtil getInstance() {
        if (instance == null) {
            synchronized (XMLReaderUtil.class) {
                if (instance == null) {
                    instance = new XMLReaderUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 读取 XML 文件并返回大标签内所有小标签的键值对
     * @param filePath XML 文件路径
     * @param parentTagName 需要解析的大标签名称
     * @return Map<String, String> key 为小标签名称，value 为小标签内容
     */
    public static Map<String, String> parseXML(String filePath, String parentTagName) {
        Map<String, String> resultMap = new HashMap<>();

        try {
            // 创建 XML 解析器
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));

            // 获取 XML 的根节点
            document.getDocumentElement().normalize();

            // 查找所有的父级标签
            NodeList parentNodes = document.getElementsByTagName(parentTagName);

            if (parentNodes.getLength() == 0) {
                System.out.println("未找到大标签: " + parentTagName);
                return resultMap;
            }

            // 遍历所有符合的父标签，Node.ELEMENT_NODE 确保当前节点是 XML 元素（标签）
            for (int i = 0; i < parentNodes.getLength(); i++) {
                Node parentNode = parentNodes.item(i);

                if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element parentElement = (Element) parentNode;

                    // 遍历所有子标签
                    NodeList childNodes = parentElement.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node childNode = childNodes.item(j);

                        // 只处理元素节点（忽略注释）
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element childElement = (Element) childNode;
                            resultMap.put(childElement.getTagName(), childElement.getTextContent().trim());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }


    public List<Map<String, Object>> readTaskConfigs(InputStream inputStream) {
        List<Map<String, Object>> tasks = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList taskNodes = doc.getElementsByTagName("task");
            for (int i = 0; i < taskNodes.getLength(); i++) {
                Node node = taskNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element task = (Element) node;
                    Map<String, Object> config = new HashMap<>();
                    config.put("url", getElementText(task, "url"));
                    config.put("cookie", getElementText(task, "cookie"));
                    config.put("interval", getElementText(task, "interval"));
                    config.put("fieldMappings", readFieldMappings(task));

                    // 新增：解析 modelClass 节点
                    String modelClass = getElementText(task, "modelClass");
                    config.put("modelClass", modelClass); // 存入任务配置

                    tasks.add(config);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * 读取单个任务的字段映射关系（模型字段 -> JSON字段）
     */
    private Map<String, Object> readFieldMappings(Element taskElement) {
        Map<String, Object> mappings = new HashMap<>();
        NodeList mappingNodes = taskElement.getElementsByTagName("mapping");
        for (int i = 0; i < mappingNodes.getLength(); i++) {
            Element mapping = (Element) mappingNodes.item(i);
            String modelField = getElementText(mapping, "modelField");
            String dataField = getElementText(mapping, "dataField");
            String defaultValue = getElementText(mapping, "defaultValue"); // 解析默认值

            // 存储为包含 dataField 和 defaultValue 的子 Map
            Map<String, String> fieldConfig = new HashMap<>();
            fieldConfig.put("dataField", dataField);
            fieldConfig.put("defaultValue", defaultValue != null ? defaultValue : ""); // 无默认值时用空字符串

            mappings.put(modelField, fieldConfig); // 模型字段作为键，值包含 dataField 和 defaultValue
        }
        return mappings;
    }

    // 辅助方法：获取文档对象
    private Document getDocument(String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(filePath));
    }

    // 辅助方法：获取子元素文本
    private String getElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return null;
    }


}
