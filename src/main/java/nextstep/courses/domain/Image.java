package nextstep.courses.domain;

import java.util.Set;

public class Image {

    private static final int MAXIMUM_IMAGE_SIZE = 1;
    private static final int MINIMUM_WIDTH = 300;
    private static final int MINIMUM_HEIGHT = 200;
    private static final Set<String> ALLOWED_TYPES = Set.of("gif", "jpg", "jpeg", "png", "svg");

    private Long id;
    private int sessionId;
    private String fileName;
    private int fileSize;
    private String fileType;
    private int width;
    private int height;

    public Image(int sessionId, String fileName, int fileSize, String fileType, int width, int height) {
        validateSize(fileSize);
        validateFileType(fileType);
        validateFileStandard(width, height);

        this.sessionId = sessionId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.width = width;
        this.height = height;
    }

    private void validateFileType(String fileType) {
        if (!ALLOWED_TYPES.contains(fileType.toLowerCase())) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
        }

    }

    private void validateSize(int fileSize) {
        if (fileSize > MAXIMUM_IMAGE_SIZE) {
            throw new IllegalArgumentException("파일 크기는 1MB 이하여야 합니다.");
        }
    }

    private void validateFileStandard(int width, int height) {
        if (width < MINIMUM_WIDTH || height < MINIMUM_HEIGHT) {
            throw new IllegalArgumentException("파일은 너비가 300px, 높이가 200px 이상이어야 합니다.");
        }
        if (width * 2 != height * 3) {
            throw new IllegalArgumentException("파일 비율은 3:2여야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
