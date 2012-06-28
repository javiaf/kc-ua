package com.kurento.commons.ua;

public interface Continuation<T> {
	public void onSuccess(T result);

	public void onError(Throwable t);
}
