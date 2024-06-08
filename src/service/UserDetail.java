package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import connecting.Connexion;
import java.sql.Connection;
import java.sql.ResultSet;
import database_details.DatabaseDetails;
import classe.Attribut;
import java.sql.DatabaseMetaData;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UserDetail {
    private static final String TEMPLATE_FILE = "./UserDetailsTemplate.txt";
    private static String CONFIG_FILE;
    private String definition;
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
    private String impor;
    private String pack;
    private String packName;
    private String modelPackage;
    private String thi;
    private String attribution;
    private String retour;
    private String contAnnot;
    private String constAnnot;
    private String extend;
    private String contMother;
    private String repo;
    private String cors;
    private String service;
    private String startPackageService;
    private String fin;
    private String packageNameService;
    private String override;

    private static final Map<String, String> CONFIG_MAP = new HashMap<>();

    static {
        CONFIG_MAP.put("C#", "config_file/csControllerconfig.xml");
        CONFIG_MAP.put("SPRING", "config_file/springconfig.xml");
    }

    public UserDetail(String techno) throws Exception {
        CONFIG_FILE = this.getConfigFil(techno);
        this.setParameter();

    }

    public void setParameter() throws Exception {
        this.definition = this.getValue("util", "definition");
        this.endLine = this.getValue("util", "endLine");
        this.endBlock = this.getValue("util", "endBlock");
        this.startBlock = this.getValue("util", "startBlock");
        this.endClass = this.getValue("util", "endClass");
        this.startClass = this.getValue("util", "startClass");
        this.endPackage = this.getValue("util", "endPackage");
        this.startPackageService = this.getValue("util", "startPackageService");
        this.extension = this.getValue("util", "extension");
        this.prive = this.getValue("util", "private");
        this.publie = this.getValue("util", "public");
        this.impor = this.getValue("util", "import");
        this.pack = this.getValue("util", "package");
        this.packName = this.getValue("util", "packageName");
        this.modelPackage = this.getValue("util", "modelPackage");
        this.thi = this.getValue("util", "this");
        this.attribution = this.getValue("util", "attribution");
        this.retour = this.getValue("util", "return");
        this.contAnnot = this.getValue("util", "controllerAnnotation");
        this.constAnnot = this.getValue("util", "constructorAnnotation");
        this.extend = this.getValue("util", "extends");
        this.contMother = this.getValue("util", "controllerMotherClass");
        this.repo = this.getValue("util", "repository");
        this.cors = this.getValue("util", "cors");
        this.service = this.getValue("util", "service");
        this.fin = this.getValue("util", "fin");
        this.packageNameService = this.getValue("util", "packageNameService");
        this.override = this.getValue("util", "override");
    }


    public String getConfigFil(String techno) {
        String csType = CONFIG_MAP.get(techno.toUpperCase());
        return (csType != null) ? csType : "";
    }

    public String importe(String className) throws Exception {
        String importation = "";
        String[] util = this.necessaryImport();
        for (int i = 0; i < util.length; i++) {
            importation += impor + " " + util[i] + endLine + "\n";
        }
        return importation;
    }

    public String[] necessaryImport() throws Exception {
        String[] nec = new String[0];
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = (Document) builder.parse(CONFIG_FILE);

            NodeList databaseRefList = ((org.w3c.dom.Document) document).getElementsByTagName("necessaryImport");
            if (databaseRefList != null) {
                Element databaseRef = (Element) databaseRefList.item(0);
                NodeList elements = databaseRef.getElementsByTagName("utilImportDetail");
                if (elements != null) {
                    nec = new String[elements.getLength()];
                    for (int i = 0; i < elements.getLength(); i++) {
                        Node elmt = elements.item(i);
                        if (elmt != null) {
                            nec[i] = elmt.getTextContent();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nec;
    }

    public String pack() throws Exception {
        String packa = "";
        packa += pack + " " + packageNameService + ";";
        return packa;

    }
    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public String attribut(String nameClass, String classUser){
        String attr = "";
        attr += "\t private " + fin + " "  + capitalizeFirstLetter(classUser) + "Repository repository; \n\n";
        
        attr += "\t public " + nameClass + "(" + capitalizeFirstLetter(classUser) + "Repository repository ) { \n" 
             + "\t\t" + thi + "repository = repository; \n \t } \n\n"; 

        return attr;
    }

    public String load(){
        String loadUser = "";
            loadUser += "\t" + override + "\n";
            loadUser += "\t public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {\n"
                + "\t\treturn repository.findByUsername(username)\n"
                + "\t\t\t\t.orElseThrow(() -> new UsernameNotFoundException(\"User not found\"));\n"
                + "\t }\n\n";
        return loadUser;
    }

    public void generate(String className, String classUser) throws Exception {
        try {

            String classU = classUser + "Repository";

            String attributes = this.attribut(className,classUser);
            String loads = this.load();
            //System.out.println(loads);
            String template = loadTemplateFromFile();

            String pack = this.pack();
            template = template.replace("##PACKAGE##", pack);

            String imports = this.importe(className);
            template = template.replace("##IMPORTS##", imports);
            String impl = "implements UserDetailsService";

            template = template.replace("##DEFINITION##", definition);
            template = template.replace("##ANNOTATION##", service);
            template = template.replace("#CLASSNAME1#", capitalizeFirstLetter(classUser));
            template = template.replace("#CLASSNAME#", className);
            template = template.replace("##STARTCLASS##", startClass);
            template = template.replace("##ENDCLASS##", endClass);
            template = template.replace("##STARTBLOCK##", startBlock);
            template = template.replace("##ENDBLOCK##", endBlock);
            template = template.replace("##PACKAGE##", startPackageService);
            template = template.replace("#MODEL_PACKAGE#", modelPackage);
            template = template.replace("##PRIVATE##", prive);
            template = template.replace("##PUBLIC##", publie);
            template = template.replace("#IMPLEMENTATION#", impl);
            template = template.replace("#ATTRIBUTES#", attributes);
            template = template.replace("#LOAD#", loads);
            template = template.replace("##ENDPACKAGE##", endPackage);

            //System.out.println(template);

            File folder = new File(packageNameService);
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter(packageNameService + "/" + className + extension))) {
                        writer.write(template);
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new Exception("Error while creating folder");
                }
            } else {
                try (BufferedWriter writer = new BufferedWriter(
                        new FileWriter(packageNameService + "/" + className + extension))) {
                    writer.write(template);
                } catch (Exception e) {
                    throw e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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