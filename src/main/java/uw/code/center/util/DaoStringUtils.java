package uw.code.center.util;

/**
 * DAO 代码生成专用的字符串命名转换工具。
 * <p>
 * 提供表名/列名到 Java 实体名、属性名、路径风格的转换，供数据库元数据解析器
 * （{@code MySQLDataMetaImpl} / {@code PostgreSQLDataMetaImpl} / {@code OracleDataMetaImpl}）派生命名信息。
 * </p>
 *
 * @author axeon
 */
public class DaoStringUtils {

    /**
     * 将表名转为路径样式：下划线替换为斜杠并补前导斜杠。
     * <p>例如 {@code user_info} → {@code /user/info}。</p>
     *
     * @param text 表名
     * @return 路径样式字符串
     */
    public static String toPathStyle(String text) {
        return "/" + text.replace('_', '/');
    }

    /**
     * 获取表名的第一段（按首个下划线截取），用于推断所属模块/父级目录。
     * <p>例如 {@code user_info} → {@code user}；无下划线时原样返回。</p>
     *
     * @param table 表名
     * @return 首段名称
     */
    public static String getTableParent(String table) {
        int pos = table.indexOf('_');
        if (pos > -1) {
            return table.substring(0, pos);
        } else {
            return table;
        }
    }

    /**
     * 将下划线分隔的字符串转为驼峰样式（首字母保持原样）。
     * <p>例如 {@code user_name} → {@code userName}。</p>
     *
     * @param text 源字符串
     * @return 驼峰样式字符串
     */
    public static String toClearCase(String text) {
        StringBuilder sb = new StringBuilder();
        char[] data = text.toCharArray();
        boolean needClear = false;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != '_') {
                if (needClear) {
                    sb.append(Character.toUpperCase(data[i]));
                    needClear = false;
                } else {
                    sb.append(data[i]);
                }
            } else {
                needClear = true;
            }
        }
        return sb.toString();
    }

    /**
     * 首字母大写。
     *
     * @param text 源字符串
     * @return 首字母大写的字符串
     */
    public static String toUpperFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        char[] ch = text.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] -= 32;
        }
        return new String(ch);
    }

    /**
     * 首字母小写。
     *
     * @param text 源字符串
     * @return 首字母小写的字符串
     */
    public static String toLowerFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        char[] ch = text.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] += 32;
        }
        return new String(ch);
    }

}
