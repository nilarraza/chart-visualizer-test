package com.eitech1.chartv.Entity;

import lombok.Data;

import java.util.List;
@Data
public class Mail {

    private String mailFrom;

    private String mailTo;

    private String mailSubject;

    private String mailContent;

    private String contentType;

    private List<Object> attachments;


}
