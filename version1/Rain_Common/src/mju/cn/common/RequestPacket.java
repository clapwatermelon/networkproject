package mju.cn.common;

import java.io.Serializable;

public class RequestPacket implements Serializable {
	protected static final long serialVersionUID = 1L;

	public static enum SYNC_TYPE {
		SYNCHRONOUS, ASYNCHRONOUS
	};

	private String m_className;
	private String m_methodName;
	private SYNC_TYPE m_syncType;
	private Object[] m_args;

	public RequestPacket() {
	}

	public RequestPacket(String className, String methodName, Object[] args) {
		this.m_className = className;
		this.m_args = args;
		this.m_methodName = methodName;
	}

	public String getClassName() {
		return m_className;
	}

	public void setClassName(String className) {
		this.m_className = className;
	}

	public Object[] getArgs() {
		return m_args;
	}

	public void setArgs(Object[] args) {
		this.m_args = args;
	}

	public String getMethodName() {
		return m_methodName;
	}

	public void setMethodName(String methodName) {
		this.m_methodName = methodName;
	}

	public SYNC_TYPE getSyncType() {
		return m_syncType;
	}

	public void setSyncType(SYNC_TYPE syncType) {
		this.m_syncType = syncType;
	}
}
