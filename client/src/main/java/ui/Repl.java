package ui;

import webSocket.NotificationHandler;
import webSocketMessages.serverMessages.ServerMessage;
import java.util.Scanner;

public class Repl implements NotificationHandler {
    static String serverURL;
    static State state;
    PreLogin preLogin;
    PostLogin postLogin;
    static GamePlayUI gamePlayUI = new GamePlayUI();

    public Repl(String url) {
        serverURL= url;
        state = State.PRELOGIN;
        preLogin = new PreLogin();
        postLogin = new PostLogin(this);
    }
    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess. Sign in to start.");
        Scanner scanner = new Scanner(System.in);
        String resultString = "";

        while (!resultString.equals("quit")) {
            if (state == State.PRELOGIN){
                System.out.print(preLogin.help());
            } else if (state == State.POSTLOGIN){
                System.out.print(postLogin.help());
            } else if (state == State.GAMEPLAYUI){
                System.out.print(gamePlayUI.help());
            }
            System.out.print("\n>>>");
            String line = scanner.nextLine();

            try {
                if (state == State.PRELOGIN) {
                    resultString = preLogin.eval(line);
                } else if (state == State.POSTLOGIN) {
                    resultString = postLogin.eval(line);
                }

            System.out.print(resultString);
            System.out.println();
            System.out.println();


            } catch (Throwable ex) {
                String errorMessage = ex.toString();
                System.out.print(errorMessage);
            }
        }
        System.out.println();
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println(notification);
        System.out.print("\n>>>");
    }
}
