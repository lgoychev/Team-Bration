package com.company;

import java.awt.*;
import java.io.File;


import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Main {
    //SOURCE and OUTPUT file are locate in com.company folder, don't move
    public static final String inputXmlFilePath = ("SOURCE.xml"); //define a Source name: SOURCE.xml (is correct); SOURCE_01.xml (no contract number, missing "-"); SOURCE_02.xml (POnumber tag not exist)
    public static final String outputXmlFilePath = ("OUTPUT.xml");
    private static Component frame; // add to present a pop-up dialog message window

    public static void main(String argv[])


            throws Exception {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory
                .newDocumentBuilder();
        Document document = documentBuilder.parse(inputXmlFilePath);

        Node invoice = document.getElementsByTagName("Invoice").item(0);
        Node poNumber = document.getElementsByTagName("PONumber").item(0);
        Node totalNet = document.getElementsByTagName("TotalNet").item(0);
        Element contract = document.createElement("Contract");

        if (poNumber != null && !isEmpty(poNumber.getTextContent())) {
            String valueToSplit = poNumber.getTextContent();

            if (valueToSplit.contains("-")) {
                String[] parts = valueToSplit.split("-");
                String before = parts[0];
                String after = parts[1];

                invoice.insertBefore(contract, totalNet);
                poNumber.setTextContent(before);
                contract.appendChild(document.createTextNode(after));
                TransformerFactory transformerFactory = TransformerFactory
                        .newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource domSource = new DOMSource(document);
                StreamResult streamResult = new StreamResult(new File(
                        outputXmlFilePath));
                transformer.transform(domSource, streamResult);
                JOptionPane.showMessageDialog(null, "The Contract number was successfully extract!", "Message",
                        JOptionPane.INFORMATION_MESSAGE); //Pop-up Dialog window which present a message
            } else {
                JOptionPane.showMessageDialog(null, "No contract number available in the PONumber!", "Source file error",
                        JOptionPane.ERROR_MESSAGE); //Pop-up Dialog window which present an error message
            }
        } else {
            JOptionPane.showMessageDialog(frame, "PONumber tag is empty or does not exist!", "Source file error",
                    JOptionPane.ERROR_MESSAGE); //Pop-up Dioalog window which present an error message
        }
    }

    private static boolean isEmpty(String input) {
        return input == null || input.trim().length() == 0;
    }
}