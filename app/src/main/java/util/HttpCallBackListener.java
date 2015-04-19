package util;

/**
 * Created by jhcpokemon on 04/18/15.
 */
public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
