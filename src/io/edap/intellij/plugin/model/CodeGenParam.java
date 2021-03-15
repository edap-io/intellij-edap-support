package io.edap.intellij.plugin.model;

/**
 * 生成java代码的参数
 */
public class CodeGenParam {
    /**
     * 需要生成java代码的proto文件列表
     */
    private String[] protoPaths;
    /**
     * 生成java代码的源代码路径列表
     */
    private String[] srcPaths;

    /**
     * 需要生成java代码的proto文件列表
     */
    public String[] getProtoPaths() {
        return protoPaths;
    }

    public void setProtoPaths(String[] protoPaths) {
        this.protoPaths = protoPaths;
    }

    /**
     * 生成java代码的源代码路径列表
     */
    public String[] getSrcPaths() {
        return srcPaths;
    }

    public void setSrcPaths(String[] srcPaths) {
        this.srcPaths = srcPaths;
    }
}
