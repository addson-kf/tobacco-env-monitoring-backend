package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.ISoilDataDao;
import org.example.dao.impl.SoilDataDaoImpl;
import org.example.dto.MonitoringPointListDTO.MonitoringPointDTO;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.SoilDataListDTO.SoilDataListDTO;
import org.example.mapper.MonitoringPointMapper;
import org.example.mapper.SoilDataMapper;
import org.example.model.MonitoringPoint;
import org.example.model.SoilData;
import org.example.model.enums.SoilSamplingTimeEnum;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.Page.PagingHelper;
import org.example.response.common.SoilData.SoilDataListResponse;
import org.example.service.DataStorageService;
import org.example.service.ISoilDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.service.IMonitoringPointService;
import org.example.service.ISoilDataService;
import org.example.response.common.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SoilDataService 实现类
 * 提供土壤数据的业务逻辑实现
 */
@Service
@RequiredArgsConstructor
public class SoilDataServiceImpl implements ISoilDataService, DataStorageService<SoilData>
{
    @Autowired
    private SoilDataDaoImpl soilDataDAO;

    @Autowired
    private final SoilDataMapper soilDataMapper;

    @Autowired
    private MonitoringPointServiceImpl monitoringPointServiceImpl;

    @Autowired
    private MonitoringPointMapper monitoringPointMapper;

    /**
     * 查询所有未删除的土壤数据
     *
     * @return 包含所有未删除土壤数据的 SoilDataListResponse 对象
     */
    @Override
    @Transactional(readOnly = true)
    public SoilDataListResponse findAll() {
        try {
            List<SoilData> soilDataList = soilDataDAO.findAll();
            List<SoilDataDTO> dtoList = soilDataList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            SoilDataListDTO data = new SoilDataListDTO(dtoList);
            return SoilDataListResponse.ok(data);
        } catch (Exception e) {
            return SoilDataListResponse.error("查询未删除的土壤数据失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有土壤数据（包括已删除的），仅管理员使用
     *
     * @return 包含所有土壤数据的 SoilDataListResponse 对象
     */
    @Override
    @Transactional(readOnly = true)
    public SoilDataListResponse findAllForAdmin() {
        try {
            List<SoilData> soilDataList = soilDataDAO.findAllForAdmin();
            List<SoilDataDTO> dtoList = soilDataList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            SoilDataListDTO data = new SoilDataListDTO(dtoList);
            return SoilDataListResponse.ok(data);
        } catch (Exception e) {
            return SoilDataListResponse.error("查询所有土壤数据失败: " + e.getMessage());
        }
    }

    /**
     * 插入一条土壤数据，并进行分类处理
     *
     * @param soilDataDTO 待插入的土壤数据传输对象
     * @return 插入操作的 InsertResponse 响应结果
     */
    @Override
    @Transactional
    public InsertResponse insertWithClassification(SoilDataDTO soilDataDTO) {
        try {
            if(!monitoringPointServiceImpl.existsMonitoringPoint(soilDataDTO.getMonitoringPointID())){
                return new InsertResponse(false, "监测点Id不存在，更新失败");
            }
            SoilData soilData = convertToEntity(soilDataDTO);
            int rowsAffected = soilDataDAO.insertWithClassification(soilData);
            if (rowsAffected > 0) {
                return new InsertResponse(true, "插入土壤数据成功");
            } else {
                return new InsertResponse(false, "插入土壤数据失败");
            }
        } catch (Exception e) {
            return new InsertResponse(false, "插入失败: " + e.getMessage());
        }
    }

    /**
     * 根据土壤数据 ID 进行软删除操作
     *
     * @param soilDataID 待删除的土壤数据 ID
     * @return 删除操作的 DeleteResponse 响应结果
     */
    @Override
    @Transactional
    public DeleteResponse softDeleteById(int soilDataID) {
        try {

            int rowsAffected = soilDataDAO.softDeleteById(soilDataID);
            if (rowsAffected > 0) {
                return new DeleteResponse(true, "软删除土壤数据成功");
            } else {
                return new DeleteResponse(false, "软删除土壤数据失败");
            }
        } catch (Exception e) {
            return new DeleteResponse(false, "软删除失败: " + e.getMessage());
        }
    }


    /**
     * 根据土壤数据 ID 恢复已删除的土壤数据
     *
     * @param soilDataID 待恢复的土壤数据 ID
     * @return 恢复操作的 UpdateResponse 响应结果
     */
    @Override
    @Transactional
    public UpdateResponse restoreById(int soilDataID) {
        try {
            if (!soilDataDAO.existsById((long) soilDataID)) {
                return new UpdateResponse(false, "土壤数据不存在，恢复失败");
            }
            if (!soilDataDAO.isDeletedById((long) soilDataID)) {
                return new UpdateResponse(false, "土壤数据未删除，无需恢复");
            }
            int rowsAffected = soilDataDAO.restoreById(soilDataID);
            if (rowsAffected > 0) {
                return new UpdateResponse(true, "恢复土壤数据成功");
            } else {
                return new UpdateResponse(false, "恢复土壤数据失败");
            }
        } catch (Exception e) {
            return new UpdateResponse(false, "恢复失败: " + e.getMessage());
        }
    }

    /**
     * 更新土壤数据，并进行分类处理
     *
     * @param soilDataDTO 包含更新后土壤数据的传输对象
     * @return 更新操作的 UpdateResponse 响应结果
     */
    @Override
    @Transactional
    public UpdateResponse updateDataWithClassification(SoilDataDTO soilDataDTO) {
        try {
            if(!monitoringPointServiceImpl.existsMonitoringPoint(soilDataDTO.getMonitoringPointID())){
                return new UpdateResponse(false, "监测点Id不存在，更新失败");
            }
            SoilData soilData = convertToEntity(soilDataDTO);
            int rowsAffected = soilDataDAO.updateDataWithClassification(soilData);
            if (rowsAffected > 0) {
                return new UpdateResponse(true, "更新土壤数据成功");
            } else {
                return new UpdateResponse(false, "更新土壤数据失败");
            }
        } catch (Exception e) {
            return new UpdateResponse(false, "更新失败: " + e.getMessage());
        }
    }

    /**
     * 根据特定的土壤属性更新分类
     *
     * @param soilAttribute 特定的土壤属性
     * @return 更新操作的 UpdateResponse 响应结果
     */
    @Override
    @Transactional
    public UpdateResponse updateClassificationForSpecificAttribute(String soilAttribute) {
        try {
            int rowsAffected = soilDataDAO.updateClassificationForSpecificAttribute(soilAttribute);
            if (rowsAffected > 0) {
                return new UpdateResponse(true, "根据特定属性更新分类成功");
            } else {
                return new UpdateResponse(false, "根据特定属性更新分类失败");
            }
        } catch (Exception e) {
            return new UpdateResponse(false, "更新分类失败: " + e.getMessage());
        }
    }

    /**
     * 检查指定的土壤数据 ID 是否存在
     *
     * @param soilDataID 土壤数据 ID
     * @return 如果存在返回 true，否则返回 false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long soilDataID) {
        try {
            return soilDataDAO.existsById(soilDataID);
        } catch (Exception e) {
            // 可以根据实际情况记录日志
            return false;
        }
    }

    /**
     * 检查指定的土壤数据 ID 是否已被删除
     *
     * @param soilDataID 土壤数据 ID
     * @return 如果已被删除返回 true，否则返回 false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isDeletedById(Long soilDataID) {
        try {
            return soilDataDAO.isDeletedById(soilDataID);
        } catch (Exception e) {
            // 可以根据实际情况记录日志
            return false;
        }
    }

    /**
     * 将 SoilData 实体转换为 SoilDataDTO
     *
     * @param soilData SoilData 实体对象
     * @return SoilDataDTO 对象，如果输入为 null 则返回 null
     */
    private SoilDataDTO convertToDTO(SoilData soilData) {
        if (soilData == null) {
            return null;
        }
        String code = soilData.getSoilSamplingTime();
        SoilDataDTO dto = new SoilDataDTO(
                soilData.getSoilDataID(),
                soilData.getMonitoringPointID(),
                soilData.getSoilAttribute(),
                soilData.getValue(),
                soilData.getSamplingDate(),
                soilData.isDelete(),
                soilData.getClassification(),
                soilData.getOvenNumber(),
                soilData.getNotes(),
                soilData.getFarmerName(),
                soilData.getSoilSamplingTime(),
                code
        );
        dto.setSoilSamplingTimeZh(SoilSamplingTimeEnum.toZh(code));
        return dto;
    }

    /**
     * 将 SoilDataDTO 转换为 SoilData 实体
     *
     * @param soilDataDTO SoilDataDTO 对象
     * @return SoilData 实体对象，如果输入为 null 则返回 null
     */
    private SoilData convertToEntity(SoilDataDTO soilDataDTO) {
        if (soilDataDTO == null) return null;

        String input = soilDataDTO.getSoilSamplingTime();
        String code = null;

        if (input != null && !input.isBlank()) {
            // ✅ 允许前端传 “种烟前” 或 “before tobacco planting”
            code = SoilSamplingTimeEnum.fromAny(input).getCode();
        }

        return new SoilData(
                soilDataDTO.getSoilDataID(),
                soilDataDTO.getMonitoringPointID(),
                soilDataDTO.getSoilAttribute(),
                soilDataDTO.getValue(),
                soilDataDTO.getSamplingDate(),
                soilDataDTO.isDeleted(),
                soilDataDTO.getClassification(),
                soilDataDTO.getOvenNumber(),
                soilDataDTO.getNotes(),
                soilDataDTO.getFarmerName(),
                code
        );
    }
    /**
     * 实现 DataStorageService 接口的 saveAll 方法，批量保存土壤数据
     * @param dataList 待保存的土壤数据列表
     */
    @Transactional
    @Override
    public void saveAll(List<SoilData> dataList) {
        for (SoilData data : dataList) {
            try {
                if (!monitoringPointServiceImpl.existsMonitoringPoint(data.getMonitoringPointID())) {
                    continue;
                }

                // ✅ 新增：入库前统一为英文 code
                if (data.getSoilSamplingTime() != null) {
                    try {
                        data.setSoilSamplingTime(
                                SoilSamplingTimeEnum.fromAny(data.getSoilSamplingTime()).getCode()
                        );
                    } catch (Exception ignored) {}
                }

                soilDataDAO.insertWithClassification(data);
            } catch (Exception e) {
                // 记录日志可选
            }
        }
    }

    /**
     * 根据土壤数据ID查询详情（普通用户/管理员均可访问）
     *
     * @param soilDataID 待查询的土壤数据ID
     * @return 包含指定ID土壤数据的 SoilDataListResponse 对象
     */
    @Override
    @Transactional(readOnly = true)
    public SoilDataListResponse findById(int soilDataID) {
        try {
            // 1. 调用DAO层根据ID查询实体
            SoilData soilData = soilDataDAO.findById(soilDataID);

            if (soilData == null) {
                return SoilDataListResponse.error("土壤数据ID不存在: " + soilDataID);
            }

            // 2. 转换为DTO
            SoilDataDTO dto = convertToDTO(soilData);

            // 3. 构建单条数据的响应（注意：原响应设计为列表，此处包装为单元素列表）
            SoilDataListDTO data = new SoilDataListDTO(List.of(dto)); // 将单个DTO放入列表
            return SoilDataListResponse.ok(data);

        } catch (Exception e) {
            return SoilDataListResponse.error("查询土壤数据详情失败: " + e.getMessage());
        }
    }
    @Autowired
    private ISoilDataDao soilDataDao;

    // 数据访问层





    // 分页查询
    @Override
    public PageResult<SoilDataDTO> pageByCounty(PageRequest pr, String countyDistrict) {
        return PagingHelper.page(
                pr,
                // 1) 总数
                () -> soilDataMapper.countByCounty(countyDistrict),
                // 2) 当前页数据
                () -> soilDataMapper.findByCountyAndPage(
                        countyDistrict, pr.getSize(), pr.offset()),
                // 3) 行 -> DTO
                this::convertToDTO   // 形如: SoilData -> SoilDataDTO
        );
    }

    /** 按县区获取某字段全部取值（字段名白名单） */
    @Override
    public List<Object> listFieldValuesByCounty(String countyDistrict, SoilDataField field) {
        Objects.requireNonNull(field, "field 不能为空");
        // 传入 Mapper 的 field 是字符串，但来自这个枚举，安全白名单
        return soilDataMapper.listFieldValuesByCounty(countyDistrict, field.name());
    }

    /** 调试输出，可在 PrintForms 或控制台调用 */
    public void debugPrintByCounty(int page, int size, String county) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;

        long total = soilDataMapper.countByCounty( county);
        long totalPages = (total + size - 1) / size;

        List<SoilData> rows =
                soilDataMapper.findByCountyAndPage( county, size, offset);

        System.out.printf("分页结果：page=%d/%d, size=%d, total=%d%n",
                page, totalPages, size, total);
        rows.forEach(System.out::println); // 记得 DTO 已经重写 toString()
    }


    @Override
    public List<Object> listFieldValuesByRegion(String countyDistrict, String town, String village, ISoilDataService.SoilDataField field) {
        // 直接用白名单字段枚举的 name() 传给 Mapper 的 <choose/>
        return soilDataMapper.listFieldValuesByRegion(countyDistrict, town, village, field.name());
    }



    @Override
    @Transactional
    public InsertResponse batchInsertWithClassification(List<SoilDataDTO> soilDataDTOList) {
        int success = 0;

        for (SoilDataDTO dto : soilDataDTOList) {
            try {
                SoilData soilData = convertToEntity(dto);
                soilDataDAO.insertWithClassification(soilData);
                success++;
            } catch (Exception ignored) {
            }
        }

        return new InsertResponse(true, "批量导入完成，成功导入 " + success + " 条");
    }

}
