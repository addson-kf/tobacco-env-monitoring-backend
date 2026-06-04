package org.example.config;

import org.example.service.impl.UserDetailsServiceImpl;
// import org.example.util.JwtAuthenticationFilter; // 若暂时没有此类，请先不要导入
import org.example.util.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired(required = false)
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private SecurityConfigurer securityConfigurer; // 你项目里的模块化放行/鉴权配置

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // -------- 基础放行 --------
                .authorizeHttpRequests(auth -> {
                    // 1) 预检请求必须放行（否则 CORS 永远过不去）
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.requestMatchers("/error").permitAll(); // ← 避免 404 转 /error 再被 403 混淆

                    // 登录与校验
                    auth.requestMatchers(HttpMethod.POST, "/user/login", "/api/user/login").permitAll();
                    auth.requestMatchers(HttpMethod.GET,  "/user/verifyToken", "/api/user/verifyToken").permitAll();
                    // ✅ 临时放行

                    // 用户（仅列表/只读接口）
                    auth.requestMatchers(HttpMethod.GET, "/api/user/**", "/api/users/**").permitAll();
                    // 监测点（首页列表/筛选）

                    auth.requestMatchers(HttpMethod.POST,   "/api/monitoring-points/*/restore").permitAll(); // 单段通配;
                    auth.requestMatchers(HttpMethod.DELETE, "/api/monitoring-points/*").permitAll();
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/soilData/allActive", "/soilData/allActive"
                    ).permitAll(); // 土壤表（只看未删除）

                    auth.requestMatchers(HttpMethod.GET,
                            "/api/soilData/all", "/soilData/all"
                    ).permitAll(); // 土壤表 - 含已删除数据

                    auth.requestMatchers(HttpMethod.DELETE,
                            "/api/soilData/delete/**", "/soilData/delete/**"
                    ).permitAll();

                    auth.requestMatchers(HttpMethod.POST,
                            "/api/soilData/restore/**", "/soilData/restore/**"
                    ).permitAll();

                    auth.requestMatchers(HttpMethod.GET,
                            "/api/soilData/by-county", "/soilData/by-county"
                    ).permitAll();

                    // 烟叶评吸数据（列表、查询）
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/smoking-evaluation-data/all",
                            "/api/smoking-evaluation-data/allActive",
                            "/api/smokingEvaluationData/all",
                            "/api/smokingEvaluationData/allActive"
                    ).permitAll();

                    //烟叶数据（列表、查询）
                    auth.requestMatchers(HttpMethod.GET,    "/api/tobaccoLeafData/**").permitAll();
                    auth.requestMatchers(HttpMethod.DELETE, "/api/tobaccoLeafData/delete/*").permitAll();
                    auth.requestMatchers(HttpMethod.POST,   "/api/tobaccoLeafData/restore/*").permitAll();

                    // 天气数据
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/weatherData/**", "/api/weather/**"
                    ).permitAll();

                    // 土壤分类
                    auth.requestMatchers(HttpMethod.OPTIONS, "/api/soil-classification/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET,     "/api/soil-classification/**").permitAll();

                    // 三明（基础与区域）
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/sanming/**",
                            "/api/sanming/basic/**"
                    ).permitAll();

                    // 土壤酶活性（列表、查询）
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/soilEnzymeActivityData/**"
                    ).permitAll();
                    auth.requestMatchers(HttpMethod.POST,"/api/soilEnzymeActivityData/**").permitAll();
                    auth.requestMatchers(HttpMethod.DELETE, "/api/soilEnzymeActivityData/delete/*").permitAll();  // ★ 新增这一条

                    //天气
                    auth.requestMatchers(HttpMethod.GET, "/api/weather/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/weather/**").permitAll();
                    auth.requestMatchers(HttpMethod.DELETE, "/api/weather/**").permitAll();

                    //其余基本表
                    // 县区烟叶生产历史数据
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/sanming-tobacco-production-situation",
                            "/api/sanming-tobacco-production-situation/**"
                    ).permitAll();
                    auth.requestMatchers(HttpMethod.POST,
                            "/api/sanming-tobacco-production-situation/update"
                    ).permitAll();

                    auth.requestMatchers(HttpMethod.POST,
                            "/api/sanming-tobacco-production-situation/add"
                    ).permitAll();

                    auth.requestMatchers(HttpMethod.DELETE,
                            "/api/sanming-tobacco-production-situation/delete"
                    ).permitAll();

                    //后端计算公式
                    auth.requestMatchers(HttpMethod.OPTIONS, "/api/calc/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST,    "/api/calc/execute").permitAll();
                    //后端分析
                    auth.requestMatchers(HttpMethod.GET, "/api/analysis/**", "/api/maxmin/**").permitAll();
                    auth.requestMatchers(HttpMethod.OPTIONS, "/api/analysis/**", "/api/maxmin/**").permitAll();




// 静态资源 & error
                    auth.requestMatchers(
                            "/api/monitoring-points/all",
                            "/api/monitoring-points/allActive",   // ✅ 改成 allActive
                            "/api/monitoringPoint/all",
                            "/api/monitoringPoint/allActive"      // 旧路径也顺手对齐
                    ).permitAll();



// 监测点只读接口（首页要用）
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/monitoring-points/all",
                            "/api/monitoring-points/allActive",   // ✅ 同样改成 allActive
                            "/api/monitoringPoint/all",
                            "/api/monitoringPoint/allActive"
                    ).permitAll();

                    // 4) 其余接口按你的模块化配置
                    //    ⚠️ 这一段要放在放行规则之后，才能生效且不覆盖前面的 permitAll
                    securityConfigurer.configureUserEndpoints(auth);
                    securityConfigurer.configureWeatherEndpoints(auth);
                    securityConfigurer.configureMonitoringPointEndpoints(auth);
                    securityConfigurer.configureTobaccoLeafDataEndpoints(auth);
                    securityConfigurer.configureSoilEnzymeActivityDataEndpoints(auth);
                    securityConfigurer.configureSoilDataEndpoints(auth);
                    securityConfigurer.configureSoilClassificationEndpoints(auth);
                    securityConfigurer.configureSmokingEvaluationDataEndpoints(auth);
                    securityConfigurer.configureSanMingBasicDataEndpoints(auth);
                    securityConfigurer.configureSanMingXianquPolygonEndpoints(auth);

                    // 5) 兜底：其余都需要认证
                    auth.anyRequest().authenticated();
                });


        if (jwtAuthenticationFilter != null) {
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }

    // ✅ CORS：开发阶段允许 localhost / 127 / 局域网 / “Origin: null”（file://）
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost:*", "http://127.0.0.1:*", "http://192.168.*.*", "null"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
