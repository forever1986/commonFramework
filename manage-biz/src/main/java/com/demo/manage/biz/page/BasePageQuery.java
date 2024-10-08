package com.demo.manage.biz.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BasePageQuery {

    @ApiModelProperty(
            value = "页码",
            example = "1"
    )
    private int pageNum = 1;
    @ApiModelProperty(
            value = "每页记录数",
            example = "10"
    )
    private int pageSize = 10;

    public int getPageSize() {
        return this.pageSize < 0 ? 2147483647 : this.pageSize;
    }

    public BasePageQuery() {
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String toString() {
        return "BasePageQuery(pageNum=" + this.getPageNum() + ", pageSize=" + this.getPageSize() + ")";
    }

}
