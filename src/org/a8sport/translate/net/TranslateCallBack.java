package org.a8sport.translate.net;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Pinger on 2016/12/10.
 * 翻译数据请求的回调
 */

public abstract class TranslateCallBack<T> {

    public Type mType;

    public TranslateCallBack() {
        mType = getSuperclassTypeParameter(getClass());
    }

    /** 数据类型 */
    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing mType parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    /** 请求成功 */
    public abstract void onSuccess(T result);

    /** 请求失败 */
    public abstract void onFailure(String message);

    /** 请求发生错误 */
    public abstract void onError(String message);
}
