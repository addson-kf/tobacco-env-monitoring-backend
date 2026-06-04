package org.example.util;

public class GeoUtil {
    /**
     * 解析监测点坐标字符串，支持：
     * 1) "lat,lon"
     * 2) "lon,lat"
     *
     * 通过范围自动判断：
     * - 纬度绝对值 <= 90
     * - 经度绝对值 <= 180
     *
     * @param coordinates 坐标字符串
     * @return double[]{lat, lon}
     */
    public static double[] parseLatLon(String coordinates) {
        if (coordinates == null || coordinates.trim().isEmpty()) {
            throw new IllegalArgumentException("监测点坐标为空");
        }

        String[] parts = coordinates.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("监测点坐标格式错误，应为 lat,lon 或 lon,lat");
        }

        double a;
        double b;
        try {
            a = Double.parseDouble(parts[0].trim());
            b = Double.parseDouble(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("监测点坐标不是数字: " + coordinates);
        }

        if (Math.abs(a) <= 90 && Math.abs(b) <= 180) {
            return new double[]{a, b};
        }

        if (Math.abs(b) <= 90 && Math.abs(a) <= 180) {
            return new double[]{b, a};
        }

        throw new IllegalArgumentException("监测点坐标超出地理范围: " + coordinates);
    }

    /**
     * 解析监测点坐标数组，支持：
     * 1) [lat, lon]
     * 2) [lon, lat]
     *
     * 通过范围自动判断：
     * - 纬度绝对值 <= 90
     * - 经度绝对值 <= 180
     *
     * @param coordinates 坐标数组
     * @return double[]{lat, lon}
     */
    public static double[] parseLatLon(double[] coordinates) {
        if (coordinates == null || coordinates.length != 2) {
            throw new IllegalArgumentException("监测点坐标为空或长度不为2");
        }

        double a = coordinates[0];
        double b = coordinates[1];

        if (Math.abs(a) <= 90 && Math.abs(b) <= 180) {
            return new double[]{a, b};
        }

        if (Math.abs(b) <= 90 && Math.abs(a) <= 180) {
            return new double[]{b, a};
        }

        throw new IllegalArgumentException("监测点坐标超出地理范围: [" + a + "," + b + "]");
    }
}
