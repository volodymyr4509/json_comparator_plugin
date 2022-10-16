package com.single.ton.json_comparator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.diff.actions.CompareFilesAction;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.contents.DocumentContentImpl;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.ErrorDiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

import static com.intellij.diff.DiffRequestFactoryImpl.getContentTitle;
import static com.intellij.diff.DiffRequestFactoryImpl.getTitle;
import static com.intellij.vcsUtil.VcsUtil.getFilePath;

public class JsonCompareFilesAction extends CompareFilesAction {
    private JsonProcessor jsonProcessor = new JsonProcessor();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        e.getPresentation().setText("Compare Files with JSON Comparator");
    }

    @Override
    protected boolean isAvailable(@NotNull AnActionEvent e) {
        return super.isAvailable(e)
                && Stream.of(Objects.requireNonNull(e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))).allMatch(this::isValidJsonFile);
    }

    @Override
    protected DiffRequest getDiffRequest(@NotNull AnActionEvent e) {
        VirtualFile[] data = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        VirtualFile left = data[0];
        VirtualFile right;

        if (data.length < 2) {
            right = getOtherFile(e.getProject(), data[0]);

            if (right == null || !hasContent(right) || !isValidJsonFile(right)) {
                return new ErrorDiffRequest("Problem with second JSON file");
            }

            if (!data[0].isValid()) {
                return new ErrorDiffRequest("First JSON file is not valid"); // getOtherFile() shows dialog that can invalidate this file
            }
        } else {
            right = data[1];
        }

        try {
            return new SimpleDiffRequest(mainTitle(left, right), sortAndPrettify(left), sortAndPrettify(right), contentTitle(left), contentTitle(right));
        } catch (IOException | RuntimeException exception) {
            exception.printStackTrace();
            return new ErrorDiffRequest("Problem with some of JSON files");
        }
    }

    private DiffContent sortAndPrettify(VirtualFile file) throws IOException {
        Object json = mapper.readValue(new String(file.contentsToByteArray(), file.getCharset()), Object.class);
        jsonProcessor.deepSort(json);
        String prettyJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        return new DocumentContentImpl(new DocumentImpl(prettyJsonString));
    }

    private String contentTitle(VirtualFile file) {
        return getContentTitle(getFilePath(file));
    }

    private String mainTitle(VirtualFile file1, VirtualFile file2) {
        FilePath path1 = file1 != null ? getFilePath(file1) : null;
        FilePath path2 = file2 != null ? getFilePath(file2) : null;

        return getTitle(path1, path2, " <-> ");
    }

    private boolean isValidJsonFile(VirtualFile file) {
        return file != null && file.isValid() && !file.isDirectory() && "json".equalsIgnoreCase(file.getExtension());
    }

    private VirtualFile getOtherFile(Project project, VirtualFile file) {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, true, true, false);

        return FileChooser.chooseFile(descriptor, project, file);
    }
}
