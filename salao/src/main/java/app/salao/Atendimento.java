package app.salao; // Certifique-se que este é o nome correto do seu pacote

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale; // Para formatação de moeda

public class Atendimento {
    private String descricaoProcedimento;
    private double valorCobrado;
    private LocalDate dataDoAtendimento;
    private boolean pago;

    // Construtor
    public Atendimento(String descricaoProcedimento, double valorCobrado, LocalDate dataDoAtendimento, boolean pago) {
        this.descricaoProcedimento = descricaoProcedimento;
        this.valorCobrado = valorCobrado;
        this.dataDoAtendimento = dataDoAtendimento;
        this.pago = pago;
    }

    // Getters (para ler os dados)
    public String getDescricaoProcedimento() {
        return descricaoProcedimento;
    }

    public double getValorCobrado() {
        return valorCobrado;
    }

    public LocalDate getDataDoAtendimento() {
        return dataDoAtendimento;
    }

    public boolean isPago() { // Para boolean, o getter geralmente começa com "is"
        return pago;
    }

    // Setters (para alterar os dados)
    public void setDescricaoProcedimento(String descricaoProcedimento) {
        this.descricaoProcedimento = descricaoProcedimento;
    }

    public void setValorCobrado(double valorCobrado) {
        this.valorCobrado = valorCobrado;
    }

    public void setDataDoAtendimento(LocalDate dataDoAtendimento) {
        this.dataDoAtendimento = dataDoAtendimento;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    // Método para facilitar a visualização do Atendimento (útil para debug)
    @Override
    public String toString() {
        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = (dataDoAtendimento != null) ? dataDoAtendimento.format(formatterData) : "N/A";
        
        // Formata o valor como moeda brasileira (R$)
        String valorFormatado = String.format(Locale.forLanguageTag("pt-BR"), "R$%.2f", valorCobrado);

        return String.format("Procedimento: %s, Valor: %s, Data: %s, Pago: %s",
                descricaoProcedimento, valorFormatado, dataFormatada, (pago ? "Sim" : "Não"));
    }
}