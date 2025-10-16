package ui;

import br.com.erikferreira.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

@AllArgsConstructor
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in);

    private final BoardEntity board;

    public void execute() {
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

    private void showBoard() {
    }

    private void showColumn() {
    }

    private void showCard() {
    }

}
