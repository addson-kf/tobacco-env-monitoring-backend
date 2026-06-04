package org.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.permissions")
public class SecurityPermissionsConfig
{
    private Endpoints endpoints;

    @Data
    public static class Endpoints {
        private User user;
        private Weather weather;
        private MonitoringPoint monitoringPoint;
        private TobaccoLeafData tobaccoLeafData;
        private SoilEnzymeActivityData soilEnzymeActivityData;
        private SoilData soilData;
        private SoilClassification soilClassification;
        private SmokingEvaluationData smokingEvaluationData;
        private SanMingBasicData sanMingBasicData;
        private SanMingXianquPolygon sanMingXianquPolygon;

    }

    @Data
    public static class User {
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

    @Data
    public static class Weather {
        private String insert;
        private String active;
        private String all;
        private String delete;
        private String restore;
        private String monitoring;
        private String update;
    }

    @Data
    public static class MonitoringPoint {
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

    @Data
    public static class TobaccoLeafData {
        private String insert;
        private String allActive;
        private String all;
        private String delete;
        private String restore;
        private String monitoringPoint;
        private String update;
    }

    @Data
    public static class SoilEnzymeActivityData {
        private String insert;
        private String AllActive;
        private String All;
        private String delete;
        private String restore;
        private String ByMonitoringPointId;
        private String update;
    }

    @Data
    public static class SoilData {
        private String allActive;
        private String all;
        private String insert;
        private String delete;
        private String restore;
        private String update;
        private String updateClassification;
        private String byId;
    }
    @Data
    public static class SoilClassification {
        private String rules;
        private String quality;
        private String update;
        private String findAll;
    }
    @Data
    public static class SmokingEvaluationData {
        private String insert;
        private String delete;
        private String update;
        private String restore;
        private String allActive;
        private String all;
        private String byMonitoringPointId;
    }
    @Data
    public static class SanMingBasicData {
        private String insert;
        private String active;
        private String all;
        private String delete;
        private String restore;
        private String update;
        private String byMonitoringPointId;
    }

    @Data
    public static class SanMingXianquPolygon {
        private String all; // admin（查询所有）
    }
}
