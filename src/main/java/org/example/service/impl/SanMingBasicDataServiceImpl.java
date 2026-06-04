package org.example.service.impl;

import org.example.dto.SanMingBasicDataListDTO.SanMingBasicDataDTO;
import org.example.dto.SanMingBasicDataListDTO.SanMingBasicDataListDTO;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.mapper.SanMingBasicDataMapper;
import org.example.model.SanMingBasicData;
import org.example.model.SoilData;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.Page.PagingHelper;
import org.example.response.common.SanMingBasicData.SanMingBasicDataListResponse;
import org.example.service.DataStorageService;
import org.example.service.ISanMingBasicDataService;
import org.example.service.ISoilDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
public class SanMingBasicDataServiceImpl implements ISanMingBasicDataService
{
    @Autowired
    private SanMingBasicDataMapper sanMingBasicDataMapper;

    @Autowired
    private MonitoringPointServiceImpl monitoringPointServiceImpl;

    // ========== 业务方法实现 ==========

    @Transactional
    @Override
    public InsertResponse insertSanMingBasicData(SanMingBasicDataDTO dto) {
        // 检查监测点 ID 是否存在（调用业务层方法）
        if (!monitoringPointServiceImpl.existsMonitoringPoint(dto.getMonitoringPointId())) {
            return InsertResponse.error("监测点ID不存在，插入失败");
        }

        SanMingBasicData entity = convertToEntity(dto);
        int rows = sanMingBasicDataMapper.insertSanMingBasicData(entity);
        return rows > 0 ?
                InsertResponse.ok("数据插入成功") :
                InsertResponse.error("数据插入失败");
    }

    @Transactional
    @Override
    public UpdateResponse updateSanMingBasicDataById(SanMingBasicDataDTO dto) {
        try {
            int id = dto.getId();
            if (!checkIdExists(id)) {
                return UpdateResponse.error("数据ID不存在，更新失败");
            }

            // 检查监测点 ID 是否存在（调用业务层方法）
            if (!monitoringPointServiceImpl.existsMonitoringPoint(dto.getMonitoringPointId())) {
                return UpdateResponse.error("监测点ID不存在，更新失败");
            }

            SanMingBasicData entity = convertToEntity(dto);
            int rows = sanMingBasicDataMapper.updateSanMingBasicDataById(entity);
            return rows > 0 ?
                    UpdateResponse.ok("更新成功") :
                    UpdateResponse.error("更新失败");
        } catch (Exception e) {
            return UpdateResponse.error("更新失败：" + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SanMingBasicDataListResponse getAllActiveSanMingBasicData() {
        try {
            List<SanMingBasicData> entityList = sanMingBasicDataMapper.getAllActiveSanMingBasicData();
            SanMingBasicDataListDTO listDTO = convertToListDTO(entityList);
            return SanMingBasicDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return SanMingBasicDataListResponse.error("查询活跃数据失败：" + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SanMingBasicDataListResponse getAllSanMingBasicData() {
        try {
            List<SanMingBasicData> entityList = sanMingBasicDataMapper.getAllSanMingBasicData();
            if (entityList == null) {
                entityList = Collections.emptyList();
            }

            // 用下面新的 convertToListDTO，会自己过滤 null
            SanMingBasicDataListDTO listDTO = convertToListDTO(entityList);
            return SanMingBasicDataListResponse.ok(listDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return SanMingBasicDataListResponse.error("查询所有数据失败：" + e.getClass().getSimpleName());
        }
    }

    @Transactional
    @Override
    public DeleteResponse deleteSanMingBasicDataById(int id) {
        try {
            if (!checkIdExists(id)) {
                return DeleteResponse.error("数据ID不存在，删除失败");
            }
            int rows = sanMingBasicDataMapper.deleteSanMingBasicDataById(id);
            return rows > 0 ?
                    DeleteResponse.ok("软删除成功") :
                    DeleteResponse.error("软删除失败");
        } catch (Exception e) {
            return DeleteResponse.error("删除失败：" + e.getMessage());
        }
    }

    @Transactional
    @Override
    public UpdateResponse restoreSanMingBasicDataById(int id) {
        try {
            if (!checkIdExists(id)) {
                return UpdateResponse.error("数据ID不存在，恢复失败");
            }
            if (!isSanMingBasicDataDeleted(id)) {
                return UpdateResponse.error("数据未删除，无需恢复");
            }
            int rows = sanMingBasicDataMapper.restoreSanMingBasicDataById(id);
            return rows > 0 ?
                    UpdateResponse.ok("恢复成功") :
                    UpdateResponse.error("恢复失败");
        } catch (Exception e) {
            return UpdateResponse.error("恢复失败：" + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public boolean checkIdExists(int id) {
        return sanMingBasicDataMapper.checkIdExists(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isSanMingBasicDataDeleted(int id) {
        return sanMingBasicDataMapper.isSanMingBasicDataDeleted(id);
    }

    // ========== 新增：根据监测点 ID 查询 ==========
    @Transactional(readOnly = true)
    @Override
    public SanMingBasicDataListResponse getSanMingBasicDataByMonitoringPointId(int monitoringPointId) {
        try {
            // 检查监测点 ID 是否存在（调用业务层方法）
            if (!monitoringPointServiceImpl.existsMonitoringPoint(monitoringPointId)) {
                return SanMingBasicDataListResponse.error("监测点ID不存在，查询失败");
            }

            List<SanMingBasicData> entityList =
                    sanMingBasicDataMapper.getSanMingBasicDataByMonitoringPointId(monitoringPointId);
            SanMingBasicDataListDTO listDTO = convertToListDTO(entityList);
            return SanMingBasicDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return SanMingBasicDataListResponse.error("根据监测点ID查询失败：" + e.getMessage());
        }
    }


    // ========== 辅助方法 ==========
    private SanMingBasicData convertToEntity(SanMingBasicDataDTO dto) {
        SanMingBasicData entity = new SanMingBasicData();
        entity.setId(dto.getId());
        entity.setMonitoringPointId(dto.getMonitoringPointId()); // 注意：此处应为监测点 ID
        entity.setPlantingVariety(dto.getPlantingVariety());
        entity.setPlantingArea(dto.getPlantingArea());
        entity.setPlannedContractAmount(dto.getPlannedContractAmount());
        entity.setPurchaseQuantity(dto.getPurchaseQuantity());
        entity.setDescribe(dto.getDescribe());
        entity.setDelete(dto.getDelete()); // DTO 的 isDelete() 对应实体的 delete 字段
        return entity;
    }

    private SanMingBasicDataDTO convertToDTO(SanMingBasicData entity) {
        SanMingBasicDataDTO dto = new SanMingBasicDataDTO();
        dto.setId(entity.getId());
        dto.setMonitoringPointId(entity.getMonitoringPointId()); // 注意：此处应为监测点 ID
        dto.setPlantingVariety(entity.getPlantingVariety());
        dto.setPlantingArea(entity.getPlantingArea());
        dto.setPlannedContractAmount(entity.getPlannedContractAmount());
        dto.setPurchaseQuantity(entity.getPurchaseQuantity());
        dto.setDescribe(entity.getDescribe());
        dto.setDelete(entity.getDelete()); // 实体的 delete 字段对应 DTO 的 isDelete()
        return dto;
    }

    private SanMingBasicDataListDTO convertToListDTO(List<SanMingBasicData> entityList) {
        if (entityList == null) {
            entityList = Collections.emptyList();
        }

        List<SanMingBasicDataDTO> dtoList = entityList.stream()
                .filter(Objects::nonNull)      // 防止列表里混进 null
                .map(this::convertToDTO)       // 你原来就有的那个方法
                .collect(Collectors.toList());

        return new SanMingBasicDataListDTO(dtoList);
    }


    // 分页查询
    @Override
    public PageResult<SanMingBasicDataDTO> pageByCounty(PageRequest pr, String countyDistrict) {
        return PagingHelper.page(
                pr,
                // 1) 总数
                () -> sanMingBasicDataMapper.countByCounty(countyDistrict),
                // 2) 当前页数据
                () -> sanMingBasicDataMapper.findByCountyAndPage(
                        countyDistrict, pr.getSize(), pr.offset()),
                // 3) 行 -> DTO
                this::convertToDTO
        );
    }

    /** 按县区获取某字段全部取值（字段名白名单） */
    @Override
    public List<Object> listFieldValuesByCounty(String countyDistrict, SanMingBasicDataField field) {
        Objects.requireNonNull(field, "field 不能为空");
        // 传入 Mapper 的 field 是字符串，但来自这个枚举，安全白名单
        return sanMingBasicDataMapper.listFieldValuesByCounty(countyDistrict, field.name());
    }

    /** 调试输出，可在 PrintForms 或控制台调用 */
    public void debugPrintByCounty(int page, int size, String county) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;

        long total = sanMingBasicDataMapper.countByCounty( county);
        long totalPages = (total + size - 1) / size;

        List<SanMingBasicData> rows =
                sanMingBasicDataMapper.findByCountyAndPage( county, size, offset);

        System.out.printf("分页结果：page=%d/%d, size=%d, total=%d%n",
                page, totalPages, size, total);
        rows.forEach(System.out::println); // 记得 DTO 已经重写 toString()
    }

    @Override
    public List<Object> listFieldValuesByRegion(String countyDistrict, String town, String village, SanMingBasicDataField field) {
        // 直接用白名单字段枚举的 name() 传给 Mapper 的 <choose/>
        return sanMingBasicDataMapper.listFieldValuesByRegion(countyDistrict, town, village, field.name());
    }


}
