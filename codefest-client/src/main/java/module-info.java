/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
module codefest.client {
    requires codefest.common;
    requires javafx.controls;
    requires javafx.fxml;

    requires java.net.http;
    requires java.compiler;

    opens codefest.client to javafx.fxml;

    exports codefest.client to javafx.graphics, javafx.fxml;
}