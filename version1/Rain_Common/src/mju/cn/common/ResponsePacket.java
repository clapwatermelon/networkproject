package mju.cn.common;

import java.io.Serializable;

public class ResponsePacket implements Serializable {
	protected static final long serialVersionUID = 1L;

	private Object[] m_args;
	private String m_responseType;

	public ResponsePacket() {
	}

	public ResponsePacket(String responseType, Object[] args) {
		m_responseType = responseType;
		m_args = args;
	}

	public Object[] getArgs() {
		return m_args;
	}

	public void setArgs(Object[] args) {
		this.m_args = args;
	}

	public String getResponseType() {
		return m_responseType;
	}

	public void setResponseType(String responseType) {
		this.m_responseType = responseType;
	}

}
