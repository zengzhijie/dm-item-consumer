package cn.dm.exception;

import cn.dm.common.IErrorCode;

public enum ItemErrorCode implements IErrorCode {
    /**
     * 节目项目异常
     **/

    ITEM_NO_DATA("2001", "没有查询到数据"),
    IMAGE_NO_Exist("2002", "图片不存在");
    private String errorCode;
    private String errorMessage;

    private ItemErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
