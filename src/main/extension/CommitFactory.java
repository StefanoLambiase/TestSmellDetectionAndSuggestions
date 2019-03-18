package main.extension;

import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.InstanceVariableBean;
import it.unisa.testSmellDiffusion.beans.MethodBean;
import it.unisa.testSmellDiffusion.computation.TestSmellDetector;
import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.MethodWithEagerTest;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.*;

import main.toolWindowConstruction.TestSmellWindowFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class CommitFactory  extends CheckinHandlerFactory{

    //Oggetto usato per ottenere i file salvati durante la fase di commit
    private CheckinProjectPanel myPanel;
    //ToolWindow da visualizzare
    private ToolWindow testWindow;

    //Variabile usata per eseguire le analisi
    //DA ELIMINARE
    TestSmellDetector tsm;


    @NotNull
    @Override
    public CheckinHandler createHandler(@NotNull CheckinProjectPanel panel, @NotNull CommitContext commitContext) {
        myPanel = panel;

        final CheckinHandler checkinHandler = new CheckinHandler() {
            @Override
            public ReturnResult beforeCheckin() {
                //Stampa di inizio
                System.out.println("\n\nHello! Inizio la fase pre-commit");

                /*
                //In questa parte mi prendo le classi di test per lavorarci in seguito
                ArrayList<VirtualFile> testClasses = getTestClasses();

                //Creazione programmatica della ToolWindow
                if(testClasses.isEmpty()){
                    System.out.println("\nNon si e' committata alcuna classe di test");
                } else {
                    createToolWindowWithFiles(testClasses);
                    testWindow.show(null);
                }
                */

                //Questa parte riguarda l'analisi degli Smells
                tsm = new TestSmellDetector();

                executeDetectionForGeneralFixture();
                executeDetectionForEagerTest();


                //Chiamata finale
                return super.beforeCheckin();
            }
        };
        return checkinHandler;
    }


    /**
     * Questo metodo si occupa di trovare all'interno di tutte le classi nel commit quelle di test
     * @return un array contenente tutte le classe di test nel commit
     */
    private ArrayList<VirtualFile> getTestClasses(){
        ArrayList<VirtualFile> testClassesFiles = new ArrayList<>();
        ArrayList<VirtualFile> allFiles = (ArrayList<VirtualFile>) myPanel.getVirtualFiles();
        //Eseguo una doppia stampa a fini di test
        //Stampe per la verifica di TUTTI file ottenuti
        System.out.println("\nFile inseriti nel Commit:");
        for (VirtualFile fileX: allFiles) {
            System.out.println("\nNOME FILE: " + fileX.getName());
            System.out.println(fileX.getPath());
            //Inserisco solo i file di "test" all'interno del nuovo array
            if(fileX.getName().contains("Test")){
                testClassesFiles.add(fileX);
            }
        }
        //Stampe per i file di test aggiunti al commit
        if(!testClassesFiles.isEmpty()){
            System.out.println("\nFile di test:");
            for (VirtualFile fileX: testClassesFiles) {
                System.out.println("\nNOME FILE: " + fileX.getName());
                System.out.println(fileX.getPath());
            }
        }
        return testClassesFiles;
    }

    /**
     * Questo metodo si occupa di creare la ToolWindow che mostrerà i file di test individuati
     * @param testFiles la lista di file di test individuati
     */
    private void createToolWindowWithFiles(ArrayList<VirtualFile> testFiles){
        Project activeProject = myPanel.getProject();

        System.out.println("\nTOOL WINDOW: Inizio del processo per registrare la ToolWindow: TestWindow\n");
        //Creo la ToolWindow
        ToolWindowManager twm = ToolWindowManager.getInstance(activeProject);
        System.out.println("Ho preso il ToolWindowManager");

        //Questa parte serve a cancellare una eventuale ToolWindow precedentemente presente
        ToolWindow toolWindow = ToolWindowManager.getInstance(activeProject).getToolWindow("TestWindow");
        if(toolWindow != null){
            twm.unregisterToolWindow("TestWindow");
            System.out.println("Ho dovuto disattivare una precedente istanza della ToolWindow");
        }

        testWindow = twm.registerToolWindow("TestWindow", false, ToolWindowAnchor.BOTTOM);
        testWindow.setTitle("TestWindow");
        System.out.println("Ho registrato la ToolWindow");

        //Instanzio la classe che si occupa della formattazione della ToolWindow
        TestSmellWindowFactory testWindowFactory = new TestSmellWindowFactory(testFiles);
        //Questo metodo si occupa di creare la formattazione interna della ToolWindow e anche di aggiungervela
        testWindowFactory.createToolWindowWithTestFile(testWindow);

        System.out.println("Ho completato le operazioni riguardanti la ToolWindow");
    }



    public void executeDetectionForGeneralFixture(){
        ArrayList<ClassBean> myTestClasses = getAllTestClassesInTheProject();

        //Parte relativa a GeneralFixture
        System.out.println("\nDETECTOR: inizio a cercare per GENERAL FIXTURE: ");
        ArrayList<GeneralFixtureInfo> classesWithGeneralFixture = tsm.checkGeneralFixture(myTestClasses);

        //Parte relativa alla stampa dei dati ottenuti
        for(GeneralFixtureInfo gfi : classesWithGeneralFixture){
            String className = "\nNOME CLASSE: "+gfi.getTestClass().getName();

            for(MethodWithGeneralFixture method : gfi.getMethodsThatCauseGeneralFixture()){
                String methodName = "\nNome Metodo: "+method.getMethod().getName();

                System.out.println(method.getMethod().getName());
                for(InstanceVariableBean instance : method.getListOfInstances()){
                    methodName = new StringBuilder()
                            .append(methodName)
                            .append("\n   "+instance.getName())
                            .toString();

                    System.out.println(instance.getName());
                }
                className = new StringBuilder()
                        .append(className)
                        .append(methodName)
                        .toString();
            }

            System.out.println(className);
        }
    }

    public void executeDetectionForEagerTest(){
        ArrayList<ClassBean> myTestClasses = getAllTestClassesInTheProject();

        //Parte relativa a EagerTest
        System.out.println("\nDETECTOR: inizio a cercare per EagerTest: ");
        ArrayList<EagerTestInfo> classesWithEagerTest = tsm.checkEagerTest(myTestClasses);

        //Parte relativa alla stampa dei dati ottenuti
        for(EagerTestInfo eti : classesWithEagerTest){
            String className = "\nNOME CLASSE: "+eti.getTestClass().getName();
            String productionClassName = "\nNOME PRODUCTION CLASS: "+eti.getProductionClass().getName();

            for(MethodWithEagerTest method : eti.getMethodsThatCauseEagerTest()){
                String methodName = "\nNome Metodo: "+method.getMethod().getName()+"\n   Lista metodi chiamati:";

                for(MethodBean mb : method.getListOfMethodsCalled()){
                    methodName = new StringBuilder()
                            .append(methodName)
                            .append("\n   "+mb.getName())
                            .toString();
                }
                className = new StringBuilder()
                        .append(className)
                        .append(productionClassName)
                        .append(methodName)
                        .toString();
            }

            System.out.println(className);
        }
    }

    /**
     * Metodo usato per trovare tutte le classi di test all'interno del progetto attivo
     * @return la lista delle classi di test
     */
    public ArrayList<ClassBean> getAllTestClassesInTheProject(){
        System.out.println("\nDETECTOR: sono il Detector, inizio a lavorare:\nEcco la path del progetto attivo: "+myPanel.getProject().getBasePath());
        ArrayList<ClassBean> myTestClasses = tsm.getAllTestClass(myPanel.getProject().getBasePath());
        System.out.println("\nEcco le classi trovate:");
        for (ClassBean cb : myTestClasses){
            System.out.println("\n"+cb.toString());
        }
        return myTestClasses;
    }

}
