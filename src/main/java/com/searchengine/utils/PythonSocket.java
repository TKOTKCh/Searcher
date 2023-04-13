package com.searchengine.utils;


import com.searchengine.entity.QueryKeyword;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class PythonSocket {

    public static Socket socket=null;


    public static List<QueryKeyword> getKeyWord(String content){
        Socket socket = null;
        String Code_Adress = "127.0.0.1";
        List<QueryKeyword>result=new LinkedList<>();
        try {
            if(socket==null){
                socket=new Socket("127.0.0.1",9006);
            }
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            outputStream.write(content.getBytes());

            int len = inputStream.read(bytes);
            // ����ֽڳ���
//            System.out.println(len);
            String str = new String(bytes,0,len);
            str=str.replace("'","");
            str=str.replace("[","");
            str=str.replace("]","");
            str=str.replace("(","");
            str=str.replace(")","");
            String[] list=str.split(",");

            for(int i=0;i<list.length;i=i+2){
                String word=list[i];
                double weight=Double.parseDouble(list[i+1]);
                result.add(new QueryKeyword(word,weight));
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
