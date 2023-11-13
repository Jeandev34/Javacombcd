package org.example;

import java.sql.*;
import java.util.Scanner;

public class Produto {

    private static final String URL = "jdbc:sqlite:exemplo.db";

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection connection = DriverManager.getConnection(URL);
            System.out.println("Conexão com o banco de dados estabelecida.");

            criarTabelaProdutos(connection);
            menuInterativo(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void criarTabelaProdutos(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS produtos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "codigo INTEGER NOT NULL," +
                "descricao TEXT NOT NULL," +
                "fornecedor TEXT NOT NULL," +
                "datadecadastro INTEGER NOT NULL," +
                "quantidade INTEGER NOT NULL)";
        statement.execute(createTableSQL);
        System.out.println("Tabela 'produtos' criada com sucesso.");
    }

    private static void menuInterativo(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        int opcao;
        do {
            System.out.println("\n** Menu de Produtos **");
            System.out.println("1. Inserir Produto");
            System.out.println("2. Buscar Produto");
            System.out.println("3. Excluir Produto");
            System.out.println("4. Listar Produtos");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção (1-5): ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            switch (opcao) {
                case 1:
                    inserirProduto(connection, scanner);
                    break;
                case 2:
                    buscarProduto(connection, scanner);
                    break;
                case 3:
                    excluirProduto(connection, scanner);
                    break;
                case 4:
                    listarProdutos(connection);
                    break;
                case 5:
                    System.out.println("Encerrando o programa.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 5);

        scanner.close(); // Fechar o Scanner no final
    }

    private static void inserirProduto(Connection connection, Scanner scanner) {
        try {
            System.out.println("Digite o nome do produto: ");
            String nomeProduto = scanner.nextLine();

            System.out.println("Digite o código do produto: ");
            int codigoProduto = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            System.out.println("Digite a descrição do produto: ");
            String descricaoProduto = scanner.nextLine();

            System.out.println("Digite o fornecedor do produto: ");
            String fornecedorProduto = scanner.nextLine();

            System.out.println("Digite a data de cadastro do produto: ");
            int dataCadastroProduto = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            System.out.println("Digite a quantidade em estoque: ");
            int quantidadeProduto = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            String insertSQL = "INSERT INTO produtos (nome, codigo, descricao, fornecedor, datadecadastro, quantidade) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, nomeProduto);
            preparedStatement.setInt(2, codigoProduto);
            preparedStatement.setString(3, descricaoProduto);
            preparedStatement.setString(4, fornecedorProduto);
            preparedStatement.setInt(5, dataCadastroProduto);
            preparedStatement.setInt(6, quantidadeProduto);
            preparedStatement.executeUpdate();

            System.out.println("Produto inserido com sucesso: " + nomeProduto);

        } catch (SQLException e) {
            System.out.println("Erro ao inserir o produto. Verifique os dados e tente novamente.");
        }
    }

    private static void buscarProduto(Connection connection, Scanner scanner) {
        try {
            System.out.println("Digite o código do produto que deseja buscar: ");
            int codigoBuscar = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            String selectSQL = "SELECT * FROM produtos WHERE codigo = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, codigoBuscar);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Produto encontrado:");
                System.out.println("Nome: " + resultSet.getString("nome"));
                System.out.println("Código: " + resultSet.getInt("codigo"));
                System.out.println("Descrição: " + resultSet.getString("descricao"));
                System.out.println("Fornecedor: " + resultSet.getString("fornecedor"));
                System.out.println("Data de cadastro: " + resultSet.getInt("datadecadastro"));
                System.out.println("Quantidade em estoque: " + resultSet.getInt("quantidade"));
            } else {
                System.out.println("Produto não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar o produto. Verifique os dados e tente novamente.");
        }
    }

    private static void excluirProduto(Connection connection, Scanner scanner) {
        try {
            System.out.println("Digite o código do produto que deseja excluir: ");
            int codigoExcluir = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            String deleteSQL = "DELETE FROM produtos WHERE codigo = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, codigoExcluir);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Produto excluído com sucesso.");
            } else {
                System.out.println("Produto não encontrado. Nenhum produto foi excluído.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir o produto. Verifique os dados e tente novamente.");
        }
    }

    private static void listarProdutos(Connection connection) {
        try {
            String selectAllSQL = "SELECT * FROM produtos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllSQL);

            System.out.println("\nLista de Produtos:");
            while (resultSet.next()) {
                System.out.println("Nome: " + resultSet.getString("nome"));
                System.out.println("Código: " + resultSet.getInt("codigo"));
                System.out.println("Descrição: " + resultSet.getString("descricao"));
                System.out.println("Fornecedor: " + resultSet.getString("fornecedor"));
                System.out.println("Data de cadastro: " + resultSet.getInt("datadecadastro"));
                System.out.println("Quantidade em estoque: " + resultSet.getInt("quantidade"));
                System.out.println("---------------------");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar os produtos. Verifique os dados e tente novamente.");
        }
    }
}
