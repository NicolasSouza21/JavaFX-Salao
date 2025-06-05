module app.salao { // Ou o nome do seu módulo, se for diferente
    requires javafx.controls;
    requires javafx.fxml;
    // Adicione 'requires transitive javafx.graphics;' se não estiver lá e erros persistirem
    // requires transitive javafx.graphics; // Para certas funcionalidades gráficas

    opens app.salao to javafx.fxml; // Permite que o FXML acesse o controller
    exports app.salao;          // TORNA AS CLASSES DO PACOTE app.salao (como Cliente) VISÍVEIS
}