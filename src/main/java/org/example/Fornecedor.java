package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Fornecedor {
    private static final String URL = "jdbc:sqlite:exemplo.db";

    public static void main(String[] args) {
        try {
            // Carrega o driver JDBC do SQLite
            Class.forName("org.sqlite.JDBC");

            // Conecta ao banco de dados (se não existir, um novo banco será criado)
            Connection connection = DriverManager.getConnection(URL);
            System.out.println("Conexão com o banco de dados estabelecida.");

            // Criação da tabela de fornecedores
            Statement statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS fornecedores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "cnpj TEXT NOT NULL," +
                    "logradouro TEXT NOT NULL," +
                    "bairro TEXT NOT NULL," +
                    "cidade TEXT NOT NULL," +
                    "estado TEXT NOT NULL," +
                    "cep TEXT NOT NULL," +
                    "cpf TEXT NOT NULL," +
                    "rg TEXT NOT NULL," +
                    "telefone TEXT NOT NULL," +
                    "email TEXT NOT NULL," +
                    "dataCadastro INTEGER NOT NULL," +
                    "dataNascimento INTEGER NOT NULL)";
            statement.execute(createTableSQL);
            System.out.println("Tabela 'fornecedores' criada com sucesso.");

            // Menu interativo
            int opcao;
            do {
                System.out.println("\n** Menu de Navegação **");
                System.out.println("1. Cadastrar Fornecedor");
                System.out.println("2. Buscar Fornecedor por Nome");
                System.out.println("3. Listar Fornecedores");
                System.out.println("4. Deletar Fornecedor");
                System.out.println("5. Sair");
                System.out.print("Escolha uma opção (1-5): ");
                Scanner scanner = new Scanner(System.in);
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer de entrada

                switch (opcao) {
                    case 1:
                        cadastrarFornecedor(connection);
                        break;
                    case 2:
                        buscarFornecedorPorNome(connection);
                        break;
                    case 3:
                        listarFornecedores(connection);
                        break;
                    case 4:
                        deletarFornecedor(connection);
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

    private static void cadastrarFornecedor(Connection connection) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Digite o nome do fornecedor: ");
            String nome = scanner.nextLine();

            System.out.println("Digite o CNPJ do fornecedor: ");
            String cnpj = scanner.nextLine();

            System.out.println("Digite o logradouro do fornecedor: ");
            String logradouro = scanner.nextLine();

            System.out.println("Digite o bairro do fornecedor: ");
            String bairro = scanner.nextLine();

            System.out.println("Digite a cidade do fornecedor: ");
            String cidade = scanner.nextLine();

            System.out.println("Digite o estado do fornecedor: ");
            String estado = scanner.nextLine();

            System.out.println("Digite o CEP do fornecedor: ");
            String cep = scanner.nextLine();

            System.out.println("Digite o CPF do fornecedor: ");
            String cpf = scanner.nextLine();

            System.out.println("Digite o RG do fornecedor: ");
            String rg = scanner.nextLine();

            System.out.println("Digite o telefone do fornecedor: ");
            String telefone = scanner.nextLine();

            System.out.println("Digite o email do fornecedor: ");
            String email = scanner.nextLine();

            System.out.println("Digite a data de cadastro do fornecedor: ");
            long dataCadastro = scanner.nextLong();
            scanner.nextLine(); // Limpar o buffer de entrada

            System.out.println("Digite a data de nascimento do fornecedor: ");
            long dataNascimento = scanner.nextLong();
            scanner.nextLine(); // Limpar o buffer de entrada

            String insertSQL = "INSERT INTO fornecedores (nome, cnpj, logradouro, bairro, cidade, estado, cep, cpf, rg, telefone, email, dataCadastro, dataNascimento) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, cnpj);
            preparedStatement.setString(3, logradouro);
            preparedStatement.setString(4, bairro);
            preparedStatement.setString(5, cidade);
            preparedStatement.setString(6, estado);
            preparedStatement.setString(7, cep);
            preparedStatement.setString(8, cpf);
            preparedStatement.setString(9, rg);
            preparedStatement.setString(10, telefone);
            preparedStatement.setString(11, email);
            preparedStatement.setLong(12, dataCadastro);
            preparedStatement.setLong(13, dataNascimento);
            preparedStatement.executeUpdate();

            System.out.println("Fornecedor cadastrado com sucesso: " + nome);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void buscarFornecedorPorNome(Connection connection) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Digite o nome do fornecedor que deseja buscar: ");
            String nomeBuscar = scanner.nextLine();

            String selectSQL = "SELECT * FROM fornecedores WHERE nome = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, nomeBuscar);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("\nFornecedor encontrado:");
                exibirInformacoesFornecedor(resultSet);
            } else {
                System.out.println("Fornecedor não encontrado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listarFornecedores(Connection connection) {
        try {
            String selectAllSQL = "SELECT * FROM fornecedores";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllSQL);

            System.out.println("\nLista de Fornecedores:");
            while (resultSet.next()) {
                exibirInformacoesFornecedor(resultSet);
                System.out.println("---------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deletarFornecedor(Connection connection) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Digite o nome do fornecedor que deseja deletar: ");
            String nomeDeletar = scanner.nextLine();

            String deleteSQL = "DELETE FROM fornecedores WHERE nome = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, nomeDeletar);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Fornecedor excluído com sucesso.");
            } else {
                System.out.println("Fornecedor não encontrado. Nenhum fornecedor foi excluído.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void exibirInformacoesFornecedor(ResultSet resultSet) {
        try {
            System.out.println("ID: " + resultSet.getInt("id"));
            System.out.println("Nome: " + resultSet.getString("nome"));
            System.out.println("CNPJ: " + resultSet.getString("cnpj"));
            System.out.println("Logradouro: " + resultSet.getString("logradouro"));
            System.out.println("Bairro: " + resultSet.getString("bairro"));
            System.out.println("Cidade: " + resultSet.getString("cidade"));
            System.out.println("Estado: " + resultSet.getString("estado"));
            System.out.println("CEP: " + resultSet.getString("cep"));
            System.out.println("CPF: " + resultSet.getString("cpf"));
            System.out.println("RG: " + resultSet.getString("rg"));
            System.out.println("Telefone: " + resultSet.getString("telefone"));
            System.out.println("Email: " + resultSet.getString("email"));
            System.out.println("Data de Cadastro: " + resultSet.getLong("dataCadastro"));
            System.out.println("Data de Nascimento: " + resultSet.getLong("dataNascimento"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
