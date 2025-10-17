package ui;

import br.com.erikferreira.persistence.entity.BoardColumnEntity;
import br.com.erikferreira.persistence.entity.BoardEntity;
import br.com.erikferreira.service.BoardColumnQueryService;
import br.com.erikferreira.service.BoardQueryService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

import static br.com.erikferreira.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in);

    private final BoardEntity board;

    public void execute() {
        try {
            System.out.printf("Bem vindo ao board %s, selecione a operação desejada:".formatted(board.getName()));
            var option = -1;
            while(option != 9){
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Ver board");
                System.out.println("7 - ver coluna com cards");
                System.out.println("8 - ver card");
                System.out.println("9 - Voltar ao menu principal");
                System.out.println("10 - Sair");
                option = scanner.nextInt();
                switch (option){
                    case 1 -> createCard();
                    case 2 -> moveCard();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando ao menu principal...");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void createCard() {
    }

    private void moveCard() {
    }

    private void blockCard() {
    }

    private void unblockCard() {
    }

    private void cancelCard() {
    }

    private void showBoard() throws SQLException {
        try(var connection = getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(board.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.boardColumnDTO().forEach(c -> {
                    System.out.printf("Coluna [%s] tipo: %s tem %s cards\n", c.name(), c.kind(), c.cardsAmaunt());
                });
            });
        }
    }

    private void showColumn() throws SQLException {

        var columnsIds = board.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        var selectedColumn = -1L;
        while(!columnsIds.contains(selectedColumn)){
            System.out.printf("Escolha uma coluna do board %s\n", board.getName());
            board.getBoardColumns().forEach(
                    c -> System.out.printf("%s - %s - %s\n", c.getId(), c.getName(), c.getKind()));
            selectedColumn = scanner.nextLong();
        }
        try(var connection = getConnection()){
            var column = new BoardColumnQueryService(connection).findById(selectedColumn);
            column.ifPresent(co -> {
                System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
                co.getCards().forEach(
                        ca -> System.out.printf("Card %s: %s\nDescrição: %s",
                                ca.getId(), ca.getTitle(), ca.getDescription()));
            });
        }
    }

    private void showCard() {
    }

}
