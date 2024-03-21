package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    public static State state;
    public static String url;
    private final PreLogin preLogin = new PreLogin();
    private final PostLogin postLogin = new PostLogin();

    public Repl(String serverURL) {
        state = State.PRELOGIN;
        url = serverURL;
    }
    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess. Sign in to start.");
        System.out.print(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                if (state == State.PRELOGIN) {
                    result = preLogin.eval(line, url).toString();
                } else if (state == State.POSTLOGIN) {
                    result = postLogin.eval(line, url).toString();
                }

            System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n>>>");
    }
}
