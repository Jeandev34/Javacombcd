package org.example;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Estoque {
    private static final String URL = "jdbc:sqlite:exemplo.db";

    public static void main(String[] args) {
        try {
            // Carrega o driver JDBC do SQLite
            Class.forName("org.sqlite.JDBC");

            // Conecta ao banco de dados (se não existir, um novo banco será criado)
            Connection connection = DriverManager.getConnection(URL);
            System.out.println("Conexão com o banco de dados estabelecida.");

            // Menu interativo
            int opcao;
            do {
                System.out.println("\n** Menu de Estoque **");
                System.out.println("1. Atualizar Estoque de Produtos");
                System.out.println("2. Adicionar Estoque de Produtos");
                System.out.println("3. Remover Estoque de Produtos");
                System.out.println("4. Listar Produtos Disponíveis");
                System.out.println("5. Sair");
                System.out.print("Escolha uma opção (1-5): ");
                Scanner scanner = new Scanner(System.in);
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer de entrada

                switch (opcao) {
                    case 1:
                        atualizarEstoque(connection);
                        break;
                    case 2:
                        adicionarEstoque(connection);
                        break;
                    case 3:
                        removerEstoque(connection);
                        break;
                    case 4:
                        listarProdutosDisponiveis(connection);
                        break;
                    case 5:
                        System.out.println("Encerrando o programa.");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } while (opcao != 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void atualizarEstoque(Connection connection) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Digite o ID do produto para atualizar o estoque: ");
            int idProduto = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            System.out.println("Digite a nova quantidade em estoque: ");
            int novaQuantidade = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            String updateSQL = "UPDATE produtos SET quantidade = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1, novaQuantidade);
            preparedStatement.setInt(2, idProduto);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Estoque atualizado com sucesso.");
            } else {
                System.out.println("Produto não encontrado. Nenhum estoque foi atualizado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void adicionarEstoque(Connection connection) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Digite o ID do produto para adicionar ao estoque: ");
            int idProduto = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            System.out.println("Digite a quantidade a ser adicionada ao estoque: ");
            int quantidadeAdicionada = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            String selectSQL = "SELECT quantidade FROM produtos WHERE id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
            selectStatement.setInt(1, idProduto);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int quantidadeAtual = resultSet.getInt("quantidade");
                int novaQuantidade = quantidadeAtual + quantidadeAdicionada;

                String updateSQL = "UPDATE produtos SET quantidade = ? WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
                updateStatement.setInt(1, novaQuantidade);
                updateStatement.setInt(2, idProduto);
                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Estoque atualizado com sucesso.");
                } else {
                    System.out.println("Produto não encontrado. Nenhum estoque foi atualizado.");
                }
            } else {
                System.out.println("Produto não encontrado. Nenhum estoque foi atualizado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void removerEstoque(Connection connection) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Digite o ID do produto para remover do estoque: ");
            int idProduto = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            System.out.println("Digite a quantidade a ser removida do estoque: ");
            int quantidadeRemovida = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer de entrada

            String selectSQL = "SELECT quantidade FROM produtos WHERE id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
            selectStatement.setInt(1, idProduto);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int quantidadeAtual = resultSet.getInt("quantidade");
                int novaQuantidade = quantidadeAtual - quantidadeRemovida;

                if (novaQuantidade >= 0) {
                    String updateSQL = "UPDATE produtos SET quantidade = ? WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
                    updateStatement.setInt(1, novaQuantidade);
                    updateStatement.setInt(2, idProduto);
                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Estoque atualizado com sucesso.");
                    } else {
                        System.out.println("Produto não encontrado. Nenhum estoque foi atualizado.");
                    }
                } else {
                    System.out.println("Quantidade a ser removida é maior do que a quantidade em estoque.");
                }
            } else {
                System.out.println("Produto não encontrado. Nenhum estoque foi atualizado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listarProdutosDisponiveis(Connection connection) {
        try {
            String selectAvailableSQL = "SELECT id, nome, quantidade FROM produtos WHERE quantidade > 0";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAvailableSQL);

            System.out.println("\nLista de Produtos Disponíveis:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Nome: " + resultSet.getString("nome"));
                System.out.println("Quantidade em Estoque: " + resultSet.getInt("quantidade"));
                System.out.println("---------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
