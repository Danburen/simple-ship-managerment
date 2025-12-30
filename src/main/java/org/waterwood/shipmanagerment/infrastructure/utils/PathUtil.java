package org.waterwood.shipmanagerment.infrastructure.utils;

import cn.hutool.core.date.DateUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public final class PathUtil {
    public static String getUniquePathFile(String fileSuffix) {
        String uuid = UUID.randomUUID().toString();
        return DateUtil.today().replace("-", "/") + "/" + uuid + "." + fileSuffix;
    }

    public static String buildPath(Serializable... segments){
        return Arrays.stream(segments)
                .map(String::valueOf)
                .collect(Collectors.joining("/"));
    }
    public static String getFilenameWithNoSuffix(String path){
        return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
    }
}