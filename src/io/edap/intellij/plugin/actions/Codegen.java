package io.edap.intellij.plugin.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import io.edap.intellij.plugin.model.CodeGenParam;
import io.edap.intellij.plugin.ui.ProtoJavaGenForm;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成的动作处理逻辑
 */
public class Codegen extends AnAction {

    private Project mProject;

    private static final List EMPTY_LIST = new ArrayList();


    @Override
    public void actionPerformed(AnActionEvent event) {
        mProject = event.getData(PlatformDataKeys.PROJECT);
        DataContext dataContext = event.getDataContext();
        VirtualFile file = LangDataKeys.VIRTUAL_FILE.getData(dataContext);
        // 如果选择的为目录而且目录名称为proto则显示生成代码菜单
        if (file == null || !file.exists()) {
            return;
        }
        Module module = ModuleUtil.findModuleForFile(file, mProject);
        List<VirtualFile> srcDirs = ModuleRootManager.getInstance(module).getSourceRoots(JavaSourceRootType.SOURCE);
        if (file.isDirectory() && "proto".equalsIgnoreCase(file.getName())) {
            List<VirtualFile> protos = getProtoFiles(file.getChildren());
            StringBuilder msg = new StringBuilder();
            for (VirtualFile proto : protos) {
                msg.append(proto.getPath()).append("\n");
            }
            Messages.showMessageDialog(mProject, msg.toString(), "select file", Messages.getInformationIcon());
        } else if ("proto".equalsIgnoreCase(file.getExtension())) { // 如果文件后缀名为proto则显示菜单
            // Messages.showMessageDialog(mProject, file.getPath(), "select file", Messages.getInformationIcon());
            CodeGenParam genParam = new CodeGenParam();
            genParam.setProtoPaths(new String[]{file.getPath()});
            String[] srcs = new String[srcDirs.size()];
            if (srcDirs != null) {
                for (int i=0;i<srcDirs.size();i++) {
                    srcs[i] = srcDirs.get(i).getPath();
                }
            }
            genParam.setSrcPaths(srcs);
            boolean result = new ProtoJavaGenForm(genParam).showAndGet();
            System.out.println("result=" + result);
        } else {

        }
    }

    private List<VirtualFile> getProtoFiles(VirtualFile[] children) {
        if (children == null || children.length == 0) {
            return EMPTY_LIST;
        }
        List<VirtualFile> protos = new ArrayList<>();
        for (VirtualFile child : children) {
            if (child.isDirectory()) {
                List<VirtualFile> tmp = getProtoFiles(child.getChildren());
                if (tmp != null && !tmp.isEmpty()) {
                    protos.addAll(tmp);
                }
            } else {
                if ("proto".equalsIgnoreCase(child.getExtension())) {
                    protos.add(child);
                }
            }
        }
        return protos;
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setEnabledAndVisible(isShowGenAction(event.getDataContext()));
    }

    public static String getFileExtension(DataContext dataContext) {
        VirtualFile file = LangDataKeys.VIRTUAL_FILE.getData(dataContext);
        return file == null ? null : file.getExtension();
    }

    /**
     * 根据选择的内容判断是否显示生成代码的菜单
     * @param dataContext
     * @return
     */
    public static boolean isShowGenAction(DataContext dataContext) {
        VirtualFile file = LangDataKeys.VIRTUAL_FILE.getData(dataContext);
        // 如果选择的为目录而且目录名称为proto则显示生成代码菜单
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isDirectory() && "proto".equalsIgnoreCase(file.getName())) {
            return true;
        } else if ("proto".equalsIgnoreCase(file.getExtension())) { // 如果文件后缀名为proto则显示菜单
            return true;
        }
        return false;
    }
}
