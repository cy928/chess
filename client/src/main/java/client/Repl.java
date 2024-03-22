package client;

import java.util.Scanner;


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
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            if (state == State.PRELOGIN){
                System.out.print(preLogin.help());
            }
            else if (state == client.State.POSTLOGIN){
                System.out.print(postLogin.help());
            }
            printPrompt();
            String line = scanner.nextLine();

            try {
                if (state == State.PRELOGIN) {
                    result = preLogin.eval(line, url);
                } else if (state == State.POSTLOGIN) {
                    result = postLogin.eval(line, url);
                }

            System.out.print(result);
            System.out.println();

            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
                result = "";
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n>>>");
    }
}
