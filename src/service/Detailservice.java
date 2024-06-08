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

public class Detailservice {
    private static final String TEMPLATE_FILE = "./AuthenticationServiceTemplate.txt";
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

    private static final Map<String, String> CONFIG_MAP = new HashMap<>();

    static {
        CONFIG_MAP.put("C#", "config_file/csControllerconfig.xml");
        CONFIG_MAP.put("SPRING", "config_file/springconfig.xml");
    }

    public Detailservice(String techno) throws Exception {
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

        private static String mapColumnType(String dbType) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(CONFIG_FILE);

        NodeList databaseRefList = document.getElementsByTagName("databaseRef");
        if (databaseRefList != null) {
            Element databaseRef = (Element) databaseRefList.item(0);
            NodeList elements = databaseRef.getElementsByTagName(dbType);
            if (elements != null && elements.getLength() > 0) {
                return elements.item(0).getTextContent();
            }
        }
        return dbType;
    }

    public static Attribut[] getAllColonne(String table, Connection con) throws Exception {
        Attribut[] list_colonne = null;
        try {
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, table, null);
            int counteur = 0;
            while (rs.next()) {
                counteur++;
            }
            list_colonne = new Attribut[counteur];
            rs = metaData.getColumns(null, null, table, null);
            int index = 0;
            while (rs.next()) {
                Attribut colonne = new Attribut();
                colonne.setNom(mapColumnType(rs.getString("COLUMN_NAME")));
                colonne.setType(mapColumnType(rs.getString("TYPE_NAME")));
                ResultSet rsGetForeignKey = metaData.getImportedKeys(null, null, table);
                boolean isForeignKey = false;
                while (rsGetForeignKey.next()) {
                    String foreignKeyName = rsGetForeignKey.getString("FKCOLUMN_NAME");
                    if (colonne.getNom().equalsIgnoreCase(foreignKeyName)) {
                        String referencesTable = rsGetForeignKey.getString("PKTABLE_NAME");
                        String vide = null;
                        colonne.setType(vide);
                        colonne.setNom(vide);
                        isForeignKey = true;
                        break;
                    }
                }
                rsGetForeignKey.close();
                list_colonne[index] = colonne;
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            list_colonne = new Attribut[0];
        }
        return list_colonne;
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
                NodeList elements = databaseRef.getElementsByTagName("utilImportServiceAuth");
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

    public String attribut(String classUser, String token){
        String attr = "";
        attr += "\t" + prive + " " + fin +" " + capitalizeFirstLetter(classUser) + "Repository" + " " + classUser + "Repository" + endLine + "\n";
        attr += "\t" + prive + " " + fin +" " + capitalizeFirstLetter(token) + "Repository" + " " + token + "Repository" + endLine + "\n";
        attr += "\t" + prive + " " + fin +" " + "JwtService" + " " + "jwtService" + endLine + "\n";
        attr += "\t" + prive + " " + fin +" " + "AuthenticationManager" + " " + "authenticationManager" + endLine + "\n";
        attr += "\t" + prive + " " + fin +" " + "PasswordEncoder" + " " + "passwordEncoder" + endLine + "\n";
        return attr;
    }

    public String constructeur(List<String> attrs, String className){
        List<String> constr = attrs;

        String cons = "";
        boolean isFirstAttribute = true;
        cons += "\t" + publie + " " + className + "(";
        for (int j = 0; j < constr.size(); j++) {
            if (constr.get(j) != null) {
                if (!isFirstAttribute) {
                    cons += ", ";
                }
                cons += capitalizeFirstLetter(constr.get(j)) + " " + constr.get(j);
                isFirstAttribute = false;
            }
        }

        cons += " )\n\t" + startBlock;

        for (int j = 0; j < constr.size(); j++) {
            if (constr.get(j) != null) {
                cons += "\n\t\t";
                cons += thi + constr.get(j) + " " + "=" + " "
                        + constr.get(j)
                        + endLine;
            }
        }

        cons += "\n\t" + endBlock;
        return cons;
 
    }

    public String splitUnderscoreC(String input) {
        StringBuilder result = new StringBuilder();
        String[] parts = input.split("_");
    
        result.append(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            String capitalized = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
            result.append(capitalized);
        }
    
        return result.toString();
    }

    public String registre(String className,String classUser, Connection con) throws Exception{
        String reg = "";
        String rep = "AuthenticationResponse";
        String req = "request";
        Attribut[] attribute = this.getAllColonne(classUser,con);

        reg += "\t" + "public" + " " + rep + " " + "registre" + "(" + capitalizeFirstLetter(classUser) + " " + req + ")" + startClass + "\n"
                    + "\t if(" + classUser + "Repository" + "." + "findByUsername(" + req + ".getUsername()).isPresent())" + "" + startBlock + "\n"
                    + "\t\t" + "return" + " " + "new " + rep + "(null, \"" + classUser + " already exist\")" + endLine + "\n" + "\t" +endBlock +"\n";

        reg += "\t " + capitalizeFirstLetter(classUser) + " " + classUser + " = " + "new " + capitalizeFirstLetter(classUser)+ "()" + endLine + "\n";       
        if (attribute != null) {
            System.out.println(classUser);
            for(int i = 0 ; i < attribute.length ; i++) {
                if(!attribute[i].getNom().equals("id") && !attribute[i].getNom().equals("password")){
                    reg += "\t " + classUser + ".set" +splitUnderscoreC(capitalizeFirstLetter(attribute[i].getNom())) + "(" + req + ".get" 
                        + splitUnderscoreC(capitalizeFirstLetter(attribute[i].getNom())) + "())" + endLine + "\n";
                }else if (attribute[i].getNom().equals("password")){
                    reg += "\t " + classUser + ".set" +splitUnderscoreC(capitalizeFirstLetter(attribute[i].getNom())) + "(passwordEncoder.encode("
                    + req + ".get" 
                    + splitUnderscoreC(capitalizeFirstLetter(attribute[i].getNom())) + "()))" + endLine + "\n";
                }

            }
        }

        reg += "\t " + classUser + " = " + classUser + "Repository.save(" + classUser + ")" + endLine +  "\n"; 
        reg += "\t " + "String jwt = jwtService.generateToken(" + classUser + ")" + endLine +"\n";
        reg += "\t saveUserToken(jwt, " + classUser + ")" + endLine + "\n";
        reg += "\t return new " + rep + " (jwt, \"" + classUser + " registration was successful\")" + endLine + "\n" + "\t" +endClass;

        return reg;
    }
    
    public String authenticate(String classUser){
        String authent = "";
        String rep = "AuthenticationResponse";
        String req = "request";

        authent += "\t" + "public" + " " + rep + " " + "registre" + "(" + capitalizeFirstLetter(classUser) + " " + req + ")" + startClass + "\n"
                + "\t " + "authenticationManager.authenticate(" + "\n" 
                + "\t  new UsernamePasswordAuthenticationToken( \n" 
                + "\t   " + req + "getUsername(), \n" +  "\t   " + req + "getPassword() \n \t ) \n \t)" + endLine;

        authent += "\t " + capitalizeFirstLetter(classUser) + " " + req + " = " + classUser + "Repository.findByUsername(request.getUsername()).orElseThrow(); \n";
        authent += "\t String jwt = jwtService.generateToken(" + classUser + "); \n";
        authent += "\t revokeAllTokenBy" + capitalizeFirstLetter(classUser) + "(" + classUser + "); \n" + "\t saveUserToken(jwt, " + classUser + "); \n";
        authent += "\t return new " + rep + "(jwt, \"" + classUser + " login was successful\")" + endLine + "\n" + "\t" +endClass + "\n";

        return authent;
    }

    public String revokeAllToken(String className,String classUser,Connection con) throws Exception{
        String revoke = "";
        Attribut[] attribute = this.getAllColonne(classUser,con);

        revoke += "\t"+ "private void revokeAllTokenByUser(" + capitalizeFirstLetter(classUser) + " " + classUser + ") " + startClass + "\n"
                + "\t List<Token> validTokens = tokenRepository.findAllTokensBy" + capitalizeFirstLetter(classUser) + "(" + classUser + ".get" 
                + capitalizeFirstLetter(attribute[0].getNom()) + "()); \n"; 
        
        revoke += "\t if(validTokens.isEmpty()) { \n" + "\t  return; \n" + "\t }";
        revoke += "\t validTokens.forEach(t-> { \n" + "\t  t.setLoggedOut(true); \n" + "\t }); \n";
        revoke += "\t tokenRepository.saveAll(validTokens); \n" + "\t" + endClass ;
                
        return revoke;
    }

    public String saveUserToken(String classUser, String token){
        String save = "";
        save += "\t" + "private void saveUserToken(String jwt, " + capitalizeFirstLetter(classUser) + " " + classUser + ") "+ startClass + "\n";
        save += "\t " + capitalizeFirstLetter(token) + " " + token + " = new " + capitalizeFirstLetter(token) + "(); \n" 
                + "\t " + token + ".set" + token + "(jwt); \n"
                + "\t " + token + ".setLoggedOut(false); \n" 
                + "\t " + token + ".set" + capitalizeFirstLetter(classUser) + "(" + classUser + "); \n" 
                + "\t " + token + "Repository.save" + "(" + token + "); \n" + "\t "+endBlock + "\n";  

        return save;
    }
    

    public void generate(String className, String classUser, String token) throws Exception {
        try {
            String base = "postgres";
            Connection con = Connexion.getConnection(base);

            String classU = classUser + "Repository";
            String tokens = token + "Repository";
            List<String> attss = new ArrayList<>();
            attss.add(classU);
            attss.add(tokens); 
            attss.add("jwtService"); 
            attss.add("passwordEncoder"); 
            attss.add("authenticationManager");

            String registres = this.registre(className, classUser, con);
    
            String attributes = this.attribut(classUser,token);
            String constructeurs = this.constructeur(attss,className);
            String authenticates = this.authenticate(classUser);
            String revokeAllTokens = this.revokeAllToken(className, classUser, con);
            String saveUserTokens = this.saveUserToken(classUser,token);

            //System.out.println(saveUserTokens);
            String template = loadTemplateFromFile();

            String pack = this.pack();
            template = template.replace("##PACKAGE##", pack);

            String imports = this.importe(className);
            template = template.replace("##IMPORTS##", imports);


            template = template.replace("##DEFINITION##", definition);
            template = template.replace("##ANNOTATION##", service);
            template = template.replace("#CLASSNAME1#", classUser);
            template = template.replace("#CLASSNAME2#", token);
            template = template.replace("#CLASSNAME#", className);
            template = template.replace("##STARTCLASS##", startClass);
            template = template.replace("##ENDCLASS##", endClass);
            template = template.replace("##STARTBLOCK##", startBlock);
            template = template.replace("##ENDBLOCK##", endBlock);
            template = template.replace("##PACKAGE##", startPackageService);
            template = template.replace("#MODEL_PACKAGE#", modelPackage);
            template = template.replace("##PRIVATE##", prive);
            template = template.replace("##PUBLIC##", publie);
            template = template.replace("##ENDPACKAGE##", endPackage);
            template = template.replace("#ATTRIBUTES#", attributes);
            template = template.replace("#CONSTRUCTOR#", constructeurs);
            template = template.replace("#REGISTRE#", registres);
            template = template.replace("#AUTHENTICATE#", authenticates);
            template = template.replace("#REVOKE#", revokeAllTokens);
            template = template.replace("#SAVE#", saveUserTokens);

            //System.out.println(packName);

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