package org.example;

import org.example.Test.PrintForms;
import org.example.service.impl.SanMingBasicDataServiceImpl;
import org.example.util.EnumMapperUtil;
import org.example.util.UniversalExcelImporter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.io.InputStream;

@SpringBootApplication
@EnableScheduling
public class Main
{

    private static SanMingBasicDataServiceImpl service;
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);

        EnumMapperUtil enumMapperUtil = EnumMapperUtil.getInstance();
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("enum-mappings.xml");
        if (inputStream != null) {
            // 调用 loadMappings 函数加载配置
            enumMapperUtil.loadMappings(inputStream);
            System.out.println("枚举映射配置加载成功");
        } else {
            System.err.println("未找到枚举映射配置文件");
        }

        PrintForms printForms = context.getBean(PrintForms.class);
        printForms.printAllReports();
    }
}

