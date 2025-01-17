package tp_servidor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {
    private static String token = "none";
    private LigacaoToBD ligacao = new LigacaoToBD(TP_Servidor.ipMaquinaBD);
    private String nome,password;
   @RequestMapping("/")
    public ResponseEntity<String> home(){
        if(ligacao.conn_ligacao == null)
            ligacao.criarLigacaoBD(TP_Servidor.servidores.getCds().getnumBD());
        StringBuilder sb = new StringBuilder();
        sb.append("<div>"
                    + "<h3>Rest Api TP_PD </h3>"
                    + "<button onclick=\"location.href='/loginpage'\" type=\"button\">Fazer Login</button>"
                +"</div>");
       
        return new ResponseEntity<String>(sb.toString(),HttpStatus.OK);
   }
   
    
    @RequestMapping("/loginpage")
    public String loginpage(){
        StringBuilder sb = new StringBuilder();
        sb.append("<div>"+
                    "<h3>Página de Login</h3>"+
                    "<form action=\"/login\" method=\"post\">"+
                        "<div><p>Introduzir Username:</p>"+
                        "<input type='text' name='username' placeholder='Introduza o username'></div>"+
                        "<div><p>Introduzir Palavra Passe: </p>"+
                        "<input type='password' name='password' placeholder='Palavra Passe'></div>"+
                        "<div><input type='submit' value='Login'></div>"+
                    "</form></div>");
       
        return sb.toString();
}
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(value="username", defaultValue="") String username, @RequestParam(value="password", defaultValue="") String password,HttpServletResponse response) {
        if (token.equals("none")){
            
            boolean isLogged = ligacao.executalogin(ConstantesServer.ResolveMessages("tipo | login ; username | "+username+" ; password | "+password+" ; ip | "+TP_Servidor.servidores.getCds().getIpServer()+" ; porto | 0"));
            if (isLogged){
                token = "loggedIn";
                nome = username;
                this.password = password;
                StringBuilder sb = new StringBuilder();
                sb.append("<div>"+
                            "<h3>Bem Vindo</h3>"+
                            "<button onclick=\"location.href='/listarmusicas'\" type=\"button\">Listar Músicas</button>"+
                            "<button onclick=\"location.href='/logout'\" type=\"button\">Lougout</button>"+
                        "</div>");
       
                 return sb.toString();
            }
        }   
        return "Login incorreto";
    }

    @RequestMapping("/logout")
    public String logout() {
        token = "none";
        ligacao.executalogout(ConstantesServer.ResolveMessages("tipo | logout ; username | "+nome));
        return "<button onclick=\"location.href='/'\" type=\"button\">Lougout</button>";
    }
    
    @RequestMapping("/listarmusicas")
    public String listMusics(HttpServletResponse response) {

        if (token.equals("loggedIn")){
            StringBuilder sb = new StringBuilder();
            sb.append("<table> " +
                    "<tr>\n" +
                    "    <th>Nome da Música</th>\n" +
                    "    <th>Nome do Artista</th>\n" +
                    "    <th>Albúm</th>\n" +
                    "    <th>Ano de lançamento</th>" +
                    "    <th>Duração</th>" +
                    "    <th>Género</th>" +
                    "    <th>Ouvir</th>" +
                    "</tr>");
            
            ArrayList<Musica> listamusicas = ligacao.getListaMusicas(TP_Servidor.servidores);
            for(Musica m : listamusicas){
                sb.append("<tr>");
                sb.append("<td>").append(m.getNome()).append("</td>");
                sb.append("<td>").append(m.getAutor()).append("</td>");
                sb.append("<td>").append(m.getAlbum()).append("</td>");
                sb.append("<td>").append(m.getAno()).append("</td>");
                sb.append("<td>").append(m.getDuracao()).append("</td>");
                sb.append("<td>").append(m.getGenero()).append("</td>");
                sb.append("<td><a href='/download?name=").append(m.getNome()).append("'>Ouvir</a></td>");
                sb.append("</tr>");
            }
            sb.append("</table>");
            sb.append("<button onclick=\"location.href='/logout'\" type=\"button\">Lougout</button>");
            return sb.toString();
        }

        return "Não existem dados para serem apresentados";
    }

    @RequestMapping("/download")
    public void downloadMusic(@RequestParam(value="name", defaultValue="none") String name,HttpServletResponse response) throws IOException {
        if (!name.equals("none") && token.equals("loggedIn")){
            String fich = ligacao.getFicheiroNome(name);
            File file = new File(ConstantesServer.PATHLOCATION+"\\"+fich);

            if (file.exists()){

                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

                response.setContentLength((int) file.length());

                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

                FileCopyUtils.copy(inputStream, response.getOutputStream());

            }
        }
    }
}
