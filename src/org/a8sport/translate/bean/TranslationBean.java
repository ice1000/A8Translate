package org.a8sport.translate.bean;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Pinger on 2016/12/10.
 * 翻译结果javabean
 */
public class TranslationBean {

    /**
     * 返回错误码的状态，有道返回
     */
    private final static int SUCCESS = 0;  // 成功
    private final static int QUERY_STRING_TOO_LONG = 20;  // 要翻译的文本过长
    private final static int CAN_NOT_TRANSLATE = 30;  // 无法进行有效的翻译
    private final static int INVALID_LANGUAGE = 40;   // 不支持的语言类型
    private final static int INVALID_KEY = 50;  // 无效的key
    private final static int NO_RESULT = 60;   // 无词典结果
    public final static String EMPTY = "返回数据为空";

    /**
     * translation : ["解决"]
     * basic : {"us-phonetic":"rɪ'zɑlv","phonetic":"rɪ'zɒlv","uk-phonetic":"rɪ'zɒlv","explains":["n. 坚决；决定要做的事","vt. 决定；溶解；使\u2026分解；决心要做\u2026","vi. 解决；决心；分解"]}
     * query : resolve
     * errorCode : 0
     * web : [{"value":["决心","决定","解决"],"key":"Resolve"},{"value":["归结为","使分解为","分解为"],"key":"resolve into"},{"value":["解决分歧","消除分歧"],"key":"resolve differences"}]
     */

    public BasicBean basic;
    public String query;
    public int errorCode;
    public List<String> translation;
    public List<WebBean> web;

    class BasicBean {
        public String usPhonetic;
        public String phonetic;
        public String ukPhonetic;
        public List<String> explains;
    }

    class WebBean {
        public String key;
        public List<String> value;
    }


    /**
     * 取错误信息
     */
    @NotNull
    @Contract(pure = true)
    private String getErrorMessage() {
        switch (errorCode) {
            case SUCCESS:
                return "成功";
            case QUERY_STRING_TOO_LONG:
                return "要翻译的文本过长";
            case CAN_NOT_TRANSLATE:
                return "无法进行有效的翻译";
            case INVALID_LANGUAGE:
                return "不支持的语言类型";
            case INVALID_KEY:
                return "无效的key";
            case NO_RESULT:
                return "无词典结果";
        }
        return "你选中的是什么鬼？";
    }


    /**
     * 获取不同语言的翻译内容
     */
    @Nullable
    private String getPhonetic() {
        if (basic == null) return null;

        String phonetic = null;
        String usPhonetic = basic.usPhonetic;
        String ukPhonetic = basic.ukPhonetic;
        if (usPhonetic == null && ukPhonetic == null) {

            phonetic = "发音：[" + basic.phonetic + "]；";
        } else {
            if (usPhonetic != null) phonetic = "美式：[" + usPhonetic + "]；";
            if (ukPhonetic != null) {
                if (phonetic == null) phonetic = "";
                phonetic = phonetic + "英式：[" + ukPhonetic + "]；";
            }
        }
        System.out.println(phonetic);
        return phonetic;
    }

    /**
     * 获取翻译
     */
    @Nullable
    private String getExplains() {
        if (basic == null) {
            return null;
        }
        StringBuilder result = null;
        List<String> explains = basic.explains;
        if (explains.size() > 0) {
            result = new StringBuilder();
            for (String explain : explains) {
                result.append(explain).append("\n");
            }
        }
        return result != null ? result.toString() : null;
    }

    /**
     * 获取直接的翻译结果
     */
    @Nullable
    private String getTranslationResult() {
        if (translation == null) {
            return null;
        }
        StringBuilder result = null;
        if (translation.size() > 0) {
            result = new StringBuilder();
            for (int i = 0; i < translation.size(); i++) {
                String keyword = translation.get(i);
                if (i < translation.size() - 1) result.append(keyword).append("，");
                else result.append(keyword).append("；");
            }
        }
        return result != null ? result.toString() : null;
    }

    /**
     * 获取网络翻译结果
     */
    @Nullable
    private String getWebResult() {
        if (web == null) {
            return null;
        }
        StringBuilder result = null;
        if (web.size() > 0) {
            result = new StringBuilder();
            for (WebBean webBean : web) {
                String key = webBean.key;
                result.append(key).append("：");

                List<String> value = webBean.value;
                for (int i = 0; i < value.size(); i++) {
                    String keyword = value.get(i);
                    if (i < value.size() - 1) {
                        result.append(keyword).append("，");
                    } else {
                        result.append(keyword);
                    }
                }

                result.append("\n");
            }
        }


        return result != null ? result.toString() : null;
    }

    private boolean isSentence() {
        return query.trim().contains(" ");
    }

    /**
     * 结果
     */
    @Override
    public String toString() {
        String string = null;
        if (errorCode != SUCCESS) {
            string = "错误代码：" + errorCode + "\n" + getErrorMessage();
        } else {
            String translation = getTranslationResult();
            if (translation != null) {
                translation = translation.substring(0, translation.length() - 1);
                if (!translation.equals(query)) {
                    if (isSentence()) {
                        string = getTranslationResult() + "\n";
                    } else {
                        string = (query + "：" + getTranslationResult() + "\n");
                    }
                }
            }
            if (getPhonetic() != null) {
                if (string == null) {
                    string = "";
                }
                string += (getPhonetic() + "\n");
            }
            if (getExplains() != null) {
                if (string == null) {
                    string = "";
                }
                string += (getExplains());
            }
            if (getWebResult() != null) {
                if (string == null) {
                    string = "";
                }
                string += "网络释义：\n";
                string += (getWebResult());
            }
        }
        if (string == null) {
            string = "你选的内容：" + query + "\n抱歉,翻译不了...";
        }
        return string;
    }
}
