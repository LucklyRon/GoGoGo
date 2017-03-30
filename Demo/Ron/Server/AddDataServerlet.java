package server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class AddDataServerlet
 */
@WebServlet("/AddDataServerlet")
public class AddDataServerlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddDataServerlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String json = IOUtils.toString(request.getInputStream());
		System.out.println(json);
		JSONObject jsonObject = JSONObject.fromObject(json);
		Information information = new Information();
		information.setId(jsonObject.getString("Id"));
		System.out.println(information.getId());
		information.setSex(jsonObject.getString("sex"));
		System.out.println(information.getSex());
		information.setDay(jsonObject.getString("day"));
		System.out.println(information.getDay());
		information.setAge(jsonObject.getString("age"));
		System.out.println(information.getAge());
		information.setName(jsonObject.getString("name"));
		System.out.println(information.getName());
		information.setDescription(jsonObject.getString("description"));
		System.out.println(information.getDescription());
		if (!isExistsNode(information.getId())) {
			AddNewNode(information);
		}
	}

	private void AddNewNode(Information information) {

		try {
			String t = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			t=t.replace("%20", " ");
			int num = t.indexOf("WEB-INF");
			String imagePath = "http://172.20.16.84:8080/Demo/images/";
			String path = t.substring(1, num).replace('/', '\\') + "\\list.xml";
			File xmlFile = new File(path);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("infomation");
			Node node = nList.item(0);
			Node newNode = node.cloneNode(true);
			Element ele = (Element) newNode;
			if (node.getNodeType() == Element.ELEMENT_NODE) {
				ele.getElementsByTagName("Id").item(0).setTextContent(information.getId());
				ele.getElementsByTagName("name").item(0).setTextContent(information.getName());
				ele.getElementsByTagName("sex").item(0).setTextContent(information.getSex());
				ele.getElementsByTagName("age").item(0).setTextContent(information.getAge());
				ele.getElementsByTagName("day").item(0).setTextContent(information.getDay());
				ele.getElementsByTagName("description").item(0).setTextContent(information.getDescription());
				ele.getElementsByTagName("image").item(0).setTextContent(imagePath + information.getId() + ".jpeg");
				doc.getDocumentElement().insertBefore(newNode, node);
				WriteXML(builder, doc, path);
				System.out.println("Add a new node:" + ele.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void WriteXML(DocumentBuilder builder, Document document, String filename) {
		try {

			/** 将document中的内容写入文件中 */
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// 编码
			DOMSource source = new DOMSource(document);
			PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isExistsNode(String id) {
		boolean result = false;
		try {
			String t = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			t=t.replace("%20", " ");
			int num = t.indexOf("WEB-INF");
			String path = t.substring(1, num).replace('/', '\\') + "\\list.xml";
			File xmlFile = new File(path);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("infomation");
			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				Element ele = (Element) node;
				if (node.getNodeType() == Element.ELEMENT_NODE) {
					if (ele.getElementsByTagName("Id").item(0).getTextContent().equals(id))
						result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
