package ru.gasheva.client;

import org.xml.sax.SAXException;
import ru.gasheva.client.controllers.MainControl;
import ru.gasheva.client.database.UserDao;
import ru.gasheva.client.models.PhotoModel;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        PhotoModel photoModel = new PhotoModel();
        MainControl control = new MainControl(photoModel);
    }
}
