package org.example.service.impl;
import lombok.RequiredArgsConstructor;
import org.example.dao.impl.SoilEnzymeActivityDataDaoImpl;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.SoilEnzymeActivityDataListDTO.SoilEnzymeActivityDataDTO;
import org.example.dto.SoilEnzymeActivityDataListDTO.SoilEnzymeActivityDataListDTO;
import org.example.mapper.SoilEnzymeActivityDataMapper;
import org.example.model.SoilData;
import org.example.model.SoilEnzymeActivityData;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.Page.PagingHelper;
import org.example.response.common.SoilEnzymeActivity.SoilEnzymeActivityDataListResponse;
import org.example.service.DataStorageService;
import org.example.service.ISoilDataService;
import org.example.service.ISoilEnzymeActivityDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.response.common.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 土壤酶活性数据服务实现类
 * 实现了ISoilEnzymeActivityDataService接口，并调用DAO层的方法执行业务操作
 */
@Service
@RequiredArgsConstructor
public class SoilEnzymeActivityDataServiceImpl implements ISoilEnzymeActivityDataService, DataStorageService<SoilEnzymeActivityData>
{
    @Autowired
    private SoilEnzymeActivityDataDaoImpl soilEnzymeActivityDataDao;

    @Autowired
    private MonitoringPointServiceImpl monitoringPointServiceImpl;

    @Autowired
    private final SoilEnzymeActivityDataMapper soilEnzymeActivityDataMapper;

    /**
     * 插入土壤酶活性数据
     * 先检查土壤酶活性数据是否存在，若不存在则插入数据
     *
     * @param soilEnzymeActivityDataDTO 待插入的土壤酶活性数据传输对象
     * @return 插入操作的响应结果，包含插入操作的状态信息等
     */
    @Transactional
    @Override
    public InsertResponse insertSoilEnzymeActivityData(SoilEnzymeActivityDataDTO soilEnzymeActivityDataDTO) {
        try {
            if(!monitoringPointServiceImpl.existsMonitoringPoint(soilEnzymeActivityDataDTO.getMonitoringPointId())){
                return new InsertResponse(false, "监测点Id不存在，插入失败");
            }
            SoilEnzymeActivityData soilEnzymeActivityData = convertToEntity(soilEnzymeActivityDataDTO);
            soilEnzymeActivityDataDao.insertSoilEnzymeActivityData(soilEnzymeActivityData);
            return new InsertResponse(true, "土壤酶活性数据添加成功");
        } catch (Exception e) {
            return new InsertResponse(false, "添加失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有未删除的土壤酶活性数据
     *
     * @return 包含未删除的土壤酶活性数据的响应结果，包含数据列表及相关状态信息
     */
    @Transactional(readOnly = true)
    @Override
    public SoilEnzymeActivityDataListResponse getAllActiveSoilEnzymeActivityData() {
        try {
            List<SoilEnzymeActivityData> soilEnzymeActivityDataList = soilEnzymeActivityDataDao.getAllActiveSoilEnzymeActivityData();
            SoilEnzymeActivityDataListDTO listDTO = convertToSoilEnzymeActivityDataListDTO(soilEnzymeActivityDataList);
            return SoilEnzymeActivityDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return SoilEnzymeActivityDataListResponse.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有土壤酶活性数据
     *
     * @return 包含所有土壤酶活性数据的响应结果，包含数据列表及相关状态信息
     */
    @Transactional(readOnly = true)
    @Override
    public SoilEnzymeActivityDataListResponse getAllSoilEnzymeActivityData() {
        try {
            List<SoilEnzymeActivityData> soilEnzymeActivityDataList = soilEnzymeActivityDataDao.getAllSoilEnzymeActivityData();
            SoilEnzymeActivityDataListDTO listDTO = convertToSoilEnzymeActivityDataListDTO(soilEnzymeActivityDataList);
            return SoilEnzymeActivityDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return SoilEnzymeActivityDataListResponse.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据 SoilEnzymeActivityId 删除土壤酶活性数据（软删除）
     * 先检查土壤酶活性数据是否存在，若存在则进行删除操作
     *
     * @param soilEnzymeActivityId 土壤酶活性数据的 ID
     * @return 删除操作的响应结果，包含删除操作的状态信息等
     */
    @Transactional
    @Override
    public DeleteResponse deleteSoilEnzymeActivityDataById(int soilEnzymeActivityId) {
        try {
            // 检查土壤酶活性数据是否存在
            if (!soilEnzymeActivityDataDao.checkSoilEnzymeActivityIdExists(soilEnzymeActivityId)) {
                return new DeleteResponse(false, "土壤酶活性数据不存在，删除失败");
            }
            soilEnzymeActivityDataDao.deleteSoilEnzymeActivityDataById(soilEnzymeActivityId);
            return new DeleteResponse(true, "删除成功");
        } catch (Exception e) {
            return new DeleteResponse(false, "删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据 SoilEnzymeActivityId 恢复已删除的土壤酶活性数据
     * 先检查土壤酶活性数据是否存在且已删除，若满足条件则进行恢复操作
     *
     * @param soilEnzymeActivityId 土壤酶活性数据的 ID
     * @return 恢复操作的响应结果，包含恢复操作的状态信息等
     */
    @Transactional
    @Override
    public UpdateResponse restoreSoilEnzymeActivityDataById(int soilEnzymeActivityId) {
        try {
            // 检查土壤酶活性数据是否存在
            if (!soilEnzymeActivityDataDao.checkSoilEnzymeActivityIdExists(soilEnzymeActivityId)) {
                return new UpdateResponse(false, "土壤酶活性数据不存在，恢复失败");
            }
            // 检查土壤酶活性数据是否已删除
            if (!soilEnzymeActivityDataDao.isSoilEnzymeActivityDataDeleted(soilEnzymeActivityId)) {
                return new UpdateResponse(false, "土壤酶活性数据未删除，无需恢复");
            }
            soilEnzymeActivityDataDao.restoreSoilEnzymeActivityDataById(soilEnzymeActivityId);
            return new UpdateResponse(true, "恢复成功");
        } catch (Exception e) {
            return new UpdateResponse(false, "恢复失败: " + e.getMessage());
        }
    }

    /**
     * 检查 SoilEnzymeActivityId 是否存在，该方法用于内部检验，不返回报文给客户端
     *
     * @param soilEnzymeActivityId 土壤酶活性数据的 ID
     * @return 如果存在返回 true，否则返回 false
     */
    @Transactional(readOnly = true)
    @Override
    public boolean checkSoilEnzymeActivityIdExists(int soilEnzymeActivityId) {
        try {
            return soilEnzymeActivityDataDao.checkSoilEnzymeActivityIdExists(soilEnzymeActivityId);
        } catch (Exception e) {
            // 可以根据实际情况记录日志
            return false;
        }
    }

    /**
     * 检查 SoilEnzymeActivityId 下的数据是否已删除，该方法用于内部检验，不返回报文给客户端
     *
     * @param soilEnzymeActivityId 土壤酶活性数据的 ID
     * @return 如果已删除返回 true，否则返回 false
     */
    @Transactional(readOnly = true)
    @Override
    public boolean isSoilEnzymeActivityDataDeleted(int soilEnzymeActivityId) {
        try {
            return soilEnzymeActivityDataDao.isSoilEnzymeActivityDataDeleted(soilEnzymeActivityId);
        } catch (Exception e) {
            // 可以根据实际情况记录日志
            return false;
        }
    }

    /**
     * 根据 MonitoringPointId 获取所有土壤酶活性数据
     *
     * @param monitoringPointId 监测点 ID
     * @return 包含对应监测点的所有土壤酶活性数据的响应结果，包含数据列表及相关状态信息
     */
    @Transactional(readOnly = true)
    @Override
    public SoilEnzymeActivityDataListResponse getSoilEnzymeActivityDataByMonitoringPointId(int monitoringPointId) {
        try {
            List<SoilEnzymeActivityData> soilEnzymeActivityDataList = soilEnzymeActivityDataDao.getSoilEnzymeActivityDataByMonitoringPointId(monitoringPointId);
            SoilEnzymeActivityDataListDTO listDTO = convertToSoilEnzymeActivityDataListDTO(soilEnzymeActivityDataList);
            return SoilEnzymeActivityDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return SoilEnzymeActivityDataListResponse.error("查询失败: " + e.getMessage());
        }
    }

    // ========== 辅助方法 ==========
    /**
     * 将 SoilEnzymeActivityData 实体对象转换为 SoilEnzymeActivityDataDTO 传输对象
     *
     * @param soilEnzymeActivityData 土壤酶活性数据实体对象
     * @return 土壤酶活性数据传输对象
     */
    private SoilEnzymeActivityDataDTO convertToDTO(SoilEnzymeActivityData soilEnzymeActivityData) {
        SoilEnzymeActivityDataDTO dto = new SoilEnzymeActivityDataDTO();
        dto.setSoilEnzymeActivityId(soilEnzymeActivityData.getSoilEnzymeActivityId());
        dto.setMonitoringPointId(soilEnzymeActivityData.getMonitoringPointId());
        dto.setSoilEnzymeActivityAnalysis(soilEnzymeActivityData.getSoilEnzymeActivityData());
        dto.setValue(soilEnzymeActivityData.getValue());
        dto.setDescription(soilEnzymeActivityData.getDescribe());
        dto.setSamplingDate(soilEnzymeActivityData.getSamplingDate());
        dto.setIsDeleted(soilEnzymeActivityData.isDelete());
        return dto;
    }

    /**
     * 将 SoilEnzymeActivityData 列表转换为 SoilEnzymeActivityDataListDTO 传输对象
     *
     * @param soilEnzymeActivityDataList 土壤酶活性数据实体列表
     * @return 包含土壤酶活性数据传输对象列表的 SoilEnzymeActivityDataListDTO
     */
    private SoilEnzymeActivityDataListDTO convertToSoilEnzymeActivityDataListDTO(List<SoilEnzymeActivityData> soilEnzymeActivityDataList) {
        List<SoilEnzymeActivityDataDTO> dtoList = soilEnzymeActivityDataList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new SoilEnzymeActivityDataListDTO(dtoList);
    }

    /**
     * 将 SoilEnzymeActivityDataDTO 传输对象转换为 SoilEnzymeActivityData 实体对象
     *
     * @param soilEnzymeActivityDataDTO 土壤酶活性数据传输对象
     * @return 土壤酶活性数据实体对象
     */
    private SoilEnzymeActivityData convertToEntity(SoilEnzymeActivityDataDTO soilEnzymeActivityDataDTO) {
        SoilEnzymeActivityData soilEnzymeActivityData = new SoilEnzymeActivityData();
        soilEnzymeActivityData.setSoilEnzymeActivityId(soilEnzymeActivityDataDTO.getSoilEnzymeActivityId());
        soilEnzymeActivityData.setMonitoringPointId(soilEnzymeActivityDataDTO.getMonitoringPointId());
        soilEnzymeActivityData.setSoilEnzymeActivityData(soilEnzymeActivityDataDTO.getSoilEnzymeActivityAnalysis());
        soilEnzymeActivityData.setValue(soilEnzymeActivityDataDTO.getValue());
        soilEnzymeActivityData.setDescribe(soilEnzymeActivityDataDTO.getDescription());
        soilEnzymeActivityData.setSamplingDate(soilEnzymeActivityDataDTO.getSamplingDate());
        soilEnzymeActivityData.setDelete(soilEnzymeActivityDataDTO.isDeleted());
        return soilEnzymeActivityData;
    }

    /**
     * 根据 SoilEnzymeActivityId 更新土壤酶活性数据
     * 执行更新操作前，检查要更新的土壤酶活性数据的 SoilEnzymeActivityId 是否存在
     *
     * @param soilEnzymeActivityDataDTO 包含要更新的土壤酶活性数据的对象，其中应包含 SoilEnzymeActivityId 以及其他要更新的字段值
     * @return 更新操作的响应结果，包含更新操作的状态信息等
     */
    @Transactional
    @Override
    public UpdateResponse updateSoilEnzymeActivityDataById(SoilEnzymeActivityDataDTO soilEnzymeActivityDataDTO) {
        try {
            int soilEnzymeActivityId = soilEnzymeActivityDataDTO.getSoilEnzymeActivityId();
            // 检查 soilEnzymeActivityId 是否存在
            if (!checkSoilEnzymeActivityIdExists(soilEnzymeActivityId)) {
                return new UpdateResponse(false, "要更新的土壤酶活性数据的 SoilEnzymeActivityId 不存在，更新失败");
            }
            // 将 DTO 对象转换为实体对象
            SoilEnzymeActivityData soilEnzymeActivityData = convertToEntity(soilEnzymeActivityDataDTO);
            // 调用数据访问层方法执行更新操作，并获取受影响的行数
            int rowsAffected = soilEnzymeActivityDataDao.updateSoilEnzymeActivityDataById(soilEnzymeActivityData);
            if (rowsAffected > 0) {
                return new UpdateResponse(true, "根据 SoilEnzymeActivityId 更新土壤酶活性数据成功");
            } else {
                return new UpdateResponse(false, "根据 SoilEnzymeActivityId 更新土壤酶活性数据失败");
            }
        } catch (Exception e) {
            return new UpdateResponse(false, "更新失败: " + e.getMessage());
        }
    }

    /**
     * 实现 DataStorageService 接口的 saveAll 方法，用于批量保存土壤酶活性数据
     * @param dataList 要保存的土壤酶活性数据列表
     */
    @Transactional
    @Override
    public void saveAll(List<SoilEnzymeActivityData> dataList) {
        for (SoilEnzymeActivityData data : dataList) {
            try {
                if (!monitoringPointServiceImpl.existsMonitoringPoint(data.getMonitoringPointId())) {
                    continue; // 跳过监测点 ID 不存在的数据
                }
                soilEnzymeActivityDataDao.insertSoilEnzymeActivityData(data);
            } catch (Exception e) {
                // 可根据实际情况记录日志
            }
        }
    }



    // 分页查询
    @Override
    public PageResult<SoilEnzymeActivityDataDTO> pageByCounty(PageRequest pr, String countyDistrict) {
        return PagingHelper.page(
                pr,
                // 1) 总数
                () -> soilEnzymeActivityDataMapper.countByCounty(countyDistrict),
                // 2) 当前页数据
                () -> soilEnzymeActivityDataMapper.findByCountyAndPage(
                        countyDistrict, pr.getSize(), pr.offset()),
                // 3) 行 -> DTO
                this::convertToDTO   // 形如: SoilData -> SoilDataDTO
        );
    }

    /** 按县区获取某字段全部取值（字段名白名单） */
    @Override
    public List<Object> listFieldValuesByCounty(String countyDistrict, ISoilEnzymeActivityDataService.ISoilEnzymeActivityDataField field) {
        Objects.requireNonNull(field, "field 不能为空");
        // 传入 Mapper 的 field 是字符串，但来自这个枚举，安全白名单
        return soilEnzymeActivityDataMapper.listFieldValuesByCounty(countyDistrict, field.name());
    }

    /** 调试输出，可在 PrintForms 或控制台调用 */
    @Override
    public void debugPrintByCounty(int page, int size, String county) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;

        long total = soilEnzymeActivityDataMapper.countByCounty( county);
        long totalPages = (total + size - 1) / size;

        List<SoilEnzymeActivityData> rows =
                soilEnzymeActivityDataMapper.findByCountyAndPage( county, size, offset);

        System.out.printf("分页结果：page=%d/%d, size=%d, total=%d%n",
                page, totalPages, size, total);
        rows.forEach(System.out::println); // 记得 DTO 已经重写 toString()
    }

    @Override
    public List<Object> listFieldValuesByRegion(String countyDistrict, String town, String village,ISoilEnzymeActivityDataField field) {
        // 直接用白名单字段枚举的 name() 传给 Mapper 的 <choose/>
        return soilEnzymeActivityDataMapper.listFieldValuesByRegion(countyDistrict, town, village, field.name());
    }


}
