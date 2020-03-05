package com.natsuzora.sankakuviewer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class catchcapi extends Thread{
    @Override
    public void run() {
        // 定义即将访问的链接
        String url = "https://capi-v2.sankakucomplex.com/posts?lang=english&page=" + String.valueOf(MainActivity.page_num) + "&limit=20&tags=" + MainActivity.tages;
        // 定义一个字符串用来存储网页内容
        String result = "";
        // 定义一个缓冲字符输入流
        BufferedReader in = null;
        try
        {
            // 将string转成url对象
            URL realUrl = new URL(url);
            // 初始化一个链接到那个url的连接
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134");
            // 开始实际的连接
            connection.connect();
            // 初始化 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // 用来临时存储抓取到的每一行的数据
            String line;
            while ((line = in.readLine()) != null)
            {
                // 遍历抓取到的每一行并将其存储到result里面
                result += line + "\n";
            }
        } catch (Exception e)
        {
            System.out.println("发送GET请求出现异常！" + e);
            MainActivity.load_handler.sendEmptyMessage(1);
            e.printStackTrace();
        } // 使用finally来关闭输入流
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            } catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
        System.out.println(result);

        int i = 0;
        String[] preview_url = new String[20];
        String[] image_id = new String[20];

        Pattern pattern_pre = Pattern.compile("\"preview_url\":\"(.*?)\"");
        Matcher matcher_pre = pattern_pre.matcher(result);

        Pattern pattern_id = Pattern.compile("\"id\":(\\d+),\"rating\"");
        Matcher matcher_id = pattern_id.matcher(result);

        while (matcher_pre.find() & matcher_id.find()) {
            preview_url[i] = matcher_pre.group(1);
            image_id[i] = matcher_id.group(1);
            i++;
        }
        for (String res : preview_url) {
            System.out.println(res);
        }

        if ((MainActivity.is_searched == 1 & MainActivity.is_loadingmore == 0) | MainActivity.is_refresh == 1){
            MainActivity.mData.clear();
        }

        for(int n = 0;n < 20;n++){
            itemBean data = new itemBean();
            data.id = image_id[n];
            data.url = preview_url[n];
            MainActivity.mData.add(data);
        }

        MainActivity.load_handler.sendEmptyMessage(0);
    }
}
