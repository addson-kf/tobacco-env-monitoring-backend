package org.example.model;

//分页要求类

public class PageRequest {
    private int offset;  // 偏移量，通常为 (页数 - 1) * 每页条数
    private int limit;   // 每页条数


    public PageRequest(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }
    public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }


}

