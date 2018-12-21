package nah.hannah.behindview;

/**
 * Created by hannah on 2018. 12. 21..
 */
public class Contributor {
    String login;
    String html_url;

    int contributions;

    @Override
    public String toString() {
        return login + " (" + contributions + ")";
    }
}
