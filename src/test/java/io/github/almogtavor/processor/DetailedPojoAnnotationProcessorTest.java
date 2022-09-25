package io.github.almogtavor.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

class DetailedPojoAnnotationProcessorTest {
    @Language("JAVA")
    private final String sourceJavaFileOfTargetFileWithFields = "package io.github.almogtavor.mocks;\n" +
                                                                "                \n" +
                                                                "import io.github.almogtavor.annotations.DetailedPojo;\n" +
                                                                "                \n" +
                                                                "import java.util.Date;\n" +
                                                                "                \n" +
                                                                "@DetailedPojo\n" +
                                                                "public class TargetFile {\n" +
                                                                "    private String itemId;\n" +
                                                                "    private String parentId;\n" +
                                                                "    private Date receptionTime;\n" +
                                                                "    private String text;\n" +
                                                                "    private String html;\n" +
                                                                "                \n" +
                                                                "    public TargetFile(String itemId, String parentId, Date receptionTime, String text, String html) {\n" +
                                                                "        this.itemId = itemId;\n" +
                                                                "        this.parentId = parentId;\n" +
                                                                "        this.receptionTime = receptionTime;\n" +
                                                                "        this.text = text;\n" +
                                                                "        this.html = html;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public String getItemId() {\n" +
                                                                "        return itemId;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public void setItemId(String itemId) {\n" +
                                                                "        this.itemId = itemId;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public String getParentId() {\n" +
                                                                "        return parentId;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public void setParentId(String parentId) {\n" +
                                                                "        this.parentId = parentId;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public Date getReceptionTime() {\n" +
                                                                "        return receptionTime;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public void setReceptionTime(Date receptionTime) {\n" +
                                                                "        this.receptionTime = receptionTime;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public String getText() {\n" +
                                                                "        return text;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public void setText(String text) {\n" +
                                                                "        this.text = text;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public String getHtml() {\n" +
                                                                "        return html;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public void setHtml(String html) {\n" +
                                                                "        this.html = html;\n" +
                                                                "    }\n" +
                                                                "}";
    @Language("JAVA")
    private final String sourceJavaFileOfTargetFileWithoutFields = "package io.github.almogtavor.mocks;\n" +
                                                                   "                \n" +
                                                                   "import io.github.almogtavor.annotations.DetailedPojo;\n" +
                                                                   "                                                \n" +
                                                                   "@DetailedPojo\n" +
                                                                   "public class TargetFile {\n" +
                                                                   "}";

    @Test
    void testSuccessfulCodeGenerator() {
        Compilation compilation = Compiler.javac()
                .withProcessors(new DetailedPojoAnnotationProcessor())
                .compile(JavaFileObjects.forSourceString("io.github.almogtavor.mocks.TargetFile", sourceJavaFileOfTargetFileWithFields));
        CompilationSubject.assertThat(compilation).succeeded();
    }

    @Test
    void shouldGenerateExpectedFileWhenGivenFileWithFields() {
        Compilation compilation = Compiler.javac()
                .withProcessors(new DetailedPojoAnnotationProcessor())
                .compile(JavaFileObjects.forSourceString("io.github.almogtavor.mocks.TargetFile", sourceJavaFileOfTargetFileWithFields));
        @Language("JAVA")
        String expected = "package io.github.almogtavor.mocks;\n" +
                          "                        \n" +
                          "import io.github.almogtavor.model.FieldDetails;\n" +
                          "import java.lang.String;\n" +
                          "import java.util.Arrays;\n" +
                          "import java.util.Date;\n" +
                          "import java.util.List;\n" +
                          "import javax.annotation.Generated;\n" +
                          "               \n" +
                          "@Generated(\"io.github.almogtavor.processor.DetailedPojoAnnotationProcessor\")\n" +
                          "public class DetailedTargetFile {\n" +
                          "    public static final List<FieldDetails> fieldDetailsList = Arrays.asList(\n" +
                          "            new FieldDetails<TargetFile, String>(\"itemId\", (TargetFile t) -> t.getItemId(), (TargetFile t1, String t2) -> t1.setItemId(t2)),\n" +
                          "            new FieldDetails<TargetFile, String>(\"parentId\", (TargetFile t) -> t.getParentId(), (TargetFile t1, String t2) -> t1.setParentId(t2)),\n" +
                          "            new FieldDetails<TargetFile, Date>(\"receptionTime\", (TargetFile t) -> t.getReceptionTime(), (TargetFile t1, Date t2) -> t1.setReceptionTime(t2)),\n" +
                          "            new FieldDetails<TargetFile, String>(\"text\", (TargetFile t) -> t.getText(), (TargetFile t1, String t2) -> t1.setText(t2)),\n" +
                          "            new FieldDetails<TargetFile, String>(\"html\", (TargetFile t) -> t.getHtml(), (TargetFile t1, String t2) -> t1.setHtml(t2)));\n" +
                          "}";
        CompilationSubject.assertThat(compilation)
                .generatedSourceFile("io.github.almogtavor.mocks.DetailedTargetFile")
                .hasSourceEquivalentTo(JavaFileObjects.forSourceString("io.github.almogtavor.mocks.DetailedTargetFile", expected));
    }

    @Test
    void shouldGenerateExpectedFileWhenGivenFileWithoutFields() {
        Compilation compilation = Compiler.javac()
                .withProcessors(new DetailedPojoAnnotationProcessor())
                .compile(JavaFileObjects.forSourceString("io.github.almogtavor.mocks.TargetFile", sourceJavaFileOfTargetFileWithoutFields));
        @Language("JAVA")
        String expected = "package io.github.almogtavor.mocks;\n" +
                          "                    \n" +
                          "import io.github.almogtavor.model.FieldDetails;\n" +
                          "import java.util.Arrays;\n" +
                          "import java.util.List;\n" +
                          "import javax.annotation.Generated;\n" +
                          "                    \n" +
                          "@Generated(\"io.github.almogtavor.processor.DetailedPojoAnnotationProcessor\")\n" +
                          "public class DetailedTargetFile {\n" +
                          "    public static final List<FieldDetails> fieldDetailsList = Arrays.asList();\n" +
                          "}";
        CompilationSubject.assertThat(compilation)
                .generatedSourceFile("io.github.almogtavor.mocks.DetailedTargetFile")
                .hasSourceEquivalentTo(JavaFileObjects.forSourceString("io.github.almogtavor.mocks.DetailedTargetFile", expected));
    }

}