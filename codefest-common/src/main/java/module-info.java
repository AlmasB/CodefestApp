/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
module codefest.common {
    requires transitive kotlin.stdlib;
    requires transitive sslogger.main;

    requires transitive jackson.annotations;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive com.fasterxml.jackson.module.kotlin;

    opens codefest.data to com.fasterxml.jackson.databind;

    exports codefest.data;
}