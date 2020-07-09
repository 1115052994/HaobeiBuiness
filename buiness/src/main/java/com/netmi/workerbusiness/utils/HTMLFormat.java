package com.netmi.workerbusiness.utils;

import android.util.Log;
import com.netmi.baselibrary.utils.ScreenUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/1/6
 * 修改备注：
 */
public class HTMLFormat {


    public static String getNewContent(String htmlText) {

        Document doc = Jsoup.parse(htmlText);

        Elements elements = doc.getElementsByTag("img");
//        for (Element element : elements) {
//
//            element.attr("width", "100%").attr("height", "auto");
//            if(element.parent().tagName().contains("p") || element.parent().tagName().contains("P") ){
//                element.parent().attr("style")
//            }
//        }

        Elements tables = doc.getElementsByTag("table");
        for (Element element : tables) {
            element.attr("width", "100%");
        }

        return doc.toString();
    }

//    public String cssStr(String str)
//    {
//        if (!str.contains("margin-bottom")) { return str; }
//        String s1 = str.substring(0, str.indexOf("margin-bottom"));
//        String s2 = str.substring(str.indexOf("margin-bottom"), str.length());
//        String s3 = s2.substring(s2.indexOf(";"));
//
//        return s1 + "margin-bottom:0pt" + s3;
//    }

    /**
     * 设置img标签下的width为手机屏幕宽度，height自适应
     *
     * @param data html字符串
     * @return 更新宽高属性后的html字符串
     */
    public static String getNewData(String data) {
        Document document = Jsoup.parse(data);
        Elements body = document.select("body");
        body.attr("style", "margin:0px");
        Elements pElements = document.select("p:has(img)");
        for (Element pElement : pElements) {
            pElement.attr("style", "font-size:0px;margin:0px");
            pElement.attr("max-width", String.valueOf(ScreenUtils.getScreenWidth() + "px"))
                    .attr("height", "auto");
        }
        Elements imgElements = document.select("img");
        for (Element imgElement : imgElements) {
            //重新设置宽高
            imgElement.attr("max-width", "100%")
                    .attr("height", "auto");
            imgElement.attr("style", "max-width:100%;height:auto;padding:0px;margin:0px");
        }
        Log.i("newData:", document.toString());
        return document.toString();
    }

}
