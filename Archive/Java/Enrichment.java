import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Enrichment {

	public static final String inputXmlFilePath = "C:\\Users\\user1176\\Desktop\\Teamwork\\SOURCE.xml";
	public static final String outputXmlFilePath = "C:\\Users\\user1176\\Desktop\\Teamwork\\OUTPUT.xml";

	public static void main(String argv[]) throws Exception {

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
				System.out.println("The XML File was successfully enriched!");
			} else {
				throw new Exception(
						"No contract number available in the PONumber!");
			}
		} else {
			throw new Exception("PONumber tag is empty or does not exist!");
		}
	}

	private static boolean isEmpty(String input) {
		return input == null || input.trim().length() == 0;
	}
}