package uw.code.center.util;

import java.util.zip.ZipEntry;

/**
 * Zip 打包安全工具。
 * 主要用于防止 Zip Slip（路径穿越）攻击，以及过滤不合法的 entry 名。
 */
public final class ZipUtils {

    private ZipUtils() {
    }

    /**
     * 校验并归一化 zip entry 名称，防御 Zip Slip 路径穿越。
     * <p>
     * 模板渲染出的文件名可能包含外部输入（表名、apiName 等），若出现 "../"、绝对路径或盘符，
     * 解压时会跳出目标目录覆盖任意文件。此方法对非法名称统一返回 null，由调用方跳过该 entry。
     *
     * @param name 待写入 zip 的 entry 名称
     * @return 安全的 entry 名称（仅保留相对路径，去除前导分隔符）；非法时返回 null
     */
    public static String safeEntryName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        // 统一分隔符，去除首尾空白与前导分隔符
        String trimmed = name.trim().replace('\\', '/');
        while (trimmed.startsWith("/")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.isEmpty()) {
            return null;
        }
        // 拒绝任何路径穿越片段（含 "/.."、起始 "../"、纯 ".."）
        String[] parts = trimmed.split("/");
        for (String part : parts) {
            if (part.isEmpty() || ".".equals(part) || "..".equals(part)) {
                return null;
            }
        }
        // 拒绝 Windows 盘符（如 "C:"）
        if (trimmed.length() >= 2 && trimmed.charAt(1) == ':') {
            return null;
        }
        return trimmed;
    }

    /**
     * 构造安全的 ZipEntry，名称非法时返回 null。
     *
     * @param name entry 名称
     * @return ZipEntry 或 null（非法名称）
     */
    public static ZipEntry safeEntry(String name) {
        String safe = safeEntryName(name);
        return safe == null ? null : new ZipEntry(safe);
    }
}
