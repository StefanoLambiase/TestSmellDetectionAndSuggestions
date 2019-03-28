package main.testSmellDetection;

import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface IDetector {
    public ArrayList<GeneralFixtureInfo> executeDetectionForGeneralFixture(@NotNull String pFolderPath);

    public ArrayList<EagerTestInfo> executeDetectionForEagerTest(@NotNull String pFolderPath);
}
