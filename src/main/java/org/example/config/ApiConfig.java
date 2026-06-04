package org.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiConfig
{
    private UserConfig user;
    private WeatherConfig weather;
    private MonitoringPointConfig monitoringPoint;
    private TobaccoLeafDataConfig tobaccoLeafData;
    private SoilEnzymeActivityDataConfig soilEnzymeActivityData;
    private SoilDataConfig soilData;
    private SoilClassificationConfig soilClassification;
    private SmokingEvaluationDataConfig smokingEvaluationData;
    private SanMingBasicDataConfig sanMingBasicData;

    private SanMingXianquPolygonConfig sanMingXianquPolygon;

    private CalculationConfig calculation;


    @Data
    public static class UserConfig {
        private String basePath;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private String login;
            private String register;
            private String delete;
            private String updatePassword;
            private String setAdmin;
            private String getUsers;
            private String restoreUser;
            private String changePassword;
            private String verifyToken;
        }
    }

    @Data
    public static class WeatherConfig {
        private String basePath;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private String insert;
            private String active;
            private String all;
            private String delete;
            private String restore;
            private String monitoring;
            private String update;
        }
    }

    @Data
    public static class MonitoringPointConfig {
        private String basePath;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private String insert;
            private String delete;
            private String update;
            private String restore;
            private String allActive;
            private String all;
            private String bySoilCondition;
            private String byWeatherCondition;
            private String byAdministrativeDivision;
            private String byTobaccoCondition;
            private String byPlantingArea;
            private String byPlannedContractAmount;
            private String byPurchaseQuantity;
        }
    }

    @Data
    public static class TobaccoLeafDataConfig {
        private String basePath;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private String insert;
            private String allActive;
            private String all;
            private String delete;
            private String restore;
            private String monitoringPoint;
            private String update;
        }
    }

    @Data
    public static class SoilEnzymeActivityDataConfig {
        private String basePath;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private String insert;
            private String AllActive;
            private String All;
            private String delete;
            private String restore;
            private String ByMonitoringPointId;
            private String update;
        }
    }

    @Data
    public static class SoilDataConfig {
        private String basePath;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private String allActive;
            private String all;
            private String insert;
            private String delete;
            private String restore;
            private String update;
            private String updateClassification;
            private String byId;
        }
    }

    @Data
    public static class SoilClassificationConfig {
        private String basePath;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private String rules;
            private String quality;
            private String update;
            private String findAll;
        }
    }

    @Data
    public static class SmokingEvaluationDataConfig {
        private String basePath;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private String insert;
            private String delete;
            private String update;
            private String restore;
            private String allActive;
            private String all;
            private String byMonitoringPointId;
        }
    }
    @Data
    public static class SanMingBasicDataConfig {
        private String basePath;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private String insert;
            private String active;
            private String all;
            private String delete;
            private String restore;
            private String update;
            private String byMonitoringPointId;
        }
    }

    @Data
    public static class SanMingXianquPolygonConfig {
        private String basePath; // 基础路径：/sanmingxianqu/polygon
        private Endpoints endpoints; // 端点配置

        @Data
        public static class Endpoints {
            private String all; // /all
        }
    }

    @Data
    public static class CalculationConfig {
        private String basePath;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private String execute;     // 例如 /execute
            private String attributes;  // 可选：/attributes（如果你要下发属性字典）
        }
    }
}
