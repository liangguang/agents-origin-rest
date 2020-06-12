package com.my.agents.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class Java2XML {

    static HashMap<Integer, String> typeMap = new HashMap<>();

    public static String toXML(String path) throws IOException {

        Element root = new Element("ADI");
        // 将根节点添加到文档中；
        Document Doc = new Document(root);

        CDATA text = null;
        // 创建节点
        Element elements = new Element("Objects");
        Element object = new Element("Object");
        elements.addContent(object);

        Element distributecreatetime = new Element("DistributeCreateTime");
        distributecreatetime.setAttribute("Name", "DistributeCreateTime");
        distributecreatetime.setText("<![CDATA[2019-11-25 13:02:08]]>");
        object.addContent(distributecreatetime);

        Element distributecreateuser = new Element("DistributeCreateUser");
        distributecreateuser.setAttribute("Name", "DistributeCreateUser");
        object.addContent(distributecreateuser);


        Element title = new Element("title");
        title.setAttribute("Name", "title");
        text = new CDATA("");
        text.setText("");
        title.setContent(text);
        object.addContent(title);

        root.addContent(elements);
        // 使xml文件 缩进效果
        Format format = Format.getPrettyFormat();
        format.setOmitEncoding(true);
        format.setOmitDeclaration(true);
        XMLOutputter XMLOut = new XMLOutputter(format);
        XMLOut.output(Doc, new FileOutputStream(path));
        return path;
    }

    public static void main(String[] args) throws IOException {
        toXML("C:/123.xml");
    }
}    