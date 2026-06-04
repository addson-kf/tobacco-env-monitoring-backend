package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.impl.SmokingEvaluationDataDaoImpl;
import org.example.dto.SmokingEvaluationDataListDTO.SmokingEvaluationDataDTO;
import org.example.dto.SmokingEvaluationDataListDTO.SmokingEvaluationDataListDTO;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.mapper.SmokingEvaluationDataMapper;
import org.example.model.SmokingEvaluationData;
import org.example.model.SoilData;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.response.common.Page.PagingHelper;
import org.example.response.common.SmokingEvaluationData.SmokingEvaluationDataListResponse;
import org.example.service.DataStorageService;
import org.example.service.ISmokingEvaluationDataService;
import org.example.service.ISoilDataService;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 吸烟评估数据服务实现类
 */
@Service
@RequiredArgsConstructor
public class SmokingEvaluationDataServiceImpl implements ISmokingEvaluationDataService, DataStorageService<SmokingEvaluationData>
{

    private static final Logger log = LoggerFactory.getLogger(SmokingEvaluationDataServiceImpl.class);

    @Autowired
    private SmokingEvaluationDataDaoImpl smokingEvaluationDataDao;

    @Autowired
    private MonitoringPointServiceImpl monitoringPointServiceImpl;

    @Autowired
    private final SmokingEvaluationDataMapper smokingEvaluationDataMapper;



    /**
     * 插入一条新的吸烟评估数据。
     * 先检查吸烟评估数据是否存在，若不存在则插入数据。
     *
     * @param smokingEvaluationDataDTO 吸烟评估数据传输对象
     * @return 插入操作的响应结果，包含插入操作的状态信息等
     */
    @Transactional
    @Override
    public InsertResponse insertSmokingEvaluationData(SmokingEvaluationDataDTO smokingEvaluationDataDTO) {
        try {
            if(!monitoringPointServiceImpl.existsMonitoringPoint(smokingEvaluationDataDTO.getMonitoringPointId())){
                return new InsertResponse(false, "监测点Id不存在，插入失败");
            }
            SmokingEvaluationData smokingEvaluationData = convertToEntity(smokingEvaluationDataDTO);
            smokingEvaluationDataDao.insertSmokingEvaluationData(smokingEvaluationData);
            return new InsertResponse(true, "吸烟评估数据添加成功");
        } catch (Exception e) {
            return new InsertResponse(false, "添加失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有未删除的吸烟评估数据。
     *
     * @return 包含未删除的吸烟评估数据的响应结果，包含数据列表及相关状态信息
     */
    @Transactional(readOnly = true)
    @Override
    public SmokingEvaluationDataListResponse getAllActiveSmokingEvaluationData() {
        try {
            List<SmokingEvaluationData> smokingEvaluationDataList = smokingEvaluationDataDao.getAllActiveSmokingEvaluationData();
            SmokingEvaluationDataListDTO listDTO = convertToSmokingEvaluationDataListDTO(smokingEvaluationDataList);
            return SmokingEvaluationDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return SmokingEvaluationDataListResponse.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有吸烟评估数据。
     *
     * @return 包含所有吸烟评估数据的响应结果，包含数据列表及相关状态信息
     */
    @Transactional(readOnly = true)
    @Override
    public SmokingEvaluationDataListResponse getAllSmokingEvaluationData() {
        try {
            List<SmokingEvaluationData> rows =
                    smokingEvaluationDataDao.getAllSmokingEvaluationData();
            if (rows == null) rows = java.util.Collections.emptyList();   // ← 兜底

            SmokingEvaluationDataListDTO listDTO = convertToSmokingEvaluationDataListDTO(rows);
            return SmokingEvaluationDataListResponse.ok(listDTO);         // ← 空数组也算成功
        } catch (Exception e) {
            log.error(() -> "getAllSmokingEvaluationData 出错", e);             // ← 打完整栈
            String msg = "查询失败: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
            return SmokingEvaluationDataListResponse.error(msg);
        }
    }

    /**
     * 根据 smokingEvaluationId 删除吸烟评估数据（软删除）。
     * 先检查吸烟评估数据是否存在，若存在则进行删除操作。
     *
     * @param smokingEvaluationId 吸烟评估数据的ID
     * @return 删除操作的响应结果，包含删除操作的状态信息等
     */
    @Transactional
    @Override
    public DeleteResponse deleteSmokingEvaluationDataById(int smokingEvaluationId) {
        try {
            // 检查吸烟评估数据是否存在
            if (!smokingEvaluationDataDao.checkSmokingEvaluationIdExists(smokingEvaluationId)) {
                return new DeleteResponse(false, "吸烟评估数据不存在，删除失败");
            }
            smokingEvaluationDataDao.deleteSmokingEvaluationDataById(smokingEvaluationId);
            return new DeleteResponse(true, "删除成功");
        } catch (Exception e) {
            return new DeleteResponse(false, "删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据 smokingEvaluationId 恢复已删除的吸烟评估数据。
     * 先检查吸烟评估数据是否存在且已删除，若满足条件则进行恢复操作。
     *
     * @param smokingEvaluationId 吸烟评估数据的ID
     * @return 恢复操作的响应结果，包含恢复操作的状态信息等
     */
    @Transactional
    @Override
    public UpdateResponse restoreSmokingEvaluationDataById(int smokingEvaluationId) {
        try {
            // 检查吸烟评估数据是否存在
            if (!smokingEvaluationDataDao.checkSmokingEvaluationIdExists(smokingEvaluationId)) {
                return new UpdateResponse(false, "吸烟评估数据不存在，恢复失败");
            }
            // 检查吸烟评估数据是否已删除
            if (!smokingEvaluationDataDao.isSmokingEvaluationDataDeleted(smokingEvaluationId)) {
                return new UpdateResponse(false, "吸烟评估数据未删除，无需恢复");
            }
            smokingEvaluationDataDao.restoreSmokingEvaluationDataById(smokingEvaluationId);
            return new UpdateResponse(true, "恢复成功");
        } catch (Exception e) {
            return new UpdateResponse(false, "恢复失败: " + e.getMessage());
        }
    }

    /**
     * 检查 smokingEvaluationId 是否存在，该方法用于内部检验，不返回报文给客户端。
     *
     * @param smokingEvaluationId 吸烟评估数据的ID
     * @return 如果存在返回true，否则返回false
     */
    @Transactional(readOnly = true)
    @Override
    public boolean checkSmokingEvaluationIdExists(int smokingEvaluationId) {
        try {
            return smokingEvaluationDataDao.checkSmokingEvaluationIdExists(smokingEvaluationId);
        } catch (Exception e) {
            // 可以根据实际情况记录日志
            return false;
        }
    }

    /**
     * 检查 smokingEvaluationId 下的数据是否已删除，该方法用于内部检验，不返回报文给客户端。
     *
     * @param smokingEvaluationId 吸烟评估数据的ID
     * @return 如果已删除返回true，否则返回false
     */
    @Transactional(readOnly = true)
    @Override
    public boolean isSmokingEvaluationDataDeleted(int smokingEvaluationId) {
        try {
            return smokingEvaluationDataDao.isSmokingEvaluationDataDeleted(smokingEvaluationId);
        } catch (Exception e) {
            // 可以根据实际情况记录日志
            return false;
        }
    }

    /**
     * 根据 monitoringPointId 获取所有吸烟评估数据。
     *
     * @param monitoringPointId 监测点ID
     * @return 包含对应监测点的所有吸烟评估数据的响应结果，包含数据列表及相关状态信息
     */
    @Transactional(readOnly = true)
    @Override
    public SmokingEvaluationDataListResponse getSmokingEvaluationDataByMonitoringPointId(int monitoringPointId) {
        try {
            List<SmokingEvaluationData> smokingEvaluationDataList = smokingEvaluationDataDao.getSmokingEvaluationDataByMonitoringPointId(monitoringPointId);
            SmokingEvaluationDataListDTO listDTO = convertToSmokingEvaluationDataListDTO(smokingEvaluationDataList);
            return SmokingEvaluationDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return SmokingEvaluationDataListResponse.error("查询失败: " + e.getMessage());
        }
    }

    // ========== 辅助方法 ==========
    /**
     * 将 SmokingEvaluationDataDTO 转换为 SmokingEvaluationData 实体对象。
     *
     * @param dto 吸烟评估数据传输对象
     * @return 吸烟评估数据实体对象
     */
    private SmokingEvaluationData convertToEntity(SmokingEvaluationDataDTO dto) {
        SmokingEvaluationData smokingEvaluationData = new SmokingEvaluationData();
        smokingEvaluationData.setSmokingEvaluationId(dto.getSmokingEvaluationId());
        smokingEvaluationData.setMonitoringPointId(dto.getMonitoringPointId());
        smokingEvaluationData.setSmokingEvaluationData(dto.getSmokingEvaluationData());
        smokingEvaluationData.setDescribe(dto.getDescribe());
        smokingEvaluationData.setSamplingDate(dto.getSamplingDate());
        smokingEvaluationData.setDelete(dto.isDelete());
        return smokingEvaluationData;
    }

    /**
     * 将 SmokingEvaluationData 实体对象转换为 SmokingEvaluationDataDTO 传输对象。
     *
     * @param smokingEvaluationData 吸烟评估数据实体对象
     * @return 吸烟评估数据传输对象
     */
    private SmokingEvaluationDataDTO convertToDTO(SmokingEvaluationData smokingEvaluationData) {
        SmokingEvaluationDataDTO dto = new SmokingEvaluationDataDTO();
        dto.setSmokingEvaluationId(smokingEvaluationData.getSmokingEvaluationId());
        dto.setMonitoringPointId(smokingEvaluationData.getMonitoringPointId());
        dto.setSmokingEvaluationData(smokingEvaluationData.getSmokingEvaluationData());
        dto.setDescribe(smokingEvaluationData.getDescribe());
        dto.setSamplingDate(smokingEvaluationData.getSamplingDate());
        dto.setDelete(smokingEvaluationData.isDelete());
        return dto;
    }

    /**
     * 将 SmokingEvaluationData 列表转换为 SmokingEvaluationDataListDTO 传输对象。
     *
     * @param smokingEvaluationDataList 吸烟评估数据实体列表
     * @return 包含吸烟评估数据传输对象列表的SmokingEvaluationDataListDTO
     */
    private SmokingEvaluationDataListDTO convertToSmokingEvaluationDataListDTO(List<SmokingEvaluationData> smokingEvaluationDataList) {
        List<SmokingEvaluationDataDTO> dtoList = smokingEvaluationDataList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new SmokingEvaluationDataListDTO(dtoList);
    }


    /**
     * 根据 SmokingEvaluationId 更新吸烟评估数据。
     * 执行更新操作前，不进行额外的特殊检查（假设传入的SmokingEvaluationData对象数据合理），直接尝试更新。
     *
     * @param smokingEvaluationData 包含要更新的吸烟评估数据的对象，其中应包含SmokingEvaluationId以及其他要更新的字段值
     * @return 更新操作的响应结果，包含更新操作的状态信息等
     */
    @Transactional
    @Override
    public UpdateResponse updateSmokingEvaluationDataById(SmokingEvaluationDataDTO smokingEvaluationData) {
        try {
            int smokingEvaluationId = smokingEvaluationData.getSmokingEvaluationId();
            // 检查smokingEvaluationId是否存在
            if (!checkSmokingEvaluationIdExists(smokingEvaluationId)) {
                return new UpdateResponse(false, "吸烟评估数据的smokingEvaluationId不存在，更新失败");
            }
            if(!monitoringPointServiceImpl.existsMonitoringPoint(smokingEvaluationData.getMonitoringPointId())){
                return new UpdateResponse(false, "监测点Id不存在，更新失败");
            }
            // 调用数据访问层方法执行更新操作，并获取受影响的行数
            int rowsAffected =
                    smokingEvaluationDataDao.updateSmokingEvaluationDataById(convertToEntity(smokingEvaluationData));
            if (rowsAffected > 0) {
                return new UpdateResponse(true, "更新吸烟评估数据成功");
            } else {
                return new UpdateResponse(false, "更新吸烟评估数据失败");
            }
        } catch (Exception e) {
            return new UpdateResponse(false, "更新失败: " + e.getMessage());
        }
    }

    /**
     * 实现 DataStorageService 接口的 saveAll 方法，批量保存吸烟评估数据
     * @param dataList 要保存的吸烟评估数据列表
     */
    @Transactional
    @Override
    public void saveAll(List<SmokingEvaluationData> dataList) {
        for (SmokingEvaluationData data : dataList) {
            try {
                if (!monitoringPointServiceImpl.existsMonitoringPoint(data.getMonitoringPointId())) {
                    continue; // 跳过监测点 ID 不存在的数据
                }
                smokingEvaluationDataDao.insertSmokingEvaluationData(data);
            } catch (Exception e) {
                // 可根据实际情况记录日志
            }
        }
    }



    // 分页查询
    @Override
    public PageResult<SmokingEvaluationDataDTO> pageByCounty(PageRequest pr, String countyDistrict) {
        return PagingHelper.page(
                pr,
                () -> smokingEvaluationDataMapper.countByCounty(countyDistrict),
                () -> smokingEvaluationDataMapper.findByCountyAndPage(countyDistrict, pr.getSize(), pr.offset()),
                this::convertToDTO
        );
    }

    /** 按县区获取某字段全部取值（字段名白名单） */
    @Override
    public List<Object> listFieldValuesByCounty(String countyDistrict, ISmokingEvaluationDatafield field) {
        Objects.requireNonNull(field, "field 不能为空");
        // 传入 Mapper 的 field 是字符串，但来自这个枚举，安全白名单
        return smokingEvaluationDataMapper.listFieldValuesByCounty(countyDistrict, field.name());
    }

    /** 调试输出，可在 PrintForms 或控制台调用 */
    public void debugPrintByCounty(int page, int size, String county) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;

        long total = smokingEvaluationDataMapper.countByCounty( county);
        long totalPages = (total + size - 1) / size;

        List<SmokingEvaluationData> rows =
               smokingEvaluationDataMapper.findByCountyAndPage( county, size, offset);

        System.out.printf("分页结果：page=%d/%d, size=%d, total=%d%n",
                page, totalPages, size, total);
        rows.forEach(System.out::println); // 记得 DTO 已经重写 toString()
    }

    @Override
    public List<Object> listFieldValuesByRegion(String countyDistrict, String town, String village, ISmokingEvaluationDatafield field) {
        // 直接用白名单字段枚举的 name() 传给 Mapper 的 <choose/>
        return smokingEvaluationDataMapper.listFieldValuesByRegion(countyDistrict, town, village, field.name());
    }
}
