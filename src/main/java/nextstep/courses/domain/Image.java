package nextstep.courses.domain;

import java.util.Set;

public class Image {

    private final static int MAXIMUM_IMAGE_SIZE = 1;
    private final static int MAXIMUM_WIDTH = 300;
    private final static int MAXIMUM_HEIGHT = 200;
    private static final Set<String> ALLOWED_TYPES = Set.of("gif", "jpg", "jpeg", "png", "svg");

    private final String fileName;
    private final int fileSize;
    private final String fileType;
    private final int width;
    private final int height;

    public Image(String fileName, int fileSize, String fileType, int width, int height) {
        validationFileSize(fileSize);
        validationFileType(fileType);
        validationFileStandard(width, height);

        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.width = width;
        this.height = height;
    }

    private void validationFileType(String fileType) {
        if (!ALLOWED_TYPES.contains(fileType.toLowerCase())) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
        }

    }

    private void validationFileSize(int fileSize) {
        if (fileSize > MAXIMUM_IMAGE_SIZE) {
            throw new IllegalArgumentException("파일 크기는 1MB 이하여야 합니다.");
        }
    }

    private void validationFileStandard(int width, int height) {
        if (width < MAXIMUM_WIDTH || height < MAXIMUM_HEIGHT) {
            throw new IllegalArgumentException("파일은 너비가 300px, 높이가 200px 미만이어야 합니다.");
        }
        if (width * 2 != height * 3) {
            throw new IllegalArgumentException("파일 비율은 3:2여야 합니다.");
        }
    }

}
