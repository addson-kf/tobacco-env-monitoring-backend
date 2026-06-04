package org.example.job;

import lombok.RequiredArgsConstructor;
import org.example.mapper.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SoftDeletePurgeJob {
    private final SoilDataMapper soilDataMapper;
    private final WeatherDataMapper weatherDataMapper;
    private final TobaccoLeafDataMapper tobaccoLeafDataMapper;
    private final SoilEnzymeActivityDataMapper soilEnzymeActivityDataMapper;
    private final SmokingEvaluationDataMapper smokingEvaluationDataMapper;
    private final SanMingBasicDataMapper sanMingBasicDataMapper;
    private final MonitoringPointMapper monitoringPointMapper;
    private final UserMapper userMapper;

    @Value("${soft-delete.retention-days:30}")
    private long retentionDays;

    // 每天凌晨 3 点执行（可配置）
    @Scheduled(cron = "${soft-delete.purge-cron:0 0 3 * * *}")
    @Transactional
    public void purgeExpired() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);

        // 强烈建议：先删“子表”，再删“父表”，避免外键约束报错
        soilDataMapper.hardDeleteExpired(cutoff);
        weatherDataMapper.hardDeleteExpired(cutoff);
        tobaccoLeafDataMapper.hardDeleteExpired(cutoff);
        soilEnzymeActivityDataMapper.hardDeleteExpired(cutoff);
        smokingEvaluationDataMapper.hardDeleteExpired(cutoff);
        sanMingBasicDataMapper.hardDeleteExpired(cutoff);
        monitoringPointMapper.hardDeleteExpired(cutoff);
    }
}
