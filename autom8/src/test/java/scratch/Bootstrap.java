package scratch;

import autom8.Autom8;

public class Bootstrap {
    public static void main(String[] args) {
        Autom8.execute("""
                go to https://google.com
                expect that "Images" is in the body
                expect that "Store" is in the body""");
    }
}
