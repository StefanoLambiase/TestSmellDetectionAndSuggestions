package main.toolWindowConstruction;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import main.toolWindowConstruction.testSmellPanel.TestClassPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TestSmellWindowFactory {
    private JPanel principalPanel;

    private ArrayList<VirtualFile> testClassesFile;


    public TestSmellWindowFactory(ArrayList<VirtualFile> files){
        principalPanel = new JPanel();
        principalPanel.setLayout(new GridLayout(files.size(),1));

        testClassesFile = files;
    }

    /**
     * Questo metodo crea il contenuto della ToolWindow
     * @param tw la ToolWindow in cui inserire il contenuto
     */
    public void createToolWindowWithTestFile(ToolWindow tw){
        for (VirtualFile x : testClassesFile) {
            principalPanel.add(new TestClassPanel(x.getName(), x.getPath()));
        }

        //Parte relativa all'aggiunta del Component alla ToolWindow
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(principalPanel, "", true);
        tw.getContentManager().addContent(content);
    }


}
