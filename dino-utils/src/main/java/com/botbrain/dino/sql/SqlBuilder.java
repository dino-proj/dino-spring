package com.botbrain.dino.sql;

import java.util.List;

public interface SqlBuilder {
    /**
     * 获取生成的sql语句
     * @return
     */
    String getSql();

    /**
     * 获取sql语句需要的参数数组
     * @return
     */
    Object[] getParams();

    /**
      * Constructs a list of items with given separators.
      *
      * @param sql  StringBuilder to which the constructed string will be appended.
      * @param list List of objects (usually strings) to join.
      * @param start String to be added to the start of the list, before any of the
      * <p>            items.
      * @param sep  Separator string to be added between items in the list.
      * @param end  String to be append to the end of the list, after all of the
      * <p>            items.
      */
    default StringBuilder appendList(final StringBuilder sql, final List<?> list, final String start, final String sep,
            final String end) {
        var first = true;

        for (final Object s : list) {
            if (first) {
                sql.append(start);
            } else {
                sql.append(sep);
            }
            sql.append(s);
            first = false;
        }
        if (end != null && !list.isEmpty()) {
            sql.append(end);
        }
        return sql;
    }

    default StringBuilder appendList(final StringBuilder sql, final List<?> list, final String start,
            final String sep) {
        return appendList(sql, list, start, sep, null);
    }
}
