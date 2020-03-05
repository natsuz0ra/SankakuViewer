package com.natsuzora.sankakuviewer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getpreviewinfo extends Thread {
    @Override
    public void run(){
        String url = "https://capi-v2.sankakucomplex.com/posts/" + imagePreviewActivity.id;
        String result = "";
        BufferedReader in = null;
        try{
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134");
            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                // 遍历抓取到的每一行并将其存储到result里面
                result += line + "\n";
            }
        } catch (Exception e)
        {
            System.out.println("发送GET请求出现异常！" + e);
            imagePreviewActivity.load_handler.sendEmptyMessage(1);
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
        String sample_url = new String();

        Pattern pattern_sam = Pattern.compile("\"sample_url\":\"(.*?)\"");
        Matcher matcher_sam = pattern_sam.matcher(result);

        while (matcher_sam.find()) {
            sample_url = matcher_sam.group(1);
        }
        System.out.println(sample_url);

        imagePreviewActivity.sample_url = sample_url;
        imagePreviewActivity.load_handler.sendEmptyMessage(0);
    }
}
