package classe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

public class ClassGeneratorAuth {
    private static final String TEMPLATE_FILE = "./ClassTemplate.txt";
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

    public ClassGeneratorAuth(String techno) throws Exception {
        this.CONFIG_FILE = this.getConfigFil(techno);
        this.setParameter();
    }

    public String getConfigFil(String techno) {
        String csType = CONFIG_MAP.get(techno.toUpperCase());
        return (csType != null) ? csType : "";
    }

    public void setParameter() throws Exception {
        this.definition = this.getValue("util", "definition");
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

    public HashMap<String, ArrayList<Attribut>> data() throws Exception {
        ArrayList<DatabaseDetails> details = DatabaseDetails.getDatabaseDetailsFromDatabase();
        HashMap<String, ArrayList<Attribut>> hs = new HashMap<String, ArrayList<Attribut>>();
        for (int i = 0; i < details.size(); i++) {
            DatabaseDetails databaseDetails = details.get(i);
            if (hs.containsKey(databaseDetails.getTableName())) {
                ArrayList<Attribut> arr = hs.get(databaseDetails.getTableName());
                arr.add(new Attribut(databaseDetails.getColumnName(),
                        this.getValue("databaseRef", databaseDetails.getColumnType()),
                        this.getValue("import", databaseDetails.getColumnType())));
                hs.put(databaseDetails.getTableName(), arr);
            } else {
                ArrayList<Attribut> arr = new ArrayList<Attribut>();
                arr.add(new Attribut(databaseDetails.getColumnName(),
                        this.getValue("databaseRef", databaseDetails.getColumnType()),
                        this.getValue("import", databaseDetails.getColumnType())));
                hs.put(databaseDetails.getTableName(), arr);
            }
        }
        return hs;
    }

    public static Attribut[] getAllFK(String table, Connection con) throws Exception {
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
                String val = rs.getString("COLUMN_NAME");
                ResultSet rsGetForeignKey = metaData.getImportedKeys(null, null, table);
                boolean isForeignKey = false;
                while (rsGetForeignKey.next()) {
                    String foreignKeyName = rsGetForeignKey.getString("FKCOLUMN_NAME");
                    if (val.equalsIgnoreCase(foreignKeyName)) {
                        String referencesTable = rsGetForeignKey.getString("PKTABLE_NAME");
                        colonne.setType(referencesTable);
                        colonne.setNom(val);
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
        }
        return list_colonne;
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
        }
        return list_colonne;
    }

    public String splitUnderscore(String input) {
        StringBuilder result = new StringBuilder();
        String[] parts = input.split("_");
        for (String part : parts) {
            result.append(part);
        }
    
        return result.toString();
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

    public String attribut(HashMap<String, ArrayList<Attribut>> hs, String tableName, Connection con) throws Exception {
        String attributes = "";
        Attribut[] attribute = this.getAllColonne(tableName,con);
        Attribut[] foreignK = this.getAllFK(tableName,con);
        attributes += "\t@Id\n\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n";
        for (int i = 0; i < attribute.length; i++) {
            if (attribute[i].getType() != null) {
                if (!attribute[i].getNom().equals("role")) {
                    attributes += "\t@Column(name = \"" + attribute[i].getNom() + "\")" + "\n";
                    attributes += "\t" + prive + " " + attribute[i].getType() + " " + splitUnderscore(attribute[i].getNom()) + endLine + "\n";
                }
            }
        }
        for (int j = 0 ; j < foreignK.length; j ++){
            if(foreignK[j].getType()!= null){
                attributes += "\t @ManyToOne" + "\n";
                attributes += "\t @JoinColumn(name = \"" + foreignK[j].getNom() + "\", nullable = false)" + "\n";
                attributes += "\t" + prive + " " + capitalizeFirstLetter(foreignK[j].getType()) + " " + foreignK[j].getType() + endLine + "\n";
            }
        }

        String r = "role";
        attributes += "\t@Enumerated(value = EnumType.STRING)\n";
        attributes += "\t" + prive + " " + capitalizeFirstLetter(r) + " " + r + endLine + "\n";

        attributes += "\t@OneToMany(mappedBy = \"" + tableName + "\")" + "\n";
        attributes += "\t" + prive + "List<Token>" + " " + "tokens" + endLine + "\n";

        return attributes;
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
    

    public String setter(HashMap<String, ArrayList<Attribut>> hs, String tableName, Connection con) throws Exception {
        String setter = "";
        Attribut[] foreignK = this.getAllFK(tableName,con);
        Attribut[] attribute = this.getAllColonne(tableName,con);
        for (int i = 0; i < attribute.length; i++) {
            if(attribute[i].getType() != null){
                setter += "\t" + publie + " void set" + capitalizeFirstLetter(splitUnderscoreC(attribute[i].getNom())) + "( "
                        + attribute[i].getType() + " " + splitUnderscore(attribute[i].getNom()) + " )\n\t" + startBlock + "\n\t\t"
                        + thi
                        + splitUnderscore(attribute[i].getNom()) + attribution
                        + splitUnderscore(attribute[i].getNom()) + endLine + " \n\t" + endBlock + "\n\n";
            }
        }
        for (int j = 0 ; j < foreignK.length; j ++){
            if(foreignK[j].getType()!= null){
                setter += "\t" + publie + " void set" +  capitalizeFirstLetter(foreignK[j].getType()) + "( "
                        + capitalizeFirstLetter(foreignK[j].getType()) + " " + foreignK[j].getType() + " )\n\t" + startBlock + "\n\t\t"
                        + thi
                        + foreignK[j].getType() + attribution
                        + foreignK[j].getType() + endLine + " \n\t" + endBlock + "\n\n";
            }
        }

        return setter;
    }

    public String getter(HashMap<String, ArrayList<Attribut>> hs, String tableName, Connection con) throws Exception {
        String getter = "";
        Attribut[] foreignK = this.getAllFK(tableName, con);
        Attribut[] attribute = this.getAllColonne(tableName,con);
        for (int i = 0; i < attribute.length; i++) {
            if(attribute[i].getType() != null){
                getter += "\t" + publie + " " + attribute[i].getType() + " get"
                        + capitalizeFirstLetter(splitUnderscoreC(attribute[i].getNom())) + "()\n\t" + startBlock + "\n\t\t"
                        + retour + " "
                        + thi
                        + attribute[i].getNom() + endLine + " \n\t" + endBlock + "\n\n";
            }

        }
        for (int j = 0 ; j < foreignK.length; j ++){
            if(foreignK[j].getType()!= null){
                getter += "\t" + publie + " " + capitalizeFirstLetter(foreignK[j].getType()) + " get"
                        + capitalizeFirstLetter(foreignK[j].getType()) + "()\n\t" + startBlock + "\n\t\t"
                        + retour + " "
                        + foreignK[j].getType() + endLine + " \n\t" + endBlock + "\n\n";
            }
        }
        return getter;
    }

    public String param() throws Exception {
        String parm = "";
        String ret = "return true";
        parm += "\t@Override\n"
                +"\t"+ publie + " boolean isAccountNonExpired()" + startBlock + "\n\t\t"
                + ret + endLine + " \n\t" + endBlock + "\n\n";
        
                parm += "\t@Override\n"
                +"\t"+ publie + " boolean isAccountNonLocked()" + startBlock + "\n\t\t"
                + ret + endLine + " \n\t" + endBlock + "\n\n";
                
                parm += "\t@Override\n"
                +"\t"+ publie + " boolean isCredentialsNonExpired()" + startBlock + "\n\t\t"
                + ret + endLine + " \n\t" + endBlock + "\n\n";

                parm += "\t@Override\n"
                +"\t"+ publie + " boolean isEnabled()" + startBlock + "\n\t\t"
                + ret + endLine + " \n\t" + endBlock + "\n\n";

                parm += "\t@Override\n"
                + "\tpublic Collection<? extends GrantedAuthority> getAuthorities()" + startBlock + "\n"
                + "\t\treturn List.of(new SimpleGrantedAuthority(role.name()));" + endLine + "\n"
                + "\t" + endBlock + "\n\n";

                       
        return parm;
        
    }

    public String importe(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String importation = "";
        ArrayList<Attribut> attribut = hs.get(tableName);
        for (int i = 0; i < attribut.size(); i++) {
            if (!importation.contains(attribut.get(i).getImportation())) {
                importation += impor + " " + attribut.get(i).getImportation() + endLine + "\n";
            }
        }
        String[] util = this.necessaryImportAuth();
        for (int i = 0; i < util.length; i++) {
            importation += impor + " " + util[i] + endLine + "\n";
        }
        return importation;
    }

    public String table(String tableName) throws Exception {
        String table = "";
        table += "@Table(name= \"" + tableName + "\")" + "\n";
        return table;
    }


    public String[] necessaryImportAuth() throws Exception {
        String[] nec = new String[0];
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = (Document) builder.parse(CONFIG_FILE);

            NodeList databaseRefList = ((org.w3c.dom.Document) document).getElementsByTagName("necessaryImportAuth");
            if (databaseRefList != null) {
                Element databaseRef = (Element) databaseRefList.item(0);
                NodeList elements = databaseRef.getElementsByTagName("utilImportAuth");
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

    public void generate() {
        try {
            HashMap<String, ArrayList<Attribut>> hs = this.data();
            Set<Entry<String, ArrayList<Attribut>>> entrees = hs.entrySet();
            for (Entry<String, ArrayList<Attribut>> table : entrees) {
                String tableName = table.getKey();
                this.generate(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generate(String tableName) {
        
        try {
            String base = "postgres";
            Connection con = Connexion.getConnection(base);
            Attribut[] data = this.getAllFK(tableName,con);

                HashMap<String, ArrayList<Attribut>> hs = this.data();
                String attributs = this.attribut(hs, tableName, con);
                String imports = this.importe(hs, tableName);
                String className = capitalizeFirstLetter(tableName);
                String template = loadTemplateFromFile();
                String packa = this.pack();
                String table = this.table(tableName);
                String setters = this.setter(hs, tableName, con);
                String getters = this.getter(hs, tableName, con);
                String paramet = this.param();
                template = template.replace("##PACKAGE##", packa);
                template = template.replace("##STARTPACKAGE##", startPackage);
                template = template.replace("##DEFINITION##", definition);
                template = template.replace("#CLASS_NAME#", className);
                template = template.replace("##STARTCLASS##", startClass);
                template = template.replace("##IMPORTS##", imports);
                template = template.replace("##ENDCLASS##", endClass);
                template = template.replace("##ENDPACKAGE##", endPackage);
                template = template.replace("##ANNOTATION##", annotation);
                template = template.replace("##TABLE##", table);
                template = template.replace("#ATTRIBUTS#", attributs);
                template = template.replace("#SETTERS#", setters);
                template = template.replace("#GETTERS#", getters);
                template = template.replace("##PARAME##", paramet);
                template = template.replace("#CONSTRUCTOR#", "");
                template = template.replace("#EMPTYCONSTRUCTOR#", "");
                //System.out.println(template);
                //System.out.println(paramet);
                
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

    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
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