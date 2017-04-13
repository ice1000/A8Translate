package org.a8sport.translate.net;

import com.google.gson.Gson;
import org.a8sport.translate.bean.TranslationBean;
import org.a8sport.translate.main.LocalData;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pinger on 2016/12/10.
 * 网络工具类，可以直接访问，不需要开子线程
 *
 * @author ice1000
 * @author Pinger
 */
public class HttpUtils {

    private static final String BASE_URL = "http://fanyi.youdao.com/openapi.do?keyfrom=Skykai521&key=977124034&type=data&doctype=json&version=1.1&q=";

    /**
     * 请求网络数据
     */
    public static void requestNetData(String queryWord, TranslateCallBack callBack) {
        // TODO 读取本地缓存
        String local = LocalData.read(queryWord);
        if (local != null) {
            callBack.onSuccess(new Gson().fromJson(local, callBack.mType));
            return;
        }

        try {
            URL url = new URL(BASE_URL + queryWord);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod(conn.getRequestMethod());

            // 连接成功
            if (conn.getResponseCode() == 200) {
                InputStream ins = conn.getInputStream();

                // 获取到Json字符串
                String content = StreamUtils.getStringFromStream(ins);
                if (StringUtils.isNotEmpty(content)) {
                    callBack.onSuccess(new Gson().fromJson(content, callBack.mType));
                    LocalData.store(queryWord, content);
                } else callBack.onFailure(TranslationBean.EMPTY);
            } else {
                callBack.onFailure(conn.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            callBack.onFailure(e.getMessage());
        }
    }
}
