package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    public static State state;
    private String url;
    private PreLogin preLogin;
    private PostLogin postLogin;
    private GameUI gameUI;

    public Repl(String serverURL) {
        state = State.PRELOGIN;
        url = serverURL;
    }
    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
        System.out.print(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                if (state == State.PRELOGIN) {
                    result = preLogin.eval(line, url);
                } else if (state == State.POSTLOGIN) {
                    result = postLogin.eval(line, url);
                } else if (state == State.GAMEUI) {
                    result = gameUI.eval(line, url);
                }

            System.out.print(SET_BG_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + SET_BG_COLOR_BLUE + ">>>" + SET_BG_COLOR_BLUE);
    }
}
