package org.example.service.impl;
import lombok.RequiredArgsConstructor;
import org.example.dao.ITobaccoLeafDataDao;
import org.example.dao.impl.TobaccoLeafDataDaoImpl;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.TobaccoLeafDataListDTO.TobaccoLeafDataDTO;
import org.example.dto.TobaccoLeafDataListDTO.TobaccoLeafDataListDTO;
import org.example.mapper.TobaccoLeafDataMapper;
import org.example.model.SoilData;
import org.example.model.TobaccoLeafData;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.Page.PagingHelper;
import org.example.response.common.TobaccoLeafData.TobaccoLeafDataListResponse;
import org.example.service.DataStorageService;
import org.example.service.ISoilDataService;
import org.example.service.ITobaccoLeafDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.service.IMonitoringPointService;
import org.example.response.common.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 烟草叶片数据服务实现类，实现了 ITobaccoLeafDataService 接口，
 * 负责处理与烟草叶片数据相关的业务逻辑，包括数据的插入、查询、删除、恢复和更新等操作。
 */
@Service
@RequiredArgsConstructor
public class TobaccoLeafDataServiceImpl implements ITobaccoLeafDataService, DataStorageService<TobaccoLeafData>
{
    @Autowired
    private TobaccoLeafDataDaoImpl tobaccoLeafDataDao; // 使用接口而不是具体的实现类
    @Autowired
    private MonitoringPointServiceImpl monitoringPointServiceImpl;

    private final TobaccoLeafDataMapper tobaccoLeafDataMapper;

    /**
     * 插入一条新的烟草叶片数据。
     * 在插入之前，会检查该数据是否已经存在，若存在则插入失败；若不存在，则将 DTO 对象转换为实体对象并插入数据库。
     *
     * @param tobaccoLeafDataDTO 待插入的烟草叶片数据传输对象
     * @return 插入操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    @Override
    @Transactional
    public InsertResponse insertTobaccoLeafData(TobaccoLeafDataDTO tobaccoLeafDataDTO) {
        try {
            if(!monitoringPointServiceImpl.existsMonitoringPoint(tobaccoLeafDataDTO.getMonitoringPointId())){
                return new InsertResponse(false, "监测点Id不存在，插入失败");
            }

            TobaccoLeafData tobaccoLeafData = convertToEntity(tobaccoLeafDataDTO);
            tobaccoLeafDataDao.insertTobaccoLeafData(tobaccoLeafData);
            return new InsertResponse(true, "烟草叶片数据添加成功");
        } catch (Exception e) {
            return new InsertResponse(false, "添加失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有未删除的烟草叶片数据。
     * 调用数据访问层的方法获取数据列表，然后将实体列表转换为 DTO 列表，并封装到响应对象中返回。
     *
     * @return 包含未删除的烟草叶片数据的响应结果，若查询失败则返回包含错误信息的响应
     */
    @Override
    @Transactional(readOnly = true)
    public TobaccoLeafDataListResponse getAllActiveTobaccoLeafData() {
        try {
            // 调用数据访问层的方法获取所有未删除的烟草叶片数据
            List<TobaccoLeafData> tobaccoLeafDataList = tobaccoLeafDataDao.getAllActiveTobaccoLeafData();
            // 将实体列表转换为 DTO 列表
            TobaccoLeafDataListDTO listDTO = convertToTobaccoLeafDataListDTO(tobaccoLeafDataList);
            return TobaccoLeafDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return TobaccoLeafDataListResponse.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有烟草叶片数据，包括已删除的数据。
     * 调用数据访问层的方法获取数据列表，然后将实体列表转换为 DTO 列表，并封装到响应对象中返回。
     *
     * @return 包含所有烟草叶片数据的响应结果，若查询失败则返回包含错误信息的响应
     */
    @Override
    @Transactional(readOnly = true)
    public TobaccoLeafDataListResponse getAllTobaccoLeafData() {
        try {
            // 调用数据访问层的方法获取所有烟草叶片数据
            List<TobaccoLeafData> tobaccoLeafDataList = tobaccoLeafDataDao.getAllTobaccoLeafData();
            // 将实体列表转换为 DTO 列表
            TobaccoLeafDataListDTO listDTO = convertToTobaccoLeafDataListDTO(tobaccoLeafDataList);
            return TobaccoLeafDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return TobaccoLeafDataListResponse.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据烟草叶片数据的 ID 删除该数据（软删除）。
     * 在删除之前，会检查该数据是否存在，若不存在则删除失败；若存在，则调用数据访问层的删除方法。
     *
     * @param tobaccoDataId 要删除的烟草叶片数据的 ID
     * @return 删除操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    @Override
    @Transactional
    public DeleteResponse deleteTobaccoLeafDataByTobaccoDataId(int tobaccoDataId) {
        try {
            // 检查烟草叶片数据是否存在
            if (!tobaccoLeafDataDao.checkTobaccoDataIdExists(tobaccoDataId)) {
                return new DeleteResponse(false, "烟草叶片数据不存在，删除失败");
            }
            // 调用数据访问层的删除方法
            tobaccoLeafDataDao.deleteTobaccoLeafDataByTobaccoDataId(tobaccoDataId);
            return new DeleteResponse(true, "删除成功");
        } catch (Exception e) {
            return new DeleteResponse(false, "删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据烟草叶片数据的 ID 恢复已删除的数据。
     * 在恢复之前，会检查该数据是否存在且已被删除，若不满足条件则恢复失败；若满足条件，则调用数据访问层的恢复方法。
     *
     * @param tobaccoDataId 要恢复的烟草叶片数据的 ID
     * @return 恢复操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    @Override
    @Transactional
    public UpdateResponse restoreTobaccoLeafDataByTobaccoDataId(int tobaccoDataId) {
        try {
            // 检查烟草叶片数据是否存在
            if (!tobaccoLeafDataDao.checkTobaccoDataIdExists(tobaccoDataId)) {
                return new UpdateResponse(false, "烟草叶片数据不存在，恢复失败");
            }
            // 检查烟草叶片数据是否已删除
            if (!tobaccoLeafDataDao.isTobaccoLeafDataDeleted(tobaccoDataId)) {
                return new UpdateResponse(false, "烟草叶片数据未删除，无需恢复");
            }
            // 调用数据访问层的恢复方法
            tobaccoLeafDataDao.restoreTobaccoLeafDataByTobaccoDataId(tobaccoDataId);
            return new UpdateResponse(true, "恢复成功");
        } catch (Exception e) {
            return new UpdateResponse(false, "恢复失败: " + e.getMessage());
        }
    }

    /**
     * 检查指定 ID 的烟草叶片数据是否存在。
     * 调用数据访问层的方法进行检查，若出现异常则返回 false。
     *
     * @param tobaccoDataId 要检查的烟草叶片数据的 ID
     * @return 若数据存在则返回 true，否则返回 false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkTobaccoDataIdExists(int tobaccoDataId) {
        try {
            return tobaccoLeafDataDao.checkTobaccoDataIdExists(tobaccoDataId);
        } catch (Exception e) {
            // 可以根据实际情况记录日志
            return false;
        }
    }

    /**
     * 检查指定 ID 的烟草叶片数据是否已被删除。
     * 调用数据访问层的方法进行检查，若出现异常则返回 false。
     *
     * @param tobaccoDataId 要检查的烟草叶片数据的 ID
     * @return 若数据已被删除则返回 true，否则返回 false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isTobaccoLeafDataDeleted(int tobaccoDataId) {
        try {
            return tobaccoLeafDataDao.isTobaccoLeafDataDeleted(tobaccoDataId);
        } catch (Exception e) {
            // 可以根据实际情况记录日志
            return false;
        }
    }

    /**
     * 根据监测点 ID 获取该监测点下的所有烟草叶片数据。
     * 调用数据访问层的方法获取数据列表，然后将实体列表转换为 DTO 列表，并封装到响应对象中返回。
     *
     * @param monitoringPointId 监测点的 ID
     * @return 包含该监测点下所有烟草叶片数据的响应结果，若查询失败则返回包含错误信息的响应
     */
    @Override
    @Transactional(readOnly = true)
    public TobaccoLeafDataListResponse getTobaccoLeafDataByMonitoringPointId(int monitoringPointId) {
        try {
            // 调用数据访问层的方法获取指定监测点下的所有烟草叶片数据
            List<TobaccoLeafData> tobaccoLeafDataList = tobaccoLeafDataDao.getTobaccoLeafDataByMonitoringPointId(monitoringPointId);
            // 将实体列表转换为 DTO 列表
            TobaccoLeafDataListDTO listDTO = convertToTobaccoLeafDataListDTO(tobaccoLeafDataList);
            return TobaccoLeafDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return TobaccoLeafDataListResponse.error("查询失败: " + e.getMessage());
        }
    }

    // ========== 辅助方法 ==========

    /**
     * 将 TobaccoLeafDataDTO 转换为 TobaccoLeafData 实体对象。
     *
     * @param dto 烟草叶片数据传输对象
     * @return 转换后的烟草叶片数据实体对象
     */
    private TobaccoLeafData convertToEntity(TobaccoLeafDataDTO dto) {
        TobaccoLeafData tobaccoLeafData = new TobaccoLeafData();
        tobaccoLeafData.setTobaccoDataId(dto.getTobaccoDataId());
        tobaccoLeafData.setMonitoringPointID(dto.getMonitoringPointId());
        tobaccoLeafData.setTobaccoLeafData(dto.getTobaccoLeafData());
        tobaccoLeafData.setValue(dto.getValue());
        tobaccoLeafData.setSamplingDate(dto.getSamplingDate());
        tobaccoLeafData.setDelete(dto.isDeleted());
        tobaccoLeafData.setClassification(dto.getClassification());
        return tobaccoLeafData;
    }

    /**
     * 将 TobaccoLeafData 实体对象转换为 TobaccoLeafDataDTO 传输对象。
     *
     * @param tobaccoLeafData 烟草叶片数据实体对象
     * @return 转换后的烟草叶片数据传输对象
     */
    private TobaccoLeafDataDTO convertToDTO(TobaccoLeafData tobaccoLeafData) {
        TobaccoLeafDataDTO dto = new TobaccoLeafDataDTO();
        dto.setTobaccoDataId(tobaccoLeafData.getTobaccoDataId());
        dto.setMonitoringPointId(tobaccoLeafData.getMonitoringPointID());
        dto.setTobaccoLeafData(tobaccoLeafData.getTobaccoLeafData());
        dto.setValue(tobaccoLeafData.getValue());
        dto.setSamplingDate(tobaccoLeafData.getSamplingDate());
        dto.setDeleted(tobaccoLeafData.isDelete());
        dto.setClassification(tobaccoLeafData.getClassification());
        return dto;
    }

    /**
     * 将 TobaccoLeafData 列表转换为 TobaccoLeafDataListDTO 传输对象。
     *
     * @param tobaccoLeafDataList 烟草叶片数据实体列表
     * @return 包含烟草叶片数据传输对象列表的 TobaccoLeafDataListDTO
     */
    private TobaccoLeafDataListDTO convertToTobaccoLeafDataListDTO(List<TobaccoLeafData> tobaccoLeafDataList) {
        List<TobaccoLeafDataDTO> dtoList = tobaccoLeafDataList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new TobaccoLeafDataListDTO(dtoList);
    }

    /**
     * 根据烟草叶片数据的 ID 更新该数据。
     * 在更新之前，会检查该数据是否存在，若不存在则更新失败；若存在，则调用数据访问层的更新方法，并根据受影响的行数判断更新是否成功。
     *
     * @param tobaccoLeafData 包含要更新的烟草叶片数据的对象，其中应包含 TobaccoDataId 以及其他要更新的字段值
     * @return 更新操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    @Override
    @Transactional
    public UpdateResponse updateTobaccoLeafDataByTobaccoDataId(TobaccoLeafDataDTO tobaccoLeafData) {
        try {
            int tobaccoDataId = tobaccoLeafData.getTobaccoDataId();
            // 检查 tobaccoDataId 是否存在
            if (!checkTobaccoDataIdExists(tobaccoDataId)) {
                return new UpdateResponse(false, "要更新的烟草叶片数据的 TobaccoDataId 不存在，更新失败");
            }
            // 调用数据访问层方法执行更新操作，并获取受影响的行数
            int rowsAffected =
                    tobaccoLeafDataDao.updateTobaccoLeafDataByTobaccoDataId(convertToEntity(tobaccoLeafData));
            if (rowsAffected > 0) {
                return new UpdateResponse(true, "根据 TobaccoDataId 更新烟草叶片数据成功");
            } else {
                return new UpdateResponse(false, "根据 TobaccoDataId 更新烟草叶片数据失败");
            }
        } catch (Exception e) {
            return new UpdateResponse(false, "更新失败: " + e.getMessage());
        }
    }

    /**
     * 实现 DataStorageService 接口的 saveAll 方法
     * @param dataList 要保存的烟草叶片数据列表
     */
    @Transactional
    @Override
    public void saveAll(List<TobaccoLeafData> dataList) {
        for (TobaccoLeafData data : dataList) {
            try {
                if(!monitoringPointServiceImpl.existsMonitoringPoint(data.getMonitoringPointID())){
                    continue; // 跳过监测点ID不存在的数据
                }
                tobaccoLeafDataDao.insertTobaccoLeafData(data);
            } catch (Exception e) {
                // 可以根据实际情况记录日志
            }
        }
    }


    // 分页查询
    @Override
    public PageResult<TobaccoLeafDataDTO> pageByCounty(PageRequest pr, String countyDistrict) {
        return PagingHelper.page(
                pr,
                // 1) 总数
                () -> tobaccoLeafDataMapper.countByCounty(countyDistrict),
                // 2) 当前页数据
                () -> tobaccoLeafDataMapper.findByCountyAndPage(
                        countyDistrict, pr.getSize(), pr.offset()),
                // 3) 行 -> DTO
                this::convertToDTO
        );
    }




    /**
     * 根据县区获取烟草数据并分页
     * @param countyDistrict
     * @param field
     * @return
     */
    @Override
    public List<Object> listFieldValuesByCounty(String countyDistrict, TobaccoLeafDataField field) {
        Objects.requireNonNull(field, "field 不能为空");
        // 传入 Mapper 的 field 是字符串，但来自这个枚举，安全白名单
        return tobaccoLeafDataMapper.listFieldValuesByCounty(countyDistrict, field.name());
    }

    /** 调试输出，可在 PrintForms 或控制台调用 */
    public void debugPrintByCounty(int page, int size, String county) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;

        long total = tobaccoLeafDataMapper.countByCounty( county);
        long totalPages = (total + size - 1) / size;

        List<TobaccoLeafData> rows =
                tobaccoLeafDataMapper.findByCountyAndPage( county, size, offset);

        System.out.printf("分页结果：page=%d/%d, size=%d, total=%d%n",
                page, totalPages, size, total);
        rows.forEach(System.out::println); //DTO 已经重写 toString()
    }
}
