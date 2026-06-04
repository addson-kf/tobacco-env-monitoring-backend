package org.example.service.impl;

import org.example.dao.IMonitoringPointDao;
import org.example.dto.MonitoringPointListDTO.MonitoringPointDTO;
import org.example.dto.MonitoringPointListDTO.MonitoringPointListDTO;
import org.example.model.MonitoringPoint;
import org.example.model.enums.SoilAttribute;
import org.example.model.enums.TobaccoLeafDataEnum;
import org.example.model.enums.WeatherType;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.MonitoringPoint.MonitoringPointListResponse;
import org.example.service.IMonitoringPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.response.common.Page.PagingHelper;
import lombok.RequiredArgsConstructor;
import org.example.mapper.MonitoringPointMapper;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitoringPointServiceImpl implements IMonitoringPointService
{
    @Autowired
    private IMonitoringPointDao monitoringPointDao;
    @Autowired
    private MonitoringPointMapper monitoringPointMapper;


    /**
     * 检查某个监测点 ID 是否存在。
     *
     * @param monitoringPointID 待检查的监测点 ID
     * @return 如果监测点 ID 存在则返回 true，否则返回 false
     */
    @Override
    public boolean existsMonitoringPoint(int monitoringPointID) {
        return monitoringPointDao.existsMonitoringPoint(monitoringPointID);
    }

    /**
     * 插入一个新的监测点记录。
     * 首先检查监测点 ID 是否已存在，若存在则插入失败；若不存在则进行插入操作。
     *
     * @param monitoringPointDTO 包含新监测点信息的数据传输对象
     * @return 插入操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    @Override
    @Transactional
    public InsertResponse insertMonitoringPoint(MonitoringPointDTO monitoringPointDTO) {
        try {
            // 将 DTO 转换为实体对象
            MonitoringPoint monitoringPoint = convertToEntity(monitoringPointDTO);
            // 检查监测点 ID 是否已存在
            if (monitoringPointDao.existsMonitoringPoint(monitoringPoint.getMonitoringPointID())) {
                return new InsertResponse(false, "监测点 ID 已存在，插入失败");
            }
            // 调用数据访问层进行插入操作
            monitoringPointDao.insertMonitoringPoint(monitoringPoint);
            return new InsertResponse(true, "插入监测点成功");
        } catch (Exception e) {
            return new InsertResponse(false, "插入监测点失败：" + e.getMessage());
        }
    }

    /**
     * 根据 ID 逻辑删除一个监测点。
     * 首先检查监测点是否存在，若不存在则删除失败；若存在则进行删除操作。
     *
     * @param monitoringPointID 待删除的监测点 ID
     * @return 删除操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    @Override
    @Transactional
    public DeleteResponse deleteMonitoringPointById(int monitoringPointID) {
        try {
            // 检查监测点是否存在
            if (!monitoringPointDao.existsMonitoringPoint(monitoringPointID)) {
                return new DeleteResponse(false, "监测点不存在，删除失败");
            }
            // 调用数据访问层进行删除操作
            monitoringPointDao.deleteMonitoringPointById(monitoringPointID);
            return new DeleteResponse(true, "删除监测点成功");
        } catch (Exception e) {
            return new DeleteResponse(false, "删除监测点失败：" + e.getMessage());
        }
    }

    /**
     * 根据 ID 修改整个监测点对象。
     * 首先检查监测点是否存在，若不存在则更新失败；若存在则进行更新操作。
     *
     * @param monitoringPointDTO 包含更新后监测点信息的数据传输对象
     * @return 更新操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    @Override
    @Transactional
    public UpdateResponse updateMonitoringPointById(MonitoringPointDTO monitoringPointDTO) {
        try {
            // 将 DTO 转换为实体对象
            MonitoringPoint monitoringPoint = convertToEntity(monitoringPointDTO);
            // 检查监测点是否存在
            if (!monitoringPointDao.existsMonitoringPoint(monitoringPoint.getMonitoringPointID())) {
                return new UpdateResponse(false, "监测点不存在，更新失败");
            }
            // 调用数据访问层进行更新操作
            monitoringPointDao.updateMonitoringPointById(monitoringPoint);
            return new UpdateResponse(true, "更新监测点成功");
        } catch (Exception e) {
            return new UpdateResponse(false, "更新监测点失败：" + e.getMessage());
        }
    }

    /**
     * 恢复某个被逻辑删除的监测点。
     * 首先检查监测点是否存在，若不存在则恢复失败；然后检查监测点是否已被删除，若未删除则无需恢复；若满足条件则进行恢复操作。
     *
     * @param monitoringPointID 待恢复的监测点 ID
     * @return 恢复操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    @Override
    @Transactional
    public UpdateResponse restoreMonitoringPointById(int monitoringPointID) {
        try {
            // 检查监测点是否存在
            if (!monitoringPointDao.existsMonitoringPoint(monitoringPointID)) {
                return new UpdateResponse(false, "监测点不存在，恢复失败");
            }
            // 检查监测点是否已被删除
            if (!monitoringPointDao.isMonitoringPointDeleted(monitoringPointID)) {
                return new UpdateResponse(false, "监测点未被删除，无需恢复");
            }
            // 调用数据访问层进行恢复操作
            monitoringPointDao.restoreMonitoringPointById(monitoringPointID);
            return new UpdateResponse(true, "恢复监测点成功");
        } catch (Exception e) {
            return new UpdateResponse(false, "恢复监测点失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有未被逻辑删除的监测点。
     * 调用数据访问层获取未删除的监测点列表，将实体列表转换为 DTO 列表，封装到响应对象中返回。
     *
     * @return 包含未删除监测点列表的响应结果，包含操作是否成功的标志、消息和监测点列表数据
     */
    @Override
    @Transactional
    public MonitoringPointListResponse getAllMonitoringPoints() {
        try {
            // 调用数据访问层获取未删除的监测点列表
            List<MonitoringPoint> monitoringPoints = monitoringPointDao.getAllMonitoringPoints();
            // 将实体列表转换为 DTO 列表
            List<MonitoringPointDTO> dtoList = monitoringPoints.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            // 创建包含 DTO 列表的 MonitoringPointListDTO 对象
            MonitoringPointListDTO monitoringPointListDTO = new MonitoringPointListDTO(dtoList);
            // 返回包含成功信息和数据的响应结果
            return MonitoringPointListResponse.success("查询未删除监测点成功", monitoringPointListDTO);
        } catch (Exception e) {
            e.printStackTrace();
            // 返回包含错误信息的响应结果
            return MonitoringPointListResponse.error("查询未删除监测点失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有监测点（包括被逻辑删除的）—— 仅管理员使用。
     * 调用数据访问层获取所有监测点列表，将实体列表转换为 DTO 列表，封装到响应对象中返回。
     *
     * @return 包含所有监测点列表的响应结果，包含操作是否成功的标志、消息和监测点列表数据
     */
    @Override
    @Transactional
    public MonitoringPointListResponse getAllMonitoringPointsWithDeleted() {
        try {
            // 调用数据访问层获取所有监测点列表
            List<MonitoringPoint> monitoringPoints = monitoringPointDao.getAllMonitoringPointsWithDeleted();
            // 将实体列表转换为 DTO 列表
            List<MonitoringPointDTO> dtoList = monitoringPoints.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            // 创建包含 DTO 列表的 MonitoringPointListDTO 对象
            MonitoringPointListDTO monitoringPointListDTO = new MonitoringPointListDTO(dtoList);
            // 返回包含成功信息和数据的响应结果
            return MonitoringPointListResponse.success("查询所有监测点成功", monitoringPointListDTO);
        } catch (Exception e) {
            e.printStackTrace();
            // 返回包含错误信息的响应结果
            return MonitoringPointListResponse.error("查询所有监测点失败：" + e.getMessage());
        }
    }

    /**
     * 查询指定 ID 的监测点是否已删除。
     *
     * @param monitoringPointID 待查询的监测点 ID
     * @return 如果监测点已被删除则返回 true，否则返回 false
     */
    @Override
    public boolean isMonitoringPointDeleted(int monitoringPointID) {
        return monitoringPointDao.isMonitoringPointDeleted(monitoringPointID);
    }

    /**
     * 将 MonitoringPointDTO 转换为 MonitoringPoint 实体对象。
     *
     * @param dto 待转换的 MonitoringPointDTO 对象
     * @return 转换后的 MonitoringPoint 实体对象
     */
    private MonitoringPoint convertToEntity(MonitoringPointDTO dto) {
        MonitoringPoint monitoringPoint = new MonitoringPoint();
        monitoringPoint.setMonitoringPointID(dto.getMonitoringPointID());
        monitoringPoint.setCoordinates(dto.getCoordinates());
        monitoringPoint.setElevation(dto.getElevation());
        monitoringPoint.setTobaccoFieldType(dto.getTobaccoFieldType());
        monitoringPoint.setCountyDistrict(dto.getCountyDistrict());
        monitoringPoint.setTown(dto.getTown());
        monitoringPoint.setVillage(dto.getVillage());
        monitoringPoint.setDelete(dto.isDeleted());
        monitoringPoint.setVideoCode(dto.getVideoCode());
        monitoringPoint.setIfLongTermMonitoringPoint(dto.isIfLongTermMonitoringPoint());
        return monitoringPoint;
    }

    /**
     * 将 MonitoringPoint 实体对象转换为 MonitoringPointDTO 数据传输对象。
     *
     * @param monitoringPoint 待转换的 MonitoringPoint 实体对象
     * @return 转换后的 MonitoringPointDTO 数据传输对象
     */
    private MonitoringPointDTO convertToDTO(MonitoringPoint monitoringPoint) {
        MonitoringPointDTO dto = new MonitoringPointDTO();
        dto.setMonitoringPointID(monitoringPoint.getMonitoringPointID());
        dto.setCoordinates(monitoringPoint.getCoordinates());
        dto.setElevation(monitoringPoint.getElevation());
        dto.setTobaccoFieldType(monitoringPoint.getTobaccoFieldType());
        dto.setCountyDistrict(monitoringPoint.getCountyDistrict());
        dto.setTown(monitoringPoint.getTown());
        dto.setVillage(monitoringPoint.getVillage());
        dto.setDeleted(monitoringPoint.isDelete());
        dto.setVideoCode(monitoringPoint.getVideoCode());
        dto.setIfLongTermMonitoringPoint(monitoringPoint.isIfLongTermMonitoringPoint());

        return dto;
    }

    @Override
    @Transactional
    public void printAllMonitoringPoints() {
        try {
            // 调用数据访问层获取所有监测点列表
            List<MonitoringPoint> monitoringPoints = monitoringPointDao.getAllMonitoringPointsWithDeleted();
            for (MonitoringPoint point : monitoringPoints) {
                System.out.println(point);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("查询并输出所有监测点失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MonitoringPointListResponse getMonitoringPointsBySoilCondition(
            SoilAttribute soilAttribute, Double minValue, Double maxValue) {
        try {
            // 参数校验
            if (soilAttribute == null) {
                return MonitoringPointListResponse.error("土壤属性不能为空");
            }
            if (maxValue != null && minValue != null && maxValue < minValue) {
                return MonitoringPointListResponse.success("参数范围无效，未查询到数据",
                        new MonitoringPointListDTO(List.of()));
            }

            // 调用DAO层，将枚举值转换为数据库对应的字符串
            List<MonitoringPoint> monitoringPoints =
                    monitoringPointDao.getMonitoringPointsBySoilCondition(
                            soilAttribute,
                            minValue,
                            maxValue
                    );

            // 后续逻辑保持不变...
            List<MonitoringPointDTO> dtoList = monitoringPoints.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            MonitoringPointListDTO resultDTO = new MonitoringPointListDTO(dtoList);
            return MonitoringPointListResponse.success(
                    "查询成功，共" + dtoList.size() + "条数据", resultDTO);
        } catch (Exception e) {
            return MonitoringPointListResponse.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MonitoringPointListResponse getMonitoringPointsByWeatherCondition(
            WeatherType weatherType, Double minValue, Double maxValue) {
        try {
            // 参数校验
            if (weatherType == null) {
                return MonitoringPointListResponse.error("天气类型不能为空");
            }
            if (maxValue != null && minValue != null && maxValue < minValue) {
                return MonitoringPointListResponse.success("参数范围无效，未查询到数据",
                        new MonitoringPointListDTO(List.of()));
            }

            // 查询数据
            List<MonitoringPoint> points = monitoringPointDao.getMonitoringPointsByWeatherCondition(
                    weatherType, // 枚举值转数据库值
                    minValue,
                    maxValue
            );

            // 转换为DTO
            List<MonitoringPointDTO> dtoList = points.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            // 返回响应
            MonitoringPointListDTO resultDTO = new MonitoringPointListDTO(dtoList);
            return MonitoringPointListResponse.success(
                    "查询成功，共" + dtoList.size() + "条数据", resultDTO);
        } catch (Exception e) {
            return MonitoringPointListResponse.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MonitoringPointListResponse getMonitoringPointsByAdministrativeDivision(
            String countyDistrict, String town, String village) {
        try {
            // 参数处理：允许空值（表示不过滤该字段）
            List<MonitoringPoint> points = monitoringPointDao.getMonitoringPointsByAdministrativeDivision(
                    countyDistrict, town, village
            );

            // 转换为DTO
            List<MonitoringPointDTO> dtoList = points.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            // 封装响应
            MonitoringPointListDTO resultDTO = new MonitoringPointListDTO(dtoList);
            return MonitoringPointListResponse.success(
                    "查询成功，共" + dtoList.size() + "条数据", resultDTO
            );
        } catch (Exception e) {
            return MonitoringPointListResponse.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MonitoringPointListResponse getMonitoringPointsByTobaccoCondition(
            TobaccoLeafDataEnum tobaccoLeafData, Double minValue, Double maxValue) {
        try {
            // 参数校验
            if (tobaccoLeafData == null) {
                return MonitoringPointListResponse.error("烟叶数据类型不能为空");
            }
            if (maxValue != null && minValue != null && maxValue < minValue) {
                return MonitoringPointListResponse.success("参数范围无效，未查询到数据",
                        new MonitoringPointListDTO(List.of()));
            }

            // 查询数据（枚举值转换为数据库对应字符串）
            List<MonitoringPoint> points = monitoringPointDao.getMonitoringPointsByTobaccoCondition(
                    tobaccoLeafData,
                    minValue,
                    maxValue
            );

            // 转换为DTO
            List<MonitoringPointDTO> dtoList = points.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            // 封装响应
            MonitoringPointListDTO resultDTO = new MonitoringPointListDTO(dtoList);
            return MonitoringPointListResponse.success(
                    "查询成功，共" + dtoList.size() + "条数据", resultDTO);
        } catch (Exception e) {
            return MonitoringPointListResponse.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MonitoringPointListResponse getMonitoringPointsByPlantingArea(
            Double minValue, Double maxValue) {
        try {
            if (maxValue != null && minValue != null && maxValue < minValue) {
                return MonitoringPointListResponse.success("参数范围无效", new MonitoringPointListDTO(List.of()));
            }

            List<MonitoringPoint> points = monitoringPointDao.getMonitoringPointsByPlantingArea(
                    minValue, maxValue
            );

            List<MonitoringPointDTO> dtoList = points.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return MonitoringPointListResponse.success(
                    "查询成功，共" + dtoList.size() + "个监测点",
                    new MonitoringPointListDTO(dtoList)
            );
        } catch (Exception e) {
            return MonitoringPointListResponse.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MonitoringPointListResponse getMonitoringPointsByPlannedContractAmount(
            Double minValue, Double maxValue) {
        try {
            if (maxValue != null && minValue != null && maxValue < minValue) {
                return MonitoringPointListResponse.success("参数范围无效", new MonitoringPointListDTO(List.of()));
            }

            List<MonitoringPoint> points = monitoringPointDao.getMonitoringPointsByPlannedContractAmount(
                    minValue, maxValue
            );

            List<MonitoringPointDTO> dtoList = points.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return MonitoringPointListResponse.success(
                    "查询成功，共" + dtoList.size() + "个监测点",
                    new MonitoringPointListDTO(dtoList)
            );
        } catch (Exception e) {
            return MonitoringPointListResponse.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MonitoringPointListResponse getMonitoringPointsByPurchaseQuantity(
            Double minValue, Double maxValue) {
        try {
            if (maxValue != null && minValue != null && maxValue < minValue) {
                return MonitoringPointListResponse.success("参数范围无效", new MonitoringPointListDTO(List.of()));
            }

            List<MonitoringPoint> points = monitoringPointDao.getMonitoringPointsByPurchaseQuantity(
                    minValue, maxValue
            );

            List<MonitoringPointDTO> dtoList = points.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return MonitoringPointListResponse.success(
                    "查询成功，共" + dtoList.size() + "个监测点",
                    new MonitoringPointListDTO(dtoList)
            );
        } catch (Exception e) {
            return MonitoringPointListResponse.error("查询失败：" + e.getMessage());
        }
    }


    @Override
    public RegionPage<MonitoringPoint> pageActiveByRegion(String countyDistrict, String town, String village,
                                                          int page, int size) {
        // 校正分页参数
        if (page <= 0) page = 1;
        if (size <= 0) size = 20;
        if (size > 200) size = 200;
        int offset = (page - 1) * size;

        countyDistrict = norm(countyDistrict);
        town           = norm(town);
        village        = norm(village);

        long total = monitoringPointMapper.countActiveByRegion(countyDistrict, town, village);
        List<MonitoringPoint> list = (total > 0)
                ? monitoringPointMapper.pageActiveByRegion(countyDistrict, town, village, size, offset)
                : Collections.emptyList();

        return new RegionPage<>(page, size, total, list);
    }

    private String norm(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }



}
