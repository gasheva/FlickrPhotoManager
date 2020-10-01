package ru.gasheva.client;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XMLParser {
    public String getStatus(String xml) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        String stat = xpath.evaluate("rsp/@stat", document).trim();
        return stat;

    }
    public Photo getPhotoInfo(String xml) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        List<String> tagsName = new ArrayList<>();
        List<String> tagsId = new ArrayList<>();
        Set<Tag> tags = new HashSet<>();

        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        String id = xpath.evaluate("rsp/photo/@id", document).trim();
        String title = xpath.evaluate("rsp/photo/title", document).trim();
        String description = xpath.evaluate("rsp/photo/description", document).trim();
        String urls = xpath.evaluate("rsp/photo/urls", document).trim();

        String latitString = xpath.evaluate("rsp/photo/location/@latitude", document);
        int latit = latitString.length()!=0?(int)Double.parseDouble(latitString):-1000;
        String longitString = xpath.evaluate("rsp/photo/location/@longitude", document);
        int longit = longitString.length()!=0?(int)Double.parseDouble(longitString):-1000;
        String accuracyString = xpath.evaluate("rsp/photo/location/@accuracy", document);
        int accuracy = longitString.length()!=0?(int)Double.parseDouble(accuracyString):-1000;

        NodeList nodeListId = (NodeList) xpath.compile("rsp/photo/tags/tag/@raw").evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodeListId.getLength(); i++)
            tagsName.add(nodeListId.item(i).getNodeValue().trim());

        nodeListId = (NodeList) xpath.compile("rsp/photo/tags/tag/@id").evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodeListId.getLength(); i++)
            tagsId.add(nodeListId.item(i).getNodeValue().trim());

        for (int i = 0; i < tagsId.size(); i++)
            tags.add(new Tag(tagsId.get(i), tagsName.get(i)));

        System.out.println("info:");
        System.out.println(title+" "+description+" "+urls);
        tagsName.forEach(System.out::println);

        Photo photo = new Photo();
        photo.setId(id);
        photo.setDescription(description);
        photo.setTitle(title);
        photo.setUrl(urls);
        photo.setTags(tags);
        photo.setAccurancy(accuracy);
        photo.setLongit(longit);
        photo.setLatit(latit);
        return photo;
    }

    public List<Photo> getPhotos(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        List<String> ids = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        NodeList nodeListId = (NodeList) xpath.compile("rsp/photos/photo/@id").evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodeListId.getLength(); i++)
            ids.add(nodeListId.item(i).getNodeValue().trim());

        nodeListId = (NodeList) xpath.compile("rsp/photos/photo/@title").evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodeListId.getLength(); i++)
            titles.add(nodeListId.item(i).getNodeValue().trim());


        System.out.println("ids:");
        for (int i = 0; i < ids.size(); i++){
            System.out.println(ids.get(i)+" "+titles.get(i));
        }

        List<Photo> photos = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++)
            photos.add(new Photo(ids.get(i), titles.get(i)));
        return photos;
    }
    public String getPhotoId(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        return  xpath.evaluate("rsp/photoid", document).trim();
    }
}
