package capstone.skini;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

@RestController
@RequestMapping("/api")
public class MainController {

    @PostMapping("/")
    public String main() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(name);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();
        String role = authority.getAuthority();
        System.out.println(role);
        return name + " 의 role : " + role;
    }

}
