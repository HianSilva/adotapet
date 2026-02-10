package br.edu.ifrn.hiansil.adotapet.service;

import br.edu.ifrn.hiansil.adotapet.dto.request.AbrigoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.request.LoginRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AbrigoResponseDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.LoginResponseDTO;
import br.edu.ifrn.hiansil.adotapet.model.AbrigoModel;
import br.edu.ifrn.hiansil.adotapet.repository.AbrigoRepository;
import br.edu.ifrn.hiansil.adotapet.security.JwtTokenService;
import br.edu.ifrn.hiansil.adotapet.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Injeção de dependência automática do Lombok
public class AbrigoService {

    private final AbrigoRepository abrigoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public AbrigoResponseDTO cadastrar(AbrigoRequestDTO dto) {
        if (abrigoRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Já existe um abrigo cadastrado com esse e-mail.");
        }
        if (abrigoRepository.existsByDocumento(dto.getDocumento())) {
            throw new IllegalArgumentException("Já existe um abrigo cadastrado com esse documento.");
        }

        AbrigoModel abrigo = new AbrigoModel();
        abrigo.setNome(dto.getNome());
        abrigo.setEmail(dto.getEmail());
        abrigo.setDocumento(dto.getDocumento());
        abrigo.setTelefone(dto.getTelefone());
        abrigo.setSenha(passwordEncoder.encode(dto.getSenha())); // Criptografa a senha

        abrigoRepository.save(abrigo);

        return new AbrigoResponseDTO(abrigo);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        // Autentica o usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());
        
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Gera o token JWT
        String token = jwtTokenService.generateToken(userDetails);
        
        // Busca o abrigo para retornar os dados
        AbrigoModel abrigo = abrigoRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        
        return new LoginResponseDTO(token, abrigo.getId(), abrigo.getNome(), abrigo.getEmail());
    }

    public List<AbrigoResponseDTO> listar() {
        return abrigoRepository.findAll().stream()
                .map(AbrigoResponseDTO::new)
                .collect(Collectors.toList());
    }
}