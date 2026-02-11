package com.davendra.buzzer.dto.response;

import com.davendra.buzzer.enums.MediaType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadedFileResponse {
    public String url;
    public MediaType type;
}
