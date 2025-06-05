package app.salao; // Certifique-se que este é o nome correto do seu pacote

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; // Import necessário para ArrayList
import java.util.List;    // Import necessário para List

public class Cliente {
    private String nome;
    private LocalDate dataNascimento;
    private String redeSocial;
    private String telefone;
    private List<Atendimento> historicoDeAtendimentos; // NOVO CAMPO: Lista para guardar os atendimentos

    // Construtor padrão (sem argumentos)
    public Cliente() {
        // Inicializa a lista de atendimentos para evitar NullPointerException
        this.historicoDeAtendimentos = new ArrayList<>();
    }

    // Construtor parametrizado
    public Cliente(String nome, String telefone, LocalDate dataNascimento, String redeSocial) {
        this.nome = nome;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.redeSocial = redeSocial;
        // Inicializa a lista de atendimentos também neste construtor
        this.historicoDeAtendimentos = new ArrayList<>();
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getRedeSocial() {
        return redeSocial;
    }

    public String getTelefone() {
        return telefone;
    }

    // Getter para o histórico de atendimentos
    public List<Atendimento> getHistoricoDeAtendimentos() {
        return historicoDeAtendimentos;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setRedeSocial(String redeSocial) {
        this.redeSocial = redeSocial;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    // Não vamos criar um setter para a lista inteira (setHistoricoDeAtendimentos),
    // em vez disso, teremos um método para adicionar atendimentos um por um.

    // Método para adicionar um novo atendimento ao histórico do cliente
    public void adicionarAtendimento(Atendimento atendimento) {
        if (atendimento != null) {
            this.historicoDeAtendimentos.add(atendimento);
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataNascFormatada = (dataNascimento != null) ? dataNascimento.format(formatter) : "N/A";
        String redeSocialInfo = (redeSocial != null && !redeSocial.trim().isEmpty()) ? redeSocial : "N/A";

        return "Cliente [Nome: " + nome +
               ", Data Nasc: " + dataNascFormatada +
               ", Rede Social: " + redeSocialInfo +
               ", Telefone: " + telefone + "]";
        // O toString do Cliente não precisa mostrar todos os atendimentos,
        // apenas os dados do cliente. O histórico pode ser acessado via getHistoricoDeAtendimentos().
    }
}