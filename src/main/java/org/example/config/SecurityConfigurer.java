package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;


import java.util.Map;

@Component
public class SecurityConfigurer
{
    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private SecurityPermissionsConfig permissionsConfig;

    /**
     * 配置用户控制器的权限
     */
    public void configureUserEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        String basePath = apiConfig.getUser().getBasePath();
        ApiConfig.UserConfig.Endpoints endpoints = apiConfig.getUser().getEndpoints();
        SecurityPermissionsConfig.User userPermissions = permissionsConfig.getEndpoints().getUser();

        // 配置每个端点的权限
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getLogin(), userPermissions.getLogin());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getRegister(), userPermissions.getRegister());
        configureEndpoint(auth, HttpMethod.DELETE, basePath + endpoints.getDelete(), userPermissions.getDelete());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getUpdatePassword(), userPermissions.getUpdatePassword());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getSetAdmin(), userPermissions.getSetAdmin());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getGetUsers(), userPermissions.getGetUsers());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getRestoreUser(), userPermissions.getRestoreUser());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getChangePassword(), userPermissions.getChangePassword());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getVerifyToken(), userPermissions.getVerifyToken());
    }

    public void configureWeatherEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        String basePath = apiConfig.getWeather().getBasePath();
        ApiConfig.WeatherConfig.Endpoints endpoints = apiConfig.getWeather().getEndpoints();
        SecurityPermissionsConfig.Weather weatherPermissions = permissionsConfig.getEndpoints().getWeather();

        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getInsert(), weatherPermissions.getInsert());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getActive(), weatherPermissions.getActive());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAll(), weatherPermissions.getAll());
        configureEndpoint(auth, HttpMethod.DELETE, basePath + endpoints.getDelete(), weatherPermissions.getDelete());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getRestore(), weatherPermissions.getRestore());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getMonitoring(), weatherPermissions.getMonitoring());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getUpdate(), weatherPermissions.getUpdate());
    }

    public void configureMonitoringPointEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        String basePath = apiConfig.getMonitoringPoint().getBasePath();
        ApiConfig.MonitoringPointConfig.Endpoints endpoints = apiConfig.getMonitoringPoint().getEndpoints();
        SecurityPermissionsConfig.MonitoringPoint monitoringPointPermissions = permissionsConfig.getEndpoints().getMonitoringPoint();

        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getInsert(), monitoringPointPermissions.getInsert());
        configureEndpoint(auth, HttpMethod.DELETE, basePath + endpoints.getDelete(), monitoringPointPermissions.getDelete());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getUpdate(), monitoringPointPermissions.getUpdate());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getRestore(), monitoringPointPermissions.getRestore());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAllActive(), monitoringPointPermissions.getAllActive());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAll(), monitoringPointPermissions.getAll());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getBySoilCondition(), monitoringPointPermissions.getBySoilCondition());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getByWeatherCondition(), monitoringPointPermissions.getByWeatherCondition());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getByAdministrativeDivision(), monitoringPointPermissions.getByAdministrativeDivision());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getByTobaccoCondition(), monitoringPointPermissions.getByTobaccoCondition());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getByPlantingArea(), monitoringPointPermissions.getByPlantingArea());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getByPlannedContractAmount(), monitoringPointPermissions.getByPlannedContractAmount());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getByPurchaseQuantity(), monitoringPointPermissions.getByPurchaseQuantity());
    }

    public void configureTobaccoLeafDataEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        String basePath = apiConfig.getTobaccoLeafData().getBasePath();
        ApiConfig.TobaccoLeafDataConfig.Endpoints endpoints = apiConfig.getTobaccoLeafData().getEndpoints();
        SecurityPermissionsConfig.TobaccoLeafData tobaccoPermissions = permissionsConfig.getEndpoints().getTobaccoLeafData();

        // 配置每个端点的权限
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getInsert(), tobaccoPermissions.getInsert());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAllActive(), tobaccoPermissions.getAllActive());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAll(), tobaccoPermissions.getAll());
        configureEndpoint(auth, HttpMethod.DELETE, basePath + endpoints.getDelete(), tobaccoPermissions.getDelete());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getRestore(), tobaccoPermissions.getRestore());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getMonitoringPoint(), tobaccoPermissions.getMonitoringPoint());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getUpdate(), tobaccoPermissions.getUpdate());
    }

    public void configureSoilEnzymeActivityDataEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {

        String basePath = apiConfig.getSoilEnzymeActivityData().getBasePath();
        ApiConfig.SoilEnzymeActivityDataConfig.Endpoints endpoints = apiConfig.getSoilEnzymeActivityData().getEndpoints();
        SecurityPermissionsConfig.SoilEnzymeActivityData permissions = permissionsConfig.getEndpoints().getSoilEnzymeActivityData();

        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getInsert(), permissions.getInsert());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAllActive(), permissions.getAllActive());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAll(), permissions.getAll());
        configureEndpoint(auth, HttpMethod.DELETE, basePath + endpoints.getDelete(), permissions.getDelete());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getRestore(), permissions.getRestore());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getByMonitoringPointId(), permissions.getByMonitoringPointId());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getUpdate(), permissions.getUpdate());
    }

    public void configureSoilDataEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        String basePath = apiConfig.getSoilData().getBasePath();
        ApiConfig.SoilDataConfig.Endpoints endpoints = apiConfig.getSoilData().getEndpoints();
        SecurityPermissionsConfig.SoilData permissions = permissionsConfig.getEndpoints().getSoilData();

        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAllActive(), permissions.getAllActive());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAll(), permissions.getAll());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getInsert(), permissions.getInsert());
        configureEndpoint(auth, HttpMethod.DELETE, basePath + endpoints.getDelete(), permissions.getDelete());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getRestore(), permissions.getRestore());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getUpdate(), permissions.getUpdate());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getUpdateClassification(), permissions.getUpdateClassification());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getById(), permissions.getById());
    }

    public void configureSoilClassificationEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        String basePath = apiConfig.getSoilClassification().getBasePath();
        ApiConfig.SoilClassificationConfig.Endpoints endpoints = apiConfig.getSoilClassification().getEndpoints();
        SecurityPermissionsConfig.SoilClassification permissions = permissionsConfig.getEndpoints().getSoilClassification();

        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getRules(), permissions.getRules());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getQuality(), permissions.getQuality());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getUpdate(), permissions.getUpdate());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getFindAll(), permissions.getFindAll());
    }

    public void configureSmokingEvaluationDataEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        String basePath = apiConfig.getSmokingEvaluationData().getBasePath();
        ApiConfig.SmokingEvaluationDataConfig.Endpoints endpoints = apiConfig.getSmokingEvaluationData().getEndpoints();
        SecurityPermissionsConfig.SmokingEvaluationData permissions = permissionsConfig.getEndpoints().getSmokingEvaluationData();

        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getInsert(), permissions.getInsert());
        configureEndpoint(auth, HttpMethod.DELETE, basePath + endpoints.getDelete(), permissions.getDelete());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getUpdate(), permissions.getUpdate());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getRestore(), permissions.getRestore());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAllActive(), permissions.getAllActive());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAll(), permissions.getAll());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getByMonitoringPointId(), permissions.getByMonitoringPointId());
    }

    public void configureSanMingBasicDataEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        String basePath = apiConfig.getSanMingBasicData().getBasePath();
        ApiConfig.SanMingBasicDataConfig.Endpoints endpoints = apiConfig.getSanMingBasicData().getEndpoints();
        SecurityPermissionsConfig.SanMingBasicData permissions = permissionsConfig.getEndpoints().getSanMingBasicData();

        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getInsert(), permissions.getInsert());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getActive(), permissions.getActive());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAll(), permissions.getAll());
        configureEndpoint(auth, HttpMethod.DELETE, basePath + endpoints.getDelete(), permissions.getDelete());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getRestore(), permissions.getRestore());
        configureEndpoint(auth, HttpMethod.POST, basePath + endpoints.getUpdate(), permissions.getUpdate());
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getByMonitoringPointId(), permissions.getByMonitoringPointId());
    }

    public void configureSanMingXianquPolygonEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        String basePath = apiConfig.getSanMingXianquPolygon().getBasePath();
        ApiConfig.SanMingXianquPolygonConfig.Endpoints endpoints = apiConfig.getSanMingXianquPolygon().getEndpoints();
        SecurityPermissionsConfig.SanMingXianquPolygon permissions = permissionsConfig.getEndpoints().getSanMingXianquPolygon();

        // 查询所有数据
        configureEndpoint(auth, HttpMethod.GET, basePath + endpoints.getAll(), permissions.getAll());
    }

    /**
     * 配置单个端点的权限
     */
    private void configureEndpoint(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth,
            HttpMethod method,
            String path,
            String permission) {

        // 特殊处理 OPTIONS 请求，全局允许
        if (HttpMethod.OPTIONS == method) {
            auth.requestMatchers(method, path).permitAll();
            return;
        }

        // 原有权限配置逻辑...
        if (permission == null || permission.isEmpty()) {
            auth.requestMatchers(method, path).denyAll();
            return;
        }

        switch (permission) {
            case "none":
                auth.requestMatchers(method, path).permitAll();
                break;
            case "admin":
                auth.requestMatchers(method, path).hasRole("ADMIN");
                break;
            case "user":
                auth.requestMatchers(method, path).hasAnyRole("USER", "ADMIN");
                break;
            default:
                // 默认需要认证
                auth.requestMatchers(method, path).authenticated();
        }
    }
}
