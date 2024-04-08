package com.filescompressorgroup.files_compressor.exceptions.handler;

import java.io.Serializable;

public class ExceptionResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer status;
	private String title;
	private String detail;
	
	public ExceptionResponse(Integer status, String type, String title, String detail) {
		setStatus(status);
		setTitle(title);
		setDetail(detail);
	}
	
	public ExceptionResponse() {}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
