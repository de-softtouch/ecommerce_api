package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UploadFileResponse {


    private Long id;

    private String fileName;

    private String fileDownloadUrl;

    private String fileType;

    private long size;
}
