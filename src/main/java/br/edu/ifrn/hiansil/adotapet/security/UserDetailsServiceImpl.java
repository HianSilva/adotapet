package br.edu.ifrn.hiansil.adotapet.security;

import br.edu.ifrn.hiansil.adotapet.model.AbrigoModel;
import br.edu.ifrn.hiansil.adotapet.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AbrigoRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AbrigoModel user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        return new UserDetailsImpl(user);
    }
}