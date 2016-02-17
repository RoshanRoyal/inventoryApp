package in.periculum.ims.listener;


public interface HttpRequestCallback<T> {
	void response(String errro, T returnType);
	void onError(String errorMessage);

}
