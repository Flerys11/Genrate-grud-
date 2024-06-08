package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import classe.Attribut;
import classe.ClassGenerator;
import connecting.Connexion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LoginGenerator {
    private static final String TEMPLATE_FILE = "./ReactLoginTemplate.txt";
    private final String CONFIG_FILE;
    private String utilsLogin;
    private String handleLogout;
    private String redirectLogin;
    private String formLogin;
    private String cssLogin;
    private String dossier;
    private String extension;

    private static final Map<String, String> CONFIG_MAP = new HashMap<>();

    static {
        CONFIG_MAP.put("SPRING", "config_file/springform.xml");
    }

    public LoginGenerator(String techno) throws Exception {
        this.CONFIG_FILE = this.getConfigFil(techno);
        this.setParameter();

    }

    public void setParameter() throws Exception {

        this.utilsLogin = this.getOneValue("utilsLogin");
        this.handleLogout = this.getOneValue("handleLogout");
        this.redirectLogin = this.getOneValue("redirectLogin");
        this.formLogin = this.getOneValue("formLogin");
        this.cssLogin = this.getOneValue("cssLogin");
        this.extension = this.getValue("util", "extension");
        this.dossier = this.getValue("util", "dossier");
    }

    public String getConfigFil(String techno) {
        String csType = CONFIG_MAP.get(techno.toUpperCase());
        return (csType != null) ? csType : "";
    }

    public void generate() throws Exception {
        try {

            String className = "Login";
            String template = loadTemplateFromFile();
            String div = "<div>";
            String end_div = "</div>";
            String input = "<input";
            String end = "/>";
            String or = "&&";
            String bouton = "<button";
            String end_bouton = "</button>";
            String label = "<label";
            String end_label = "</label>";
            String h = "<h1>";
            String end_h = "</h1>";

            template = template.replace("#FETCH#", utilsLogin);
            template = template.replace("#LOGOUT#", handleLogout);
            template = template.replace("#CONTENTES#", formLogin);
            template = template.replace("#REDIRECT#", redirectLogin);
            template = template.replace("#DIV#", div);
            template = template.replace("#H#", h);
            template = template.replace("#END_H#", end_h);
            template = template.replace("#LABEL#", label);
            template = template.replace("#END_LABEL#", end_label);
            template = template.replace("#INPUT#", input);
            template = template.replace("#END#", end);
            template = template.replace("#END_DIV#", end_div);
            template = template.replace("#BOUTON#", bouton);
            template = template.replace("#END_BOUTON#", end_bouton);
            template = template.replace("#OR#", "&&");
            template = template.replace("#WELCOM#", "<WelcomePage");
            //System.out.println(template);
            File folder = new File(dossier);
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter(dossier + "/Login" + extension))) {
                        writer.write(template);
                    } catch (Exception e) {
                        throw e;
                    }
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter(dossier + "/Login.css"))) {
                        writer.write(cssLogin);
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new Exception("Error while creating folder");
                }
            } else {
                try (BufferedWriter writer = new BufferedWriter(
                        new FileWriter(dossier + "/Login" + extension))) {
                    writer.write(template);
                } catch (Exception e) {
                    throw e;
                }
                try (BufferedWriter writer = new BufferedWriter(
                        new FileWriter(dossier + "/Login.css"))) {
                    writer.write(cssLogin);
                } catch (Exception e) {
                    throw e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkClasse(String className) {
        boolean result = false;
        String filename = className + extension;
        Path filePath = Paths.get("./dossier", filename);
        if (Files.exists(filePath)) {
            result = true;
        }
        return result;
    }

    private static String loadTemplateFromFile() throws Exception {
        StringBuilder template = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEMPLATE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                template.append(line).append("\n");
            }
        }
        return template.toString();
    }

    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public String getValue(String parent, String element) throws Exception {
        String type = "";
        Path projectRoot = Paths.get(".").toAbsolutePath();
        System.setProperty("user.dir", projectRoot.toString());

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = (Document) builder.parse(CONFIG_FILE);

            NodeList databaseRefList = ((org.w3c.dom.Document) document).getElementsByTagName(parent);
            if (databaseRefList != null) {
                Element databaseRef = (Element) databaseRefList.item(0);
                if (databaseRef != null) {
                    Node elmt = databaseRef.getElementsByTagName(element).item(0);
                    if (elmt != null) {
                        type = elmt.getTextContent();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }


    public String getOneValue(String parent) throws Exception {
        String type = "";
        Path projectRoot = Paths.get(".").toAbsolutePath();
        System.setProperty("user.dir", projectRoot.toString());
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = (Document) builder.parse(CONFIG_FILE);

            Element element = (Element) document.getElementsByTagName(parent).item(0);
            if (element != null) {
                type = element.getTextContent();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

}