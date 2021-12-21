package com.company.addonsdemo.app;

import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageLocator;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component("demo_MyFileProvider")
public class MyFileProvider {
    @Autowired
    private FileStorageLocator fileStorageLocator;

    public File getFile() {
        return FileUtils.getFile("src/main/resources/tosend/changelog.xml");
    }

    public byte[] getFileAsByteArray() throws Exception {
        File file = FileUtils.getFile("src/main/resources/tosend/changelog.xml");
        return FileUtils.readFileToByteArray(file);
    }

    public FileRef getFileAsFileRef() throws Exception {
        File file = FileUtils.getFile("src/main/resources/tosend/changelog.xml");
        FileStorage fileStorage = fileStorageLocator.getDefault();
        return fileStorage.saveStream("myHandMadeFileRef", FileUtils.openInputStream(file));
    }
}