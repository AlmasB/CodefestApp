/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
module codefest.client {
    requires codefest.common;
    requires javafx.controls;
    requires javafx.fxml;

    requires java.net.http;

    opens codefest to javafx.fxml;

    exports codefest to javafx.graphics, javafx.fxml;
}