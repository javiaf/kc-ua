package com.kurento.commons.ua;

public interface Continuation<T, U> {
	public void onSuccess(T result);

	public void onProgress(U progress);

	public void onError(Throwable t);
}
