package com.centit.support.data.dataset;

import com.centit.support.data.core.DataSetReader;
import com.centit.support.data.core.DataSetWriter;

/**
 * 需要设置一个文件路径
 */
public abstract class FileDataSet implements DataSetReader, DataSetWriter {

    protected String fileBaseDir;

    public String getFileBaseDir() {
        return fileBaseDir;
    }

    public void setFileBaseDir(String fileBaseDir) {
        this.fileBaseDir = fileBaseDir;
    }
}
