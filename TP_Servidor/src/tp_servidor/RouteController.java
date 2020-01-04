package tp_servidor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {
   @RequestMapping("/")
   public String home(){
       return "Inicio";
   }
}
