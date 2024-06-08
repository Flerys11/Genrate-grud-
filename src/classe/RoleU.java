package classe;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import connecting.Connexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.CollationElementIterator;
import java.sql.*;
import java.io.*;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.file.Paths;
import java.nio.file.Path;
import database_details.DatabaseDetails;
import database_details.ForeignKeyDetails;
import classe.ClassGenerator;

public class RoleU {
    private static final String TEMPLATE_FILE = "./RoleTemplat.txt";
    private static String CONFIG_FILE;
    private String definitionenum;
    private String endLine;
    private String endBlock;
    private String startBlock;
    private String endClass;
    private String startClass;
    private String endPackage;
    private String startPackage;
    private String extension;
    private String prive;
    private String publie;
    private String protect;
    private String annotation;
    private String impor;
    private String pack;
    private String packName;
    private String thi;
    private String attribution;
    private String retour;
    private String table;
    private String column;

    private static final Map<String, String> CONFIG_MAP = new HashMap<>();

    static {
        CONFIG_MAP.put("SPRING", "config_file/javaconfig.xml");
        CONFIG_MAP.put("C#", "config_file/csconfig.xml");
    }

    public RoleU(String techno) throws Exception {
        this.CONFIG_FILE = this.getConfigFil(techno);
        this.setParameter();
    }

    public String getConfigFil(String techno) {
        String csType = CONFIG_MAP.get(techno.toUpperCase());
        return (csType != null) ? csType : "";
    }

    public void setParameter() throws Exception {
        this.definitionenum = this.getValue("util", "definitionenum");
        this.endLine = this.getValue("util", "endLine");
        this.endBlock = this.getValue("util", "endBlock");
        this.startBlock = this.getValue("util", "startBlock");
        this.endClass = this.getValue("util", "endClass");
        this.startClass = this.getValue("util", "startClass");
        this.endPackage = this.getValue("util", "endPackage");
        this.startPackage = this.getValue("util", "startPackage");
        this.extension = this.getValue("util", "extension");
        this.prive = this.getValue("util", "private");
        this.publie = this.getValue("util", "public");
        this.protect = this.getValue("util", "protected");
        this.impor = this.getValue("util", "import");
        this.pack = this.getValue("util", "package");
        this.packName = this.getValue("util", "packageName");
        this.thi = this.getValue("util", "this");
        this.attribution = this.getValue("util", "attribution");
        this.retour = this.getValue("util", "return");
        this.annotation = this.getValue("util", "annotation");
        this.table = this.getValue("util", "table");
        this.column = this.getValue("util", "column");
    }

    public String attribut(){
        String attr = "";
        attr += "\t USER, \n \t ADMIN \n";
        return attr;
    }


    public void generate(String nameClass) {
        
        try {
                String className = nameClass;
                String template = loadTemplateFromFile();
                String packa = this.pack();
                String attributes = this.attribut();
                template = template.replace("##PACKAGE##", packa);
                template = template.replace("##STARTPACKAGE##", startPackage);
                template = template.replace("#CLASS_NAME#", className);
                template = template.replace("##DEFINITION##", definitionenum);
                template = template.replace("##STARTCLASS##", startClass);
                template = template.replace("##ENDCLASS##", endClass);
                template = template.replace("##ENDPACKAGE##", endPackage);
                template = template.replace("#ATTRIBUTS#", attributes);
                //System.out.println(template);
                //System.out.println(attributes);
                
                File folder = new File(packName);
                if (!folder.exists()) {
                    if (folder.mkdirs()) {
                        try (BufferedWriter writer = new BufferedWriter(
                                new FileWriter(packName + "/" + className + extension))) {
                            writer.write(template);
                        } catch (Exception e) {
                            throw e;
                        }
                    } else {
                        throw new Exception("Error while creating folder");
                    }
                } else {
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter(packName + "/" + className + extension))) {
                        writer.write(template);
                    } catch (Exception e) {
                        throw e;
                    }
                }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static String loadTemplateFromFile() throws IOException {
        StringBuilder template = new StringBuilder();
        Path projectRoot = Paths.get("../..").toAbsolutePath();
        System.setProperty("user.dir", projectRoot.toString());
        try (BufferedReader reader = new BufferedReader(new FileReader(TEMPLATE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                template.append(line).append("\n");
            }
        }
        return template.toString();
    }

    public String pack() throws Exception {
        String packa = "";
        packa += pack + " " + packName;
        return packa;
    }

    public String getValue(String parent, String element) throws Exception {
        String type = "";
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

}