package io.edap.intellij.plugin.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import io.edap.intellij.plugin.model.CodeGenParam;
import io.edap.protobuf.builder.JavaBuildOption;
import io.edap.protobuf.codegen.JavaGenerator;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ProtoJavaGenForm extends DialogWrapper {

    /**
     * 标题输入框
     */
    private ComboBox<String> srcDirsBox;
    private JCheckBox rpcAnnBox;
    private CodeGenParam codeGenParam;
    private static Label srcDirLabel = new Label("src path");

    static {
        srcDirLabel.setText("src path");
    }

    private String[] srcDirs;

    public ProtoJavaGenForm(CodeGenParam genParam) {
        super(true);
        srcDirs = genParam.getSrcPaths();
        codeGenParam = genParam;
        rpcAnnBox = new JCheckBox("add edap-rpc annotation");
        init();
        setTitle("Protocol buffer Java code generate");
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1));

        panel.add(srcDirLabel);

        srcDirsBox = new ComboBox(srcDirs);
        srcDirsBox.setEnabled(true);
        srcDirsBox.setSelectedIndex(0);
        srcDirsBox.setEditable(true);
        panel.add(srcDirsBox);
        panel.add(rpcAnnBox);
        return panel;
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel south = new JPanel();

//        south.setHorizontalAlignment(SwingConstants.RIGHT); //水平居中
//        south.setVerticalAlignment(SwingConstants.CENTER); //垂直居中

        JButton submit = new JButton("generate");
        submit.setHorizontalAlignment(SwingConstants.CENTER); //水平居中
        submit.setVerticalAlignment(SwingConstants.CENTER); //垂直居中
        south.add(submit);

        //按钮事件绑定
        submit.addActionListener(this::actionPerformed);

        return south;
    }

    private void actionPerformed(ActionEvent e) {
        System.out.println("代码生成逻辑执行");
        System.out.println("proto 文件列表");
        JavaBuildOption buildOption = new JavaBuildOption();
        buildOption.setEdapRpc(rpcAnnBox.isSelected());
        buildOption.setChainOper(true);
        for (String proto : codeGenParam.getProtoPaths()) {
            System.out.println("\tproto=" + proto);
            JavaGenerator.generate(new File(proto), new File(srcDirsBox.getItemAt(srcDirsBox.getSelectedIndex())), buildOption);
        }
        System.out.println("srcDirsBox=" + srcDirsBox.getItemAt(srcDirsBox.getSelectedIndex()));
        System.out.println("rpcAnnBox=" + rpcAnnBox.isSelected());


        this.dispose();
    }
}
