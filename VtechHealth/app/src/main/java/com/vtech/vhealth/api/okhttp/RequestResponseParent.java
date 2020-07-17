package com.vtech.vhealth.api.okhttp;

import java.util.List;

/**
 * 分页请求返回对象
 */
public class RequestResponseParent<T> {
    private int start;//起始行
    private int limit;//每页条数
    private int total;//总条数
    private List<T> rows;//返回列表

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
