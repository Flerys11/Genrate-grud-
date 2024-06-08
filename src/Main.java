import java.util.Scanner;

import classe.ClassGenerator;
import classe.AuthRepons;
import classe.RoleU;
import classe.ClassGeneratorAuth;
import service.Detailservice;
import service.UserDetail;
import service.Jwt;
import config.ServiceConf;
import config.Logout;
import filter.JwtFilter;
import controller.ControllerGenerator;
import controller.ControllerAuth;
import repository.RepositoryGenerator;
import view.ListeGenerator;
import view.UpdateGenerator;
import view.FormGenerator;
import view.LoginGenerator;

public class Main {
    public static void main(String[] args) throws Exception {
        while(true) {
            Scanner scanner = new Scanner(System.in);
                System.out.println("Quel type de classe voulez-vous générer ? (crud/login)");
                
                String type = scanner.nextLine();
                String typeMinuscule = type.toLowerCase();
                    if(typeMinuscule.equals("crud")){
                        System.out.println("crud");
                        System.out.println("Entrez la technologie : ");
                        String technologie = scanner.nextLine();

                        System.out.println("Entrez le nom de la classe à générer : ");
                        String nomClasse = scanner.nextLine();

                        new ClassGenerator(technologie).generate(nomClasse);
                        new RepositoryGenerator(technologie).generate(nomClasse,"","");
                        new ControllerGenerator(technologie).generate(nomClasse);
                        new ListeGenerator(technologie).generate(nomClasse);
                        new FormGenerator(technologie).generate(nomClasse);
                        new UpdateGenerator(technologie).generate(nomClasse);
                        System.out.println("Génération terminée");
                        System.out.println("Voulez-vous continuer ? (O/N) [par défaut O]");
                        String continuer = scanner.nextLine();
                        if(continuer.equalsIgnoreCase("N")) {
                            break;
                        }else{
                            continue;
                        }
                    }else if(typeMinuscule.equals("login")){
                        System.out.println("login");
                        System.out.println("Entrez la technologie : ");
                        String technologie = scanner.nextLine();
    
                        System.out.println("Entrez le nom de la classe à générer : ");
                        String nomClasse = scanner.nextLine();

                        System.out.println("Entrez du table token : ");
                        String nomClasse2 = scanner.nextLine();
    
                        String authrepo = "AuthenticationResponse";
                        String r = "Role";
                        String AuthenticationService = "AuthenticationService";
                        String JwtService = "JwtService";
                        String UserDetailsServiceImp = "UserDetailsServiceImp";
                        String SecurityConfig = "SecurityConfig";
                        String CustomLogoutHandler = "CustomLogoutHandler";
                        String JwtAuthenticationFilter = "JwtAuthenticationFilter";
                        String AuthenticationController = "AuthenticationController";
                        new LoginGenerator(technologie).generate();
                        new ControllerAuth(technologie).generate(AuthenticationController,nomClasse);
                        new RepositoryGenerator(technologie).generate(nomClasse,nomClasse,nomClasse2);
                        new JwtFilter(technologie).generate(JwtAuthenticationFilter);
                        new Logout(technologie).generate(CustomLogoutHandler,nomClasse2);
                        new ServiceConf(technologie).generate(SecurityConfig);
                        new UserDetail(technologie).generate(UserDetailsServiceImp,nomClasse);
                        new Jwt(technologie).generate(JwtService,nomClasse,nomClasse2);
                        new Detailservice(technologie).generate(AuthenticationService,nomClasse,nomClasse2);
                        new ClassGenerator(technologie).generate(nomClasse2);

                    }



        }
    }
}