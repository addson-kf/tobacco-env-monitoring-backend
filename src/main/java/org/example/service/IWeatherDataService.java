package org.example.service;

import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.WeatherListDTO.WeatherDTO;
import org.example.model.WeatherData;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.response.common.Weather.WeatherListResponse;

import org.example.response.common.CommonResponse;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;

import java.util.List;

public interface IWeatherDataService
{
        /**
         * 插入一条新的天气数据。
         *
         * @param weatherDTO 天气数据传输对象
         * @return 插入操作的响应结果，包含插入操作的状态信息等
         */
        InsertResponse insertWeatherData(WeatherDTO weatherDTO);

        /**
         * 获取所有未删除的天气数据。
         *
         * @return 包含未删除的天气数据的响应结果，包含数据列表及相关状态信息
         */
        WeatherListResponse getAllActiveWeatherData();

        /**
         * 获取所有天气数据。
         *
         * @return 包含所有天气数据的响应结果，包含数据列表及相关状态信息
         */
        WeatherListResponse getAllWeatherData();

        /**
         * 根据 weatherId 删除天气数据（软删除）。
         *
         * @param weatherId 天气数据的 ID
         * @return 删除操作的响应结果，包含删除操作的状态信息等
         */
        DeleteResponse deleteWeatherDataByWeatherId(int weatherId);

        /**
         * 根据 weatherId 恢复已删除的天气数据。
         *
         * @param weatherId 天气数据的 ID
         * @return 恢复操作的响应结果，包含恢复操作的状态信息等
         */
        UpdateResponse restoreWeatherDataByWeatherId(int weatherId);

        /**
         * 查询 weatherId 是否存在。
         *
         * @param weatherId 天气数据的 ID
         * @return 如果存在返回 true，否则返回 false
         */
        boolean checkWeatherIdExists(int weatherId);

        /**
         * 查询 weatherId 下的数据是否已删除。
         *
         * @param weatherId 天气数据的 ID
         * @return 如果已删除返回 true，否则返回 false
         */
        boolean isWeatherDataDeleted(int weatherId);

        /**
         * 根据 monitoringPointId 获取所有天气数据。
         *
         * @param monitoringPointId 监测点 ID
         * @return 包含对应监测点的所有天气数据的响应结果，包含数据列表及相关状态信息
         */
        WeatherListResponse getWeatherDataByMonitoringPointId(int monitoringPointId);

        UpdateResponse updateWeatherDataByWeatherId(WeatherDTO weatherData);




        /**
         * 按县区分页查询
         * @param pr
         * @param countyDistrict
         * @return
         */
        PageResult<WeatherDTO> pageByCounty(PageRequest pr, String countyDistrict);

        /** 按县区获取某字段全部取值（字段名白名单） */
        List<Object> listFieldValuesByCounty(String countyDistrict, WeatherDataField field);

        enum WeatherDataField {
                WeatherType, Value, Describe, AcquisitionCycle
        }
        public void debugPrintByCounty(int page, int size, String county);

        List<Object> listFieldValuesByRegion(String countyDistrict, String town, String village, WeatherDataField field);
}
