package app.salao;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class PrimaryController implements Initializable {

    // --- CAMPOS PARA CADASTRO DE CLIENTES ---
    @FXML private TextField nomeTextField;
    @FXML private DatePicker dataNascimentoPicker;
    @FXML private TextField redeSocialTextField;
    @FXML private TextField telefoneTextField;
    @FXML private Button adicionarButton;

    @FXML private TableView<Cliente> clientesTableView;
    @FXML private TableColumn<Cliente, String> colunaNome;
    @FXML private TableColumn<Cliente, String> colunaTelefone;
    @FXML private TableColumn<Cliente, LocalDate> colunaDataNascimento;
    @FXML private TableColumn<Cliente, String> colunaRedeSocial;

    private ObservableList<Cliente> listaDeClientes;
    
    // --- CAMPOS PARA A SEÇÃO DE ATENDIMENTOS ---
    @FXML private Label atendimentosHeaderLabel;
    @FXML private TextField descricaoProcedimentoTextField;
    @FXML private TextField valorCobradoTextField;
    @FXML private DatePicker dataAtendimentoPicker; // Alvo da sua correção
    @FXML private CheckBox pagoCheckBox;
    @FXML private Button salvarAtendimentoButton;

    @FXML private TableView<Atendimento> atendimentosTableView;
    @FXML private TableColumn<Atendimento, String> colunaDescricaoProcedimento;
    @FXML private TableColumn<Atendimento, Double> colunaValorCobrado;
    @FXML private TableColumn<Atendimento, LocalDate> colunaDataAtendimento;
    @FXML private TableColumn<Atendimento, Boolean> colunaPago;

    private ObservableList<Atendimento> listaDeAtendimentosDoClienteSelecionado;
    private Cliente clienteSelecionado; 

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // --- CONFIGURAÇÃO DOS CLIENTES ---
        listaDeClientes = FXCollections.observableArrayList();
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        colunaRedeSocial.setCellValueFactory(new PropertyValueFactory<>("redeSocial"));
        clientesTableView.setItems(listaDeClientes);

        configurarDatePicker(dataNascimentoPicker); // Configura o DatePicker de Nascimento
        formatarColunaDataCliente(colunaDataNascimento); // Formata a coluna de data na tabela de clientes

        // --- CONFIGURAÇÃO DOS ATENDIMENTOS ---
        listaDeAtendimentosDoClienteSelecionado = FXCollections.observableArrayList();
        colunaDescricaoProcedimento.setCellValueFactory(new PropertyValueFactory<>("descricaoProcedimento"));
        colunaValorCobrado.setCellValueFactory(new PropertyValueFactory<>("valorCobrado"));
        colunaDataAtendimento.setCellValueFactory(new PropertyValueFactory<>("dataDoAtendimento"));
        colunaPago.setCellValueFactory(new PropertyValueFactory<>("pago")); 
        atendimentosTableView.setItems(listaDeAtendimentosDoClienteSelecionado);

        formatarColunaDataAtendimento(colunaDataAtendimento); // Formata a coluna de data na tabela de atendimentos
        // Poderíamos formatar a coluna "Pago" para mostrar "Sim/Não" também

        clientesTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> handleClienteSelecionado(newValue)
        );

        configurarDatePicker(dataAtendimentoPicker); // Configura também o DatePicker de Atendimento
        
        habilitarSecaoAtendimentos(false);
    }

    private void configurarDatePicker(DatePicker datePicker) {
        TextField editor = datePicker.getEditor();
        UnaryOperator<TextFormatter.Change> dateFilter = change -> {
            String novoTextoCompleto = change.getControlNewText();
            if (change.isDeleted()) return change;
            String apenasDigitos = novoTextoCompleto.replaceAll("[^0-9]", "");
            int totalDeDigitos = apenasDigitos.length();
            if (totalDeDigitos > 8) return null;
            StringBuilder textoFormatado = new StringBuilder();
            for (int i = 0; i < totalDeDigitos; i++) {
                textoFormatado.append(apenasDigitos.charAt(i));
                if (i == 1 && totalDeDigitos > 2) textoFormatado.append('/');
                else if (i == 3 && totalDeDigitos > 4) textoFormatado.append('/');
            }
            change.setText(textoFormatado.toString());
            change.setRange(0, change.getControlText().length());
            change.setCaretPosition(textoFormatado.length());
            change.setAnchor(textoFormatado.length());
            return change;
        };
        editor.setTextFormatter(new TextFormatter<>(dateFilter));

        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }
            @Override public LocalDate fromString(String string) {
                if (string != null && !string.trim().isEmpty()) {
                    try { return LocalDate.parse(string, dateFormatter); }
                    catch (DateTimeParseException e) { 
                        System.err.println("StringConverter (fromString DataPicker): Falha ao converter String '" + string + "': " + e.getMessage());
                        return null; }
                } return null;
            }
        });
    }
    
    private void formatarColunaDataCliente(TableColumn<Cliente, LocalDate> coluna) {
        coluna.setCellFactory(col -> new TableCell<Cliente, LocalDate>() {
            @Override
            protected void updateItem(LocalDate data, boolean empty) {
                super.updateItem(data, empty);
                setText( (data == null || empty) ? null : dateFormatter.format(data) );
            }
        });
    }
    
    private void formatarColunaDataAtendimento(TableColumn<Atendimento, LocalDate> coluna) {
         coluna.setCellFactory(col -> new TableCell<Atendimento, LocalDate>() {
            @Override
            protected void updateItem(LocalDate data, boolean empty) {
                super.updateItem(data, empty);
                setText( (data == null || empty) ? null : dateFormatter.format(data) );
            }
        });
    }

    @FXML
    private void handleAdicionarCliente() {
        String nome = nomeTextField.getText();
        String redeSocial = redeSocialTextField.getText();
        String telefone = telefoneTextField.getText();
        String dataTextoNascimento = dataNascimentoPicker.getEditor().getText().trim();
        LocalDate dataNascimento = null;

        if (!dataTextoNascimento.isEmpty()) {
            try {
                dataNascimento = LocalDate.parse(dataTextoNascimento, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("LOG: Data de nascimento '" + dataTextoNascimento + "' é inválida (não pôde ser convertida).");
                dataNascimentoPicker.requestFocus();
                return;
            }
        }
        
        if (nome == null || nome.trim().isEmpty()) { /* ... */ return; }
        if (telefone == null || telefone.trim().isEmpty()) { /* ... */ return; }
        
        Cliente novoCliente = new Cliente(nome, telefone, dataNascimento, redeSocial);
        listaDeClientes.add(novoCliente);
        System.out.println("Novo Cliente Adicionado à lista: " + novoCliente.toString());

        nomeTextField.clear();
        dataNascimentoPicker.getEditor().clear();
        dataNascimentoPicker.setValue(null); 
        redeSocialTextField.clear();
        telefoneTextField.clear();
        nomeTextField.requestFocus();
    }

    private void handleClienteSelecionado(Cliente cliente) {
        this.clienteSelecionado = cliente;
        listaDeAtendimentosDoClienteSelecionado.clear(); 

        if (cliente != null) {
            atendimentosHeaderLabel.setText("Atendimentos de: " + cliente.getNome());
            listaDeAtendimentosDoClienteSelecionado.addAll(cliente.getHistoricoDeAtendimentos());
            habilitarSecaoAtendimentos(true);
        } else {
            atendimentosHeaderLabel.setText("Atendimentos do Cliente: (Selecione um cliente)");
            habilitarSecaoAtendimentos(false);
        }
    }

    @FXML
    private void handleSalvarAtendimento() {
        if (clienteSelecionado == null) {
            System.out.println("LOG: Nenhum cliente selecionado para adicionar atendimento.");
            return;
        }

        String descProc = descricaoProcedimentoTextField.getText();
        String valorTexto = valorCobradoTextField.getText();
        boolean pago = pagoCheckBox.isSelected();
        double valorCobrado = 0.0;

        // --- INÍCIO DA SUA LÓGICA ATUALIZADA PARA DATA DO ATENDIMENTO ---
        String dataTextoAtendimento = dataAtendimentoPicker.getEditor().getText().trim();
        LocalDate dataAtendimento = null; // Começa como null

        if (!dataTextoAtendimento.isEmpty()) { // Só tenta converter se o campo não estiver vazio
            try {
                // Reutilizando o dateFormatter definido para a classe
                dataAtendimento = LocalDate.parse(dataTextoAtendimento, dateFormatter);
            } catch (DateTimeParseException e) { // Captura especificamente DateTimeParseException
                System.out.println("LOG: Data do atendimento '" + dataTextoAtendimento + "' é inválida (não pôde ser convertida).");
                dataAtendimentoPicker.requestFocus();
                return; // Interrompe se a data não vazia for inválida
            }
        }
        // Se dataTextoAtendimento for vazia, dataAtendimento permanece null.
        // Precisamos decidir se a data do atendimento é obrigatória.
        // --- FIM DA SUA LÓGICA ATUALIZADA PARA DATA DO ATENDIMENTO ---

        // Validações adicionais
        if (descProc == null || descProc.trim().isEmpty()) {
            System.out.println("LOG: Descrição do procedimento não pode estar vazia.");
            descricaoProcedimentoTextField.requestFocus();
            return;
        }
        try {
            valorCobrado = Double.parseDouble(valorTexto.replace(",", "."));
            if (valorCobrado < 0) {
                System.out.println("LOG: Valor cobrado não pode ser negativo.");
                valorCobradoTextField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("LOG: Valor cobrado inválido.");
            valorCobradoTextField.requestFocus();
            return;
        }
        if (dataAtendimento == null) { // Tornando a data do atendimento obrigatória
            System.out.println("LOG: Data do atendimento é obrigatória e deve ser válida.");
            dataAtendimentoPicker.requestFocus();
            return;
        }

        Atendimento novoAtendimento = new Atendimento(descProc, valorCobrado, dataAtendimento, pago);
        clienteSelecionado.adicionarAtendimento(novoAtendimento);
        listaDeAtendimentosDoClienteSelecionado.add(novoAtendimento);
        
        System.out.println("Novo atendimento salvo para " + clienteSelecionado.getNome() + ": " + novoAtendimento);

        descricaoProcedimentoTextField.clear();
        valorCobradoTextField.clear();
        dataAtendimentoPicker.getEditor().clear();
        dataAtendimentoPicker.setValue(null);
        pagoCheckBox.setSelected(false);
        descricaoProcedimentoTextField.requestFocus();
    }
    
    private void habilitarSecaoAtendimentos(boolean habilitar) {
        descricaoProcedimentoTextField.setDisable(!habilitar);
        valorCobradoTextField.setDisable(!habilitar);
        dataAtendimentoPicker.setDisable(!habilitar);
        pagoCheckBox.setDisable(!habilitar);
        salvarAtendimentoButton.setDisable(!habilitar);
        atendimentosTableView.setDisable(!habilitar); 
        
        if(!habilitar){
            descricaoProcedimentoTextField.clear();
            valorCobradoTextField.clear();
            dataAtendimentoPicker.getEditor().clear();
            dataAtendimentoPicker.setValue(null);
            pagoCheckBox.setSelected(false);
            atendimentosHeaderLabel.setText("Atendimentos do Cliente: (Selecione um cliente)");
            if(listaDeAtendimentosDoClienteSelecionado != null) { // Adicionado null check
                listaDeAtendimentosDoClienteSelecionado.clear();
            }
        }
    }
}