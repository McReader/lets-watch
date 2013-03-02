package com.sickfuture.letswatch.task;

public interface ParamCallback<C> {

	void onSuccess(C c);

	void onError(Throwable e);

}
